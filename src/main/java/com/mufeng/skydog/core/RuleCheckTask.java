package com.mufeng.skydog.core;

import com.mufeng.skydog.core.SdRuleCheckCoreService;
import lombok.extern.slf4j.Slf4j;

/**
 * 规则校验task
 */
@Slf4j
public class RuleCheckTask implements Runnable {

    private String taskCode;
    private SdRuleCheckCoreService sdRuleCheckCoreService;

    public RuleCheckTask(String taskCode,SdRuleCheckCoreService sdRuleCheckCoreService){
        this.taskCode = taskCode;
        this.sdRuleCheckCoreService = sdRuleCheckCoreService;
    }

    @Override
    public void run() {
        try{
            log.info("checkRule taskCode:{} begin",taskCode);
            sdRuleCheckCoreService.checkRule(taskCode);
            log.info("checkRule taskCode:{} end",taskCode);
        }catch (Exception e){
            log.error("RuleCheckTask error",e);
        }
    }

    public String getTaskCode() {
        return taskCode;
    }
}
