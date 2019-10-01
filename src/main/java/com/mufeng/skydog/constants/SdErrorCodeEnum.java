package com.mufeng.skydog.constants;

/**
 * 错误代码
 */
public enum SdErrorCodeEnum {

    SUCCEED("0","成功"),
    BUS_FAIL("1001","业务处理失败"),
    SYS_ERROR("2001","系统异常");

    private String code;
    private String name;

    private SdErrorCodeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static SdErrorCodeEnum getEnumByCode(String code) {
        for (SdErrorCodeEnum an : SdErrorCodeEnum.values()) {
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
