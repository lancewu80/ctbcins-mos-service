package com.ctbcins.mos.entity;

import lombok.Data;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "TsAttachment", schema = "dbo")  // 明確指定 schema 和表名
public class TsAttachment {
    
    @Id
    @Column(name = "FId", columnDefinition = "uniqueidentifier")
    private UUID id;
    
    @Column(name = "FName", columnDefinition = "nvarchar(255)")
    private String name;
    
    @Column(name = "FSize")
    private Integer size;
    
    @Column(name = "FUnitId", columnDefinition = "uniqueidentifier")
    private UUID unitId;
    
    @Column(name = "FEntityId", columnDefinition = "uniqueidentifier")
    private UUID entityId;
    
    @Column(name = "FUserId", columnDefinition = "uniqueidentifier")
    private UUID userId;
    
    @Column(name = "FUploadTime")
    private LocalDateTime uploadTime;
    
    @Column(name = "FDiskFileId", columnDefinition = "uniqueidentifier")
    private UUID diskFileId;
    
    @Column(name = "FDescription", columnDefinition = "nvarchar(200)")
    private String description;
    
    @Column(name = "FIsFromSupportMail")
    private Boolean isFromSupportMail;
    

    @Column(name = "FTextContent", columnDefinition = "nvarchar(MAX)")
    private String textContent;
    
    @Column(name = "UIsCcmsApi")
    private Boolean isCcmsApi;
    
    @Column(name = "UEOfficialDoc")
    private Boolean isOfficialDoc;
}