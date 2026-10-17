package com.peter.shortener.features.audit.mapper;

import com.peter.shortener.features.audit.dto.AuditResponse;
import com.peter.shortener.features.audit.model.AuditLog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuditMapper {

    AuditResponse toDto(AuditLog auditLog);
}
