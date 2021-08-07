package bsmart.technology.rru.base.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.blankj.utilcode.util.PhoneUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;

/**
 * Author: yoda
 * DateTime: 2020/5/29 23:09
 */
public class BSmartUtil {

    public static String Start_IMEI = "9";

    public static String Rwanda = "250";
    public static String Kenya = "254";
    public static String Tanzania = "255";
    public static String Uganda = "256";
    public static String Burundi = "257";
    public static String South_Sudan = "211";
    public static String DRC = "243";
    public static String China = "86";

    public static String getCodeByCountry(String country){
        if ("Rwanda".equals(country)){
            return Rwanda;
        }else if ("Kenya".equals(country)){
            return Kenya;
        }else if ("Tanzania".equals(country)){
            return Tanzania;
        }else if ("Uganda".equals(country)){
            return Uganda;
        }else if ("Burundi".equals(country)){
            return Burundi;
        }else if ("South Sudan".equals(country)){
            return South_Sudan;
        }else if ("DRC".equals(country)){
            return DRC;
        }else if ("Test".equals(country)){
            return "1";
        }else if ("USA".equals(country)){
            return "1";
        }else if ("India".equals(country)){
            return "1";
        }else if ("United Kingdom".equals(country)){
            return "1";
        }else{
            return "0";
        }
    }

    /**
     * 获取设备信息
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        String deviceId = "";
        deviceId = PhoneUtils.getDeviceId();
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return deviceId;
    }

    public static String getIMEI(String countryCode,String phoneNumber){
        String whole = ""+countryCode+phoneNumber;
        if (whole.length()<15){
            int left = 15-whole.length();
            for (int i = 0; i < left; i++) {
                whole = "9" + whole;
            }
        }
        return whole;
    }

    public static String getQRCodeDigit(String account,String smart_driver_id,String booking_id){
        String digit = "";
        try{
            Long value = new Long(account+smart_driver_id+booking_id);
            String hex = Long.toHexString(value).toUpperCase();
            digit = hex+"-"+value;
            if (digit.length()<32){
                digit = "-"+hex+"-"+value;
                int left = 31-digit.length();
                for (int i = 0; i < left; i++) {
                    digit = "0" + digit;
                }
            }
        }catch(Exception e){

        }
        return digit;
    }

    public static Date formatNormal(String var){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(var);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static String formatTimestampEnglish(Long timestamp){
        Date date = new Date(timestamp);
        return formatEnglish(date);
    }

    public static String format(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String formatEnglish(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("d/MMM/yyyy HH:mm", Locale.ENGLISH);
        return sdf.format(date);
    }

    public static String displayPhoneNumber(String countryCode,String phoneNumber){
        return "";
    }

    /**
     * Rwanda = 250
     * Kenya = 254
     * Tanzania = 255
     * Uganda = 256
     * Burundi = 257
     * South Sudan = 211
     * DRC = 243
     * @param mPhoneNumber
     * @return
     */
    public static String getAvailableAfricaMobileNo(String mPhoneNumber){
        if (!TextUtils.isEmpty(mPhoneNumber)){
            mPhoneNumber =  mPhoneNumber.replace("+","");
            if(mPhoneNumber.startsWith(Rwanda)
                    ||mPhoneNumber.startsWith(Kenya)
                    ||mPhoneNumber.startsWith(Tanzania)
                    ||mPhoneNumber.startsWith(Uganda)
                    ||mPhoneNumber.startsWith(Burundi)
                    ||mPhoneNumber.startsWith(South_Sudan)
                    ||mPhoneNumber.startsWith(DRC)){
                mPhoneNumber =  mPhoneNumber.substring(3);
                return mPhoneNumber;
            }else if(mPhoneNumber.startsWith("0")){
                mPhoneNumber =  mPhoneNumber.substring(1);
                return mPhoneNumber;
            }else if(mPhoneNumber.length()==9){
                return mPhoneNumber;
            }else if(mPhoneNumber.startsWith(China)){ //为了中国本土的开发者能够使用
                mPhoneNumber =  mPhoneNumber.substring(2);
                if (mPhoneNumber.length()==11){
                    return mPhoneNumber;
                }
            }
        }
        return "";
    }

    /**
     *
     * @param countryCode : 250, 254, 255, 256, 257, 211, 243
     * @param phoneNumber 9 length in Africa
     * @return
     */
    public static String getBidByPhoneNum(String countryCode,String phoneNumber){

        if (null==countryCode || ""==countryCode.trim() || null == phoneNumber || ""==phoneNumber.trim()){
            return "";
        }

        if (countryCode.length()<3){
            return "";
        }
        String bid = "";

        Integer first = Integer.parseInt(countryCode.substring(0,1));
        Integer third = Integer.parseInt(countryCode.substring(2,3));

        Long decValue = Long.parseLong(phoneNumber);
        String hexValue = Long.toHexString(decValue).toUpperCase();

        String firstValue = hexValue.substring(0,1);

        Integer i_prefix = first+third+new Integer(firstValue);

        String prefix = Integer.toHexString(i_prefix).toUpperCase();

        bid = prefix+hexValue.substring(1);

        return bid;
    }

    public static String getPhoneNum(Context context) {

        if (ActivityCompat.checkSelfPermission(context, READ_PHONE_NUMBERS) ==
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager tMgr = (TelephonyManager)   context.getSystemService(Context.TELEPHONY_SERVICE);
            String mPhoneNumber = tMgr.getLine1Number();

            if (!TextUtils.isEmpty(mPhoneNumber)){

            }

            return mPhoneNumber;
        }
        return "";
    }

    public static String getDateByDay(int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, day);
        String dayDate = sdf.format(c.getTime());
        return dayDate+" 23:59:59";
    }

    public static String getDateByDayBefore(int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, day);
        String dayDate = sdf.format(c.getTime());
        return dayDate+" 00:00:00";
    }

    public static Date getCovid19ValidDate(String posted_at){
        Date posted_date = parseToDate(posted_at,"yyyy-MM-dd hh:mm:ss");
        if (null == posted_at){
            posted_date = new Date();
        }
        Date valid =  getDateDateByDay(14,posted_date);
        return valid;
    }

    public static String formatYYYYMMDDHHMM(String posted_at){
        Date posted_date = parseToDate(posted_at,"yyyy-MM-dd hh:mm:ss");
        if (null == posted_date){
            posted_date = new Date();
        }
        SimpleDateFormat post_sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        return post_sdf.format(posted_date);
    }

    public static String getddMMyyyy(String posted_at){
        Date posted_date = parseToDate(posted_at,"yyyy-MM-dd hh:mm:ss");
        if (null == posted_date){
            posted_date = new Date();
        }
        SimpleDateFormat post_sdf = new SimpleDateFormat("dd/MM/yyyy");
        return post_sdf.format(posted_date);
    }

    public static String getyyyMMdd(String posted_at){
        Date posted_date = parseToDate(posted_at,"yyyy-MM-dd hh:mm:ss");
        if (null == posted_date){
            posted_date = new Date();
        }
        SimpleDateFormat post_sdf = new SimpleDateFormat("yyyyMMdd");
        return post_sdf.format(posted_date);
    }

    public static Date parseToDate(String var, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(var);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getNowDate(){
        Date now = new Date();
        SimpleDateFormat post_sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return post_sdf.format(now);
    }

    public static String getGPSTime(long timestamp){
        Date gps = new Date(timestamp);
        SimpleDateFormat post_sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return post_sdf.format(gps);
    }

    public static Date getDateDateByDay(int day, Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, day);
        return c.getTime();
    }


    public static String getAndroidId(Context context){
        return Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

}
