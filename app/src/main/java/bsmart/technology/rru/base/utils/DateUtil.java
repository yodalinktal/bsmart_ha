package bsmart.technology.rru.base.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT_OUTPUT = "yyyy/MM/dd";

    public static Date parseToDate_yyyyMMdd(String var) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        try {
            return sdf.parse(var);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String format(Date date, String format) {
        if (null == date){
            return "";
        }
        if (TextUtils.isEmpty(format)) {
            format = DATE_FORMAT_OUTPUT;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String formatVaccineDate(String var){
        String result = "";
        result = format(parseToDate_yyyyMMdd(var),DATE_FORMAT_OUTPUT);
        if (TextUtils.isEmpty(result)){
            return var;
        }
        return result;
    }

}
