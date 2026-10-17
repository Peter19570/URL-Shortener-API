package com.peter.shortener.features.url.service;

import com.fayupable.snowflake.port.IdGenerator;
import com.peter.shortener.features.click.model.Click;
import com.peter.shortener.features.click.repo.ClickRepo;
import com.peter.shortener.features.shared.dto.ClientInfo;
import com.peter.shortener.features.shared.dto.PageResponse;
import com.peter.shortener.features.shared.dto.UserPrincipal;
import com.peter.shortener.features.shared.exceptions.custom.ExpiredException;
import com.peter.shortener.features.shared.exceptions.custom.NotFoundException;
import com.peter.shortener.features.url.dto.request.UrlRequest;
import com.peter.shortener.features.url.dto.response.UrlDetailedResponse;
import com.peter.shortener.features.url.dto.response.UrlResponse;
import com.peter.shortener.features.url.dto.response.UrlStats;
import com.peter.shortener.features.url.filters.UrlFilter;
import com.peter.shortener.features.url.mapper.UrlMapper;
import com.peter.shortener.features.url.model.Url;
import com.peter.shortener.features.url.repo.UrlRepo;
import com.peter.shortener.features.url.specification.UrlSpecification;
import com.peter.shortener.features.url.utils.Base62;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HexFormat;
import java.util.UUID;

import static com.peter.shortener.features.shared.utils.ClientInfoUtils.getClientInfo;
import static com.peter.shortener.features.url.specification.UrlSpecification.withFilter;

@Service
@Transactional
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepo urlRepo;
    private final ClickRepo clickRepo;
    private final UrlMapper urlMapper;
    private final IdGenerator idGenerator;

    @Value("${app.click.ip-salt}")
    private String ipSalt;

    public UrlResponse generateUrlCode(UrlRequest request, UserPrincipal principal){
        Url url = urlMapper.toEntity(request);

        if (request.expiresInDays() != null) {
            url.setExpiresAt(Instant.now().plus(request.expiresInDays(), ChronoUnit.DAYS));
        }

        if (principal != null){
            url.setOwnerId(principal.id());
        }

        url.setCode(idGenerator.nextId());
        return urlMapper.toDto(urlRepo.save(url));
    }

    public String resolveAndTrack(String code){
        long snowflakeCode;
        try {
            snowflakeCode = Base62.decode(code);
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("URL code not found");
        }

        Url url = urlRepo.findByCode(snowflakeCode)
                .orElseThrow(() -> new NotFoundException("URL code not found"));

        Instant now = Instant.now();
        if (url.getExpiresAt() != null && url.getExpiresAt().isBefore(now)){
            throw new ExpiredException("The URL link is expired");
        }

        urlRepo.recordClick(url.getId(), now);

        ClientInfo clientInfo = getClientInfo();

        Click click = new Click();
        click.setUrl(url);
        click.setIpHash(hashIp(clientInfo.ipAddress()));
        click.setUserAgent(truncate(clientInfo.userAgent(), 512));
        click.setReferrer(extractHost(clientInfo.referrer()));
        clickRepo.save(click);

        return url.getOriginalUrl();
    }

    @Transactional(readOnly = true)
    public PageResponse<UrlResponse> getAllUrls(Pageable pageable, UrlFilter filter, UUID ownerId){
        Specification<Url> spec = UrlSpecification.ownedBy(ownerId).and(withFilter(filter));
        Page<Url> urlPage = urlRepo.findAll(spec, pageable);
        return PageResponse.from(urlPage.map(urlMapper::toDto));
    }

   @Transactional(readOnly = true)
    public UrlDetailedResponse getUrlById(UUID urlId, UUID ownerId){
        Url url = urlRepo.findByIdAndOwnerId(urlId, ownerId)
                .orElseThrow(() -> new NotFoundException("URL not found"));

        UrlStats stats = new UrlStats(
                clickRepo.countUniqueVisitors(urlId),
                clickRepo.clicksPerDay(urlId, Instant.now().minus(30, ChronoUnit.DAYS)),
                clickRepo.topReferrers(urlId, PageRequest.of(0, 5))
        );

        return urlMapper.toDetailedResponse(url).withStats(stats);
    }

    public UrlDetailedResponse updateUrl(UrlRequest request, UUID urlId, UUID ownerId){
        Url url = urlRepo.findByIdAndOwnerId(urlId, ownerId)
                .orElseThrow(() -> new NotFoundException("URL not found"));

        urlMapper.updateEntityFromDto(request, url);
        return urlMapper.toDetailedResponse(url);
    }

    public void deleteUrl(UUID urlId, UUID ownerId){
        Url url = urlRepo.findByIdAndOwnerId(urlId, ownerId)
                .orElseThrow(() -> new NotFoundException("URL not found"));

        clickRepo.deleteByUrlId(urlId);
        urlRepo.delete(url);
    }

    private String hashIp(String ip) {
        if (ip == null) return null;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(ipSalt.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return HexFormat.of().formatHex(mac.doFinal(ip.getBytes(StandardCharsets.UTF_8)));
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(e);
        }
    }

    private String extractHost(String referrer) {
        if (referrer == null || referrer.isBlank()) return null;
        try {
            String host = URI.create(referrer.trim()).getHost();
            return host == null ? null : truncate(host, 255);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private String truncate(String value, int max) {
        if (value == null) return null;
        return value.length() <= max ? value : value.substring(0, max);
    }

}
