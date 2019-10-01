package com.mufeng.skydog.dao;

import com.mufeng.skydog.dataobject.SdSqlContentDO;

import java.util.List;

public interface SdSqlContentDAO {
    int deleteByPrimaryKey(Long id);

    int insert(SdSqlContentDO record);

    int insertSelective(SdSqlContentDO record);

    SdSqlContentDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SdSqlContentDO record);

    int updateByPrimaryKey(SdSqlContentDO record);

    List<SdSqlContentDO> selectListByDO(SdSqlContentDO record);
}