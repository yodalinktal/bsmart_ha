package bsmart.technology.rru.pages;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import bsmart.technology.rru.base.Const;
import bsmart.technology.rru.base.utils.download.DownloadReceiver;
import bsmart.technology.rru.base.utils.notification.NotifyUtil;
import bsmart.technology.rru.service.CheckCaseService;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class MainBaseActivity extends AppCompatActivity {

    private DownloadReceiver downloadReceiver;
    private ServiceConnection serviceConnection;
    private int lastSetInterval = 1 * 60000;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService();

        IntentFilter msgFilter = new IntentFilter(Const.MSG_ACTION);
        this.registerReceiver(this.msgReceiver, msgFilter);

        IntentFilter filter = new IntentFilter();

        filter.addAction(Intent.ACTION_SCREEN_ON);

        filter.addAction(Intent.ACTION_SCREEN_OFF);

        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(homeAndLockReceiver, filter);

    }


    private void startService() {

        if(!NotifyUtil.isIgnoringBatteryOptimizations(this)){
            NotifyUtil.requestIgnoreBatteryOptimizations(this);
        }

        Intent i = new Intent(this, CheckCaseService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.startForegroundService(i);
        } else {
            this.startService(i);
        }
        NotifyUtil.setAlarm(this);
        // NotifyUtil.isIgnoreBatteryOption(this);
        NotifyUtil.setJobSchedule(this);

    }

    private BroadcastReceiver homeAndLockReceiver = new BroadcastReceiver() {
        String SYSTEM_REASON = "reason";
        String SYSTEM_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
                    Toast.makeText(getApplicationContext(), "home", Toast.LENGTH_SHORT).show();
                }
            } else if (action.equals(Intent.ACTION_SCREEN_ON)) {
                //屏幕亮了
                Log.i("lock-", "--on");

            } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                //屏幕黑了
                Log.i("lock-", "--off");
            }
        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please enter home to Exit app!", Toast.LENGTH_SHORT).show();
    }

    private PowerManager pManager;
    private PowerManager.WakeLock mWakeLock;
    private static String TAG =  "MCO";

    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onResume() {
        super.onResume();
        pManager = ((PowerManager) getSystemService(POWER_SERVICE));
        mWakeLock = pManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                | PowerManager.ON_AFTER_RELEASE, TAG);
        mWakeLock.acquire();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(null != mWakeLock){
            mWakeLock.release();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(this.downloadReceiver);
        if (null != this.serviceConnection) {
            this.unbindService(serviceConnection);
        }
        this.unregisterReceiver(this.msgReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                //TODO something
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == 100) {
                Log.i("HA", "battery.");
            }
        }
    }

    private void setLocationInterval(int v) {
//        if (this.lastSetInterval == v) {
//            return;
//        }

        this.lastSetInterval = v;
    }

    private final BroadcastReceiver msgReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Const.MSG_ACTION.equals(intent.getAction())) {
                int inv = intent.getIntExtra("data", 60000);
                setLocationInterval(inv);


            }
        }
    };

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

}
