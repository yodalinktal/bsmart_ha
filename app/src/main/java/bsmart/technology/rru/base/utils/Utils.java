package bsmart.technology.rru.base.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import bsmart.technology.rru.base.ClientType;
import bsmart.technology.rru.base.Const;
import bsmart.technology.rru.base.utils.download.DownloadUtil;

public class Utils {

    public static String getVerName(Context context) {
        String verName = "1.0.0";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    // 检测本地安装包是否OK.
    public static String checkLocalVersionReady(Context ctx, String version, String md5) {
        File dir = new File(ctx.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "rru/" + version);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File apk = new File(dir, "update.apk");
        if (!apk.exists()) {
            return null;
        }
        return apk.getAbsolutePath();
    }

    public static void showUpgradeDialog(Context ctx, String version, String url) {
        new MaterialDialog.Builder(ctx)
                .title("New Version:" + version)
                .content("Click Install to update to " + version)
                .positiveText("Install")
                .negativeText("Cancel")
                .negativeColor(Color.RED)
                .canceledOnTouchOutside(false)
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Log.d("Andrewlu", "start Download");
                        downloadApk(ctx, version, url);
                        dialog.dismiss();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Log.d("Andrewlu", "download canceled");
                        //android.os.Process.killProcess(android.os.Process.myPid());
                    }
                }).show();

    }

    public static void downloadApk(Context ctx, String version, String url) {
        new MaterialDialog.Builder(ctx)
                .title("Download")
                .content("Please wait for downloading...")
                .negativeText("Exit")
                .canceledOnTouchOutside(false)
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .cancelable(false)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                }).show();
        DownloadUtil downloadUtil = new DownloadUtil(ctx);

        String appName = "HA";

        downloadUtil.download(url, appName, version + " downloading...", getDownloadFilePath(ctx, version));
    }

    private static String getDownloadFilePath(Context ctx, String version) {
        File dir = new File(ctx.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "rru/" + version);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String appName = "HA";
        File apk = new File(dir, appName+".apk");
        return apk.getAbsolutePath();
    }

    @NonNull
    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getAndroidID(Context context) {
        String ANDROID_ID = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return ANDROID_ID;
    }

    public static void navTo(Context context, String lat, String lng) {
        try {
            Uri gmmIntentUri = Uri.parse(String.format("geo:0,0?q=%s,%s", lat, lng));

            Log.i("HA", "NavTo:" + gmmIntentUri.toString());

            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            context.startActivity(mapIntent);

        } catch (Exception e) {
            Toast.makeText(context, "Please Install Google map first.", Toast.LENGTH_SHORT).show();

            Uri uri = Uri.parse("market://details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        }
    }


    public static void navTo2(Context context, String lat, String lng) {
        try {
            Uri gmmIntentUri = Uri.parse(String.format("google.navigation:q=%s,%s", lat, lng));

            Log.i("HA", "NavTo:" + gmmIntentUri.toString());

            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            context.startActivity(mapIntent);

        } catch (Exception e) {
            Toast.makeText(context, "Please Install Google map first.", Toast.LENGTH_SHORT).show();

            Uri uri = Uri.parse("market://details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        }
    }

    private final static SimpleDateFormat SERVER_DT = new SimpleDateFormat(Const.SERVER_DT);
    private final static SimpleDateFormat LOCAL_DT = new SimpleDateFormat(Const.LOCAL_DT);

    public static String parseDT(String serverDate) {
        if (serverDate == null || serverDate.length() <= 0) return "";
        try {
            Date d = SERVER_DT.parse(serverDate);
            return LOCAL_DT.format(d);
        } catch (Exception e) {
            return serverDate;
        }
    }

    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
