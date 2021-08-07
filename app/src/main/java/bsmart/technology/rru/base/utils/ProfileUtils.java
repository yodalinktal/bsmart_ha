package bsmart.technology.rru.base.utils;

import android.text.TextUtils;

import com.blankj.utilcode.util.SPUtils;

import bsmart.technology.rru.base.App;
import bsmart.technology.rru.base.api.bean.LoginRectsBean;
import bsmart.technology.rru.base.api.bean.ProfileBean;

public class ProfileUtils {
    private ProfileUtils() {
    }

    public static int getLastKnownSatelliteNumber() {
        return SPUtils.getInstance().getInt("lastKnownSatelliteNumber",0);
    }

    public static void setLastKnownSatelliteNumber(int lastKnownSatelliteNumber) {
        SPUtils.getInstance().put("lastKnownSatelliteNumber",lastKnownSatelliteNumber);
    }

    public static float getLastKnownHdop() {
       return SPUtils.getInstance().getFloat("lastKnownHdop",0.0f);
    }

    public static void setLastKnownHdop(float lastKnownHdop) {
        SPUtils.getInstance().put("lastKnownHdop",lastKnownHdop);
    }

    public static boolean getLogoutFlag(){
        return SPUtils.getInstance().getBoolean("appExit");
    }
    public static void setLogoutFlag(){
        SPUtils.getInstance().put("appExit",true);
    }

    public static void setLoginFlag(){
        SPUtils.getInstance().put("appExit",false);
    }

    public static void saveLoginCountryCode(String countryCode){
        SPUtils.getInstance().put("_countryCode",countryCode);
    }

    public static void saveLoginClinicCode(String clinicCode){
        SPUtils.getInstance().put("_clinicCode",clinicCode);
    }

    public static String getLoginCountryCode(){
       return SPUtils.getInstance().getString("_countryCode");
    }

    public static String getLoginClinicCode(){
        return SPUtils.getInstance().getString("_clinicCode");
    }


    public static void saveCurrentJobId(String jobId){
        if (!TextUtils.isEmpty(jobId)){
            SPUtils.getInstance().put("currentJobId",jobId);
        }
    }
    public static String getCurrentJobId(){
        return SPUtils.getInstance().getString("currentJobId","0");
    }

    public static void saveCountryCode(String countryCode){
        SPUtils.getInstance().put("countryCode",countryCode);
    }
    public static String getCountryCode(){
        return SPUtils.getInstance().getString("countryCode");
    }
    public static void saveMobilePhone(String mobilePhone){
        SPUtils.getInstance().put("mobilePhone",mobilePhone);
    }
    public static String getMobilePhone(){
        return SPUtils.getInstance().getString("mobilePhone");
    }

    public static void saveProfile(ProfileBean bean) {
        SPUtils.getInstance().put("profile", App.gson.toJson(bean));
    }


    ////////  End RECTs Account ///////////
    public static void saveRectProfile(LoginRectsBean bean) {
        SPUtils.getInstance().put("rect_profile", App.gson.toJson(bean));
    }

    public static LoginRectsBean getRectLoginBean(){
        return App.gson.fromJson(SPUtils.getInstance().getString("rect_profile"), LoginRectsBean.class);
    }

    public static String getCUserRid() {
        try {
            LoginRectsBean profile = getRectLoginBean();
            return profile.c_user_rid;
        } catch (Exception e) {
            return null;
        }
    }

    ////////  Start RECTs Account ///////////


    public static void removeSharePreference() {
        SPUtils.getInstance().remove("profile");
        SPUtils.getInstance().remove("officer_id");
        SPUtils.getInstance().remove("officer_password");
        SPUtils.getInstance().remove("cmc_to_call");
    }

    public static ProfileBean getProfile() {
        String profileString = SPUtils.getInstance().getString("profile");
        if (TextUtils.isEmpty(profileString)){
            return null;
        }
        return App.gson.fromJson(profileString, ProfileBean.class);
    }

    public static void saveIdAndPassword(String id, String password) {
        SPUtils.getInstance().put("officer_id", id);
        SPUtils.getInstance().put("officer_password", password);
    }

    public static String getOfficerId() {
        return SPUtils.getInstance().getString("officer_id");
    }

    public static String getOfficerPassword() {
        return SPUtils.getInstance().getString("officer_password");
    }

    public static int getCmcToCall() {
        return SPUtils.getInstance().getInt("cmc_to_call");
    }

    public static void saveCmcToCall(int i) {
        SPUtils.getInstance().put("cmc_to_call", i);
    }

    public static void setUpdateCaseTime(int key,String timestamp){
        SPUtils.getInstance().put("case_status_update_"+key,timestamp);
    }
    public static String getUpdateCaseTime(int key){
        return SPUtils.getInstance().getString("case_status_update_"+key,"");
    }
}
