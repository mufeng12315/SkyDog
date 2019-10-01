package com.mufeng.skydog.bean;

import lombok.Data;

import java.util.List;

@Data
public class SdNoticeMessage {

    /**
     * 标题
     */
    private String title;

    /**
     * 通知消息
     */
    private String message;

    /**
     * 接收人
     */
    private List<SdReceiver> receiverList;

}
