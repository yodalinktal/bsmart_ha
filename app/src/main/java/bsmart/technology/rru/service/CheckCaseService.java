package bsmart.technology.rru.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import bsmart.technology.rru.base.ClientType;
import bsmart.technology.rru.base.Const;
import bsmart.technology.rru.base.db.MySQLiteOpenHelper;
import bsmart.technology.rru.base.utils.notification.NotifyUtil;


public class CheckCaseService extends Service {
    private final ScheduledExecutorService thread = Executors.newSingleThreadScheduledExecutor();
    PowerManager.WakeLock mWakeLock;// 电源锁
    private SQLiteDatabase sqLiteDatabase;
    @Override
    public void onCreate() {
        super.onCreate();

        acquireWakeLock();

        String appName = "HA";

        Notification n = NotifyUtil.getNotification(this, appName+" is running", true, NotifyUtil.getPendingIntent(this));
        this.startForeground(1, n);
        thread.scheduleAtFixedRate(netWorker, 30, 30, TimeUnit.SECONDS);


    }

    @SuppressLint("InvalidWakeLockTag")
    private void acquireWakeLock() {
        if (null == mWakeLock) {
            PowerManager pm = (PowerManager) getSystemService(CheckCaseService.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK
                    | PowerManager.ON_AFTER_RELEASE, "myService");
            if (null != mWakeLock) {
                mWakeLock.acquire();
            }
        }
    }

    private void releaseWakeLock() {
        if (null != mWakeLock) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sqLiteDatabase.close();
        //releaseWakeLock();
        Intent i = new Intent(this, CheckCaseService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.startForegroundService(i);
        } else {
            this.startService(i);
        }//重启服务
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sqLiteDatabase = MySQLiteOpenHelper.getInstance(this).getWritableDatabase();
        return Service.START_STICKY;//当服务被杀死的时候重启服务

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private final Runnable netWorker = new Runnable() {
        private List<Integer> caseBeans = null;

        @Override
        public void run() {}
    };
}
