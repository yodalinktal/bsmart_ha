package bsmart.technology.rru.base;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import android.text.TextUtils;


import com.blankj.utilcode.util.PhoneUtils;
import com.facebook.stetho.Stetho;
import com.google.gson.Gson;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

import bsmart.technology.rru.R;
import bsmart.technology.rru.base.utils.LocationUtils;
import bsmart.technology.rru.base.utils.ProfileUtils;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

public class App extends MultiDexApplication {

    public static Context ctx;

    public static Gson gson;

    private static Location location;
    private static int batteryLevel;
    private static ScheduledExecutorService mThread;

    public static void setThread(ScheduledExecutorService thread){
        mThread = thread;
    }

    public static void stopThread(){
        if (null != mThread){
            mThread.shutdown();
            mThread = null;
        }
    }

    public static Map<String, String> getMetaRequestData() {

        HashMap<String, String> map = new HashMap<>();

        map.put("battery_level", batteryLevel + "");
        try {
            map.put("device_id", PhoneUtils.getDeviceId());
        } catch (SecurityException e) {
        }
        if (location != null) {
            map.put("latitude", String.valueOf(LocationUtils.getInstance(ctx).showLocation().getLatitude()));
            map.put("longitude", String.valueOf(LocationUtils.getInstance(ctx).showLocation().getLongitude()));
        } else {
            map.put("latitude", "0.0");
            map.put("longitude", "0.0");
        }

        String officerId = ProfileUtils.getOfficerId();
        if (!TextUtils.isEmpty(officerId)) {
            map.put("officer_id", officerId);
        }

        String officerPassword = ProfileUtils.getOfficerPassword();
        if (!TextUtils.isEmpty(officerPassword)) {
            map.put("officer_password", officerPassword);
        }

        try {
            int smart_driver_id = ProfileUtils.getProfile().id;
            map.put("smart_driver_id", smart_driver_id + "");
        } catch (Exception e) {
        }
        try {
            // String version = Utils.getVerName(App.ctx);
            String version = "1.7.7";
            map.put("app_version", version);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        ctx = this;
        gson = new Gson();

        LocationUtils.getInstance(this).showLocation();
        Intent batteryInfo = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        batteryLevel = batteryInfo.getIntExtra("level", 0);

        Stetho.initializeWithDefaults(this);
        // ExceptionUtils.getINSTANCE().initialize(this);
        CrashReport.initCrashReport(this);

        //初始化字体

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());


    }

    public static App getApp() {
        return (App) ctx;
    }

    public static int getBatteryLevel(){
        return batteryLevel;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static void resetLocation(){
        location = LocationUtils.getInstance(ctx).showLocation();
    }

    private void initClient() {

    }
}
