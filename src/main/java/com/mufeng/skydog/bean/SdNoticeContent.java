package com.mufeng.skydog.bean;

import lombok.Data;

/**
 * 通知内容
 */
@Data
public class SdNoticeContent {
    /**
     * 通知内容类型：表达式、SQL查询结果
     */
    private String type;

    /**
     * 当通知内容类型为SQL查询结果时，该字段为数据库Code
     */
    private String dataSourceCode;

    /**
     * 通知内容：类型为表达式时为表达式脚本，SQL查询结果时为查询SQL；
     */
    private String noticeContent;



}
