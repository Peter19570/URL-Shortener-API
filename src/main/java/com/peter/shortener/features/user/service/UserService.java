package com.peter.shortener.features.user.service;

import com.peter.shortener.features.audit.dto.AuditRequest;
import com.peter.shortener.features.auth.dto.request.AccountDeletionRequest;
import com.peter.shortener.features.auth.repo.PasskeyRepo;
import com.peter.shortener.features.auth.repo.RefreshTokenRepo;
import com.peter.shortener.features.auth.service.helpers.AuthHelper;
import com.peter.shortener.features.auth.service.notification.EmailService;
import com.peter.shortener.features.auth.service.memory.OTPService;
import com.peter.shortener.features.user.dto.response.UserDetailedResponse;
import com.peter.shortener.features.user.mapper.UserMapper;
import com.peter.shortener.features.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static com.peter.shortener.features.audit.enums.AuditAction.ACCOUNT_SOFT_DELETED;
import static com.peter.shortener.features.shared.constants.CacheConstants.*;
import static com.peter.shortener.features.shared.utils.ClientInfoUtils.getClientInfo;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final AuthHelper authHelper;
    private final UserMapper userMapper;
    private final OTPService otpService;
    private final PasskeyRepo passkeyRepo;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepo refreshTokenRepo;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public UserDetailedResponse getCurrentUser(UUID userId){
        User currentUser = authHelper.getUser(userId);
        return userMapper.toDetailedDto(currentUser);
    }

    public void initiateDeletion(UUID userId) {
        User currentUser = authHelper.getUser(userId);
        String code = otpService.generateOtp(currentUser.getId().toString());
        emailService.sendAccountDeletionCode(currentUser, code);
    }

    @CacheEvict(cacheNames = USERS, key = "#userId")
    public void confirmSoftDelete(UUID userId, AccountDeletionRequest request) {
        User currentUser = authHelper.getUserFromDatabase(userId);

        if (currentUser.getPassword() != null) {
            if (!passwordEncoder.matches(request.password(), currentUser.getPassword())) {
                throw new BadCredentialsException("Invalid password provided for account deletion.");
            }
        }

        otpService.validateOtp(currentUser.getId().toString(),
                request.otp().replaceAll("\\s+", ""));

        currentUser.setDeletedAt(Instant.now());
        refreshTokenRepo.deleteAllByUserId(currentUser.getId());
        passkeyRepo.deleteAllByUserId(currentUser.getId());

        eventPublisher.publishEvent(AuditRequest.log(currentUser, ACCOUNT_SOFT_DELETED,
                "User account has been soft deleted", getClientInfo(), Map.of()));
    }
}
