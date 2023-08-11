package com.boardgaming.domain.email.domain.repository;

import com.boardgaming.domain.email.domain.EmailLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {
}
