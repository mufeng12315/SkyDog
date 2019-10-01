package com.mufeng.skydog.core.maker;

import com.alibaba.fastjson.JSONObject;
import com.mufeng.skydog.bean.SdNoticeMessage;
import com.mufeng.skydog.bean.SdNoticeRule;
import com.mufeng.skydog.core.SdRuleEngine;
import com.mufeng.skydog.core.SdRuleCheckContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Java结果生产器
 */
@Slf4j
@Service("sdNoticeContentJavaExpressMaker")
public class SdNoticeContentJavaExpressMaker extends SdNoticeContentMaker {

    @Resource
    private SdRuleEngine sdRuleEngine;
    /**
     * 匹配${}中的内容
     */
    private static final String REGEX = "(?<=\\$\\{)[^}]*(?=\\})";
    @Override
    public SdNoticeMessage makeContent(SdRuleCheckContext sdRuleCheckContext, SdNoticeRule sdNoticeRule) {
        //处理占位符，把${}中的占位符替换成变量值
        String message = handlePlaceholder(sdNoticeRule.getNoticeContent().getNoticeContent(),sdRuleCheckContext);
        SdNoticeMessage sdNoticeMessage = new SdNoticeMessage();
        sdNoticeMessage.setMessage(message);
        sdNoticeMessage.setTitle(sdNoticeRule.getRuleName());
        sdNoticeMessage.setReceiverList(getSdReceiverList(sdNoticeRule));
        log.info("makeContent:{}", JSONObject.toJSONString(sdNoticeMessage));
        return sdNoticeMessage;
    }

    /**
     * 处理占位符（把${}中的占位符替换成变量值）
     * @param noticeContent
     * @param sdRuleCheckContext
     * @return
     */
    private String handlePlaceholder(String noticeContent,SdRuleCheckContext sdRuleCheckContext){
        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(noticeContent); // 获取 matcher 对象
        StringBuffer messageBuffer = new StringBuffer();
        int lastIndex = 0;
        while(m.find()) {
            String variable = noticeContent.substring(m.start(),m.end());
            if(sdRuleCheckContext.getBusData().containsKey(variable)){
                Object data = sdRuleCheckContext.getBusData().get(variable);
                //-2是为了去除${
                messageBuffer.append(noticeContent.substring(lastIndex,m.start()-2)).append(data.toString());
                //+1是为了去除}
                lastIndex = m.end()+1;
            }
        }
        //截取最后一节
        messageBuffer.append(noticeContent.substring(lastIndex,noticeContent.length()));
        return messageBuffer.toString();
    }

}
