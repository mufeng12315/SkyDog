package com.mufeng.skydog.core.cache;

import com.alibaba.fastjson.JSONObject;
import com.mufeng.skydog.bean.SdConfig;
import com.mufeng.skydog.bean.SdConfig;
import com.mufeng.skydog.bean.SdConfigValue;
import com.mufeng.skydog.bean.SdMailConfig;
import com.mufeng.skydog.constants.SdConfigCodeEnum;
import com.mufeng.skydog.repo.SdConfigRepo;
import com.mufeng.skydog.repo.SdConfigRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 配置缓存（邮件配置…）
 */
@Slf4j
@Component
public class SdConfigCache extends AbstractCache<SdConfigValue>{

    @Resource
    private SdConfigRepo sdConfigRepo;

    public<T> T getConfigValue(String code,Class<T> clas){
        SdConfigValue sdConfigValue = cacheMap.get(code);
        if(sdConfigValue!=null && !StringUtils.isEmpty(sdConfigValue.getConfigValue())){
            return JSONObject.parseObject(sdConfigValue.getConfigValue(),clas);
        }
        return null;
    }

    /**
     *更新
     */
    @Override
    public void update(String code, SdConfigValue value) {
        cacheMap.put(code,value);
    }

    /**
     * 删除
     * @param code
     */
    public synchronized void delete(String code){
        cacheMap.remove(code);
    }

    @Override
    public SdConfigValue get(String code) {
        return cacheMap.get(code);
    }

    @Override
    public void initCache() {
        //1、从库中加载规则初始数据
        SdConfig querySdConfig = new SdConfig();
        querySdConfig.setCode(SdConfigCodeEnum.SENDER_MAIL.getCode());
        List<SdConfigValue> configValueList = sdConfigRepo.selectListConfigValue(querySdConfig);
        if(CollectionUtils.isNotEmpty(configValueList)){
            for(SdConfigValue sdConfigValue:configValueList){
                update(sdConfigValue.getCode(),sdConfigValue);
            }
        }
    }
}
