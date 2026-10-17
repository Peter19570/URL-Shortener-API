package com.peter.shortener.features.audit.service;

import com.peter.shortener.features.audit.dto.AuditRequest;
import com.peter.shortener.features.audit.dto.AuditResponse;
import com.peter.shortener.features.audit.mapper.AuditMapper;
import com.peter.shortener.features.audit.model.AuditLog;
import com.peter.shortener.features.audit.repo.AuditRepo;
import com.peter.shortener.features.shared.dto.PageResponse;
import com.peter.shortener.features.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuditService {

    private final AuditRepo auditRepo;
    private final AuditMapper auditMapper;

    @Async
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createAuditLog(AuditRequest request) {
        AuditLog audit = AuditLog.builder()
                .userId(request.user().getId())
                .email(request.user().getEmail())
                .fullName(getUserFullName(request.user()))
                .description(request.description())
                .ipAddress(request.ipAddress())
                .userAgent(request.userAgent())
                .action(request.auditAction())
                .metadata(request.metaData())
                .build();

        auditRepo.save(audit);
    }

    public PageResponse<AuditResponse> getAllAudits(Pageable pageable){
        Page<AuditLog> responses = auditRepo.findAll(pageable);
        return PageResponse.from(responses.map(auditMapper::toDto));
    }

    private String getUserFullName(User user){
        if (user.getFirstName().equals("not-set") && user.getLastName().equals("not-set")){
            return "Unknown";
        }

        return user.getFirstName() + " " + user.getLastName();
    }
}
