package com.ctbcins.mos.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "scheduler.attachment.enabled", havingValue = "true", matchIfMissing = true)
public class SchedulerConfig {
    // 定時任務配置
}