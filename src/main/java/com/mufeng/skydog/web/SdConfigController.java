package com.mufeng.skydog.web;

import com.alibaba.fastjson.JSONObject;
import com.mufeng.skydog.bean.BaseResult;
import com.mufeng.skydog.bean.SdConfig;
import com.mufeng.skydog.bean.SdConfigValue;
import com.mufeng.skydog.bean.SdMailConfig;
import com.mufeng.skydog.constants.SdErrorCodeEnum;
import com.mufeng.skydog.core.cache.SdConfigCache;
import com.mufeng.skydog.repo.SdConfigRepo;
import com.mufeng.skydog.utils.SkyDogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/sdConfig")
public class SdConfigController {

    @Resource
    private SdConfigRepo sdConfigRepo;

    @Resource
    private SdConfigCache sdConfigCache;

    /**
     * 保存与更新配置
     * @param sdConfig
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public BaseResult<Void> save(@RequestBody SdConfig sdConfig){
        BaseResult<Void> result = new BaseResult<>();
        if(StringUtils.isEmpty(sdConfig.getCode())){
            result.setCode(SdErrorCodeEnum.BUS_FAIL.getCode());
            result.setMessage("code不能为空");
            return result;
        }
        if(sdConfig.getConfigValue()==null){
            result.setCode(SdErrorCodeEnum.BUS_FAIL.getCode());
            result.setMessage("configValue不能为空");
            return result;
        }
        SdConfig querySdConfig = new SdConfig();
        querySdConfig.setCode(sdConfig.getCode());
        List<SdConfig<SdMailConfig>> sdConfigList = sdConfigRepo.selectList(querySdConfig, SdMailConfig.class);
        if(CollectionUtils.isEmpty(sdConfigList)){
            //新增
            sdConfigRepo.insert(sdConfig);
        }else{
            //保存
            SdConfig dbSdConfig = sdConfigList.get(0);
            SkyDogUtils.beanCopy(sdConfig,dbSdConfig);
            sdConfigRepo.update(dbSdConfig);
        }
        //更新缓存
        sdConfigCache.update(sdConfig.getCode(),new SdConfigValue(sdConfig.getCode(), JSONObject.toJSONString(sdConfig.getConfigValue())));
        result.setSuccess();
        return result;
    }

    /**
     *  根据配置code获取配置项
     * @param code
     * @return
     */
    @RequestMapping(value = "/get/{code}", method = RequestMethod.GET)
    public BaseResult<SdConfig> get(@PathVariable("code") String code){
        BaseResult<SdConfig> result = new BaseResult<SdConfig>();
        if(StringUtils.isEmpty(code)){
            result.setCode(SdErrorCodeEnum.BUS_FAIL.getCode());
            result.setMessage("code不能为空");
            return result;
        }
        SdConfig querySdConfig = new SdConfig();
        querySdConfig.setCode(code);
        List<SdConfig<SdMailConfig>> sdConfigList = sdConfigRepo.selectList(querySdConfig,SdMailConfig.class);
        if(!CollectionUtils.isEmpty(sdConfigList)){
            result.setResult(sdConfigList.get(0));
        }
        result.setSuccess();
        return result;
    }

}
