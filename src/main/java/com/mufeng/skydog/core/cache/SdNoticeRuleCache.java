package com.mufeng.skydog.core.cache;

import com.mufeng.skydog.bean.SdNoticeRule;
import com.mufeng.skydog.core.DynamicScheduler;
import com.mufeng.skydog.core.RuleCheckTask;
import com.mufeng.skydog.core.SdRuleCheckCoreService;
import com.mufeng.skydog.repo.SdNoticeRuleRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.util.List;

/**
 * 监控规则缓存
 */
@Slf4j
@Component
public class SdNoticeRuleCache extends AbstractCache<SdNoticeRule> {

    @Resource
    private SdRuleCheckCoreService sdRuleCheckCoreService;
    @Resource
    private SdNoticeRuleRepo sdNoticeRuleRepo;

    @Resource
    private DynamicScheduler dynamicScheduler;

    @Override
    public synchronized void update(String code, SdNoticeRule value) {
        SdNoticeRule lastSdNoticeRule = cacheMap.get(value.getRuleCode());
        //修改调度时间时才需要重新生成调度任务
        if(lastSdNoticeRule==null){
            //创建
            //创建调度任务
            RuleCheckTask ruleCheckTask = new RuleCheckTask(value.getRuleCode(),sdRuleCheckCoreService);
            //开始调试任务
            dynamicScheduler.addTask(ruleCheckTask,value.getScheduleExpress());
        }else if("N".equals(value.getIsEnable())){
            //停止旧的调度任务
            dynamicScheduler.stopTask(value.getRuleCode());
        }else {
            //更新
            if (!value.getScheduleExpress().equals(lastSdNoticeRule.getScheduleExpress()) ||
                    !value.getIsEnable().equals(lastSdNoticeRule.getIsEnable())) {
                //停止旧的调度任务
                dynamicScheduler.stopTask(value.getRuleCode());
                //创建新的调度任务
                RuleCheckTask ruleCheckTask = new RuleCheckTask(value.getRuleCode(), sdRuleCheckCoreService);
                dynamicScheduler.addTask(ruleCheckTask, value.getScheduleExpress());
            }
        }
        cacheMap.put(value.getRuleCode(),value);
    }

    /**
     * 删除规则
     * @param ruleCode
     */
    public synchronized void delete(String ruleCode){
        if(StringUtils.isEmpty(ruleCode)){
            log.info("ruleCode is null");
            return;
        }
        //停止旧的调度任务
        dynamicScheduler.stopTask(ruleCode);
        cacheMap.remove(ruleCode);
    }

    @Override
    public SdNoticeRule get(String code) {
        return cacheMap.get(code);
    }

    @Override
    public void initCache() {
        //1、从库中加载规则初始数据
        SdNoticeRule querySdNoticeRule = new SdNoticeRule();
        querySdNoticeRule.setIsEnable("Y");
        List<SdNoticeRule> sdNoticeRuleList = sdNoticeRuleRepo.selectList(querySdNoticeRule);
        if(CollectionUtils.isEmpty(sdNoticeRuleList)){
            log.info("sdNoticeRuleList is null");
            return;
        }
        for(SdNoticeRule sdNoticeRule:sdNoticeRuleList){
            if(StringUtils.isEmpty(sdNoticeRule.getScheduleExpress())){
                log.warn("ruleCode:{} sdNoticeRule.getScheduleExpress() is null",sdNoticeRule.getRuleCode());
                continue;
            }
            update(sdNoticeRule.getRuleCode(),sdNoticeRule);
        }
        log.info("initCache size:{}",sdNoticeRuleList.size());
    }
}
