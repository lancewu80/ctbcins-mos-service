package com.ctbcins.mos.repository;

import com.ctbcins.mos.entity.TsAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TsAttachmentRepository extends JpaRepository<TsAttachment, UUID> {
    
    /**
     * 使用原生 SQL 查詢所有上傳時間在指定時間之後的附件
     */
    @Query(value = "SELECT * FROM dbo.TsAttachment WHERE FUploadTime > :uploadTime", nativeQuery = true)
    List<TsAttachment> findByUploadTimeAfter(@Param("uploadTime") LocalDateTime uploadTime);
    
    /**
     * 使用原生 SQL 根據檔案名稱和上傳時間查詢附件
     */
    @Query(value = "SELECT * FROM dbo.TsAttachment WHERE FName = :fileName AND FUploadTime > :uploadTime", nativeQuery = true)
    List<TsAttachment> findByFileNameAndUploadTimeAfter(
            @Param("fileName") String fileName, 
            @Param("uploadTime") LocalDateTime uploadTime);
    
    /**
     * 使用原生 SQL 查詢特定實體的附件
     */
    @Query(value = "SELECT * FROM dbo.TsAttachment WHERE FEntityId = :entityId AND FUploadTime > :uploadTime", nativeQuery = true)
    List<TsAttachment> findByEntityIdAndUploadTimeAfter(
            @Param("entityId") UUID entityId, 
            @Param("uploadTime") LocalDateTime uploadTime);
    
    /**
     * 使用原生 SQL 查詢所有記錄（用於測試）
     */
    @Query(value = "SELECT * FROM dbo.TsAttachment", nativeQuery = true)
    List<TsAttachment> findAllNative();
}