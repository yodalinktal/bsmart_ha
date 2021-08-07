package bsmart.technology.rru.base.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.OnNmeaMessageListener;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import android.util.Log;

import java.util.List;

public class LocationUtils {

    private static final String TAG = "Location";
    private volatile static LocationUtils uniqueInstance;
    private LocationManager locationManager;
    private String locationProvider;
    private Location location;
    private Context mContext;

    private OnNmeaMessageListener mOnNmeaMessageListener;
    private GpsStatus.NmeaListener mLegacyNmeaListener;

    private LocationUtils(Context context) {
        mContext = context;
        getLocation();
    }


    public static LocationUtils getInstance(Context context) {
        if (uniqueInstance == null) {
            synchronized (LocationUtils.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new LocationUtils(context);
                }
            }
        }
        return uniqueInstance;
    }

    private void getLocation() {

        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);


        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {

            Log.d(TAG, "NETWORK LOCATION");
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
            Log.d(TAG, "GPS LOCATION");
            locationProvider = LocationManager.GPS_PROVIDER;
        } else {
            Log.d(TAG, "NOT PROVIDER");
            return;
        }

        if (Build.VERSION.SDK_INT >= 23 &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            setLocation(location);
        }

        locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);
        addNmeaListener();
    }

    public void refreshLocation() {
        getLocation();
    }

    private void setLocation(Location location) {
        this.location = location;
        String address = "Latitude：" + location.getLatitude() + "Longitude：" + location.getLongitude();
        Log.d(TAG, address);
        Log.d(TAG, "locationProvider："+locationProvider);
    }


    public Location showLocation() {
        return location;
    }

    public void removeLocationUpdatesListener() {

        if (Build.VERSION.SDK_INT >= 23 &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (locationManager != null) {
            uniqueInstance = null;
            locationManager.removeUpdates(locationListener);
        }
    }

    LocationListener locationListener = new LocationListener() {


        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }


        @Override
        public void onProviderEnabled(String provider) {

        }


        @Override
        public void onProviderDisabled(String provider) {

        }


        @Override
        public void onLocationChanged(Location location) {
            location.getAccuracy();
            setLocation(location);
        }
    };

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addNmeaListenerAndroidN() {

        if (null != mOnNmeaMessageListener){
            locationManager.removeNmeaListener(mOnNmeaMessageListener);
            mOnNmeaMessageListener = null;
        }

        if (mOnNmeaMessageListener == null) {
            mOnNmeaMessageListener = new OnNmeaMessageListener() {
                @Override
                public void onNmeaMessage(String message, long timestamp) {

                }
            };
        }

        locationManager.addNmeaListener(mOnNmeaMessageListener);
    }




    @SuppressLint("MissingPermission")
    private void addLegacyNmeaListener() {

    }

    private void addNmeaListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            addNmeaListenerAndroidN();
        } else {
            addLegacyNmeaListener();
        }
    }

    GpsStatus.NmeaListener nmeaListener = new GpsStatus.NmeaListener() {
        String GGA_FIX_DATA = "$GNGGA";
        String GGA_FIX_DATA1 = "$GPGGA";

        @Override
        public void onNmeaReceived(long l, String s) {

        }
    };

    public int getLastKnownSatelliteNumber() {
       return ProfileUtils.getLastKnownSatelliteNumber();
    }

    public void setLastKnownSatelliteNumber(int lastKnownSatelliteNumber) {
        ProfileUtils.setLastKnownSatelliteNumber(lastKnownSatelliteNumber);
    }

    public float getLastKnownHdop() {
        return ProfileUtils.getLastKnownHdop();
    }

    public void setLastKnownHdop(float lastKnownHdop) {
        ProfileUtils.setLastKnownHdop(lastKnownHdop);
    }

}
