package com.ctbcins.mos.dto;

import lombok.Data;

@Data
public class DownloadResponse {
    private IndexInfo indexs;
    private String fileDownloadUrl;
    
    @Data
    public static class IndexInfo {
        private String bucketName;
        private String objectName;
    }
}