package com.mufeng.skydog.core.cache;

import com.mufeng.skydog.bean.SdDataSourceConfig;
import com.mufeng.skydog.core.DataSourceFactory;
import com.mufeng.skydog.repo.SdDataSourceConfigRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据源缓存
 */
@Slf4j
@Component
public class SdDataSourceConfigCache extends AbstractCache<SdDataSourceConfig>{

    @Resource
    private SdDataSourceConfigRepo sdDataSourceConfigRepo;

    @Resource
    private DataSourceFactory dataSourceFactory;

    @Override
    public synchronized void update(String code, SdDataSourceConfig value) {
        if(cacheMap.containsKey(value.getCode())){
            //删除原来的数据库连接
            dataSourceFactory.getDataSourceMap().remove(value.getCode());
        }
        //更新数据源配置
        cacheMap.put(value.getCode(),value);
    }

    /**
     * 删除
     * @param code
     */
    public synchronized void delete(String code){
        if(StringUtils.isEmpty(code)){
            log.info("code is null");
            return;
        }
        //删除原来的数据库连接
        dataSourceFactory.getDataSourceMap().remove(code);
        //删除数据源配置
        cacheMap.remove(code);
    }

    @Override
    public SdDataSourceConfig get(String code) {
        return cacheMap.get(code);
    }

    @Override
    public void initCache() {
        //1、从库中加载数据库初始数据
        List<SdDataSourceConfig> sdDataSourceConfigList = sdDataSourceConfigRepo.selectList(new SdDataSourceConfig());
        if(CollectionUtils.isNotEmpty(sdDataSourceConfigList)){
            for(SdDataSourceConfig config:sdDataSourceConfigList){
                cacheMap.put(config.getCode(),config);
            }
            log.info("initCache size:{}",sdDataSourceConfigList.size());
        }
    }
}
