package com.mufeng.skydog.repo;

import com.alibaba.fastjson.JSONObject;
import com.mufeng.skydog.bean.SdConfig;
import com.mufeng.skydog.bean.SdConfigValue;
import com.mufeng.skydog.dao.SdConfigDAO;
import com.mufeng.skydog.dataobject.SdConfigDO;
import com.mufeng.skydog.utils.SkyDogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service
public class SdConfigRepo {

    @Resource
    private SdConfigDAO sdConfigDAO;

    /**
     * 查询配置内容
     * @param sdConfig
     * @return
     */
    public List<SdConfigValue> selectListConfigValue(SdConfig sdConfig) {
        log.info("selectList:{}", JSONObject.toJSONString(sdConfig));
        //从数据库中查询配置
        SdConfigDO sdConfigDO = new SdConfigDO();
        SkyDogUtils.beanCopy(sdConfig,sdConfigDO);
        List<SdConfigDO> sdConfigDOList = sdConfigDAO.selectListByDO(sdConfigDO);
        if (CollectionUtils.isEmpty(sdConfigDOList)) {
            return null;
        }
        List<SdConfigValue> configValueList = new ArrayList<SdConfigValue>();
        for(SdConfigDO beanDO :sdConfigDOList){
            SdConfigValue value = new SdConfigValue(beanDO.getCode(),beanDO.getConfigValue());
            configValueList.add(value);
        }
        return configValueList;
    }

    /**
     * 查询实体
     * @param sdConfig
     * @return
     */
    public<T> List<SdConfig<T>> selectList(SdConfig sdConfig,Class<T> cls) {
        log.info("selectList:{}", JSONObject.toJSONString(sdConfig));
        //从数据库中查询配置
        SdConfigDO sdConfigDO = new SdConfigDO();
        SkyDogUtils.beanCopy(sdConfig,sdConfigDO);
        List<SdConfigDO> sdConfigDOList = sdConfigDAO.selectListByDO(sdConfigDO);
        if (CollectionUtils.isEmpty(sdConfigDOList)) {
            return null;
        }
        List<SdConfig<T>> sdConfigList = new ArrayList<SdConfig<T>>();
        for(SdConfigDO beanDO:sdConfigDOList){
            SdConfig bean = new SdConfig();
            SkyDogUtils.beanCopy(beanDO,bean);
            if(!StringUtils.isEmpty(beanDO.getConfigValue())){
                T object = JSONObject.parseObject(beanDO.getConfigValue(),cls);
                bean.setConfigValue(object);
            }
            sdConfigList.add(bean);
        }
        return sdConfigList;
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public SdConfig selectByPrimaryKey(String id) {
        log.info("selectByPrimaryKey:{}", id);
        SdConfigDO sdConfigDO = sdConfigDAO.selectByPrimaryKey(id);
        if(sdConfigDO!=null){
            SdConfig sdConfig = new SdConfig();
            SkyDogUtils.beanCopy(sdConfigDO,sdConfig);
            return sdConfig;
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
        return sdConfigDAO.deleteByPrimaryKey(id);
    }

    /**
     * 更新实体
     * @param sdConfig
     * @return
     */
    public Integer update(SdConfig sdConfig) {
        log.info("update:{}", JSONObject.toJSONString(sdConfig));
        if(sdConfig==null || StringUtils.isEmpty(sdConfig.getId())){
            throw new RuntimeException("id is not null");
        }
        sdConfig.setGmtModified(SkyDogUtils.formatDateYYYY_MM_DD_HH_MM_SS(new Date()));
        SdConfigDO sdConfigDO = new SdConfigDO();
        SkyDogUtils.beanCopy(sdConfig,sdConfigDO);
        if(sdConfig.getConfigValue()!=null){
            sdConfigDO.setConfigValue(JSONObject.toJSONString(sdConfig.getConfigValue()));
        }
        return sdConfigDAO.updateByPrimaryKeySelective(sdConfigDO);
    }

    /**
     * 新增实体
     * @param sdConfig
     * @return
     */
    public Integer insert(SdConfig sdConfig) {
        log.info("insert:{}", JSONObject.toJSONString(sdConfig));
        if(sdConfig==null){
            throw new RuntimeException("id is not null");
        }
        sdConfig.setId(UUID.randomUUID().toString());
        sdConfig.setGmtCreated(SkyDogUtils.formatDateYYYY_MM_DD_HH_MM_SS(new Date()));
        sdConfig.setGmtModified(SkyDogUtils.formatDateYYYY_MM_DD_HH_MM_SS(new Date()));
        SdConfigDO sdConfigDO = new SdConfigDO();
        SkyDogUtils.beanCopy(sdConfig,sdConfigDO);
        if(sdConfig.getConfigValue()!=null){
            sdConfigDO.setConfigValue(JSONObject.toJSONString(sdConfig.getConfigValue()));
        }
        return sdConfigDAO.insert(sdConfigDO);
    }
}
