package com.ctbcins.mos.service;

import com.ctbcins.mos.dto.ApiResponse;
import com.ctbcins.mos.dto.DownloadRequest;
import com.ctbcins.mos.dto.DownloadResponse;
import com.ctbcins.mos.dto.UploadRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class MosService {

    private final RestTemplate restTemplate;
    
    @Value("${mos.api.base-url:https://uat-kong.ctbcins.com:8443}")
    private String mosBaseUrl;
    
    @Value("${mos.api.download-endpoint:/mos/api/v1/download/fileUrl}")
    private String downloadEndpoint;
    
    @Value("${mos.api.upload-endpoint:/mos/api/v2/upload/file}")
    private String uploadEndpoint;
    
    public MosService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    public ApiResponse<DownloadResponse> getDownloadUrl(DownloadRequest request) {
        try {
            log.info("Getting download URL for bucket: {}, filePath: {}", 
                    request.getBucketName(), request.getFilePath());
            
            String url = mosBaseUrl + downloadEndpoint;
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<DownloadRequest> entity = new HttpEntity<>(request, headers);
            
            ResponseEntity<ApiResponse> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, ApiResponse.class);
            
            log.info("Download URL response: {}", response.getStatusCode());
            return response.getBody();
            
        } catch (Exception e) {
            log.error("Error getting download URL", e);
            return ApiResponse.<DownloadResponse>builder()
                    .resultCode("5000")
                    .resultDescription("系統錯誤")
                    .errorMessage(e.getMessage())
                    .build();
        }
    }
    
    public ApiResponse<Object> uploadFile(UploadRequest request) {
        try {
            log.info("Uploading file: {}, fileName: {}", 
                    request.getFilePath(), request.getFileName());
            
            String url = mosBaseUrl + uploadEndpoint;
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<UploadRequest> entity = new HttpEntity<>(request, headers);
            
            ResponseEntity<ApiResponse> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, ApiResponse.class);
            
            log.info("Upload file response: {}", response.getStatusCode());
            return response.getBody();
            
        } catch (Exception e) {
            log.error("Error uploading file", e);
            return ApiResponse.builder()
                    .resultCode("5000")
                    .resultDescription("系統錯誤")
                    .errorMessage(e.getMessage())
                    .build();
        }
    }
}