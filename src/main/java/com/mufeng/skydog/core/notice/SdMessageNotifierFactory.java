package com.mufeng.skydog.core.notice;

import com.mufeng.skydog.constants.SdNoticeReceiverTypeEnum;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 通知器工厂
 */
@Component
public class SdMessageNotifierFactory {

    @Resource
    private SdMessageMailNotifier sdMailMessageNotifier;

    @Resource
    private SdMessageMobileNotifier sdMobileMessageNotifier;

    /**
     * 获取通知器
     * @param type
     * @return
     */
    public SdMessageAbstractNotifier getMessageNotifier(String type){
        if(SdNoticeReceiverTypeEnum.MAIL.getCode().equals(type)){
            return sdMailMessageNotifier;
        }else if(SdNoticeReceiverTypeEnum.MOBILE.getCode().equals(type)){
            return sdMobileMessageNotifier;
        }
        return null;
    }
}
