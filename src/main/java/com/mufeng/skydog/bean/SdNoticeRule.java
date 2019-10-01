package com.mufeng.skydog.bean;

import lombok.Data;

import java.util.List;

/**
 * 告警规则
 */
@Data
public class SdNoticeRule {
    private String id;
    private String ruleCode;
    private String ruleName;

    /**
     * 告警规则表达式
     */
    private String ruleExpress;

    /**
     * 定时调度表达式，支持Con表达式
     */
    private String scheduleExpress;

    /**
     * 通知内容
     */
    private SdNoticeContent noticeContent;

    /**
     * 告警消息接收人邮件，多个接收人时以逗号分割
     */
    private String receiverMail;

    /**
     * 告警消息接收人手机，多个接收人时以逗号分割
     */
    private String receiverMobile;

//    /**
//     * sql内容，多个sql以分号分割，格式为：dataSource:sql1;datasource:sql2
//     */
//    private String sqlContent;

    /**
     * SQL内容（监控指标从SQL结果中提取）
     */
    private List<SdSqlContent> sdSqlContentList;

    private String gmtCreated;

    private String gmtModified;

    private String isEnable;
}
