package bsmart.technology.rru.base.utils.notification;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import bsmart.technology.rru.R;
import bsmart.technology.rru.base.ClientType;
import bsmart.technology.rru.base.Const;
import bsmart.technology.rru.pages.LoginActivity;
import bsmart.technology.rru.service.ScheduleService;

public class NotifyUtil {

    public static void sendNotify(Context context, String msg, boolean isStick, PendingIntent intent) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = getNotification(context, msg, isStick, intent);
        if (isStick) {
            nm.notify(1, notification);
        } else {
            nm.notify(2, notification);
        }
    }

    public static Notification getNotification(Context context, String msg, boolean isStick, PendingIntent intent) {
        // 兼容  API 26，Android 8.0

        String appName = "HA";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 第三个参数表示通知的重要程度，默认则只在通知栏闪烁一下
            NotificationChannel notificationChannel = new NotificationChannel(appName, appName, NotificationManager.IMPORTANCE_HIGH);
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // 注册通道，注册后除非卸载再安装否则不改变
            nm.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NotificationChannel.DEFAULT_CHANNEL_ID)
                .setContentTitle(appName)
                .setContentText(msg)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.icon_logo)
                .setContentIntent(intent);
        builder.setChannelId(appName);
        builder.setDefaults(Notification.DEFAULT_ALL);
        Notification notification = builder.build();
        int flags = (Notification.FLAG_FOREGROUND_SERVICE | Notification.FLAG_ONLY_ALERT_ONCE);
        if (isStick) {
            flags |= (Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR);
        } else {
            flags |= (Notification.FLAG_AUTO_CANCEL);
        }
        notification.flags |= flags;
        return notification;
    }

    public static PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    public static void setAlarm(Context context) {
        PendingIntent mPendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(
                "com.andrewlu.cn.ticker"), 0);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(mPendingIntent);
        manager.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(), 2 * 60000, mPendingIntent);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean isIgnoringBatteryOptimizations(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = activity.getPackageName();
            PowerManager pm = (PowerManager) activity
                    .getSystemService(Context.POWER_SERVICE);
            if (pm.isIgnoringBatteryOptimizations(packageName)) {
                return true;
            } else {
                return false;
            }
        }else {
            return false;
        }
    }

    /**
     * 针对N以上的Doze模式
     *
     * @param activity
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static void requestIgnoreBatteryOptimizations(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                if (isXiaomi()) {
                    showActivity("com.miui.securitycenter",
                            "com.miui.permcenter.autostart.AutoStartManagementActivity", activity);
                }else if (isHuawei()){
                    try {
                        showActivity("com.huawei.systemmanager",
                                "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity", activity);
                    } catch (Exception e) {
                        showActivity("com.huawei.systemmanager",
                                "com.huawei.systemmanager.optimize.bootstart.BootStartActivity", activity);
                    }
                }else if (isMeizu()){
                    showActivity("com.meizu.safe", activity);
                }else if (isSamsung()){
                    try {
                        showActivity("com.samsung.android.sm_cn", activity);
                    } catch (Exception e) {
                        showActivity("com.samsung.android.sm", activity);
                    }
                }else if (isOPPO()){
                    try {
                        showActivity("com.coloros.phonemanager", activity);
                    } catch (Exception e1) {
                        try {
                            showActivity("com.oppo.safe", activity);
                        } catch (Exception e2) {
                            try {
                                showActivity("com.coloros.oppoguardelf", activity);
                            } catch (Exception e3) {
                                showActivity("com.coloros.safecenter", activity);
                            }
                        }
                    }
                }else if (isVIVO()){
                    showActivity("com.iqoo.secure", activity);
                }
                Intent intent = new Intent();
                String packageName = activity.getPackageName();
                PowerManager pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
                if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                    intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + packageName));
                    activity.startActivityForResult(intent, 100);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void setJobSchedule(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            JobInfo.Builder builder = new JobInfo.Builder(1,
                    new ComponentName(context.getPackageName(), ScheduleService.class.getName()));
            builder.setPeriodic(30 * 60000);
            scheduler.cancelAll();
            scheduler.schedule(builder.build());
        }
    }

    //判断手机厂商品牌及跳转启动管理页
    public static boolean isHuawei() {
        if (Build.BRAND == null) {
            return false;
        } else {
            return Build.BRAND.toLowerCase().equals("huawei") || Build.BRAND.toLowerCase().equals("honor");
        }
    }

    public static boolean isXiaomi() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("xiaomi");
    }

    public static boolean isOPPO() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("oppo");
    }

    public static boolean isVIVO() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("vivo");
    }

    public static boolean isMeizu() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("meizu");
    }

    public static boolean isSamsung() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("samsung");
    }

    /**
     * 跳转到指定应用的首页
     */
    public static void showActivity(@NonNull String packageName, Activity activity) {
        Intent intent = activity.getPackageManager().getLaunchIntentForPackage(packageName);
        activity.startActivity(intent);
    }

    /**
     * 跳转到指定应用的指定页面
     */
    public static void showActivity(@NonNull String packageName, @NonNull String activityDir, Activity activity) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, activityDir));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
}
