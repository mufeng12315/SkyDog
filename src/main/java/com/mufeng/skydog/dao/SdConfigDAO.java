package com.mufeng.skydog.dao;

import com.mufeng.skydog.dataobject.SdConfigDO;

import java.util.List;

public interface SdConfigDAO {
    int deleteByPrimaryKey(String id);

    int insert(SdConfigDO record);

    int insertSelective(SdConfigDO record);

    SdConfigDO selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SdConfigDO record);

    List<SdConfigDO> selectListByDO(SdConfigDO record);
}
