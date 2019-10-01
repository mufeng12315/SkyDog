package com.mufeng.skydog.bean;
import lombok.Data;

/**
 * 邮箱发送bean
 */
@Data
public class SdMailConfig{

    public String smtpHost;

    public String smtpPort;

    public String senderUsername;

    public String senderPassword;

    public String senderMail;

    public String timeout;

}
