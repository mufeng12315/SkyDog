package com.mufeng.skydog.repo;

import com.alibaba.fastjson.JSONObject;
import com.mufeng.skydog.bean.SdDataSourceConfig;
import com.mufeng.skydog.dao.SdDataSourceConfigDAO;
import com.mufeng.skydog.dataobject.SdDataSourceConfigDO;
import com.mufeng.skydog.utils.SkyDogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class SdDataSourceConfigRepo {

    @Resource
    private SdDataSourceConfigDAO sdDataSourceConfigDAO;

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public SdDataSourceConfig selectByPrimaryKey(String id) {
        log.info("selectByPrimaryKey:{}", id);
        SdDataSourceConfigDO sdDataSourceConfigDO = sdDataSourceConfigDAO.selectByPrimaryKey(id);
        if(sdDataSourceConfigDO!=null){
            SdDataSourceConfig sdDataSourceConfig = new SdDataSourceConfig();
            SkyDogUtils.beanCopy(sdDataSourceConfigDO,sdDataSourceConfig);
            return sdDataSourceConfig;

        }
        return null;
    }

    /**
     * 根据条件查询
     * @param sdDataSourceConfig
     * @return
     */
    public List<SdDataSourceConfig> selectList(SdDataSourceConfig sdDataSourceConfig) {
        log.info("selectListByDO sdDataSourceConfig:{}", JSONObject.toJSONString(sdDataSourceConfig));
        //从数据库中查询配置
        List<SdDataSourceConfig> sdDataSourceConfigList = null;
        SdDataSourceConfigDO sdDataSourceConfigDO = new SdDataSourceConfigDO();
        SkyDogUtils.beanCopy(sdDataSourceConfig,sdDataSourceConfigDO);
        List<SdDataSourceConfigDO> sdDataSourceConfigDOList = sdDataSourceConfigDAO.selectListByDO(sdDataSourceConfigDO);
        if (CollectionUtils.isNotEmpty(sdDataSourceConfigDOList)) {
            sdDataSourceConfigList = SkyDogUtils.doToDtoByList(sdDataSourceConfigDOList, SdDataSourceConfig.class);
        }
        log.info("selectListByDO end");
        return sdDataSourceConfigList;
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
        return sdDataSourceConfigDAO.deleteByPrimaryKey(id);
    }

    /**
     * 更新实体
     * @param sdDataSourceConfig
     * @return
     */
    public Integer update(SdDataSourceConfig sdDataSourceConfig) {
        log.info("update:{}", JSONObject.toJSONString(sdDataSourceConfig));
        if(sdDataSourceConfig==null || StringUtils.isEmpty(sdDataSourceConfig.getId())){
            throw new RuntimeException("id is not null");
        }
        sdDataSourceConfig.setGmtModified(SkyDogUtils.formatDateYYYY_MM_DD_HH_MM_SS(new Date()));
        SdDataSourceConfigDO sdDataSourceConfigDO = new SdDataSourceConfigDO();
        SkyDogUtils.beanCopy(sdDataSourceConfig,sdDataSourceConfigDO);
        return sdDataSourceConfigDAO.updateByPrimaryKeySelective(sdDataSourceConfigDO);
    }

    /**
     * 新增实体
     * @param sdDataSourceConfig
     * @return
     */
    public Integer insert(SdDataSourceConfig sdDataSourceConfig) {
        log.info("insert:{}", JSONObject.toJSONString(sdDataSourceConfig));
        if(sdDataSourceConfig==null){
            throw new RuntimeException("id is not null");
        }
        sdDataSourceConfig.setId(UUID.randomUUID().toString());
        sdDataSourceConfig.setGmtCreated(SkyDogUtils.formatDateYYYY_MM_DD_HH_MM_SS(new Date()));
        sdDataSourceConfig.setGmtModified(SkyDogUtils.formatDateYYYY_MM_DD_HH_MM_SS(new Date()));
        SdDataSourceConfigDO sdDataSourceConfigDO = new SdDataSourceConfigDO();
        SkyDogUtils.beanCopy(sdDataSourceConfig,sdDataSourceConfigDO);
        return sdDataSourceConfigDAO.insert(sdDataSourceConfigDO);
    }


}
