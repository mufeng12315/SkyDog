package com.mufeng.skydog.dataobject;

import com.mufeng.skydog.bean.SdNoticeContent;
import lombok.Data;


@Data
public class SdNoticeRuleDO {
    private String id;

    private String ruleCode;

    private String ruleName;

    private String ruleExpress;

    private String scheduleExpress;
    /**
     * 通知内容（以json存储）
     */
    private String noticeContent;

    private String receiverMail;

    private String receiverMobile;

    private String gmtCreated;

    private String gmtModified;

    private String isEnable;

    /**
     * sql内容：以json存储
     */
    private String sqlContent;

}
