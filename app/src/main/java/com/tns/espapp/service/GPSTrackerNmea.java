package com.tns.espapp.service;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.tns.espapp.AppConstraint;
import com.tns.espapp.CaptureData;
import com.tns.espapp.HTTPPostRequestMethod;
import com.tns.espapp.LocataionData;

import com.tns.espapp.NMEAParse;
import com.tns.espapp.NetworkConnectionchecker;
import com.tns.espapp.R;
import com.tns.espapp.UnitModel;
import com.tns.espapp.Utility.FindBearing;
import com.tns.espapp.activity.HomeActivity;
import com.tns.espapp.database.DatabaseHandler;
import com.tns.espapp.database.LatLongData;
import com.tns.espapp.database.SettingData;
import com.tns.espapp.fragment.TaxiFormFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GPSTrackerNmea extends Service implements LocationListener {
    //  private final Context mContext;
    public static Handler handler = new Handler();
    public static final Bus BUS = new Bus();

    public static boolean isRunning = false;
    private Timer timer;

    boolean checkGPS = false;
    private int checkGpsspeed;
    private   int INTERVAL = 0;
    boolean checkInternet;
    double getspeed;
    Location loc;
    double latitude;
    double longitude;
    Intent intent;
    public static final String BROADCAST_ACTION = "com.tns.espapp";

    String provider;

    protected LocationManager locationManager;
    private String form_no;
    private String empid;
    private String getdate;

    private double diste;
    boolean flag_notification;
    int flag = 0;
    DatabaseHandler db;

    NMEAParse nmeaParse = new NMEAParse();

    String current_time_str;
    Handler mHandler;
    SharedPreferences preference;
    private String speed;
    int getGPSTime;
    Thread nmeaThrea;
    private static int countFlag;
    private int count;
    private String bearing_degree;
    private String bearing_name;

  /*  public GPSTracker(Context mContext) {
      //  this.mContext = mContext;
        isRunning = true;
        BUS.register(this);
        timer = new Timer();
        getLocation();
    }*/


    @Override
    public void onCreate() {
        super.onCreate();
        db = new DatabaseHandler(this);

        preference = getApplicationContext().getSharedPreferences("SERVICE", Context.MODE_PRIVATE);

        form_no = preference.getString("formno", "");
        getdate = preference.getString("getdate", "");
        empid = preference.getString("empid", "");

        List<SettingData> settingDatas = db.getGPS_settingData();
        if(settingDatas.size() >0){


            INTERVAL =settingDatas.get(0).getSett_Gpsinterval();
            checkGpsspeed =settingDatas.get(0).getSett_Gpsspeed();
        }

        intent = new Intent(BROADCAST_ACTION);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

       /* form_no = intent.getStringExtra("formno");
        getdate = intent.getStringExtra("getdate");
        empid = intent.getStringExtra("empid");*/

        registerHandler();

        startService(new Intent(getApplication(), SendLatiLongiServerIntentService.class));
        isRunning = true;
        // BUS.register(this);
        timer = new Timer();       // location.
        getLocation();


        Log.v("Service ", "ON start command is start");

        return START_STICKY;

    }

    private void getLocation() {

        try {

            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.addNmeaListener(new GpsStatus.NmeaListener() {
                public void onNmeaReceived(long timestamp, final String nmea) {
                    intent.putExtra("EXTRA", isRunning);
                    sendBroadcast(intent);

                    if (nmea.contains("$GPRMC")) {


                        SimpleDateFormat time_formatter = new SimpleDateFormat("HH:mm:ss");
                        current_time_str = time_formatter.format(timestamp);
                       // getTimer(nmea);

                        nmeaProgress(nmea,current_time_str);

                    }

                }
            });

            // getting GPS status
            checkGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status

            if (!checkGPS ) {
                Toast.makeText(getApplicationContext(), "No Service Provider Available", Toast.LENGTH_SHORT).show();
            }
            if (checkGPS ) {

                // Getting LocationManager object from System Service LOCATION_SERVICE
                // Creating a criteria object to retrieve provider
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);

                // Getting the name of the best provider
                provider = locationManager.getBestProvider(criteria, true);


                if (loc != null) {
                    Toast.makeText(getApplicationContext(), "Service Provider Available", Toast.LENGTH_SHORT).show();
                   // onLocationChanged(loc);
                }

               locationManager.requestLocationUpdates(provider, INTERVAL, 0, this);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

      //  return loc;
    }

    private  void  getTimer( final  String s ){

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {


                nmeaThrea = new Thread(){

                    @Override
                    public void run() {
                        nmeaProgress(s, current_time_str);

                    }
                };

            }
        },0,getGPSTime);

if(nmeaThrea != null){
if(! nmeaThrea.isAlive()) {
    nmeaThrea.start();

}
}
    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onLocationChanged(Location location) {

               Bundle bundle = location.getExtras();


    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }




    @Override
    public void onDestroy() {
        super.onDestroy();

        SharedPreferences.Editor editor = preference.edit();
        editor.clear();
        editor.commit();

        if (timer != null) {
            timer.cancel();

        }
        nmeaThrea.interrupted();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.removeUpdates(GPSTrackerNmea.this);

        isRunning = false;

    }





   private double distenc2(double a, double b, double c, double d){


        double distance;
        Location locationA = new Location("point A");
        locationA.setLatitude(a);
        locationA.setLongitude(b);

        Location locationB = new Location("point B");
        locationB.setLatitude(c);
        locationB.setLongitude(d);

        // distance = locationA.distanceTo(locationB);   // in meters
        distance = round(locationA.distanceTo(locationB)/1000,6);
        Log.v("Distance", distance+"");
        return distance;

    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showNotification(){
        Notification myNotication;
         NotificationManager  manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, 0);
        Notification.Builder builder = new Notification.Builder(GPSTrackerNmea.this);


        builder.setAutoCancel(true);
        builder.setTicker("Telecom Network Solutions ");
        builder.setContentTitle("Taxi App");
        builder.setContentText("Start Travelling");
        builder.setSmallIcon(R.mipmap.taxi_icon);
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(true);
        builder.setSubText("Running Status");   //API level 16
       // builder.setNumber(1);
        builder.build();
        //builder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
        myNotication = builder.getNotification();
        manager.notify(11, myNotication);

         /*
                //API level 8
                Notification myNotification8 = new Notification(R.drawable.ic_launcher, "this is ticker text 8", System.currentTimeMillis());

                Intent intent2 = new Intent(MainActivity.this, SecActivity.class);
                PendingIntent pendingIntent2 = PendingIntent.getActivity(getApplicationContext(), 2, intent2, 0);
                myNotification8.setLatestEventInfo(getApplicationContext(), "API level 8", "this is api 8 msg", pendingIntent2);
                manager.notify(11, myNotification8);
                */


    }

    private void registerHandler(){

        mHandler = new Handler() {

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public void handleMessage(Message msg) {

                NetworkConnectionchecker connectionchecker = new NetworkConnectionchecker(getApplicationContext());
                checkInternet = connectionchecker.isConnectingToInternet();

                if(!flag_notification ){
                    showNotification();
                    flag_notification = true;
                }


                // String str = (String) msg.obj;
                String time = msg.getData().getString("message_time").toString();
                String slat = msg.getData().getString("message_lat").toString();
                String slongi = msg.getData().getString("message_longi").toString();
                 speed = msg.getData().getString("speed").toString();

                double la = Double.parseDouble(slat);
                double lt = Double.parseDouble(slongi);

                List<LatLongData> latLongDataList = db.getLastLatLong(form_no);
                if(latLongDataList.size() > 0)
                {
                    diste = 0.0000000;
                    double d1_lat= Double.parseDouble(latLongDataList.get(0).getLat());
                    double d1_long = Double.parseDouble(latLongDataList.get(0).getLongi());
                    Log.d("Distance By GPS",diste+""+d1_lat+","+d1_long);
                    diste=  distenc2(d1_lat,d1_long, la,lt);
                    double bearing_degree_double = FindBearing.bearing(d1_lat, d1_long, la, lt);
                    bearing_degree =String.format("%.2f", bearing_degree_double);
                    bearing_name =FindBearing.DIRECTIONNAME;

                }
                count++;
                if(count == 5){
                    count = 0;
                    countFlag =1;
                }else {
                    countFlag =0;
                }

                if( getspeed >= checkGpsspeed) {
                    Log.d("finallatlong",""+slat+","+slongi);
                      db.addTaxiformLatLong(new LatLongData(form_no, getdate, slat, slongi, flag, String.format("%.3f", diste),time,speed,countFlag,bearing_degree,bearing_name));
                }

            }
        };

    }

    private void nmeaProgress(String rawNmea, String time ){

        if(rawNmea!= null) {


            String[] rawNmeaSplit = rawNmea.split(",");

         /*   if (rawNmeaSplit[0].equalsIgnoreCase("$GPGGA")) {

                String s = nmeaParse.parse(rawNmea).toString();
                final String[] NmeaSplit = s.split(",");

              //  Log.d("Nmea String", NmeaSplit[0] + "," + NmeaSplit[1]);
                // send GGA nmea data to handler

                Message msg = new Message();
                Bundle b = new Bundle();
                b.putString("message_lat", NmeaSplit[0]);
                b.putString("message_longi", NmeaSplit[1]);
                b.putString("message_time", current_time_str);
                msg.setData(b);
                mHandler.sendMessage(msg);


            }*/
            if(rawNmeaSplit[0].equalsIgnoreCase("$GPRMC")){

                try {

                    String s = nmeaParse.parse(rawNmea).toString();
                    final String[] NmeaSplit = s.split(",");

                Log.d("Nmea String GPRMC", NmeaSplit[0] + "," + NmeaSplit[1]+ "," + NmeaSplit[2]+ "," + NmeaSplit[6]);
                Log.d("Nmeatimestamp GPRMC",  NmeaSplit[1].toString());

                 getspeed = Double.parseDouble(NmeaSplit[6]);
                getspeed = getspeed*1.852;
            /*    latitude = round(location.getLatitude(),6);
                longitude = round(location.getLongitude(),6);*/
                Message msg = new Message();
                Bundle b = new Bundle();
                b.putString("message_lat", NmeaSplit[0]);
                b.putString("message_longi",NmeaSplit[1]);
                b.putString("message_time", time);
                b.putString("speed",  String.format("%.3f", getspeed));
                msg.setData(b);
                mHandler.sendMessage(msg);

                }catch (Exception e){

                    Toast.makeText(getApplicationContext(),"GPS ERROR",Toast.LENGTH_LONG).show();
                }
            }
        }

    }


    private boolean isValidForNmea(String rawNmea){
        boolean valid = true;
        byte[] bytes = rawNmea.getBytes();
        int checksumIndex = rawNmea.indexOf("*");

        byte checksumCalcValue = 0;
        int checksumValue;


        if ((rawNmea.charAt(0) != '$') || (checksumIndex==-1)){
            valid = false;
        }
        //
        if (valid){
            String val = rawNmea.substring(checksumIndex + 1, rawNmea.length()).trim();
            checksumValue = Integer.parseInt(val, 16);
            for (int i = 1; i < checksumIndex; i++){
                checksumCalcValue = (byte) (checksumCalcValue ^ bytes[i]);
            }
            if (checksumValue != checksumCalcValue){
                valid = false;
            }
        }
        return valid;
    }



   /* public static String covertString(String inputStr, String strFormat)
    {

        String convertstr=" ";

        for(int i = 1 ;i< inputStr.length();i++)
        {

            if(i % 2== 0){

                convertstr =  convertstr+ inputStr.charAt(i);

            }else{
                convertstr =  convertstr+ "*";
            }
        }

        return convertstr;

        }*/






    }