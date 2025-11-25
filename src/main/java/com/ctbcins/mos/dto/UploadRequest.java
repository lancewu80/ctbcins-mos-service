package com.ctbcins.mos.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
public class UploadRequest {
    @NotBlank(message = "filePath is required")
    private String filePath;
    
    @NotBlank(message = "fileName is required")
    private String fileName;
    
    @NotBlank(message = "scenarioId is required")
    private String scenarioId;
    
    @NotBlank(message = "sourceSystemId is required")
    private String sourceSystemId;
    
    @NotBlank(message = "sourceSystemName is required")
    private String sourceSystemName;
    
    @NotBlank(message = "bucketName is required")
    private String bucketName;
    
    private String insuranceTypeId;
    private String insuranceTypeName;
    
    @NotBlank(message = "createUserId is required")
    private String createUserId;
    
    @NotBlank(message = "amendUserId is required")
    private String amendUserId;
    
    private Map<String, Object> indexRel;
    private Map<String, Object> customerInfo;
}