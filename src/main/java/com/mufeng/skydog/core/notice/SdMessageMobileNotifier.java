package com.mufeng.skydog.core.notice;

import com.mufeng.skydog.bean.SdMessageNoticeResult;
import com.mufeng.skydog.bean.SdNoticeMessage;
import com.mufeng.skydog.bean.SdReceiver;
import org.springframework.stereotype.Service;

/**
 * 手机信息通知类
 */
@Service
public class SdMessageMobileNotifier extends SdMessageAbstractNotifier {

    @Override
    public SdMessageNoticeResult notice(SdNoticeMessage sdNoticeMessage, SdReceiver sdReceiver) {
        return null;
    }
}
