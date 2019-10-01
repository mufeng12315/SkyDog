package com.mufeng.skydog.core;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mufeng.skydog.bean.SdDataSourceConfig;
import com.mufeng.skydog.core.cache.SdDataSourceConfigCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class DataSourceFactory {

    /**
     * 配置读取类
     */
    @Resource
    private SdDataSourceConfigCache sdDataSourceConfigCache;

    private ConcurrentHashMap<String, DataSource> dataSourceMap = new ConcurrentHashMap<String,DataSource>();

    public static SdDataSourceConfig defaultConfig = new SdDataSourceConfig();
    static{
        defaultConfig.setMaxIdleTime(3600*2);
        defaultConfig.setMinPoolSize(1);
        defaultConfig.setMaxPoolSize(3);
        defaultConfig.setIdleConnectionTestPeriod(3600);
    }

    public DataSource getDataSource(String dataSourceCode){
        if(dataSourceMap.containsKey(dataSourceCode)){
            return dataSourceMap.get(dataSourceCode);
        }
        synchronized (this){
            try {
                if(dataSourceMap.containsKey(dataSourceCode)){
                    return dataSourceMap.get(dataSourceCode);
                }
                //第一次调用时转为map
                SdDataSourceConfig sdDataSourceConfig = sdDataSourceConfigCache.get(dataSourceCode);
                ComboPooledDataSource dataSource = new ComboPooledDataSource();
                dataSource.setDriverClass(sdDataSourceConfig.getDriverClass());
                dataSource.setJdbcUrl(sdDataSourceConfig.getJdbcUrl());
                dataSource.setUser(sdDataSourceConfig.getUser());
                dataSource.setPassword(sdDataSourceConfig.getPassword());
                dataSource.setMinPoolSize(sdDataSourceConfig.getMinPoolSize()==null?defaultConfig.getMinPoolSize():sdDataSourceConfig.getMinPoolSize());
                dataSource.setMaxPoolSize(sdDataSourceConfig.getMaxPoolSize()==null?defaultConfig.getMaxPoolSize():sdDataSourceConfig.getMaxPoolSize());
                dataSource.setMaxIdleTime(sdDataSourceConfig.getMaxIdleTime()==null?defaultConfig.getMaxIdleTime():sdDataSourceConfig.getMaxIdleTime());
                dataSource.setIdleConnectionTestPeriod(sdDataSourceConfig.getIdleConnectionTestPeriod()==null?defaultConfig.getIdleConnectionTestPeriod():sdDataSourceConfig.getIdleConnectionTestPeriod());
                dataSourceMap.put(dataSourceCode,dataSource);
            } catch (PropertyVetoException e) {
                log.error("getDataSource error",e);
            }
        }
        return dataSourceMap.get(dataSourceCode);
    }

    public ConcurrentHashMap<String, DataSource> getDataSourceMap() {
        return dataSourceMap;
    }
}
