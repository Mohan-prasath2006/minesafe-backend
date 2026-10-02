package com.minesafe.minesafe_backend.repository;

import com.minesafe.minesafe_backend.entity.CertificateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CertificateRepository
        extends JpaRepository<CertificateEntity, Long> {

    Optional<CertificateEntity>
    findByCertificateId(String certificateId);
}