package com.mufeng.skydog.core.notice;

import com.mufeng.skydog.bean.SdMailConfig;
import com.mufeng.skydog.bean.SdMessageNoticeResult;
import com.mufeng.skydog.bean.SdNoticeMessage;
import com.mufeng.skydog.bean.SdReceiver;
import com.mufeng.skydog.constants.SdConfigCodeEnum;
import com.mufeng.skydog.constants.SdErrorCodeEnum;
import com.mufeng.skydog.core.cache.SdConfigCache;
import com.sun.mail.util.MailSSLSocketFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * 邮箱信息通知类
 */
@Slf4j
@Service
public class SdMessageMailNotifier extends SdMessageAbstractNotifier {

//    @Value("${skyDog.mail.smtp.host}")
    public String smtpHost;

//    @Value("${skyDog.mail.smtp.port}")
    public String smtpPort;

//    @Value("${skyDog.mail.sender.username}")
    public String userName;

//    @Value("${skyDog.mail.sender.password}")
    public String password;

//    @Value("${skyDog.mail.sender.mail}")
    public String senderMail;

//    @Value("${skyDog.mail.sender.timeout}")
    public String timeout;

    @Resource
    private SdConfigCache sdConfigCache;

    @Override
    public SdMessageNoticeResult notice(SdNoticeMessage sdNoticeMessage, SdReceiver sdReceiver) {
        SdMessageNoticeResult result = new SdMessageNoticeResult();
        try{
            final SdMailConfig sdMailConfig = sdConfigCache.getConfigValue(SdConfigCodeEnum.SENDER_MAIL.getCode(), SdMailConfig.class);
            if(sdMailConfig==null){
                throw new RuntimeException("邮件发送配置为空，请先添加邮件发送配置");
            }
            // 1.创建一个程序与邮件服务器会话对象 Session
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "SMTP");
            props.setProperty("mail.smtp.host", sdMailConfig.getSmtpHost());
            props.setProperty("mail.smtp.port", sdMailConfig.getSmtpPort());
            // 指定验证为true
            props.setProperty("mail.smtp.auth", "true");
            props.setProperty("mail.smtp.timeout", sdMailConfig.getTimeout());
            //开 启 S S L 加 密，否 则 会 失 败
            props.put("mail.smtp.ssl.enable", "true");
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.smtp.ssl.socketFactory", sf);
            //解决附件文件名过长的问题
            props.put("mail.mime.splitlongparameters", "false");
            // 验证账号及密码，密码需要是第三方授权码
            Authenticator auth = new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication(){
                    return new PasswordAuthentication(sdMailConfig.getSenderUsername(), sdMailConfig.getSenderPassword());
                }
            };
            Session session = Session.getInstance(props, auth);
            // 2.创建一个Message，它相当于是邮件内容
            MimeMessage message = new MimeMessage(session);
            // 设置发送者
            message.setFrom(new InternetAddress(sdMailConfig.getSenderMail()));
            // 设置发送方式与接收者
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(sdReceiver.getReceiver()));
            // 设置主题
            message.setSubject(sdNoticeMessage.getTitle());
            // 设置内容
            message.setContent(sdNoticeMessage.getMessage(), "text/html;charset=utf-8");
            // 3.创建 Transport用于将邮件发送
            Transport.send(message);
            //发送成功
            result.setSucceedCode();
        }catch (Exception e){
            result.setCode(SdErrorCodeEnum.SYS_ERROR.getCode());
            result.setMessage(e.getMessage());
            log.error("notice error",e);
        }
        return result;
    }

}
