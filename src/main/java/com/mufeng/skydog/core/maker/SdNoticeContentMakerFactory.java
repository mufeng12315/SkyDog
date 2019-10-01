package com.mufeng.skydog.core.maker;

import com.mufeng.skydog.constants.SdNoticeContentTypeEnum;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 通知内容生成器工厂
 */
@Component
public class SdNoticeContentMakerFactory {


    @Resource
    private SdNoticeContentMaker sdNoticeContentJavaExpressMaker;

    @Resource
    private SdNoticeContentMaker sdNoticeContentSqlResultMaker;

    /**
     * 获取通内容生成器
     * @param type
     * @return
     */
    public SdNoticeContentMaker getContentMaker(String type){
        if(SdNoticeContentTypeEnum.JAVA_EXPRESS.getCode().equals(type)){
            return sdNoticeContentJavaExpressMaker;
        }else if(SdNoticeContentTypeEnum.SQL_RESULT.getCode().equals(type)){
            return sdNoticeContentSqlResultMaker;
        }
        return null;
    }
}
