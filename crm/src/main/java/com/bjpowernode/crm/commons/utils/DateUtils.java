package com.bjpowernode.crm.commons.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtils {

    public static String getStringAsDateTime(Date date){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        return sf.format(date);
    }
    public static String getStringAsDate(Date date){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(date);
    }
    public static String getStringAsTime(Date date){
        SimpleDateFormat sf = new SimpleDateFormat("HH-mm-ss");
        return sf.format(date);
    }
}
