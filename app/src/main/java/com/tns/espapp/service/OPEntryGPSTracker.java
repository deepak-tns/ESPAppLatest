package com.tns.espapp.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.tns.espapp.AppConstraint;
import com.tns.espapp.NetworkConnectionchecker;
import com.tns.espapp.R;
import com.tns.espapp.activity.HomeActivity;
import com.tns.espapp.database.DatabaseHandler;
import com.tns.espapp.database.LatLongData;
import com.tns.espapp.database.OPEntryLatLog;
import com.tns.espapp.database.SettingData;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

public class OPEntryGPSTracker extends Service implements com.google.android.gms.location.LocationListener,  GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener , GpsStatus.Listener {

    //  private final Context mContext;
    private static final String TAG = "Opentrygpstracker";

    public static boolean isRunning = false;


    boolean checkInternet;

    double latitude;
    double longitude;
    Intent intent;
    public static final String BROADCAST_ACTION = "OPENTRYGPSTRACKER";
    private   int INTERVAL = 0;


    private String form_no;
    private String empid;
    private String getdate;
    private String lats, longi;
    private String getTime;


    boolean flag_notification;
    int flag = 0;
    DatabaseHandler db;
    SharedPreferences preference;
    private String speed;
    private int checkGpsspeed =1;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    private static final float MIN_ACCURACY = 25.0f;


    LocationManager lm ;
    private double prelat, prelong,predistence;
    double currlat,currlong;
    double diste = 0;
    static double disteTotal = 0;
    double spredistence =0;

    @Override
    public void onCreate() {
        super.onCreate();
        db = new DatabaseHandler(this);

        preference = getApplicationContext().getSharedPreferences("OPENTRYSERVICE", Context.MODE_PRIVATE);
        form_no = preference.getString("formno", "");
        getdate = preference.getString("getdate", "");
        empid = preference.getString("empid", "");
        intent = new Intent(BROADCAST_ACTION);

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        isRunning = true;
        // BUS.register(this);
        getLocation();
        Log.v("OPEntryService ", "ON start command is start");
        return START_STICKY;

    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL *1000);
        mLocationRequest.setFastestInterval(INTERVAL *1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        lm.addGpsStatusListener(this);

    }

    private void getLocation() {
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();
        Log.d(TAG, "Location update resumed .....................");

        //  return loc;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onLocationChanged(Location location) {


        SimpleDateFormat time_formatter = new SimpleDateFormat("HH:mm:ss");

        getTime = time_formatter.format(location.getTime());
        double getspeed = 3.6 * location.getSpeed();
        speed = String.format("%.3f", getspeed);
        latitude = round(location.getLatitude(), 6);
        longitude = round(location.getLongitude(), 6);

        Log.v("OPEntryLatLong", latitude + "," + longitude);

        lats = String.format("%.6f", latitude);
        longi = String.format("%.6f", longitude);


        NetworkConnectionchecker connectionchecker = new NetworkConnectionchecker(getApplicationContext());
        checkInternet = connectionchecker.isConnectingToInternet();
        List<OPEntryLatLog> latLongDataList = db.getLastOPEntryLatLong(form_no);
        startService(new Intent(getApplication(), SendOPEntryLatiLongiServerIntentService.class));
        if (latLongDataList.size() > 0)
        {
            double d1_lat = Double.parseDouble(latLongDataList.get(0).getLat());
            double d1_long = Double.parseDouble(latLongDataList.get(0).getLongi());
            diste = distenc2(d1_lat, d1_long, latitude, longitude);
        }

        if (getspeed >= 0)
        {
            db.insertOPEntryLatLong(new OPEntryLatLog(form_no, getdate, lats, longi, flag, String.format("%.4f", diste), getTime, speed));
        }


    }
    protected void stopLocationUpdates()
    {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d(TAG, "Location update stopped .......................");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = preference.edit();
        editor.clear();
        editor.commit();
        stopLocationUpdates();
        mGoogleApiClient.disconnect();
        isRunning = false;
        //BUS.unregister(this);
    }



    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private double distenc2(double a, double b, double c, double d) {
        double distance;
        Location locationA = new Location("point A");
        locationA.setLatitude(a);
        locationA.setLongitude(b);

        Location locationB = new Location("point B");
        locationB.setLatitude(c);
        locationB.setLongitude(d);

        // distance = locationA.distanceTo(locationB);   // in meters
        distance = round(locationA.distanceTo(locationB) / 1000, 6);
        Log.v("Distance", distance + "");
        return distance;

    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "Location update started ..............: ");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended ..............: ");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed ..............: ");

    }

    @Override
    public void onGpsStatusChanged(int event) {
        GpsStatus gpsStatus = lm.getGpsStatus(null);
        if (gpsStatus != null) {
            Iterable<GpsSatellite> satellites = gpsStatus.getSatellites();
            Iterator<GpsSatellite> sat = satellites.iterator();
            String lSatellites = null;
            int i = 0;
            while (sat.hasNext()) {
                GpsSatellite satellite = sat.next();
                lSatellites = "Satellite" + (i++) + ": "
                        + satellite.getPrn() + ","
                        + satellite.usedInFix() + ","
                        + satellite.getSnr() + ","
                        + satellite.getAzimuth() + ","
                        + satellite.getElevation() + "\n\n";

                Log.d("SATELLITE", lSatellites);
            }
        }


    }

}