package com.tns.espapp.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tns.espapp.NMEAParse;
import com.tns.espapp.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;


public class LiveTaxi extends Fragment implements LocationListener {
    String[] permissions = {
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"
    };
    public static final int MULTIPLE_PERMISSIONS = 10;

    boolean isGPSEnabled = false;


    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    protected LocationManager locationManager;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    private EditText ed_lat, ed_long, ed_speed, edt_startime, ed_stoptime, ed_total_traveltime, ed_total_distence,ed_idletime,noof_satelite;
    private EditText ed_total_distence2;
    private EditText ed_total_distence3;

    String globalnmeaString, current_time_str;
    NMEAParse nmeaParse = new NMEAParse();
    Button btn_start, btn_stop;
    private  double prelat , prelong, predistence,predistence2,predistence3,totaldistance;
    private String    st_statime,st_stoptime;

    private  int idleCount,  sec =0;;

    public LiveTaxi() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_live_taxi, container, false);
        if (checkPermissions())
        {
        }
        findIDS(view);

        return view;
    }
    private void findIDS(View v) {

        ed_lat = (EditText)v. findViewById(R.id.edt_lat);
        ed_long = (EditText)v.  findViewById(R.id.edt_long);
        ed_speed = (EditText)v.  findViewById(R.id.edt_speed);
        edt_startime = (EditText)v.  findViewById(R.id.edt_starttime);
        ed_stoptime = (EditText)v.  findViewById(R.id.edt_stoptime);
        ed_total_distence = (EditText)v.  findViewById(R.id.edt_total_distance);
        ed_total_distence2 = (EditText)v.  findViewById(R.id.edt_total_distance2);
        ed_total_distence3 = (EditText)v.  findViewById(R.id.edt_total_distance3);
        ed_total_traveltime = (EditText)v.  findViewById(R.id.edt_totaltraveltime);
        ed_idletime=(EditText)v. findViewById(R.id.edt_idletime);
        noof_satelite =(EditText)v. findViewById(R.id.edt_no_of_satelite);
        btn_start = (Button)v.  findViewById(R.id.btn_start);
        btn_stop = (Button)v.  findViewById(R.id.btn_stop);

        //  String htmlAsString = getString(R.string.html);
        //  Spanned htmlAsSpanned = Html.fromHtml(htmlAsString);
        // used by TextView
        //ed_speed.setText(htmlAsSpanned);


        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
                DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                Calendar cal = Calendar.getInstance();

                st_statime =dateFormat.format(cal.getTime());
                edt_startime.setText(st_statime);

            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if(locationManager != null) {
                    st_stoptime = ed_stoptime.getText().toString();
                    locationManager.removeNmeaListener(gpslistner);
                   locationManager.removeUpdates(LiveTaxi.this);

       /*         String stsstratSplit[] =  st_statime.split(":");

                String start_hour = stsstratSplit[0];
                String start_mintue = stsstratSplit[1];
                String start_seconds = stsstratSplit[2];

                    String stsstopSplit[] = st_stoptime.split(":");

                    String stop_hour = stsstopSplit[0];
                    String stop_mintue = stsstopSplit[1];
                    String stop_seconds = stsstopSplit[2];

                    int remain_hour = Integer.parseInt(stop_hour) - Integer.parseInt(start_hour);
                    int remain_mitue = Integer.parseInt(stop_mintue) - Integer.parseInt(start_mintue);
                    int remain_sec = Integer.parseInt(stop_seconds) - Integer.parseInt(start_seconds);

                    StringBuilder add = new StringBuilder(remain_hour + ":");
                    add.append(remain_mitue + ":");
                    add.append(remain_sec + "");*/

                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    Date date1 = null;
                    Date date2 = null;
                    try {
                        date1 = format.parse(st_statime);
                        date2 = format.parse(st_stoptime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    long difference = date2.getTime() - date1.getTime();
                    if (difference != 0)

                        ed_total_traveltime.setText(convertSecondsToHMmSs(difference));


                }
            }
        });


    }

    public static String convertSecondsToHMmSs(long seconds) {
        seconds = seconds/1000;
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60)) % 24;
        return String.format("%d:%02d:%02d", h, m, s);

    }



    GpsStatus.NmeaListener  gpslistner =new GpsStatus.NmeaListener() {
        public void onNmeaReceived(long timestamp, String nmea) {



            if (nmea.contains("$GPRMC") || nmea.contains("$GPGGA")) {
                // globalnmeaString = nmea;
                SimpleDateFormat time_formatter = new SimpleDateFormat("HH:mm:ss");
                current_time_str = time_formatter.format(timestamp);
                nmeaProgress(nmea, current_time_str);
            }
        }
    };

    public Location getLocation() {
        try {
            locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
            locationManager.addNmeaListener(gpslistner);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!isGPSEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {

                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return null;
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                    Log.d("GPS Enabled", "GPS Enabled");

                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }



    @Override
    public void onLocationChanged(Location location) {
      /*  if (globalnmeaString != null && !globalnmeaString.equals("")) {

            nmeaProgress(globalnmeaString, current_time_str);

        }*/
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }







    private void nmeaProgress(String rawNmea, String time ){
        double  speed ;
        double currlat,currlong;
        try {
            if(rawNmea!= null) {

                String[] rawNmeaSplit = rawNmea.split(",");
                if (rawNmeaSplit[0].contains("$GPGGA")) {
                    String s = nmeaParse.parse(rawNmea).toString();
                    final String[] NmeaSplit = s.split(",");
                    Log.d("Nmea String GGGPA", NmeaSplit[5] + "," + NmeaSplit[6]);

                    noof_satelite.setText( NmeaSplit[5]);
                }

                if(rawNmeaSplit[0].equalsIgnoreCase("$GPRMC")){

                    //  Log.d("Nmea STTTString GPRMC",  rawNmea.toString());

                    String s = nmeaParse.parse(rawNmea).toString();
                    final String[] NmeaSplit = s.split(",");
                    Log.d("Nmea String GPRMC", NmeaSplit[0] + "," + NmeaSplit[1]+ "," + NmeaSplit[2]+  "," + NmeaSplit[4]+ "," + NmeaSplit[6]);
                    currlat = Double.parseDouble(NmeaSplit[0]);
                    currlong = Double.parseDouble(NmeaSplit[1]);
                    speed = Double.parseDouble(NmeaSplit[6]);
                    if(speed == 0){
                        idleCount = idleCount +1;
                        if( idleCount<60) {
                            String tt= String.format("%02d : %02d : %02d",0,0, idleCount);
                            ed_idletime.setText(tt);
                        }else{


                            if(sec >59){
                                sec = 0;

                            }else{
                                sec = sec +1;
                            }

                            String tt= String.format("%02d : %02d : %02d",idleCount/3600,idleCount/60, sec);
                            ed_idletime.setText( tt);
                        }

                    }

                    if(prelat >0.0 && speed >0.0 )
                    {
                        predistence= predistence + distenc2(prelat,prelong, currlat,currlong);
                     //   predistence2= predistence2+ GetDistanceFromLatLonInKm(prelat,prelong, currlat,currlong);
                     //   predistence3= predistence3+distanceBetweenTwoPoint(prelat,prelong, currlat,currlong);

                    }

                    if( currlat >0.0 && speed>0){
                        prelat = currlat;
                        prelong = currlong;
                    }

               /* SpannableStringBuilder builder = new SpannableStringBuilder();



                String white = NmeaSplit[0];
                SpannableString whiteSpannable= new SpannableString(white);
                whiteSpannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, white.length(), 0);
                builder.append(whiteSpannable);

                String blue = NmeaSplit[7];
                SpannableString blueSpannable = new SpannableString(blue);
                final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
                blueSpannable.setSpan(new ForegroundColorSpan(Color.RED), 0, blue.length(), 0);
                blueSpannable .setSpan(bss, 0, 0, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


                builder.append(blueSpannable);

                ed_lat.setText(builder, TextView.BufferType.SPANNABLE);*/

                    ed_lat.setText(  Html.fromHtml(NmeaSplit[0]+"<font color='#FF0000'><b>"+NmeaSplit[7]+"</b></font>"));
                    ed_long.setText( Html.fromHtml(NmeaSplit[01]+"<font color='#FF0000'><b>"+NmeaSplit[8]+"</b></font>") );
                    speed = speed * 1.852;
                    ed_speed.setText(String.format("%.2f  km/h",speed) );
                    ed_stoptime.setText(time);

                    double spredistence =round(predistence,3);
                  //  double spredistence2 =round(predistence2,3);
                 //   double spredistence3 =round(predistence3,3);
                    ed_total_distence.setText(spredistence +"   km" );
                   // ed_total_distence2.setText(spredistence2 +"   km::2" );
                   // ed_total_distence3.setText(spredistence3 +"   km::3" );

                }
            }
        }catch (Exception e){
            Toast.makeText(getActivity(),"GPS ERROR",Toast.LENGTH_LONG).show();
        }


    }
    public double GetDistanceFromLatLonInKm(double lat1, double lon1, double lat2, double lon2)
    {
        final int R = 6371;
        // Radius of the earth in km
        double dLat = deg2rad(lat2 - lat1);
        // deg2rad below
        double dLon = deg2rad(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        // Distance in km
        return d;
    }
    private double deg2rad(double deg)
    {
        return deg * (Math.PI / 180);
    }

    double distanceBetweenTwoPoint(double srcLat, double srcLng, double desLat, double desLng) {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(desLat - srcLat);
        double dLng = Math.toRadians(desLng - srcLng);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(srcLat))
                * Math.cos(Math.toRadians(desLat)) * Math.sin(dLng / 2)
                * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;

        double meterConversion = 1609;

        return (int) (dist * meterConversion);
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




    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(getActivity(),p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    // no permissions granted.
                    Toast.makeText(getActivity(), "permission not Granted", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }




}
