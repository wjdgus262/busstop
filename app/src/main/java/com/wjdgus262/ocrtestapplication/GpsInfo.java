package com.wjdgus262.ocrtestapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

public class GpsInfo extends Service implements LocationListener {


    private final Context mContext;

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean isGetLocation = false;
    Location location;
    double lat;
    double lon;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 2;
    private static final long MIN_TIME_BW_UPDATES = 1000*60*1;

    protected LocationManager locationManager;

    public GpsInfo(Context context)
    {
        this.mContext = context;
        getLocation();
    }


    @TargetApi(23)
    public Location getLocation()
    {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_DENIED)
        {
            return null;
        }

        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // GPS 정보 가져오기
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // 현재 네트워크 상태 값 알아오기
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                Toast.makeText(getApplicationContext(),"GPS가 잡히지 않습니다.",Toast.LENGTH_SHORT).show();
                // GPS 와 네트워크사용이 가능하지 않을때 소스 구현
            } else {
                this.isGetLocation = true;
                // 네트워크 정보로 부터 위치값 가져오기
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            1,
                            15, this);

                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            // 위도 경도 저장
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                        }
                    }
                }

                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                lat = location.getLatitude();
                                lon = location.getLongitude();
                            }
                        }
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"GPS가 잡히지 않습니다.",Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    public void stopUsingGPS()
    {
        if(locationManager != null)
        {
            locationManager.removeUpdates(GpsInfo.this);
        }
    }

    public double getLatitude()
    {
        if(location != null)
        {
            lat = location.getLatitude();
        }
        return lat;
    }

    public double getLongitude()
    {
        if(location != null)
        {
            lon = location.getLongitude();
        }
        return lon;
    }

    public boolean isGetLocation()
    {
        return this.isGetLocation;
    }

    public void showSettingsAlert()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setTitle("GPS 사용유무셋팅");
        alertDialog.setMessage("GPS 셋팅이 되지 않았을수도 있습니다. \n 설정창으로 가시겠습니까?");

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertDialog.show();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }



    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
