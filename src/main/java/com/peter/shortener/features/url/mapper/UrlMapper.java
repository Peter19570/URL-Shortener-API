package com.peter.shortener.features.url.mapper;

import com.peter.shortener.features.url.dto.request.UrlRequest;
import com.peter.shortener.features.url.dto.response.UrlDetailedResponse;
import com.peter.shortener.features.url.dto.response.UrlResponse;
import com.peter.shortener.features.url.model.Url;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UrlMapper {

    Url toEntity(UrlRequest request);

    @Mapping(target = "code", expression = "java(com.peter.shortener.features.url.utils.Base62.encode(url.getCode()))")
    UrlResponse toDto(Url url);

    @Mapping(target = "stats", ignore = true)
    @Mapping(target = "code", expression = "java(com.peter.shortener.features.url.utils.Base62.encode(url.getCode()))")
    UrlDetailedResponse toDetailedResponse(Url url);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(UrlRequest request, @MappingTarget Url url);
}
