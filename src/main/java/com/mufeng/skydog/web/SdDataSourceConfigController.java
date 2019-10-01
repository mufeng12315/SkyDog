package com.mufeng.skydog.web;

import com.mufeng.skydog.bean.*;
import com.mufeng.skydog.bean.SdDataSourceConfig;
import com.mufeng.skydog.constants.SdErrorCodeEnum;
import com.mufeng.skydog.core.cache.SdDataSourceConfigCache;
import com.mufeng.skydog.repo.SdDataSourceConfigRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/sdDataSourceConfig")
public class SdDataSourceConfigController {

    @Resource
    private SdDataSourceConfigRepo sdDataSourceConfigRepo;

    @Resource
    private SdDataSourceConfigCache sdDataSourceConfigCache;


    @RequestMapping(value = "/selectList", method = RequestMethod.POST)
    public BaseResult<List<SdDataSourceConfig>> selectList(@RequestBody SdDataSourceConfig sdDataSourceConfig){
        BaseResult<List<SdDataSourceConfig>> result = new BaseResult<List<SdDataSourceConfig>>();
        List<SdDataSourceConfig> queryResult = sdDataSourceConfigRepo.selectList(sdDataSourceConfig);
        result.setSuccess();
        result.setResult(queryResult);
        return result;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public BaseResult<Void> save(@RequestBody SdDataSourceConfig sdDataSourceConfig){
        BaseResult<Void> result = new BaseResult<>();
        if(StringUtils.isEmpty(sdDataSourceConfig.getCode())){
            result.setCode(SdErrorCodeEnum.BUS_FAIL.getCode());
            result.setMessage("code不能为空");
            return result;
        }
        if(StringUtils.isEmpty(sdDataSourceConfig.getId())){
            //新增
            //1、保存到库中
            sdDataSourceConfigRepo.insert(sdDataSourceConfig);
        }else{
            //更新
            //1、更新到数据库中
            sdDataSourceConfigRepo.update(sdDataSourceConfig);
        }
        //2、保存到上下文中
        sdDataSourceConfigCache.update(sdDataSourceConfig.getCode(),sdDataSourceConfig);
        result.setSuccess();
        return result;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public BaseResult<Void> update(@RequestBody SdDataSourceConfig sdDataSourceConfig){
        BaseResult<Void> result = new BaseResult<>();
        //1、更新到数据库中
        sdDataSourceConfigRepo.update(sdDataSourceConfig);
        //2、更新到上下文
        sdDataSourceConfigCache.update(sdDataSourceConfig.getCode(),sdDataSourceConfig);
        result.setSuccess();
        return result;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public BaseResult<Void> delete(@PathVariable("id") String id){
        BaseResult<Void> result = new BaseResult<>();
        //1、先从上下文中删除
        SdDataSourceConfig sdDataSourceConfig = sdDataSourceConfigRepo.selectByPrimaryKey(id);
        if(sdDataSourceConfig!=null) {
            sdDataSourceConfigCache.delete(sdDataSourceConfig.getCode());
        }
        //2、从数据库中删除
        sdDataSourceConfigRepo.delete(id);
        result.setSuccess();
        return result;
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BaseResult<SdDataSourceConfig> get(@PathVariable("id") String id){
        BaseResult<SdDataSourceConfig> result = new BaseResult<SdDataSourceConfig>();
        if(StringUtils.isEmpty(id)){
            result.setCode(SdErrorCodeEnum.BUS_FAIL.getCode());
            result.setMessage("id不能为空");
            return result;
        }
        SdDataSourceConfig sdDataSourceConfig = sdDataSourceConfigRepo.selectByPrimaryKey(id);
        result.setResult(sdDataSourceConfig);
        result.setSuccess();
        return result;
    }

}
