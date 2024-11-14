package tn.pfe.CnotConnectV1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tn.pfe.CnotConnectV1.entities.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

}
