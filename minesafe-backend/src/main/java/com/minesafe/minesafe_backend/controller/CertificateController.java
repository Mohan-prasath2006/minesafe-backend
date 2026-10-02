package com.minesafe.minesafe_backend.controller;

import com.minesafe.minesafe_backend.entity.CertificateEntity;
import com.minesafe.minesafe_backend.service.CertificateService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/certificates")
@CrossOrigin(origins = "*")
public class CertificateController {

    private final CertificateService certificateService;

    public CertificateController(
            CertificateService certificateService
    ) {
        this.certificateService = certificateService;
    }

    // =====================================================
    // CREATE CERTIFICATE
    // =====================================================

    @PostMapping
    public ResponseEntity<CertificateEntity> createCertificate(
            @RequestBody Map<String, Object> request
    ) {

        String candidateName =
                (String) request.get("candidateName");

        String scenario =
                (String) request.get("scenario");

        int score =
                ((Number) request.get("score")).intValue();

        int total =
                ((Number) request.get("total")).intValue();

        String completionTime =
                (String) request.get("completionTime");

        CertificateEntity certificate =
                certificateService.createCertificate(
                        candidateName,
                        scenario,
                        score,
                        total,
                        completionTime
                );

        return ResponseEntity.ok(certificate);
    }

    // =====================================================
    // VERIFY CERTIFICATE
    // =====================================================

    @GetMapping("/{certificateId}")
    public ResponseEntity<?> verifyCertificate(
            @PathVariable String certificateId
    ) {

        return certificateService
                .verifyCertificate(certificateId)
                .<ResponseEntity<?>>map(
                        ResponseEntity::ok
                )
                .orElseGet(
                        () -> ResponseEntity
                                .notFound()
                                .build()
                );
    }
}