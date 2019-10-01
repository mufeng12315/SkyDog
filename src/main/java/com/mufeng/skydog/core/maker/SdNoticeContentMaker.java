package com.mufeng.skydog.core.maker;

import com.mufeng.skydog.bean.SdNoticeMessage;
import com.mufeng.skydog.bean.SdNoticeRule;
import com.mufeng.skydog.bean.SdReceiver;
import com.mufeng.skydog.constants.SdNoticeReceiverTypeEnum;
import com.mufeng.skydog.core.SdRuleCheckContext;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 通知内容生产接口
 */
public abstract class SdNoticeContentMaker {

    public abstract SdNoticeMessage makeContent(SdRuleCheckContext sdRuleCheckContext, SdNoticeRule sdNoticeRule);

    /**
     * 获取规则中配置的通知人信息
     * @param sdNoticeRule
     * @return
     */
    protected List<SdReceiver> getSdReceiverList(SdNoticeRule sdNoticeRule){
        List<SdReceiver> mailReceiverList = getSdReceiverList(sdNoticeRule.getReceiverMail(), SdNoticeReceiverTypeEnum.MAIL.getCode());
        List<SdReceiver> mobileReceiverList = getSdReceiverList(sdNoticeRule.getReceiverMobile(), SdNoticeReceiverTypeEnum.MOBILE.getCode());
        mailReceiverList.addAll(mobileReceiverList);
        return mailReceiverList;
    }

    /**
     * 获取接收者List
     * @param receiver
     * @param type
     * @return
     */
    protected List<SdReceiver> getSdReceiverList(String receiver,String type){
        List<SdReceiver> receiverList = new ArrayList<>();
        if(StringUtils.isEmpty(receiver)){
            return receiverList;
        }
        String[] receiverStrList = receiver.split(",");
        for(String receiverStr:receiverStrList){
            SdReceiver sdReceiver = new SdReceiver();
            sdReceiver.setReceiver(receiverStr);
            sdReceiver.setType(type);
            receiverList.add(sdReceiver);
        }
        return receiverList;
    }

}
