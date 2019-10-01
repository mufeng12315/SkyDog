package com.mufeng.skydog.core.notice;

import com.alibaba.fastjson.JSONObject;
import com.mufeng.skydog.bean.SdMessageNoticeResult;
import com.mufeng.skydog.bean.SdNoticeMessage;
import com.mufeng.skydog.bean.SdReceiver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;

/**
 * 消息通知器
 */
@Service
@Slf4j
public class SdMessageNotifierWrapper {

    @Resource
    private SdMessageNotifierFactory sdMessageNotifierFactory;

    public void notice(SdNoticeMessage sdNoticeMessage) {
        if(sdNoticeMessage==null || CollectionUtils.isEmpty(sdNoticeMessage.getReceiverList())){
            log.warn("sdNoticeMessage.getReceiverList() is null");
            return;
        }
        for(SdReceiver sdReceiver:sdNoticeMessage.getReceiverList()){
            SdMessageAbstractNotifier sdMessageAbstractNotifier = sdMessageNotifierFactory.getMessageNotifier(sdReceiver.getType());
            SdMessageNoticeResult result = sdMessageAbstractNotifier.notice(sdNoticeMessage,sdReceiver);
            log.info("title:{} receiver:{},result:{}",sdNoticeMessage.getTitle(),sdReceiver.getReceiver(), JSONObject.toJSONString(result));
        }
    }
}
