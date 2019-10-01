package com.mufeng.skydog.web;

import com.alibaba.fastjson.JSONObject;
import com.mufeng.skydog.bean.BaseResult;
import com.mufeng.skydog.bean.SdNoticeRule;
import com.mufeng.skydog.bean.SdSqlContent;
import com.mufeng.skydog.constants.SdErrorCodeEnum;
import com.mufeng.skydog.constants.SdNoticeContentTypeEnum;
import com.mufeng.skydog.core.cache.SdNoticeRuleCache;
import com.mufeng.skydog.repo.SdNoticeRuleRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/sdNoticeRule")
public class SdNoticeRuleController {

    @Resource
    private SdNoticeRuleRepo sdNoticeRuleRepo;

    @Resource
    private SdNoticeRuleCache sdNoticeRuleCache;

    @RequestMapping(value = "/selectList", method = RequestMethod.POST)
    public BaseResult<List<SdNoticeRule>> selectList(@RequestBody SdNoticeRule sdNoticeRule){
        BaseResult<List<SdNoticeRule>> result = new BaseResult<List<SdNoticeRule>>();
        List<SdNoticeRule> queryResult = sdNoticeRuleRepo.selectList(sdNoticeRule);
        result.setSuccess();
        result.setResult(queryResult);
        return result;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public BaseResult<Void> save(@RequestBody SdNoticeRule sdNoticeRule){
        BaseResult<Void> result = new BaseResult<>();
        String checkResultMessage = checkSaveData(sdNoticeRule);
        if(!StringUtils.isEmpty(checkResultMessage)){
            result.setCode(SdErrorCodeEnum.BUS_FAIL.getCode());
            result.setMessage(checkResultMessage);
            return result;
        }
        if(StringUtils.isEmpty(sdNoticeRule.getId())){
            //插入
            //1、保存到数据库
            sdNoticeRuleRepo.insert(sdNoticeRule);
        }else{
            //更新
            //1、先更新数据库
            sdNoticeRuleRepo.update(sdNoticeRule);
        }
        //2、更新上下文配置
        sdNoticeRuleCache.update(sdNoticeRule.getRuleCode(),sdNoticeRule);
        result.setSuccess();
        return result;
    }

    /**
     * 校验保存数据的完整性
     * @param sdNoticeRule
     * @return
     */
    private String checkSaveData(SdNoticeRule sdNoticeRule){
        StringBuilder stringBuilder = new StringBuilder();
        if(StringUtils.isEmpty(sdNoticeRule.getRuleCode())){
            stringBuilder.append("ruleCode不能为空");
        }
        if(StringUtils.isEmpty(sdNoticeRule.getIsEnable())){
            stringBuilder.append("isEnable不能为空");
        }
        if(StringUtils.isEmpty(sdNoticeRule.getRuleExpress())){
            stringBuilder.append("ruleExpress不能为空");
        }
        if(StringUtils.isEmpty(sdNoticeRule.getScheduleExpress())){
            stringBuilder.append("scheduleExpress不能为空");
        }
        if(sdNoticeRule.getNoticeContent()==null ||
                StringUtils.isEmpty(sdNoticeRule.getNoticeContent().getNoticeContent())){
            stringBuilder.append("noticeContent不能为空");
        }
        if(sdNoticeRule.getNoticeContent()!=null &&
                SdNoticeContentTypeEnum.SQL_RESULT.getCode().equals(sdNoticeRule.getNoticeContent().getType()) &&
                StringUtils.isEmpty(sdNoticeRule.getNoticeContent().getDataSourceCode())){
            stringBuilder.append("dataSourceCode不能为空");
        }
        return stringBuilder.toString();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public BaseResult<Void> update(@RequestBody SdNoticeRule sdNoticeRule){
        BaseResult<Void> result = new BaseResult<>();
        if(StringUtils.isEmpty(sdNoticeRule.getId())){
            result.setCode(SdErrorCodeEnum.BUS_FAIL.getCode());
            result.setMessage("id不能为空");
            return result;
        }
        //1、先更新数据库
        sdNoticeRuleRepo.update(sdNoticeRule);
        //2、更新上下文配置
        sdNoticeRuleCache.update(sdNoticeRule.getRuleCode(),sdNoticeRule);
        result.setSuccess();
        return result;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public BaseResult<Void> delete(@PathVariable("id") String id){
        BaseResult<Void> result = new BaseResult<>();
        if(StringUtils.isEmpty(id)){
            result.setCode(SdErrorCodeEnum.BUS_FAIL.getCode());
            result.setMessage("id不能为空");
            return result;
        }
        //1、先从上下文中删除
        SdNoticeRule sdNoticeRule = sdNoticeRuleRepo.selectByPrimaryKey(id);
        if(sdNoticeRule!=null){
            sdNoticeRuleCache.delete(sdNoticeRule.getRuleCode());
        }
        //2、再从数据库删除
        sdNoticeRuleRepo.delete(id);
        result.setSuccess();
        return result;
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BaseResult<SdNoticeRule> get(@PathVariable("id") String id){
        BaseResult<SdNoticeRule> result = new BaseResult<SdNoticeRule>();
        if(StringUtils.isEmpty(id)){
            result.setCode(SdErrorCodeEnum.BUS_FAIL.getCode());
            result.setMessage("id不能为空");
            return result;
        }
        SdNoticeRule sdNoticeRule = sdNoticeRuleRepo.selectByPrimaryKey(id);
        result.setResult(sdNoticeRule);
        result.setSuccess();
        return result;
    }

}
