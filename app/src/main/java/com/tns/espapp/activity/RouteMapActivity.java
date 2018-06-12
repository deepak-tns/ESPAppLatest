package com.tns.espapp.activity;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.tns.espapp.AppConstraint;
import com.tns.espapp.R;
import com.tns.espapp.database.DatabaseHandler;
import com.tns.espapp.database.LatLongData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;



public class RouteMapActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    GoogleMap googleMap;
    List<LatLongData> points = new ArrayList<>();
    DatabaseHandler databaseHandler;
    private ArrayList<LatLng> latLngArrayList = new ArrayList<>();
    private TextView tv_first;
    private  LinearLayout show_linear_speed;
    private ImageView iv_speed_show,iv_speed_hide;

    private boolean flag_speedHint= true;

    String fromNumber;

    Marker marker;
    float v;
    double lat, lng;
    Handler handler;
    LatLng startPosition, endPosition;
    int index, next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_map);

        tv_first =(TextView)findViewById(R.id.tv_first);
        show_linear_speed=(LinearLayout)findViewById(R.id.show_linear_speed);
        iv_speed_hide=(ImageView)findViewById(R.id.iv_speed_hide) ;
        iv_speed_show=(ImageView)findViewById(R.id.iv_speed_show) ;

        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        Intent intent = getIntent();
        fromNumber = intent.getStringExtra(AppConstraint.SELECTEDFORMNUMBER);
        databaseHandler = new DatabaseHandler(this);
        points = databaseHandler.getLatLong(fromNumber);
        if(points.size() <1){
            showAlert();
            return;
        }
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fm.getMapAsync(this);


    iv_speed_show.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (flag_speedHint) {
                iv_speed_show.setRotation(90);
                show_linear_speed.setVisibility(View.VISIBLE);
               // iv_speed_hide.setVisibility(View.VISIBLE);
               // iv_speed_show.setVisibility(View.GONE);
                flag_speedHint = false;
            } else
                {
                iv_speed_show.setRotation(270);
                show_linear_speed.setVisibility(View.GONE);
                flag_speedHint = true;
            }
        }
    });

    iv_speed_hide.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            show_linear_speed.setVisibility(View.GONE);
            iv_speed_show.setVisibility(View.VISIBLE);
            iv_speed_hide.setVisibility(View.GONE);
        }
    });

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
     /*  mHandler = new Handler();
      final   boolean mStopHandler = false;

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // do your stuff - don't create a new runnable here!
                if (!mStopHandler) {
                    points = databaseHandler.getLatLong(fromNumber);
                    if(points.size() <1){
                        showAlert();
                        return;
                    }
                    showMarker();
                    mHandler.postDelayed(this, 1000);
                }
            }
        };

// start it with:
      mHandler.post(runnable);
*/
        showMarker();

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }


    private void showMarker() {

        final ArrayList<LatLng> points_google = new ArrayList<>();

      /*  for(LatLongData data: points){
             LatLng pointcopy = new LatLng(Double.parseDouble(data.getLat()), Double.parseDouble(data.getLongi()));
            points_google.add(pointcopy);
        }
*/


        for (int i = 0; i < points.size(); i++) {
            LatLongData latLongData = points.get(i);
            final LatLng point = new LatLng(Double.parseDouble(latLongData.getLat()), Double.parseDouble(latLongData.getLongi()));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 16));
            PolylineOptions polylineOptions = new PolylineOptions();
            points_google.add(point);
            // Setting the color of the polyline
            polylineOptions.color(Color.RED);
            // Setting the width of the polyline
            polylineOptions.width(3);
            // Adding the taped point to the ArrayList
          //  points.add(point);
            // Setting points of polyline
            polylineOptions.addAll(points_google);
            // Adding the polyline to the map
            googleMap.addPolyline(polylineOptions);

            if(i == 0){
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.start_point);

                MarkerOptions markerOptions1 = new MarkerOptions();
                markerOptions1.position(point);
                markerOptions1.title("Location");
                markerOptions1.icon(icon);
                markerOptions1.snippet("Latitude :" + Double.parseDouble(latLongData.getLat()) + " Longitude :" + Double.parseDouble(latLongData.getLongi()) + "\n" + " Date :" + latLongData.getDate() + " Time: " + latLongData.getCurrent_time_str() + " Speed: " + latLongData.getSpeed());
                googleMap.addMarker(markerOptions1);

            }

            if(i == points.size()-1){
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.stop_point);

                MarkerOptions markerOptions1 = new MarkerOptions();
                markerOptions1.position(point);
                markerOptions1.title("Location");
                markerOptions1.icon(icon);
                markerOptions1.snippet("Latitude :" + Double.parseDouble(latLongData.getLat()) + " Longitude :" + Double.parseDouble(latLongData.getLongi()) + "\n" + " Date :" + latLongData.getDate() + " Time: " + latLongData.getCurrent_time_str() + " Speed: " + latLongData.getSpeed());
                googleMap.addMarker(markerOptions1);
            }

            if(Double.parseDouble(latLongData.getSpeed())>=5 && Double.parseDouble(latLongData.getSpeed())<=20)
            {
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.greenballicon);
                MarkerOptions markerOptions1 = new MarkerOptions();
                markerOptions1.position(point);
                markerOptions1.title("Location");
                markerOptions1.icon(icon);
                markerOptions1.snippet("Latitude :" + Double.parseDouble(latLongData.getLat()) + " Longitude :" + Double.parseDouble(latLongData.getLongi()) + "\n" + " Date :" + latLongData.getDate() + " Time: " + latLongData.getCurrent_time_str() + " Speed: " + latLongData.getSpeed());
                googleMap.addMarker(markerOptions1);

            }
            else if(Double.parseDouble(latLongData.getSpeed())>=21 && Double.parseDouble(latLongData.getSpeed())<=40)
            {
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.yellowboll);

                MarkerOptions markerOptions2 = new MarkerOptions();
                markerOptions2.position(point);
                markerOptions2.title("Location");
                markerOptions2.icon(icon);
                markerOptions2.snippet("Latitude :" + Double.parseDouble(latLongData.getLat()) + " Longitude :" + Double.parseDouble(latLongData.getLongi()) + "\n" + " Date :" + latLongData.getDate() + " Time: " + latLongData.getCurrent_time_str() + " Speed: " + latLongData.getSpeed());
                googleMap.addMarker(markerOptions2);
            }
            else if(Double.parseDouble(latLongData.getSpeed())>=41 && Double.parseDouble(latLongData.getSpeed())<=60)
            {
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.redboll);

                MarkerOptions markerOptions3 = new MarkerOptions();
                markerOptions3.position(point);
                markerOptions3.title("Location");
                markerOptions3.snippet("Latitude :" + Double.parseDouble(latLongData.getLat()) + " Longitude :" + Double.parseDouble(latLongData.getLongi()) + "\n" + " Date :" + latLongData.getDate() + " Time: " + latLongData.getCurrent_time_str() + " Speed: " + latLongData.getSpeed());
                markerOptions3.icon(icon);
                googleMap.addMarker(markerOptions3);
            }
            else

            {
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.blueball);
                MarkerOptions markerOptions4 = new MarkerOptions();
                markerOptions4.position(point);
                markerOptions4.title("Location");
                markerOptions4.snippet("Latitude :" + Double.parseDouble(latLongData.getLat()) + " Longitude :" + Double.parseDouble(latLongData.getLongi()) + "\n" + " Date :" + latLongData.getDate() + " Time: " + latLongData.getCurrent_time_str() + " Speed: " + latLongData.getSpeed());
                markerOptions4.icon(icon);
                markerOptions4.visible(true);
                googleMap.addMarker(markerOptions4);
/*
                Marker marker = googleMap.addMarker(markerOptions4);
                markerOptions4.anchor(0f, 0.5f);
                marker.showInfoWindow();*/
            }

            // Adding the marker to the map
           // googleMap.addMarker(markerOptions);
           /* marker = googleMap.addMarker(new MarkerOptions().position(point)
                    .flat(true)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.noti)));
            handler = new Handler();
            index = -1;
            next = 1;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (index < points.size() - 1) {
                        index++;
                        next = index + 1;
                    }
                    if (index < points.size() - 1) {
                        startPosition = points_google.get(index);
                        endPosition = points_google.get(next);
                    }
                    ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
                    valueAnimator.setDuration(3000);
                    valueAnimator.setInterpolator(new LinearInterpolator());
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            v = valueAnimator.getAnimatedFraction();
                            lng = v * endPosition.longitude + (1 - v)
                                    * startPosition.longitude;
                            lat = v * endPosition.latitude + (1 - v)
                                    * startPosition.latitude;
                            LatLng newPos = new LatLng(lat, lng);
                            marker.setPosition(newPos);
                            marker.setAnchor(0.5f, 0.5f);
                            marker.setRotation(getBearing(startPosition, newPos));
                            googleMap.moveCamera(CameraUpdateFactory
                                    .newCameraPosition
                                            (new CameraPosition.Builder()
                                                    .target(newPos)
                                                    .zoom(15.5f)
                                                    .build()));
                        }
                    });
                    valueAnimator.start();
                    handler.postDelayed(this, 3000);
                }
            }, 3000);
*/

        }


        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }
            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(RouteMapActivity.this);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(RouteMapActivity.this);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(RouteMapActivity.this);
                title.setGravity(Gravity.CENTER);
                snippet.setTextColor(Color.BLACK);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);
                return info;
            }
        });



    /*    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker arg0) {

                Bitmap Icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                setAnimation(googleMap,points_google,Icon);
                return true;

            }

        });*/

    }
    private float getBearing(LatLng begin, LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }

    public static void setAnimation(GoogleMap myMap, final List<LatLng> directionPoint, final Bitmap bitmap) {

       // myMap.clear();
        Marker marker = myMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                .position(directionPoint.get(0))
                .flat(true));

      //  myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(directionPoint.get(0), 16));
        animateMarker(myMap, marker, directionPoint, false);
    }


    private static void animateMarker(GoogleMap myMap, final Marker marker, final List<LatLng> directionPoint,
        final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = myMap.getProjection();
        final long duration = 60000;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            int i = 0;

            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                if (i < directionPoint.size())
                    marker.setPosition(directionPoint.get(i));
                i++;


                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 150);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


    private void createData() {
        LatLongData latLong;
        latLong = new LatLongData();
        latLong.setFormno("17333/020817/002");
        latLong.setDate("02-08-17");
        latLong.setLat("28.637855");
        latLong.setLongi("77.400925");
        latLong.setCurrent_time_str("08:40:13");
        latLong.setSpeed("6.5888");
        points.add(latLong);


        latLong = new LatLongData();
        latLong.setFormno("17333/020817/002");
        latLong.setDate("02-08-17");
        latLong.setLat("28.637871");
        latLong.setLongi("77.400773");
        latLong.setCurrent_time_str("08:42:13");
        latLong.setSpeed("6.588");
        points.add(latLong);


        latLong = new LatLongData();
        latLong.setFormno("17333/020817/002");
        latLong.setDate("02-08-17");
        latLong.setLat("28.637874");
        latLong.setLongi("77.400654");
        latLong.setCurrent_time_str("08:42:21");
        latLong.setSpeed("10.080");
        points.add(latLong);


        latLong = new LatLongData();
        latLong.setFormno("17333/020817/002");
        latLong.setDate("02-08-17");
        latLong.setLat("28.637832");
        latLong.setLongi("77.401014");
        latLong.setCurrent_time_str("08:40:05");
        latLong.setSpeed("6.264");
        points.add(latLong);

        latLong = new LatLongData();
        latLong.setFormno("17333/020817/002");
        latLong.setDate("02-08-17");
        latLong.setLat("28.637836");
        latLong.setLongi("77.400513");
        latLong.setCurrent_time_str("08:42:29");
        latLong.setSpeed("5.040");
        points.add(latLong);


        latLong = new LatLongData();
        latLong.setFormno("17333/020817/002");
        latLong.setDate("02-08-17");
        latLong.setLat("28.637832");
        latLong.setLongi("77.401014");
        latLong.setCurrent_time_str("08:40:05");
        latLong.setSpeed("6.264");
        points.add(latLong);

        latLong = new LatLongData();
        latLong.setFormno("17333/020817/002");
        latLong.setDate("02-08-17");
        latLong.setLat("28.637785");
        latLong.setLongi("77.400397");
        latLong.setCurrent_time_str("08:42:42");
        latLong.setSpeed("4.608");
        points.add(latLong);


        latLong = new LatLongData();
        latLong.setFormno("17333/020817/002");
        latLong.setDate("02-08-17");
        latLong.setLat("28.629053");
        latLong.setLongi("77.379481");
        latLong.setCurrent_time_str("09:04:32");
        latLong.setSpeed("6.264");
        points.add(latLong);


    }


    private void showAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Record not found");
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // How to remove the selected item?
                // adapter.remove(adapter.getItem(p));
                dialog.dismiss();
                finish();
            }


        });
       /* builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // How to remove the selected item?
                // adapter.remove(adapter.getItem(p));

            }

        });*/

        AlertDialog alert = builder.create();
        alert.show();
    }


}
