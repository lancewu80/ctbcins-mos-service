package com.ctbcins.mos.service;

import com.ctbcins.mos.dto.ApiResponse;
import com.ctbcins.mos.dto.UploadRequest;
import com.ctbcins.mos.entity.TsAttachment;
import com.ctbcins.mos.repository.TsAttachmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class AttachmentSchedulerService {

    @Autowired
    private TsAttachmentRepository attachmentRepository;
    
    @Autowired
    private MosService mosService;
    
    @Value("${scheduler.attachment.enabled:true}")
    private boolean schedulerEnabled;
    
    /**
     * 每小時執行一次的定時任務
     * 查詢上傳時間在當前時間之後的附件記錄
     */
    @Scheduled(cron = "${scheduler.attachment.cron:0 0 * * * *}")
    public void processRecentAttachments() {
        if (!schedulerEnabled) {
            log.info("Attachment scheduler is disabled");
            return;
        }
        
        try {
            log.info("Starting attachment scheduler task...");
            
            LocalDateTime now = LocalDateTime.now();
            List<TsAttachment> attachments = attachmentRepository.findByUploadTimeAfter(now);
            
            log.info("Found {} attachments to process", attachments.size());
            
            for (TsAttachment attachment : attachments) {
                try {
                    processAttachment(attachment);
                } catch (Exception e) {
                    log.error("Error processing attachment: {}", attachment.getName(), e);
                }
            }
            
            log.info("Attachment scheduler task completed");
            
        } catch (Exception e) {
            log.error("Error in attachment scheduler task", e);
        }
    }
    
    /**
     * 處理單個附件
     * @param attachment 附件實體
     */
    private void processAttachment(TsAttachment attachment) {
        log.info("Processing attachment: {}", attachment.getName());
        
        // 創建上傳請求
        UploadRequest uploadRequest = createUploadRequestFromAttachment(attachment);
        
        // 呼叫上傳 API
        ApiResponse<Object> response = mosService.uploadFile(uploadRequest);
        
        if ("0000".equals(response.getResultCode()) || "0201".equals(response.getResultCode())) {
            log.info("Successfully uploaded attachment: {}", attachment.getName());
        } else {
            log.error("Failed to upload attachment: {}. Error: {}", 
                     attachment.getName(), response.getErrorMessage());
        }
    }
    
    /**
     * 從附件實體創建上傳請求
     * @param attachment 附件實體
     * @return 上傳請求
     */
    private UploadRequest createUploadRequestFromAttachment(TsAttachment attachment) {
        UploadRequest request = new UploadRequest();
        
        // 設定基本檔案資訊
        request.setFileName(attachment.getName());
        request.setFilePath("/attachments/" + attachment.getName());
        request.setScenarioId("DANMC_BASIC");
        request.setSourceSystemId("CLM");
        request.setSourceSystemName("多元理賠");
        request.setBucketName("clm");
        request.setCreateUserId("system-scheduler");
        request.setAmendUserId("system-scheduler");
        
        // 設定檔案大小（如果有的話）
        if (attachment.getSize() != null) {
            // 可以根據需要設定其他屬性
        }
        
        // 設定索引關係（根據業務需求調整）
        // request.setIndexRel(createIndexRel(attachment));
        
        // 設定客戶資訊（根據業務需求調整）
        // request.setCustomerInfo(createCustomerInfo(attachment));
        
        return request;
    }
    
    /**
     * 根據特定檔案名稱處理附件
     * @param fileName 檔案名稱
     */
    public void processAttachmentByFileName(String fileName) {
        try {
            LocalDateTime now = LocalDateTime.now();
            List<TsAttachment> attachments = attachmentRepository.findByFileNameAndUploadTimeAfter(fileName, now);
            
            log.info("Found {} attachments with name: {}", attachments.size(), fileName);
            
            for (TsAttachment attachment : attachments) {
                processAttachment(attachment);
            }
            
        } catch (Exception e) {
            log.error("Error processing attachments by file name: {}", fileName, e);
        }
    }
    
    /**
     * 手動觸發處理任務（用於測試）
     */
    public void manualTrigger() {
        log.info("Manual trigger for attachment processing");
        processRecentAttachments();
    }
}