package com.mufeng.skydog.constants;

/**
 * 配置项Code枚举
 */
public enum SdConfigCodeEnum {

    SENDER_MAIL("senderMail","邮件发送配置");

    private String code;
    private String name;

    private SdConfigCodeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static SdConfigCodeEnum getEnumByCode(String code) {
        for (SdConfigCodeEnum an : SdConfigCodeEnum.values()) {
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
