package com.peter.shortener.features.audit.repo;

import com.peter.shortener.features.audit.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditRepo extends JpaRepository<AuditLog, Long> {
}
