package com.mufeng.skydog.bean;

import lombok.Data;

/**
 * 告警信息接收者
 */
@Data
public class SdReceiver {
    /**
     * 接收者类型：邮件：手机
     */
    private String type;
    private String receiver;
}
