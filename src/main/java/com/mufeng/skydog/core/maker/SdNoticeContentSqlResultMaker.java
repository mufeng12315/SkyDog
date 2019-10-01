package com.mufeng.skydog.core.maker;

import com.mufeng.skydog.bean.*;
import com.mufeng.skydog.core.SdRuleCheckContext;
import com.mufeng.skydog.repo.SdSqlExecutor;
import com.mufeng.skydog.utils.SkyDogBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * SQL结果生产器
 */
@Slf4j
@Service("sdNoticeContentSqlResultMaker")
public class SdNoticeContentSqlResultMaker extends SdNoticeContentMaker {

    @Resource
    private SdSqlExecutor sdSqlExecutor;

    @Override
    public SdNoticeMessage makeContent(SdRuleCheckContext sdRuleCheckContext, SdNoticeRule sdNoticeRule) {
        log.info("sdNoticeRule.getNoticeContent :{}",sdNoticeRule.getNoticeContent());
        SdNoticeContent noticeContent = sdNoticeRule.getNoticeContent();
        if(noticeContent==null || StringUtils.isEmpty(noticeContent.getDataSourceCode())){
            log.info("sdSqlContent or dataSourceCode is null");
            return null;
        }
        List<SdRow> sdRowList = sdSqlExecutor.executeSql(noticeContent.getNoticeContent(),noticeContent.getDataSourceCode());
        if(sdRowList.isEmpty()){
            log.info("sdRowList is null");
        }
        SdNoticeMessage sdNoticeMessage = new SdNoticeMessage();
        sdNoticeMessage.setTitle(sdNoticeRule.getRuleName());
        sdNoticeMessage.setReceiverList(getSdReceiverList(sdNoticeRule));
        //生成表格内容
        sdNoticeMessage.setMessage(makeHtml(sdRowList));
        return sdNoticeMessage;
    }

    /**
     * 生成表格行
     * @param sdRow
     * @param isFirstRow
     */
    private String createTabRow(SdRow sdRow,Boolean isFirstRow){
        StringBuffer contentBuffer = new StringBuffer();
        if(sdRow.getColumnMap()==null || sdRow.getColumnMap().size()==0){
            return contentBuffer.toString();
        }
        contentBuffer.append("<tr>");
        for(String key:sdRow.getColumnMap().keySet()){
            if(isFirstRow) {
                //第1行为标题
                contentBuffer.append("<th>").append(key).append("</th>");
            }else{
                contentBuffer.append("<td>").append(sdRow.getColumnMap().get(key).getColValue()).append("</td>");
            }
        }
        contentBuffer.append("</tr>");
        return contentBuffer.toString();
    }

    /**
     * 生成html内容
     * @param sdRowList
     * @return
     */
    private String makeHtml(List<SdRow> sdRowList){
        //生成表格内容
        StringBuffer contentBuffer = new StringBuffer();
        contentBuffer.append("<table>");
        for(int i=0;i<sdRowList.size();i++){
            SdRow sdRow = sdRowList.get(i);
            if(i==0){
                //生成标题行
                contentBuffer.append(createTabRow(sdRow,Boolean.TRUE));
            }
            //生成其他行
            contentBuffer.append(createTabRow(sdRow,Boolean.FALSE));
        }
        contentBuffer.append("</table>");
        //生成html
        String html = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"\n" +
                "        \"http://www.w3.org/TR/html4/loose.dtd\">\n" +
                "<html>\n" +
                "<head>\n" +
                getStyle()+
                "</head>\n" +
                "<body>\n" +
                contentBuffer.toString()+
                "</body>\n" +
                "</html>";
        return html;
    }

    /**
     * 获取样式
     * @return
     */
    private String getStyle(){
        String style = "<style type=\"text/css\">\n" +
                "table {\n" +
                "\tfont-family: verdana,arial,sans-serif;\n" +
                "\tfont-size:11px;\n" +
                "\tcolor:#333333;\n" +
                "\tborder-width: 1px;\n" +
                "\tborder-color: #a9c6c9;\n" +
                "\tborder-collapse: collapse;\n" +
                "}\n" +
                "table th {\n" +
                "\tborder-width: 1px;\n" +
                "\tpadding: 8px;\n" +
                "\tborder-style: solid;\n" +
                "\tborder-color: #a9c6c9;\n" +
                "\tbackground: #ADD8E6;\n" +
                "}\n" +
                "table td {\n" +
                "\tborder-width: 1px;\n" +
                "\tpadding: 8px;\n" +
                "\tborder-style: solid;\n" +
                "\tborder-color: #a9c6c9;\n" +
                "\tbackground: #E6E6FA;\n" +
                "}\n" +
                "</style>";
        return style;
    }

}
