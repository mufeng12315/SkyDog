package com.mufeng.skydog.core;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 业务规则校验上下文
 */
@Data
public class SdRuleCheckContext {

    /**
     * 校验的业务数据（单jvm中可支持强转到任何类型，跨服务调用会自动转为LinkedHashMap，跨服务调用时不支持强转）
     */
    private Map<String,Object> busData;

    /**
     * 创建校验结果
     * @param code
     * @param message
     * @return
     */
//    public SdRuleCheckResultDTO makeCheckResult(String code, String message){
//        return new SdRuleCheckResultDTO(code,message);
//    }

}
