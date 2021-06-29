package com.leyantech.zeebedemo.common.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
    private static Log log = LogFactory.getLog(DateUtil.class);
	
	public static final String YMD1 = "yyyy-MM-dd";
    public static final String YMD2 = "dd/MM/yy";
    public static final String YMD3 = "yyyyMMdd";
    public static final String YMD4 = "ddMMyyyy";
    public static final String YMD5 = "yyyy-MM";
    public static final String YMD6 = "HH";
    public static final String YMD7 = "yyyy-MM-dd HH:mm";
    public static final String YMD8 = "yyyy";
    public static final String YMD_FULL = "yyyy-MM-dd HH:mm:ss";
    public static final String NUMBER_INT = "###";
    public static final String NUMBER_FLOAT = "####.##";
    private static String YMD_UTC ="yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static String YMD_UTC2 ="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    
    /**
	* Convert date type from String to Date
	* @param date Sample:2005-10-12
	* @return Date
	*/
    public static String convertToString(Date date,String format) {
    	if(date==null) return "";
    	SimpleDateFormat sdf=new SimpleDateFormat(format);
    	return sdf.format(date);
    }
    
    public static String convertToString(Date date) {
		return convertToString(date,YMD_FULL);
	}
    public static String convertToString(Long time) {
        return convertToString(new Date(time),YMD_FULL);
    }

    public static Date seekMin(Date date,int min) {
        return  new Date(date.getTime() + (((long)min) * 60000));
    }
    public static Date seekDate(Date date,int dayNum) {
		return  new Date(date.getTime() + (((long)dayNum) * 86400000));
	}
    public static String seekDate(String dateStr,int dayNum) {
        try {
            Date date = convertDate(dateStr);
            return convertToString(new Date(date.getTime() + (((long) dayNum) * 86400000)), YMD1);
        }catch (Exception e){
            log.error(e);
            return "";
        }
    }

    public static int diffDate(Date from,Date to) {
        long diff = to.getTime() - from.getTime();
        return (int)(diff/86400000);
    }
    
    public static Date trimDate(Date date) {
    	Calendar rightNow = Calendar.getInstance();
    	rightNow.setTime(date);
    	
		rightNow.set(Calendar.HOUR_OF_DAY, 0);
		rightNow.set(Calendar.MINUTE, 0);
		rightNow.set(Calendar.SECOND, 0);
        rightNow.set(Calendar.MILLISECOND, 0);
        
    	return  rightNow.getTime();
	}


    public static Date getMonthFirstDay(){
        return getMonthFirstDay(0);
    }

    /**
     * 获取月初日期
     * @param month 0:当前月；-n:n个月前；n:n个月后
     * @return
     */
    public static Date getMonthFirstDay(int month){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public  static int getDayOfMonth(){
        Calendar rightNow = Calendar.getInstance();
        return rightNow.get(Calendar.DAY_OF_MONTH);
    }
    
    public static Date getCurrentDate() {
    	return new Date();
    }
    
    public static int getAge(String birth,String format) {
    	if(birth == null){
    		return 0;
    	}
    	try {
    		return getAge(convertFromString(birth,format));
		} catch (Exception e) {
			log.error(e.toString(),e);
			return 0;
		}
    	
    }
    public static int getAge(Date birth) {
    	if(birth == null){
    		return 0;
    	}
    	Calendar rightNow = Calendar.getInstance();
    	int nowYear = rightNow.get(Calendar.YEAR);
    	rightNow.setTime(birth);
    	int birthYear = rightNow.get(Calendar.YEAR);
    	
    	return nowYear - birthYear;
    	
    }
    
    public static DateFormat getCnDateFormat(String pattern){
		return new SimpleDateFormat(pattern);
	}
    
    public static Date convertFromString(String date,String format) throws Exception{
        if(date == null) return null;
		if(format == null) format = YMD_FULL;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(date);
	}
    
    public static Date convertFromString(String date) throws Exception{
    	return convertFromString(date, null);
    }
    public static Date convertDate(String date) throws Exception{
    	return convertFromString(date, YMD1);
    }
    public static Date convertDateMonth(String date) throws Exception{
    	return convertFromString(date, YMD5);
    }
    
    public static Date seekNextDateMonth(String date) throws Exception{
    	SimpleDateFormat sdf = new SimpleDateFormat(YMD5);
    	Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(date));
        c.add(Calendar.MONTH, 1); 
        return c.getTime();
    }


    public static int diffMin(Date from,Date to) {
        long diff = to.getTime() - from.getTime();
        return (int)(diff/60000);
    }

    public static String getUTCTimeStr() {
        SimpleDateFormat sdf = new SimpleDateFormat(YMD_UTC);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date());
    }


    public static Date getUTCTime(String utcDate) throws Exception{
        SimpleDateFormat sdf ;
        if(utcDate.length()>=24){
            sdf = new SimpleDateFormat(YMD_UTC2);
        }else {
            sdf = new SimpleDateFormat(YMD_UTC);
        }
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.parse(utcDate);
    }
    
    public static int getSecondTimestamp(){  
        String timestamp = String.valueOf(new Date().getTime());  
        int length = timestamp.length();  
        if (length > 3) {  
            return Integer.valueOf(timestamp.substring(0,length-3));  
        } else {  
            return 0;  
        }  
    } 

	

}
