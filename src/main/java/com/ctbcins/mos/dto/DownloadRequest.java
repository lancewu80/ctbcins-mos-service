package com.ctbcins.mos.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DownloadRequest {
    @NotBlank(message = "bucketName is required")
    private String bucketName;
    
    @NotBlank(message = "filePath is required")
    private String filePath;
    
    private String eTagId;
}