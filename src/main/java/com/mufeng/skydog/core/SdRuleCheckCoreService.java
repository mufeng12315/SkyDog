package com.mufeng.skydog.core;

import com.mufeng.skydog.bean.*;
import com.mufeng.skydog.core.cache.SdNoticeRuleCache;
import com.mufeng.skydog.core.maker.SdNoticeContentMaker;
import com.mufeng.skydog.core.maker.SdNoticeContentMakerFactory;
import com.mufeng.skydog.core.notice.SdMessageNotifierWrapper;
import com.mufeng.skydog.repo.SdNoticeRuleRepo;
import com.mufeng.skydog.repo.SdSqlExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 规则校验核心类
 * @author tangqingsong
 */
@Service
@Slf4j
public class SdRuleCheckCoreService {

    @Resource
    private SdSqlExecutor sdSqlExecutor;

    @Resource
    private SdRuleEngine sdRuleEngine;

    @Resource
    private SdNoticeContentMakerFactory sdNoticeContentMakerFactory;

    @Resource
    private SdMessageNotifierWrapper sdMessageNotifierWrapper;

    @Resource
    private SdNoticeRuleRepo sdNoticeRuleRepo;

    @Resource
    private SdNoticeRuleCache sdNoticeRuleCache;

    public void checkRule(String ruleCode){
        //获取监控规则
        SdNoticeRule sdNoticeRule = sdNoticeRuleCache.get(ruleCode);
        if(sdNoticeRule==null){
            log.info("sdNoticeRule is null");
            return;
        }
        //构建监控指标
        List<SdMetric> sdMetricList = makeMetrics(sdNoticeRule);
        //校验监控规则
        checkRule(sdNoticeRule,sdMetricList);
    }

    /**
     * 校验监控规则
     * @param sdNoticeRule
     * @param sdMetricList
     */
    public void checkRule(SdNoticeRule sdNoticeRule, List<SdMetric> sdMetricList){
        //1、构建校验上下文
        SdRuleCheckContext sdRuleCheckContext = new SdRuleCheckContext();
        Map<String,Object> busData = new HashMap<String,Object>();
        for(SdMetric sdMetric:sdMetricList){
            busData.put(sdMetric.getMetricCode(),sdMetric.getMetricData());
        }
        sdRuleCheckContext.setBusData(busData);
        //校验规则
        Boolean checkResult = sdRuleEngine.checkRule(sdRuleCheckContext,sdNoticeRule.getRuleExpress());
        if(checkResult && sdNoticeRule.getNoticeContent()!=null ){
            //触发规则
            //获取通知内容生成器
            SdNoticeContentMaker sdNoticeContentMaker = sdNoticeContentMakerFactory.getContentMaker(sdNoticeRule.getNoticeContent().getType());
            //生成通知内容
            SdNoticeMessage sdNoticeMessage = sdNoticeContentMaker.makeContent(sdRuleCheckContext,sdNoticeRule);
            //通知接收人;
            sdMessageNotifierWrapper.notice(sdNoticeMessage);
        }
    }

    /**
     * 构建监控指标
     */
    public List<SdMetric> makeMetrics(SdNoticeRule sdNoticeRule){
        //获取监控指标对应的数据
        List<SdSqlContent> sdSqlContentList = sdNoticeRule.getSdSqlContentList();
        if(CollectionUtils.isEmpty(sdSqlContentList)){
            log.error("sdSqlContentList is null");
            throw new RuntimeException("sdSqlContentList is not null");
        }
        List<SdMetric> metricList = new ArrayList<SdMetric>();
        //构建指标
        for(SdSqlContent sdSqlContent :sdSqlContentList){
            String sql = sdSqlContent.getSqlContent();
            //获取监控来源数据
            List<SdRow> sdRowList = sdSqlExecutor.executeSql(sql,sdSqlContent.getDataSourceCode());
            //设置监控指标对应的数据
            metricList.addAll(makeMetrics(sdRowList));
        }
        return metricList;
    }

    /**
     * 根据查询结果构建监控指标
     * @param sdRowList
     */
    public List<SdMetric> makeMetrics(List<SdRow> sdRowList){
        List<SdMetric> metricList = new ArrayList<SdMetric>();
        if(CollectionUtils.isEmpty(sdRowList)){
            log.info("sdRowList is null");
            return metricList;
        }
        SdRow sdRow = sdRowList.get(0);
        for(SdColumn sdColumn:sdRow.getColumnMap().values()){
            SdMetric sdMetric = new SdMetric();
            sdMetric.setMetricCode(sdColumn.getColName());
            sdMetric.setMetricData(sdColumn.getColValue());
            metricList.add(sdMetric);
        }
        return metricList;
    }
}
