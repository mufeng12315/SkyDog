package com.mufeng.skydog.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.jexl3.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 类BaseEngine.java的实现描述：规则引擎（自动结案、复核、转人工审核）
 * 
 * @author tangqingsong 2016年12月8日 上午10:36:39
 */
@Slf4j
@Component
public class SdRuleEngine {

    /**
     * 表达式执行引擎
     */
    protected JexlEngine    engine;

    public SdRuleEngine() {
        //创建引擎
        engine = new JexlBuilder().create();
    }

    /**
     * 执行业务规则
     * @param busRuleCheckContext
     * @param ruleExpression
     * @return
     */
    public Boolean checkRule(SdRuleCheckContext busRuleCheckContext, String ruleExpression){
        //根据模型创建规则校验上下文环境
        JexlContext context = new MapContext();
        setMapContextData(context,busRuleCheckContext);
        //把变量直接获取出来，这样表达式中使用变量时不用带busData前缀
        if(busRuleCheckContext.getBusData()!=null){
            for(String key:busRuleCheckContext.getBusData().keySet()){
                context.set(key,busRuleCheckContext.getBusData().get(key));
            }
        }
        JexlScript jexlScript = engine.createScript(ruleExpression);
        Boolean evaluateResult = (Boolean) jexlScript.execute(context);
        return evaluateResult;
    }

    /**
     * 执行业务规则
     * @param busRuleCheckContext
     * @param ruleExpression
     * @return
     */
    public String executeScript(SdRuleCheckContext busRuleCheckContext, String ruleExpression){
        //根据模型创建规则校验上下文环境
        JexlContext context = getContext(busRuleCheckContext, null);
        JexlScript jexlScript = engine.createScript(ruleExpression);
        String evaluateResult = (String) jexlScript.execute(context);
        return evaluateResult;
    }


    /**
     * 根据参数创建上下文环境
     * 
     * @param model
     * @param args
     * @return
     */
    private <T> JexlContext getContext(T model, Object... args) {
        JexlContext context = null;
        if (args != null && args.length >= 1) {
            //如果使用除模型对象外的其他参数，使用MapContext,在写表达式时，必须写上变量名，变量名默认是首字母小写的类名；
            context = new MapContext();
            setMapContextData(context,model);
            for (Object arg : args) {
                setMapContextData(context,arg);
            }
        } else {
            context = new ObjectContext<T>(engine, model);
        }
        return context;
    }
    
    /**
     * 初始化对象设值
     * @param context
     * @param obj
     */
    private void setMapContextData(JexlContext context,Object obj){
        Field srcFields[] = obj.getClass().getDeclaredFields();
        if (srcFields == null || srcFields.length == 0) {
            return;
        }
        for (int i = 0; i < srcFields.length; i++) {
            Field srcField = srcFields[i];
            try {
                srcField.setAccessible(true);
                Object property = srcField.get(obj);
                if (srcField.getName().equals("serialVersionUID") || property == null) {
                    continue;
                }
                context.set(srcField.getName(), property);
            } catch (Exception e) {
                log.warn("initMapContext:{} Exception erro,e:{}",srcField.getName(),e);
            }
        }
    }

    /**
     * 执行jexl表达式
     * 
     * @param jexlExp 表达式
     * @param context 上下文环境
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T evaluate(JexlExpression jexlExp, JexlContext context,Class<T> cls) {
        return (T) jexlExp.evaluate(context);
    }

    /**
     * 字符串解析成表达式
     * 
     * @param expression
     */
    public JexlExpression parseExpression(String expression) {
        return engine.createExpression(expression);
    }

}
