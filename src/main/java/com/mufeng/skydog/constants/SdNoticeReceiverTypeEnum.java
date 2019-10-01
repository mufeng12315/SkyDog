package com.mufeng.skydog.constants;

/**
 * 通知人类型
 */
public enum SdNoticeReceiverTypeEnum {

    MAIL("mail","邮箱"),
    MOBILE("mobile","手机");

    private String code;
    private String name;

    private SdNoticeReceiverTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static SdNoticeReceiverTypeEnum getEnumByCode(String code) {
        for (SdNoticeReceiverTypeEnum an : SdNoticeReceiverTypeEnum.values()) {
            if (an.getCode().equals(code))
                return an;
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
