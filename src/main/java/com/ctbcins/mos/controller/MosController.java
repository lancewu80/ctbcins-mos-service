package com.ctbcins.mos.controller;

import com.ctbcins.mos.dto.ApiResponse;
import com.ctbcins.mos.dto.DownloadRequest;
import com.ctbcins.mos.dto.DownloadResponse;
import com.ctbcins.mos.dto.UploadRequest;
import com.ctbcins.mos.repository.TsAttachmentRepository;
import com.ctbcins.mos.service.AttachmentSchedulerService;
import com.ctbcins.mos.service.MosService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/mos")
public class MosController {

    private final MosService mosService;
    
    public MosController(MosService mosService) {
        this.mosService = mosService;
    }
    
    @Autowired
    private AttachmentSchedulerService attachmentSchedulerService;
    
    @Autowired
    private TsAttachmentRepository attachmentRepository;
    
    @PostMapping("/v1/download/fileUrl")
    public ResponseEntity<ApiResponse<DownloadResponse>> getDownloadUrl(
            @Valid @RequestBody DownloadRequest request) {
        log.info("Received download URL request: {}", request);
        ApiResponse<DownloadResponse> response = mosService.getDownloadUrl(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/v2/upload/file")
    public ResponseEntity<ApiResponse<Object>> uploadFile(
            @Valid @RequestBody UploadRequest request) {
        log.info("Received upload file request: {}", request);
        ApiResponse<Object> response = mosService.uploadFile(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("MOS Service is running");
    }
    
    @PostMapping("/scheduler/trigger")
    public ResponseEntity<String> triggerScheduler() {
        try {
            attachmentSchedulerService.manualTrigger();
            return ResponseEntity.ok("Scheduler triggered successfully");
        } catch (Exception e) {
            log.error("Error triggering scheduler", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error triggering scheduler: " + e.getMessage());
        }
    }

    @PostMapping("/scheduler/process-file/{fileName}")
    public ResponseEntity<String> processFileByName(@PathVariable String fileName) {
        try {
            attachmentSchedulerService.processAttachmentByFileName(fileName);
            return ResponseEntity.ok("File processing triggered for: " + fileName);
        } catch (Exception e) {
            log.error("Error processing file: {}", fileName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing file: " + e.getMessage());
        }
    }
    
    @GetMapping("/health/db")
    public ResponseEntity<String> checkDatabaseHealth() {
        try {
            long count = attachmentRepository.count();
            return ResponseEntity.ok("Database connection is healthy. Total attachments: " + count);
        } catch (Exception e) {
            log.error("Database health check failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Database connection failed: " + e.getMessage());
        }
    }
}