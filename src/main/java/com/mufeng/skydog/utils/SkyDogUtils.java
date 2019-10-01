package com.mufeng.skydog.utils;

import com.mufeng.skydog.bean.SdConfig;
import com.mufeng.skydog.dataobject.SdConfigDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * SkyDogUtils.java的实现描述：bean工具类
 * 
 * @author tangqingsong
 */
@Slf4j
public class SkyDogUtils {

	public static <T> List<T> doToDtoByList(List<?> doList, Class<T> classes,String...filterList) {
		List<T> result = new LinkedList<T>();
		if (doList != null && !doList.isEmpty()) {
			for (Object dataDO : doList) {
				T dto;
				try {
					dto = classes.newInstance();
					beanCopy(dataDO, dto,filterList);
					result.add(dto);
				} catch (Exception e) {
					log.info("AstraeaBeanUtils Exception",e);
				}
			}
		}
		return result;
	}

	/**
     * copy非空属性
     *
     * @param src
     * @param dest
     */
    public static void beanCopy(Object src, Object dest){
        beanCopy(src,dest,null);
    }

    
	/**
	 * copy非空属性
	 * 
	 * @param src
	 * @param dest
	 * @param filterList 增加过滤字段列表
	 */
    public static void beanCopy(Object src, Object dest,String...filterList) {
		if (src == null || dest == null) {
			return;
		}
		Map<String,String> filterMap = listToMap(filterList);//过滤字段
		Field srcFields[] = src.getClass().getDeclaredFields();
		Field destFields[] = dest.getClass().getDeclaredFields();
		Map<String, Field> destFieldMap = listToMap(destFields, "name");
		if (srcFields == null || srcFields.length == 0) {
			return;
		}
		for (int i = 0; i < srcFields.length; i++) {
			Field srcField = srcFields[i];
			try {
				srcField.setAccessible(true);
				Object property = srcField.get(src);
				if (srcField.getName().equals("serialVersionUID") || property == null
				        || filterMap.containsKey(srcField.getName())) {
					continue;
				}
				Field destField = destFieldMap.get(srcField.getName());
				if(destField==null){
					continue;
				}
				if (destField.getType().getName().equals(srcField.getType().getName())) {
					destField.setAccessible(Boolean.TRUE);
					destField.set(dest, property);
				}
			} catch (IllegalArgumentException e) {
				log.warn("fieldName:{} copy IllegalArgumentException error",srcField.getName());
				log.info("AstraeaBeanUtils IllegalArgumentException",e);
			} catch (IllegalAccessException e) {
                log.warn("fieldName:{} copy IllegalAccessException error",srcField.getName());
                log.info("AstraeaBeanUtils IllegalAccessException",e);
			}
		}
	}

	/**
	 * 把array转成map
	 * 
	 * @param list
	 * @param propertyName
	 * @return
	 */
	public static <K, T> Map<K, T> listToMap(T[] list, String propertyName) {
		if (list == null) {
			return null;
		}
		Map<K, T> result = new HashMap<>();
		try {
			for (T bean : list) {
				K key = (K) PropertyUtils.getProperty(bean, propertyName);
				result.put(key, bean);
			}
		} catch (Exception e) {
			log.info("AstraeaBeanUtils Exception",e);
		}
		return result;
	}

	/**
	 * 根据str获取BigDecimal
	 * 
	 * @param value
	 * @return
	 */
	public static BigDecimal getBigDecimalByStr(String value) {
		BigDecimal result = new BigDecimal(0);
		if (value == null || value.isEmpty()) {
			return result;
		}
		try {
			result = new BigDecimal(value);
		} catch (Exception e) {
			log.info("AstraeaBeanUtils Exception",e);
		}
		return result;
	}

	/**
	 * 根据str获取BigDecimal
	 * 
	 * @param value
	 * @return
	 */
	public static BigDecimal getBigDecimal(BigDecimal value) {
		BigDecimal result = new BigDecimal(0);
		if (value == null) {
			return result;
		}
		return value;
	}

	/**
	 * 根据str获取BigDecimal
	 * 
	 * @param value
	 * @return
	 */
	public static int getIntByStr(String value) {
		Integer result = new Integer(0);
		if (value == null || value.isEmpty()) {
			return result;
		}
		try {
			result = Integer.parseInt(value);
		} catch (Exception e) {
			log.info("AstraeaBeanUtils Exception",e);
		}
		return result;
	}

	/**
	 * 根据Long获取String
	 * 
	 * @param value
	 * @return
	 */
	public static String getStrByLong(Long value) {
		if (value == null) {
			return null;
		}
		return value.toString();
	}

	/**
	 * 根据Integer获取String
	 * 
	 * @param value
	 * @return
	 */
	public static String getStrByInteger(Integer value) {
		if (value == null) {
			return null;
		}
		return value.toString();
	}

	/**
	 * 根据BigDecimal获取String
	 * 
	 * @param value
	 * @return
	 */
	public static String getStrByBigDecimal(BigDecimal value) {
		if (value == null) {
			return null;
		}
		return value.toString();
	}

	/**
	 * 如果value为空，返回0，否则返回value 省去代码中的非空判断
	 * 
	 * @param value
	 * @return
	 */
	public static int getInt(Integer value) {
		Integer result = new Integer(0);
		if (value == null) {
			return result;
		}
		return value;
	}

	/**
	 * 根据Integer获取Long
	 * 
	 * @param value
	 * @return
	 */
	public static Long getLongByInt(Integer value) {
		if (value == null) {
			return null;
		}
		return value.longValue();
	}

	/**
	 * 根据Integer获取Long
	 * 
	 * @param value
	 * @return
	 */
	public static Long getLongByStr(String value) {
		if (value == null) {
			return null;
		}
		try {
			return Long.parseLong(value);
		} catch (Exception e) {
			log.info("AstraeaBeanUtils Exception",e);
		}
		return null;
	}
	
	/**
	 * 根据Int获取BigDecimal
	 * 
	 * @param value
	 * @return
	 */
	public static BigDecimal getBigDecimalByInt(Integer value) {
		BigDecimal result = new BigDecimal(0);
		if (value == null) {
			return result;
		}
		result = new BigDecimal(value);
		return result;
	}
	
	/**
	 * 根据Long获取BigDecimal
	 * 
	 * @param value
	 * @return
	 */
	public static BigDecimal getBigDecimalByLong(Long value) {
		BigDecimal result = new BigDecimal(0);
		if (value == null) {
			return result;
		}
		result = new BigDecimal(value);
		return result;
	}

	/**
	 * 计算两个日期之间相差天数
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static int diffDay(Date start, Date end) {
		if (start == null || end == null) {
			return 0;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(start);
		long time1 = cal.getTimeInMillis();
		cal.setTime(end);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Math.abs(Integer.parseInt(String.valueOf(between_days)));
	}

	/**
	 * 把list转成map
	 * 
	 * @param list
	 * @param propertyName
	 * @return
	 */
	public static <K, T> Map<K, T> listToMap(List<T> list, String propertyName) {
		Map<K, T> result = new HashMap<>();
		if (list == null) {
			return result;
		}
		try {
			for (T bean : list) {
				K key = (K) PropertyUtils.getProperty(bean, propertyName);
				result.put(key, bean);
			}
		} catch (Exception e) {
			log.info("AstraeaBeanUtils Exception",e);
		}
		return result;
	}
	
	/**
	 * 把list转成map，map的value为list，支持key相同时以list形式返回
	 * 
	 * @param list
	 * @param propertyName
	 * @return
	 */
	public static <K, T> Map<K, List<T>> listToMapList(List<T> list, String propertyName) {
		Map<K, List<T>> result = new HashMap<K, List<T>>();
		if (list == null) {
			return result;
		}
		try {
			for (T bean : list) {
				K key = (K) PropertyUtils.getProperty(bean, propertyName);
				if(!result.containsKey(key)){
					List<T> tempList = new LinkedList<T>();
					result.put(key, tempList);
				}
				result.get(key).add(bean);
			}
		} catch (Exception e) {
			log.info("AstraeaBeanUtils Exception",e);
		}
		return result;
	}
	

	/**
	 * 把字符串数组转成map
	 * 
	 * @param strArray
	 * @return
	 */
	public static Map<String, String> listToMap(String[] strArray) {
	    Map<String, String> result = new HashMap<>();
		if (strArray == null) {
			return result;
		}
		try {
			for (String value : strArray) {
				result.put(value, value);
			}
		} catch (Exception e) {
			log.info("AstraeaBeanUtils Exception",e);
		}
		return result;
	}


	/**
	 * 把字符串列表转成map
	 * 
	 * @param strList
	 * @return
	 */
	public static Map<String, String> listToMap(List<String> strList) {
		Map<String, String> result = new HashMap<>();
		if (strList == null) {
			return result;
		}
		try {
			for (String value : strList) {
				result.put(value, value);
			}
		} catch (Exception e) {
			log.info("AstraeaBeanUtils Exception",e);
		}
		return result;
	}
	
	/**
	 * 把map转成list
	 * 
	 * @param map
	 * @return
	 */
	public static <K, T> List<T> mapToList(Map<K, T> map) {
		List<T> result = new ArrayList<>();
		try {
			for (T bean : map.values()) {
				result.add(bean);
			}
		} catch (Exception e) {
			log.info("AstraeaBeanUtils Exception",e);
		}
		return result;
	}

	/**
	 * 日期增加天数
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date addDays(Date date, int days) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.add(Calendar.DAY_OF_MONTH, days);
		return now.getTime();
	}

	/**
	 * 获取最小的数
	 * 
	 * @param vals
	 * @return
	 */
	public static int min(int... vals) {
		int min = -1;
		for (int i = 0; i < vals.length; i++) {
			int val = vals[i];
			if (i == 0) {
				min = val;
			}
			if (val < min) {
				// 如果当前数最小，使当前数为最小数
				min = val;
			}
		}
		return min;
	}

	/**
	 * 获取最小的数
	 * 
	 * @param vals
	 * @return
	 */
	public static BigDecimal min(BigDecimal... vals) {
		BigDecimal min = new BigDecimal(0);
		for (int i = 0; i < vals.length; i++) {
			BigDecimal val = vals[i];
			if (i == 0) {
				min = val;
			}
			if (val.compareTo(min) == -1) {
				// 如果当前数最小，使当前数为最小数
				min = val;
			}
		}
		return min;
	}

	/**
	 * 获取最大的数
	 * 
	 * @param vals
	 * @return
	 */
	public static int max(int... vals) {
		int max = -1;
		for (int i = 0; i < vals.length; i++) {
			int val = vals[i];
			if (i == 0) {
				max = val;
			}
			if (val > max) {
				// 如果当前数最大，使当前数为最大数
				max = val;
			}
		}
		return max;
	}

	/**
	 * 获取最大的数
	 * 
	 * @param vals
	 * @return
	 */
	public static BigDecimal max(BigDecimal... vals) {
		BigDecimal max = new BigDecimal(0);
		for (int i = 0; i < vals.length; i++) {
			BigDecimal val = vals[i];
			if (i == 0) {
				max = val;
			}
			if (val.compareTo(max) >= 1) {
				// 如果当前数最大，使当前数为最大数
				max = val;
			}
		}
		return max;
	}

	/**
	 * string转成其他类型
	 * 
	 * @param strValue
	 * @param cls
	 *            希望返回的类型
	 * @return
	 */
	public static <T> T strToObject(String strValue, Class<T> cls) {
		if (BigDecimal.class.equals(cls)) {
			BigDecimal value = getBigDecimalByStr(strValue);
			return (T) value;
		} else if (Integer.class.equals(cls)) {
			Integer value = getIntByStr(strValue);
			return (T) value;
		} else if (String.class.equals(cls)) {
			return (T) strValue;
		}
		return null;
	}

	/**
	 * 去除字符串中最后一个字符
	 * 
	 * @param str
	 * @param mark
	 * @return
	 */
	public static StringBuffer removeLast(StringBuffer str, String mark) {
		if(str==null || str.length()==0){
			return str;
		}
		int ix = str.lastIndexOf(mark);
		if (ix > 0) {
			str = new StringBuffer(str.substring(0, ix));
		}
		return str;
	}
	
	/**
	 * 去除字符串中最后一个字符
	 * 
	 * @param str
	 * @param mark
	 * @return
	 */
	public static String removeFirst(String str, String mark) {
		if(str==null || str.isEmpty()){
			return str;
		}
		int ix = str.indexOf(mark);
		if (ix == 0) {
			str = str.substring(0+1, str.length());
		}
		return str;
	}
	
	
	/**
	 * 去除字符串中最后一个字符
	 * 
	 * @param str
	 * @param mark
	 * @return
	 */
	public static String removeLast(String str, String mark) {
		if(str==null || str.isEmpty()){
			return str;
		}
		int ix = str.lastIndexOf(mark);
		if (ix > 0) {
			str = str.substring(0, ix);
		}
		return str;
	}

	/**
	 * 去除字符串中最后一个字符
	 * 
	 * @param str
	 * @param mark
	 * @return
	 */
	public static StringBuffer removeFirst(StringBuffer str, String mark) {
		if(str==null || str.length()==0){
			return str;
		}
		int ix = str.indexOf(mark);
		if (ix == 0) {
			str = new StringBuffer(str.substring(0+1, str.length()));
		}
		return str;
	}
	
	
	/**
	 * 根据长度取字符串
	 * 
	 * @param str
	 * @param size
	 * @return
	 */
	public static String getStringBySize(String str, int size) {
		if (str == null || str.isEmpty() || size > str.length()) {
			return str;
		}
		return str.substring(0, size - 1);
	}

	/**
	 * 列表字符串转成按格式分割的字符串
	 * @param list
	 * @param mark
	 * @return
	 */
	public static String listToString(Collection<String> list,String mark) {
		if(list==null || list.isEmpty()){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for(String str:list){
			sb.append(str+mark);
		}
		String result =removeLast(sb.toString(),mark);
		return result;
	}

    
    public static<T> T[] copyList(T[] dtoList,int begin,int size){
    	if(dtoList==null || dtoList.length==0){
    		return dtoList;
    	}
    	if(dtoList.length-(begin+size+1)<0){
    		size = dtoList.length-begin;
    	}
    	//Object[] newArray = new Object[size];
    	T[] newArray = Arrays.copyOfRange(dtoList, begin, begin+size);
    	//System.arraycopy(dtoList, begin, newArray, 0, size);
    	return newArray;
    }
    
	
    /**
     * 根据身份编号获取性别
     *
     * @param idCard 身份编号
     * @return 性别(M-男，F-女，N-未知)
     */
    public static Integer getGenderByIdCard(String idCard) {
    	Integer sGender = null;

        String sCardNum = idCard.substring(16, 17);
        if (Integer.parseInt(sCardNum) % 2 != 0) {
            sGender = 1;//男
        } else {
            sGender = 2;//女
        }
        return sGender;
    }

	/**
	 * 获取最小的数
	 * 
	 * @param vals
	 * @return
	 */
	public static BigDecimal min(List<BigDecimal> vals) {
		if(vals==null || vals.isEmpty()){
			return null;
		}
		BigDecimal min = new BigDecimal(0);
		for (int i = 0; i < vals.size(); i++) {
			BigDecimal val = vals.get(i);
			if (i == 0) {
				min = val;
			}
			if (val.compareTo(min) == -1) {
				// 如果当前数最小，使当前数为最小数
				min = val;
			}
		}
		return min;
	}
    
	/**
	 * 获取最小的数
	 * 
	 * @param vals
	 * @return
	 */
	public static Integer minByInteger(Integer... vals) {
		Integer min = null;
		for (int i = 0; i < vals.length; i++) {
			Integer val = vals[i];
			if(val==null){
				continue;
			}
			if (i == 0 || min ==null) {
				min = val;
				continue;
			}
			if (val < min) {
				// 如果当前数最小，使当前数为最小数
				min = val;
			}
		}
		return min;
	}
	
	/**
	 * 获取最小的数
	 * 
	 * @param vals
	 * @return
	 */
	public static BigDecimal minByBigDecimal(BigDecimal... vals) {
		BigDecimal min = null;
		for (int i = 0; i < vals.length; i++) {
			BigDecimal val = vals[i];
			if(val==null){
				continue;
			}
			if (i == 0 || min==null) {
				min = val;
				continue;
			}
			if (val.compareTo(min) == -1) {
				// 如果当前数最小，使当前数为最小数
				min = val;
			}
		}
		return min;
	}
	
	/**
	 * 获取最小的日期
	 * 
	 * @param vals
	 * @return
	 */
	public static Date min(Date... vals) {
		Date min = null;
		for (int i = 0; i < vals.length; i++) {
			Date val = vals[i];
			if(val==null){
				continue;
			}
			if (i == 0 || min==null) {
				min = val;
				continue;
			}
			if (val.compareTo(min) == -1) {
				// 如果当前数最小，使当前数为最小数
				min = val;
			}
		}
		return min;
	}
	
	/**
	 * 获取最大的日期
	 * 
	 * @param vals
	 * @return
	 */
	public static Date max(Date... vals) {
		Date max = null;
		for (int i = 0; i < vals.length; i++) {
			Date val = vals[i];
			if(val==null){
				continue;
			}
			if (i == 0 || max==null) {
				max = val;
				continue;
			}
			if (val.compareTo(max) == 1) {
				// 如果当前数最大，使当前数为最大数
				max = val;
			}
		}
		return max;
	}
	

	/**
	 * 对两个Long类型的数据相加
	 * 
	 * @param val1
	 * @param val2
	 * @return
	 */
	public static Long addLongValue(Long val1, Long val2) {
		if (val1 == null) {
			return val2;
		}
		if (val2 == null) {
			return val1;
		}
		return val1 + val2;
	}

	/**
	 * 对两个Integer类型的数据相加
	 * 
	 * @param val1
	 * @param val2
	 * @return
	 */
	public static Integer addIntegerValue(Integer val1, Integer val2) {
		if (val1 == null) {
			return val2;
		}
		if (val2 == null) {
			return val1;
		}
		return val1 + val2;
	}

	/**
	 * 根据字符串获取Set，如111,222，根据逗号分割设置到SET
	 * @return
	 */
	public static Set<String> getSetByString(String values){
		Set<String> bankCodeSet = new HashSet<>();
		if(!StringUtils.isEmpty(values)){
			String[] bankCodes = values.split(",");
			for(String bankCode:bankCodes){
				bankCodeSet.add(bankCode);
			}
		}
		return bankCodeSet;
	}

	/**
	 * setToList
	 * @param sets
	 * @param cls
	 * @param <T>
	 * @return
	 */
	public static<T>  List<T> setToList(Set<T> sets,Class<T> cls){
		List<T> result = new ArrayList<T>();
		if(sets==null || sets.isEmpty()){
			return result;
		}
		for(T value : sets){
			result.add(value);
		}
		return result;
	}

	/**
	 * 根据str获取BigDecimal
	 *
	 * @param value
	 * @return
	 */
	public static Boolean isIntegerType(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (Exception e) {
			log.error("Exception,e:{}",e);
			return false;
		}
	}

	/**
	 * 格式化日期
	 * @param date
	 * @return
	 */
	public static String formatDateYYYY_MM_DD_HH_MM_SS(Date date) {
		String format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setLenient(false);
		return sdf.format(date);
	}

//	public static void main(String args[]){
//		SdConfig sdConfig = new SdConfig();
//		Map<String,String> data = new HashMap<>();
//		data.put("mail","673315694@qq.com");
//		sdConfig.setConfigValue(data);
//		sdConfig.setCode("test");
//		SdConfigDO sdConfigDO = new SdConfigDO();
//		beanCopy(sdConfig,sdConfigDO);
//		System.out.println(sdConfigDO);
//	}
}
