package com.tns.espapp.fragment_new;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.tns.espapp.AppConstraint;
import com.tns.espapp.R;
import com.tns.espapp.Utility.AppSingleton;
import com.tns.espapp.activity.OPEntryActivity;
import com.tns.espapp.activity.OPEntryLocationHistoryActivity;
import com.tns.espapp.activity.OPHistoryActivity;
import com.tns.espapp.database.AttendanceSettingData;
import com.tns.espapp.database.AttendenceRemoteData;
import com.tns.espapp.database.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PresenceFragment extends Fragment implements View.OnClickListener, com.google.android.gms.location.LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private RelativeLayout re_opentery,re_attendencefix,re_attendanceremote,presence_ophistory,loctorfrag_opentrylocationhistory;

    boolean flag ;
    private String LocationType;
    private static final long INTERVAL = 1000 * 5 * 1; //1 minute
    private static final long FASTEST_INTERVAL = 1000 * 2 * 1; // 1 minute
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;

    public PresenceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_presence, container, false);
        findIDs(v);

       // getLocation();

        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        if(!flag)
        {

        }
       // sendAllDataServer();
        DatabaseHandler db = new DatabaseHandler(getActivity());
        List<AttendanceSettingData> dataList = db.getAttendance_settingData();
        if(dataList.size() >0){
            LocationType = dataList.get(0).getType();
            if(LocationType.equalsIgnoreCase("Fixed")) {
                re_attendanceremote.setFocusable(false);
                re_attendanceremote.setClickable(false);
                re_attendanceremote.setBackgroundColor(getResources().getColor(R.color.lightgray));
            }else{
                re_attendencefix.setFocusable(false);
                re_attendencefix.setClickable(false);
                re_attendencefix.setBackgroundColor(getResources().getColor(R.color.lightgray));
            }
        }




        return v;
    }

    private void findIDs(View v) {
        re_opentery =(RelativeLayout)v.findViewById(R.id.presence_opentry) ;
        re_opentery.setOnClickListener(this);
        re_attendencefix =(RelativeLayout)v.findViewById(R.id.presence_attendancefix) ;
        re_attendencefix.setOnClickListener(this);
        re_attendanceremote =(RelativeLayout)v.findViewById(R.id.presence_attendance_remote) ;
        re_attendanceremote.setOnClickListener(this);

        presence_ophistory=(RelativeLayout) v.findViewById(R.id.presence_ophistory);
        presence_ophistory.setOnClickListener(this);
        loctorfrag_opentrylocationhistory=(RelativeLayout) v.findViewById(R.id.loctorfrag_opentrylocationhistory);
        loctorfrag_opentrylocationhistory.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {

        if(view == re_opentery){
            startActivity(new Intent(getActivity(), OPEntryActivity.class));
        }
        if(view == re_attendencefix){

                startActivity(new Intent(getActivity(), AttendanceFixActivity.class));


        }
        if(view == re_attendanceremote){

            startActivity(new Intent(getActivity(), AttendanceRemoteActivity.class));

        }
        if(view == presence_ophistory){
            // getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag,new RouteMapFragment()).addToBackStack(null).commit();
            startActivity(new Intent(getActivity(), OPHistoryActivity.class));
        }
        if(view == loctorfrag_opentrylocationhistory){
            // getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag,new RouteMapFragment()).addToBackStack(null).commit();
            startActivity(new Intent(getActivity(), OPEntryLocationHistoryActivity.class));
        }

    }
   /* private void getLocation() {

      LocationListener  locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
             String   lats = String.valueOf(location.getLatitude());
             String   longi = String.valueOf(location.getLongitude());

                //   Toast.makeText(getActivity(),"GPS Enabled",300).show();
                // tv.append(s);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }
        };


      LocationManager  locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
      //  statusOfGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
      //  checkNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        //noinspection Missing Permission

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);


    }*/

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("PresenceFrgament", "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

         String.valueOf(location.getLatitude());
         String.valueOf(location.getLongitude());
        //   Toast.makeText(getActivity(),"Location "+location.getLatitude()+","+location.getLongitude(),Toast.LENGTH_LONG).show();
    }

    protected void startLocationUpdates() {
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d("PresenceFrgament", "Location update started ..............: ");
    }

}
