package com.mufeng.skydog.dao;

import com.mufeng.skydog.dataobject.SdNoticeRuleDO;

import java.util.List;

public interface SdNoticeRuleDAO {
    int deleteByPrimaryKey(String id);

    int insert(SdNoticeRuleDO record);

    int insertSelective(SdNoticeRuleDO record);

    SdNoticeRuleDO selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SdNoticeRuleDO record);

    int update(SdNoticeRuleDO record);

    List<SdNoticeRuleDO> selectListByDO(SdNoticeRuleDO record);
}
