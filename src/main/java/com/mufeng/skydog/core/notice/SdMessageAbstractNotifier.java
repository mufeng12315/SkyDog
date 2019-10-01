package com.mufeng.skydog.core.notice;

import com.mufeng.skydog.bean.SdMessageNoticeResult;
import com.mufeng.skydog.bean.SdNoticeMessage;
import com.mufeng.skydog.bean.SdReceiver;

/**
 * 消息通知器抽象类
 */
public abstract class SdMessageAbstractNotifier {

    /**
     * 通知信息
     * @param sdNoticeMessage 通知消息
     * @param sdReceiver 通知接收人
     */
    public abstract SdMessageNoticeResult notice(SdNoticeMessage sdNoticeMessage, SdReceiver sdReceiver);

}
