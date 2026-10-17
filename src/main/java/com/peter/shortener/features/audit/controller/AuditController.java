package com.peter.shortener.features.audit.controller;

import com.peter.shortener.features.audit.dto.AuditResponse;
import com.peter.shortener.features.audit.service.AuditService;
import com.peter.shortener.features.shared.dto.ApiResponse;
import com.peter.shortener.features.shared.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.peter.shortener.features.shared.constants.PageConstants.DEFAULT_SORT_FIELD;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/audits")
@Tag(name = "Audit-Logging", description = "Light auditing in place to keep track of activities happening")
public class AuditController {

    private final AuditService auditService;

    @GetMapping
    @Operation(summary = "Retrieve audit records.")
    public ResponseEntity<ApiResponse<PageResponse<AuditResponse>>> getAllAudits(
            @PageableDefault(
                    page = 1, sort = DEFAULT_SORT_FIELD,
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {
        PageResponse<AuditResponse> responses = auditService.getAllAudits(pageable);
        return ResponseEntity.ok(ApiResponse.success("All Audit Logs", responses));
    }
}
