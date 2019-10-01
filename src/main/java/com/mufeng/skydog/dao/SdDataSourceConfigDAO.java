package com.mufeng.skydog.dao;

import com.mufeng.skydog.dataobject.SdDataSourceConfigDO;

import java.util.List;

public interface SdDataSourceConfigDAO {
    int deleteByPrimaryKey(String id);

    int insert(SdDataSourceConfigDO record);

    int insertSelective(SdDataSourceConfigDO record);

    SdDataSourceConfigDO selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SdDataSourceConfigDO record);

    List<SdDataSourceConfigDO> selectListByDO(SdDataSourceConfigDO record);
}
