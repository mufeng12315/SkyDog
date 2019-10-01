package com.mufeng.skydog.core.cache;
import org.springframework.beans.factory.InitializingBean;

import java.util.HashMap;
import java.util.Map;

/**
 * 缓存抽象类
 * @param <T>
 */
public abstract class AbstractCache<T> implements InitializingBean {

    protected Map<String, T> cacheMap = new HashMap<String, T>();

    /**
     *更新
     */
    public abstract void update(String code,T value);

    /**
     * 删除
     * @param code
     */
    public abstract void delete(String code);

    /**
     * 获取对象
     * @param code
     * @return
     */
    public abstract T get(String code);

    /**
     * 初始化缓存
     */
    public abstract void initCache();

    @Override
    public void afterPropertiesSet() throws Exception {
        initCache();
    }


}
