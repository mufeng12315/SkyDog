package com.mufeng.skydog.repo;

import com.alibaba.fastjson.JSONObject;
import com.mufeng.skydog.bean.SdNoticeContent;
import com.mufeng.skydog.bean.SdNoticeRule;
import com.mufeng.skydog.bean.SdSqlContent;
import com.mufeng.skydog.constants.SdNoticeContentTypeEnum;
import com.mufeng.skydog.dao.SdNoticeRuleDAO;
import com.mufeng.skydog.dataobject.SdNoticeRuleDO;
import com.mufeng.skydog.utils.SkyDogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class SdNoticeRuleRepo {

    @Resource
    private SdNoticeRuleDAO sdNoticeRuleDAO;


//    /**
//     * 根据规则code查询
//     * @param ruleCode
//     * @return
//     */
//    public SdNoticeRule selectByRuleCode(String ruleCode) {
//        log.info("selectByRuleCode begin ruleCode:{}",ruleCode);
//        SdNoticeRuleDAO sdNoticeRuleDAO = SpringContextUtils.getBean("sdNoticeRuleDAO");
//        if(sdNoticeRuleDAO==null && skyDogConfigure.getNoticeRuleMap().containsKey(ruleCode)){
//            //1、从配置文件中读取配置
//            log.info("read config ruleCode:{}",ruleCode);
//            return skyDogConfigure.getNoticeRuleMap().get(ruleCode);
//        }
//        //2、从数据库中查询配置
//        List<SdNoticeRule> sdNoticeRuleList = null;
//        SdNoticeRuleDO sdNoticeRuleDO = new SdNoticeRuleDO();
//        sdNoticeRuleDO.setRuleCode(ruleCode);
//        List<SdNoticeRuleDO> sdNoticeRuleDOList = sdNoticeRuleDAO.selectListByDO(sdNoticeRuleDO);
//        if (CollectionUtils.isNotEmpty(sdNoticeRuleDOList)) {
//            sdNoticeRuleList = SkyDogUtils.doToDtoByList(sdNoticeRuleDOList, SdNoticeRule.class);
//            return sdNoticeRuleList.get(0);
//        }
//        log.info("selectByRuleCode end");
//        return null;
//    }

    /**
     * 查询实体
     * @param sdNoticeRule
     * @return
     */
    public List<SdNoticeRule> selectList(SdNoticeRule sdNoticeRule) {
        log.info("selectList:{}", JSONObject.toJSONString(sdNoticeRule));
        //从数据库中查询配置
        SdNoticeRuleDO sdNoticeRuleDO = new SdNoticeRuleDO();
        SkyDogUtils.beanCopy(sdNoticeRule,sdNoticeRuleDO);
        List<SdNoticeRuleDO> sdNoticeRuleDOList = sdNoticeRuleDAO.selectListByDO(sdNoticeRuleDO);
        if (CollectionUtils.isEmpty(sdNoticeRuleDOList)) {
            return null;
        }
        List<SdNoticeRule> sdNoticeRuleList = new ArrayList<SdNoticeRule>();
        for(SdNoticeRuleDO beanDO:sdNoticeRuleDOList){
            SdNoticeRule bean = new SdNoticeRule();
            SkyDogUtils.beanCopy(beanDO,bean);
            //把sql内容从json转为bean
            if(!StringUtils.isEmpty(beanDO.getSqlContent())){
                List<SdSqlContent> sdSqlContentList = JSONObject.parseArray(beanDO.getSqlContent(), SdSqlContent.class);
                bean.setSdSqlContentList(sdSqlContentList);
            }
            //把通知内容从json转为bean
            if(!StringUtils.isEmpty(beanDO.getNoticeContent())){
                SdNoticeContent sdNoticeContent = JSONObject.parseObject(beanDO.getNoticeContent(), SdNoticeContent.class);
                bean.setNoticeContent(sdNoticeContent);
            }
            sdNoticeRuleList.add(bean);
        }
        log.info("selectList end");
        return sdNoticeRuleList;
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public SdNoticeRule selectByPrimaryKey(String id) {
        log.info("selectByPrimaryKey:{}", id);
        SdNoticeRuleDO sdNoticeRuleDO = sdNoticeRuleDAO.selectByPrimaryKey(id);
        if(sdNoticeRuleDO!=null){
            SdNoticeRule sdNoticeRule = new SdNoticeRule();
            SkyDogUtils.beanCopy(sdNoticeRuleDO,sdNoticeRule);
            //把sql内容从json转为bean
            if(!StringUtils.isEmpty(sdNoticeRuleDO.getSqlContent())){
                List<SdSqlContent> sdSqlContentList = JSONObject.parseArray(sdNoticeRuleDO.getSqlContent(), SdSqlContent.class);
                sdNoticeRule.setSdSqlContentList(sdSqlContentList);
            }
            //把通知内容从json转为bean
            if(!StringUtils.isEmpty(sdNoticeRuleDO.getNoticeContent())){
                SdNoticeContent sdNoticeContent = JSONObject.parseObject(sdNoticeRuleDO.getNoticeContent(), SdNoticeContent.class);
                sdNoticeRule.setNoticeContent(sdNoticeContent);
            }
            return sdNoticeRule;
        }
        return null;
    }

    /**
     * 删除实体
     * @param id
     * @return
     */
    public Integer delete(String id) {
        log.info("deleteByPrimaryKey:{}", id);
        if(id==null || id.isEmpty()){
            throw new RuntimeException("id is not null");
        }
        return sdNoticeRuleDAO.deleteByPrimaryKey(id);
    }

    /**
     * 更新实体
     * @param sdNoticeRule
     * @return
     */
    public Integer update(SdNoticeRule sdNoticeRule) {
        log.info("update:{}", JSONObject.toJSONString(sdNoticeRule));
        if(sdNoticeRule==null || StringUtils.isEmpty(sdNoticeRule.getId())){
            throw new RuntimeException("id is not null");
        }
        sdNoticeRule.setGmtModified(SkyDogUtils.formatDateYYYY_MM_DD_HH_MM_SS(new Date()));
        SdNoticeRuleDO sdNoticeRuleDO = new SdNoticeRuleDO();
        //SQL内容转为json存储
        if(!CollectionUtils.isEmpty(sdNoticeRule.getSdSqlContentList())){
            sdNoticeRuleDO.setSqlContent(JSONObject.toJSONString(sdNoticeRule.getSdSqlContentList()));
        }
        //通知结果转为json存储
        if(sdNoticeRule.getNoticeContent()!=null){
            sdNoticeRuleDO.setNoticeContent(JSONObject.toJSONString(sdNoticeRule.getNoticeContent()));
        }
        SkyDogUtils.beanCopy(sdNoticeRule,sdNoticeRuleDO);
        return sdNoticeRuleDAO.updateByPrimaryKeySelective(sdNoticeRuleDO);
    }

    /**
     * 新增实体
     * @param sdNoticeRule
     * @return
     */
    public Integer insert(SdNoticeRule sdNoticeRule) {
        log.info("insert:{}", JSONObject.toJSONString(sdNoticeRule));
        if(sdNoticeRule==null){
            throw new RuntimeException("id is not null");
        }
        sdNoticeRule.setId(UUID.randomUUID().toString());
        sdNoticeRule.setGmtCreated(SkyDogUtils.formatDateYYYY_MM_DD_HH_MM_SS(new Date()));
        sdNoticeRule.setGmtModified(SkyDogUtils.formatDateYYYY_MM_DD_HH_MM_SS(new Date()));
        SdNoticeRuleDO sdNoticeRuleDO = new SdNoticeRuleDO();
        //SQL内容转为json存储
        if(!CollectionUtils.isEmpty(sdNoticeRule.getSdSqlContentList())){
            sdNoticeRuleDO.setSqlContent(JSONObject.toJSONString(sdNoticeRule.getSdSqlContentList()));
        }
        //通知结果转为json存储
        if(sdNoticeRule.getNoticeContent()!=null){
            sdNoticeRuleDO.setNoticeContent(JSONObject.toJSONString(sdNoticeRule.getNoticeContent()));
        }
        SkyDogUtils.beanCopy(sdNoticeRule,sdNoticeRuleDO);

        return sdNoticeRuleDAO.insert(sdNoticeRuleDO);
    }
}
