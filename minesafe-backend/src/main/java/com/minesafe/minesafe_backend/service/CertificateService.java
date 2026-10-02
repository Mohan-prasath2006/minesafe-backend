package com.minesafe.minesafe_backend.service;

import com.minesafe.minesafe_backend.entity.CertificateEntity;
import com.minesafe.minesafe_backend.repository.CertificateRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class CertificateService {

    private final CertificateRepository certificateRepository;

    public CertificateService(
            CertificateRepository certificateRepository
    ) {
        this.certificateRepository =
                certificateRepository;
    }

    public CertificateEntity createCertificate(
            String candidateName,
            String scenario,
            int score,
            int total,
            String completionTime
    ) {

        CertificateEntity certificate =
                new CertificateEntity();

        // Generate unique Certificate ID
        String certificateId =
                "MS-" + System.currentTimeMillis();

        certificate.setCertificateId(
                certificateId
        );

        certificate.setCandidateName(
                candidateName
        );

        certificate.setScenario(
                scenario
        );

        certificate.setScore(
                score
        );

        certificate.setTotal(
                total
        );

        certificate.setStatus(
                score >= 50
                        ? "PASSED"
                        : "FAILED"
        );

        certificate.setCompletionTime(
                completionTime
        );

        certificate.setIssueDate(
                LocalDate.now().toString()
        );

        return certificateRepository.save(
                certificate
        );
    }

    public Optional<CertificateEntity> verifyCertificate(
            String certificateId
    ) {

        return certificateRepository
                .findByCertificateId(
                        certificateId
                );
    }
}