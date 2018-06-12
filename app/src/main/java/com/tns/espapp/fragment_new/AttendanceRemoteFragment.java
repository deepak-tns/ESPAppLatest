package com.tns.espapp.fragment_new;


import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tns.espapp.AppConstraint;
import com.tns.espapp.DrawBitmapAll;
import com.tns.espapp.R;
import com.tns.espapp.Utility.AppSingleton;
import com.tns.espapp.Utility.SharedPreferenceUtils;
import com.tns.espapp.database.AttendanceSettingData;
import com.tns.espapp.database.AttendenceFixData;
import com.tns.espapp.database.AttendenceRemoteData;
import com.tns.espapp.database.DatabaseHandler;
import com.tns.espapp.database.NearLocationData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceRemoteFragment extends Fragment implements LocationListener, View.OnClickListener {

    DatabaseHandler db;
    private ImageView edt_attandence_intime,edt_attandence_outtime;
    private TextView tv_intime_get,tv_outtime_get, txt_currdate;
    private TextView tv_attendance_remote_history;
    private Button btn_in_preview,  btn_out_preview;
    private Button btn_save;
    private String intimeIvEncodeString="",outtime_IvEncodeString="";
    private Geocoder geocoder;
    String empid;
    String current_date;
    private String lat;
    private String longi;
    private double latitude;
    private double longitude;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ArrayList<NearLocationData> nearLocationDataArrayList = new ArrayList<>();;
    private LocationManager locationManager;
    private EditText edt_remote_intimeremark,edt_remote_outtimeremark;

    List<AttendenceRemoteData> data;
    public AttendanceRemoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_attendance_remote, container, false);
        geocoder = new Geocoder(getActivity(), Locale.ENGLISH);

        sharedPreferences = getActivity().getSharedPreferences("savestatus", 0); // 0 - for private mode
        SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance();
        sharedPreferenceUtils.setContext(getActivity());
        empid = sharedPreferenceUtils.getString(AppConstraint.EMPID);
         db= new DatabaseHandler(getActivity());
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar cal = Calendar.getInstance();
        System.out.println(dateFormat.format(cal.getTime()));

        current_date = dateFormat.format(cal.getTime());
        findIds(v);
        txt_currdate.setText(current_date);
        getLocation();


   /*   if(getStatus()){
            edt_attandence_intime.setVisibility(View.INVISIBLE);

            if(!getIntime().equals(null)){
                tv_intime_get.setText(getIntime());
                tv_intime_get.setVisibility(View.VISIBLE);
                btn_in_preview.setVisibility(View.VISIBLE);
                edt_attandence_outtime.setVisibility(View.VISIBLE);
            }else{
                tv_intime_get.setVisibility(View.INVISIBLE);
            }
        }
  */


       data = db.getLastRowAttendanceRemote();
        if(data.size() >0){
            if(current_date.equalsIgnoreCase(data.get(0).getCurrentDate())){
                edt_attandence_intime.setVisibility(View.INVISIBLE);
                tv_intime_get.setText(data.get(0).getInTime());
                tv_intime_get.setVisibility(View.VISIBLE);
                btn_in_preview.setVisibility(View.VISIBLE);
                btn_out_preview.setVisibility(View.VISIBLE);

                tv_outtime_get.setText(data.get(0).getOutTime());
                tv_outtime_get.setVisibility(View.VISIBLE);
                edt_attandence_outtime.setVisibility(View.VISIBLE);

              //  btn_save.setVisibility(View.GONE);
               // alertdiaolog();
            }else{
                setStatus(false);
                statusClear();
            }
        }

      /*  String out =tv_outtime_get.getText().toString();
        String in =tv_intime_get.getText().toString();

        Log.v("getouttimetext",out);
        Log.v("getintimetext",in);*/

        return v;
    }
    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        locationManager.removeUpdates(this);
    }

    private void findIds(View v) {
        txt_currdate = (TextView) v.findViewById(R.id.txt_currdate);
        edt_attandence_intime = (ImageView) v.findViewById(R.id.edt_attandence_intime);
        edt_attandence_outtime = (ImageView) v.findViewById(R.id.edt_attandence_outtime);
        tv_intime_get = (TextView) v.findViewById(R.id.tv_intime_get);
        tv_outtime_get = (TextView) v.findViewById(R.id.tv_outtime_get);
        btn_in_preview = (Button) v.findViewById(R.id.btn_in_preview);
        btn_out_preview = (Button) v.findViewById(R.id.btn_out_preview);
        btn_save= (Button) v.findViewById(R.id.btn_save);
        tv_attendance_remote_history=  (TextView) v.findViewById(R.id.tv_attendance_remote_history);
        edt_remote_intimeremark =  (EditText) v.findViewById(R.id.edt_remote_intimeremark);
        edt_remote_outtimeremark =  (EditText) v.findViewById(R.id.edt_remote_outtimeremark);

        edt_attandence_intime.setOnClickListener(this);
        edt_attandence_outtime.setOnClickListener(this);
        btn_in_preview.setOnClickListener(this);
        btn_out_preview.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        tv_attendance_remote_history.setOnClickListener(this);


    }
    @Override
    public void onLocationChanged(Location location) {
        lat = String.valueOf(location.getLatitude());
        longi = String.valueOf(location.getLongitude());
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if(addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("Address:");
                // for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
                strReturnedAddress =  strReturnedAddress.append(returnedAddress.getAddressLine(0)).append("\n");
                //}
                if(!strReturnedAddress.equals(null)) {
                    //getAddress.setText(strReturnedAddress);
                }
                Log.v("getLocation",strReturnedAddress.toString());
                // Toast.makeText(getActivity(), strReturnedAddress.toString() , Toast.LENGTH_LONG).show();
                // tv_first.setText("Your Current Address is : "+strReturnedAddress.toString());

            }
            else{
                //  tv_first.setText("No Address returned!");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            // tv_first.setText("Canont get Address!");
        }

        FindNearest();

        //  Toast.makeText(getActivity(), lats + "," + longi, Toast.LENGTH_LONG).show();
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

    private void getLocation() {

         locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        // Creating an empty criteria object
        Criteria criteria = new Criteria();
        // Getting the name of the provider that meets the criteria
        String provider = locationManager.getBestProvider(criteria, false);
        if (provider != null && !provider.equals("")) {
            // Get the location from the given provider
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                // ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                // public void onRequestPermissionsResult(int requestCode, String[] permissions,
                // int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider, 5000, 0, this);
            if(location!=null)
                onLocationChanged(location);
            else
                Toast.makeText(getActivity(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(getActivity(), "No Provider Found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {

        if(view.getId() ==R.id.edt_attandence_intime){

                //Toast.makeText(getActivity(),"All Ready Image Capture",Toast.LENGTH_LONG).show();
            if (edt_remote_intimeremark.getText().toString().equals("")) {
                Toast.makeText(getActivity(), "Please Enter Remark", Toast.LENGTH_LONG).show();
                return;
            }else {
                selectImage("intime");
            }
        }
        if(view.getId() ==R.id.edt_attandence_outtime){

        /*    if(getStatus())
            {
                selectImage("outtime");
            }else{
                Toast.makeText(getActivity(),"Please Intime Image Capture",Toast.LENGTH_LONG).show();
            }*/

           // statusClear();
            selectImage("outtime");
            btn_out_preview.setVisibility(View.VISIBLE);
        }
        if(view.getId() ==R.id.btn_in_preview){
            imagePreviewDialogIn();
        }
        if(view.getId() ==R.id.btn_out_preview){
            imagePreviewDialogOut();
        }
        if(view.getId() ==R.id.btn_save){

            if(tv_intime_get.getText().toString().equals("Intime")){
              Toast.makeText(getActivity(),"Please Capture Intime Image",Toast.LENGTH_LONG).show();
              return;
            }
            if(tv_outtime_get.getText().toString().equals("Outtime")){
                Toast.makeText(getActivity(),"Please Capture Outtime Image",Toast.LENGTH_LONG).show();
                return;
            }
            if(edt_remote_intimeremark.getText().toString().equals("")){
                Toast.makeText(getActivity(),"Please Enter Remark",Toast.LENGTH_LONG).show();
                return;
            }

            else {

                sendAllDataServer();
            }


        }
        if(view.getId() ==R.id. tv_attendance_remote_history){
             Intent intent = new Intent(getActivity(),AttendanceRemoteFixHistoryActivity.class);
             intent.putExtra("value","Remote");
             startActivity(intent);
        }

    }

    public void imagePreviewDialogIn(){
        // custom dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_photo_previewdialog);
        dialog.setCancelable(true);
        //  dialog.setTitle("Title...");
        dialog.getWindow().setLayout( LinearLayout.LayoutParams.WRAP_CONTENT, 800);
        // set the custom dialog components - text, image and button
        ImageView imageView =(ImageView)dialog.findViewById(R.id.iv_photo_preview);
        final TextView tv_image_not_found =(TextView)dialog.findViewById(R.id.tv_image_not_found);

        if(!(data.get(0).getIntimeImage()).equals("")){
            Bitmap bt = decodeBase64(data.get(0).getIntimeImage());
            imageView.setImageBitmap(bt);
            tv_image_not_found.setVisibility(View.GONE);

        }else{

            tv_image_not_found.setVisibility(View.VISIBLE);
            // tv_image_not_found  .setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(),"Please Capture Image First",Toast.LENGTH_LONG).show();
        }
        dialog.show();
        // Window window = dialog.getWindow();
        //  window.setLayout(300, 300);
    }
    public void imagePreviewDialogOut(){
        // custom dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_photo_previewdialog);
        dialog.setCancelable(true);
        //  dialog.setTitle("Title...");
        dialog.getWindow().setLayout( LinearLayout.LayoutParams.WRAP_CONTENT, 800);
        // set the custom dialog components - text, image and button
        ImageView imageView =(ImageView)dialog.findViewById(R.id.iv_photo_preview);
        final TextView tv_image_not_found =(TextView)dialog.findViewById(R.id.tv_image_not_found);

        if(!(data.get(0).getOuttimeImage()).equals("")){
            Bitmap bt = decodeBase64(data.get(0).getOuttimeImage());
            imageView.setImageBitmap(bt);
            tv_image_not_found.setVisibility(View.GONE);

        }else{

            tv_image_not_found.setVisibility(View.VISIBLE);
            // tv_image_not_found  .setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(),"Please Capture Image First",Toast.LENGTH_LONG).show();
        }

        dialog.show();
        // Window window = dialog.getWindow();
        //  window.setLayout(300, 300);
    }


    private void onCaptureImageResult(Intent data, String name) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        if (name.equals("intime"))
        {

                SimpleDateFormat time_formatter = new SimpleDateFormat("HH:mm:ss");
                String current_time_str = time_formatter.format(System.currentTimeMillis());
                if (lat == null) {
                    Toast.makeText(getActivity(), "please wait gps location not found", Toast.LENGTH_LONG).show();

                } else {
                    latitude = Double.parseDouble(lat);
                    longitude = Double.parseDouble(longi);
                    String totalString = current_date + current_time_str + "\nLat :" + String.format("%.4f", latitude) + ",  Long :" + String.format("%.4f", longitude) + "\nInTime Image ";
                    // Bitmap setTextwithImage =    ProcessingBitmap(thumbnail,totalString);
                    Bitmap setTextwithImage = DrawBitmapAll.drawTextToBitmap(getContext(), thumbnail, totalString);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    setTextwithImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                    String destinationpath = Environment.getExternalStorageDirectory().toString();
                    File destination = new File(destinationpath + "/ESPAttendance/");

                    if (!destination.exists()) {
                        destination.mkdirs();
                    }
                    File file = null;
                    FileOutputStream fo;
                    try {
                        // destination.createNewFile();
                        file = new File(destination, current_date + "_" + current_time_str + ".jpg");
                        fo = new FileOutputStream(file);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println(file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\") + 1));
                    String path = (file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\") + 1));
                    // startkmImageEncodeString = encodeToBase64(thumbnail, Bitmap.CompressFormat.JPEG, 50);
                    intimeIvEncodeString = encodeToBase64(setTextwithImage, Bitmap.CompressFormat.JPEG, 80);

                    tv_intime_get.setText(current_time_str);
                    setStatus(true);
                    setIntime(current_time_str, lat, longi);
                    setIntimePic(intimeIvEncodeString);
                    tv_intime_get.setVisibility(View.VISIBLE);

                    edt_attandence_outtime.setClickable(true);
                    edt_attandence_outtime.setFocusable(true);
                    edt_attandence_outtime.setVisibility(View.VISIBLE);
                    btn_in_preview.setVisibility(View.VISIBLE);
                    edt_attandence_intime.setVisibility(View.INVISIBLE);
                    db.insertAttandanceRemote(new AttendenceRemoteData(empid, current_date, tv_intime_get.getText().toString(), tv_outtime_get.getText().toString(), intimeIvEncodeString, outtime_IvEncodeString, getIntimeLat(), getIntimeLog(), lat, longi, edt_remote_intimeremark.getText().toString(), "", nearLocationDataArrayList.get(0).getName(), nearLocationDataArrayList.get(0).getName(), 0));
                    sendAllDataServerIntime();
                }

        }
        if (name.equals("outtime")) {
            SimpleDateFormat time_formatter = new SimpleDateFormat("HH:mm:ss");
            String current_time_str = time_formatter.format(System.currentTimeMillis());
            if (lat == null) {
                Toast.makeText(getActivity(), "please wait gps location not found", Toast.LENGTH_LONG).show();

            } else {
                latitude = Double.parseDouble(lat);
                longitude = Double.parseDouble(longi);

                String totalString = current_date + current_time_str + "\nLat:" + String.format("%.4f", latitude) + ",Long:" + String.format("%.4f", longitude) + "\nOutTime Image ";
                //  Bitmap setTextwithImage =    ProcessingBitmap(thumbnail,totalString);
                Bitmap setTextwithImage = DrawBitmapAll.drawTextToBitmap(getContext(), thumbnail, totalString);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                setTextwithImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String destinationpath = Environment.getExternalStorageDirectory().toString();

                File destination = new File(destinationpath + "/ESPAttendance/");
                if (!destination.exists()) {
                    destination.mkdirs();
                }
                FileOutputStream fo;
                try {
                    //  file.createNewFile();
                    File file = new File(destination,  current_date + "_" + current_time_str + ".jpg");
                    fo = new FileOutputStream(file);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(destination.getAbsolutePath().substring(destination.getAbsolutePath().lastIndexOf("\\") + 1));
                String path = (destination.getAbsolutePath().substring(destination.getAbsolutePath().lastIndexOf("\\") + 1));
                // endkmImageEncodeString= encodeToBase64(thumbnail, Bitmap.CompressFormat.JPEG,50);
                outtime_IvEncodeString = encodeToBase64(setTextwithImage, Bitmap.CompressFormat.JPEG, 80);
                tv_outtime_get.setText(current_time_str);
                tv_outtime_get.setVisibility(View.VISIBLE);

                db.updateAttandenceRemoteStatus(db.getLastInsertIdAttendanceRemote(),tv_outtime_get.getText().toString(),outtime_IvEncodeString,lat,longi,edt_remote_intimeremark.getText().toString(),nearLocationDataArrayList.get(0).getName(),  0);


            }
            // setImg.setImageBitmap(thumbnail);
        }
    }

    private void selectImage(String Value) {

        if (Value.equals("intime")) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
            intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
            intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
            startActivityForResult(intent, 3);
        }
        if (Value.equals("outtime")) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
            intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
            intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
            startActivityForResult(intent, 4);
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            if (requestCode == 3) {
                onCaptureImageResult(data, "intime");
            }

            if (requestCode == 4) {
                onCaptureImageResult(data, "outtime");
            }
        }
    }


    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }


    private void sendAllDataServer() {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, AppConstraint.ATTENDANCEREMOTE, JsonParameterSendServer(),
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        Log.e("!_@@_SUCESS", response + "");
                        btn_save.setEnabled(false);
                        try {
                            JSONObject jsonobj= new JSONObject(response.toString());
                            String status = jsonobj.getString("status");
                            if(status.equalsIgnoreCase("true")){
                                db.updateAttandenceRemoteStatus(db.getLastInsertIdAttendanceRemote(),tv_outtime_get.getText().toString(),outtime_IvEncodeString,lat,longi,edt_remote_intimeremark.getText().toString(),nearLocationDataArrayList.get(0).getName(),  1);

                                Toast.makeText(getActivity(), "Uploaded Successfully...", Toast.LENGTH_LONG).show();
                            }else{
                                db.updateAttandenceRemoteStatus(db.getLastInsertIdAttendanceRemote(),tv_outtime_get.getText().toString(),outtime_IvEncodeString,lat,longi,edt_remote_intimeremark.getText().toString(),nearLocationDataArrayList.get(0).getName(),  0);

                                Toast.makeText(getActivity(), "Uploaded failed...", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            db.updateAttandenceRemoteStatus(db.getLastInsertIdAttendanceRemote(),tv_outtime_get.getText().toString(),outtime_IvEncodeString,lat,longi,edt_remote_intimeremark.getText().toString(),nearLocationDataArrayList.get(0).getName(),  0);

                            Toast.makeText(getActivity(), "Json Parse Exception...", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                        //  db.insertAttandanceRemote(new AttendenceRemoteData(empid,current_date,tv_intime_get.getText().toString(),tv_outtime_get.getText().toString(),intimeIvEncodeString,outtime_IvEncodeString,getIntimeLat(),getIntimeLog(),lat,longi,edt_remote_intimeremark.getText().toString(),"",1));

                        edt_remote_intimeremark.getText().clear();
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("!_@@_Errors--", error + "");
                        pDialog.dismiss();
                        Toast.makeText(getActivity(), "Uploaded Failed Exception...", Toast.LENGTH_LONG).show();
                     //   db.insertAttandanceRemote(new AttendenceRemoteData(empid,current_date,tv_intime_get.getText().toString(),tv_outtime_get.getText().toString(),intimeIvEncodeString,outtime_IvEncodeString,getIntimeLat(),getIntimeLog(),lat,longi,edt_remote_intimeremark.getText().toString(),"",0));
                        db.updateAttandenceRemoteStatus(db.getLastInsertIdAttendanceRemote(),tv_outtime_get.getText().toString(),outtime_IvEncodeString,lat,longi,edt_remote_intimeremark.getText().toString(),nearLocationDataArrayList.get(0).getName(),  0);

                        edt_remote_intimeremark.getText().clear();
                    }
                }) {

        /*
          @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
               // params.put("Authorization", "key=" + Legacy_SERVER_KEY);
                params.put("Content-Type", "application/json");
                return params;
            }  */
        };
        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest, null);

    }

    private JSONObject JsonParameterSendServer() {
        List<AttendenceRemoteData>   dataLast = db.getLastRowAttendanceRemote();
        NearLocationData nd=  nearLocationDataArrayList.get(0);
        String nLat = String.valueOf( nd.getLat());
        String nLog = String.valueOf( nd.getLog());
        String nlocation = String.valueOf( nd.getName());

        JSONObject jsonObject = new JSONObject();

   /*     SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MMM-yyyy");

        try {

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy");

            Date dtt = df.parse(current_date);
            Date ds = new Date(dtt.getTime());
            getDate= dateFormat2.format(ds);
            System.out.println(getDate);
            Log.v("datess",getDate);

        } catch (ParseException e) {
            Log.v("datess",e.getMessage());
            e.printStackTrace();
        }
        */
        try {

            jsonObject.put("DatabaseName", "TNS_HR");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");
            //   jsonObject.put("spName", "AndroidattendanceInsert");
            jsonObject.put("EmpId", empid);
            jsonObject.put("InTime", tv_intime_get.getText().toString());
            jsonObject.put("InTimePic", getIntimePic());
            jsonObject.put("OutTime", tv_outtime_get.getText().toString());
            jsonObject.put("OutTimePic", dataLast.get(0).getOuttimeImage());
            jsonObject.put("InLat", getIntimeLat());
            jsonObject.put("InLog", getIntimeLog());
            jsonObject.put("InRemark",edt_remote_intimeremark.getText().toString());
            jsonObject.put("OutLat", dataLast.get(0).getOutlat());
            jsonObject.put("OutLog", dataLast.get(0).getOutlongi());
            jsonObject.put("OutRemark", edt_remote_intimeremark.getText().toString());
            jsonObject.put("LocationType", "Anywhere");
            jsonObject.put("RadialDistance", "0");
            jsonObject.put("Date", current_date);
            // jsonObject.put("NearestLat", nLat);
            // jsonObject.put("NearestLog", nLog);
            jsonObject.put("NearestLocationInTime", dataLast.get(0).getIntimetimeNear());
            jsonObject.put("NearestLocationOuttime", nlocation);

            Log.i("Json Param",jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void sendAllDataServerIntime() {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, AppConstraint.ATTENDANCEREMOTE, JsonParameterSendServerInTime(),
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        Log.e("!_@@_SUCESS", response + "");

                        try {
                            JSONObject jsonobj= new JSONObject(response.toString());
                            String status = jsonobj.getString("status");
                            if(status.equalsIgnoreCase("true")){
                                Toast.makeText(getActivity(), "Uploaded Successfully...", Toast.LENGTH_LONG).show();
                                edt_remote_intimeremark.getText().clear();
                            }else{
                                Toast.makeText(getActivity(), "Uploaded failed...", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), "Json Parse Exception...", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("!_@@_Errors--", error + "");
                        pDialog.dismiss();
                        Toast.makeText(getActivity(), "Uploaded Failed Exception...", Toast.LENGTH_LONG).show();

                    }
                }) {

        /*
          @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
               // params.put("Authorization", "key=" + Legacy_SERVER_KEY);
                params.put("Content-Type", "application/json");
                return params;
            }  */
        };
        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest, null);

    }

    private JSONObject JsonParameterSendServerInTime() {
        String nlocation ="";
        if (nearLocationDataArrayList.size() > 0) {
            NearLocationData nd = nearLocationDataArrayList.get(0);
            String nLat = String.valueOf(nd.getLat());
            String nLog = String.valueOf(nd.getLog());
            nlocation = String.valueOf(nd.getName());
        }else{
            nlocation ="Unknown";
        }
        JSONObject jsonObject = new JSONObject();

   /*     SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy");
            Date dtt = df.parse(current_date);
            Date ds = new Date(dtt.getTime());
            getDate= dateFormat2.format(ds);
            System.out.println(getDate);
            Log.v("datess",getDate);

        } catch (ParseException e) {
            Log.v("datess",e.getMessage());
            e.printStackTrace();
        }
        */
        try {
            jsonObject.put("DatabaseName", "TNS_HR");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");
         //   jsonObject.put("spName", "AndroidattendanceInsert");
            jsonObject.put("EmpId", empid);
            jsonObject.put("InTime", tv_intime_get.getText().toString());
            jsonObject.put("InTimePic", getIntimePic());
            jsonObject.put("OutTime", "");
            jsonObject.put("OutTimePic", "");
            jsonObject.put("InLat", getIntimeLat());
            jsonObject.put("InLog", getIntimeLog());
            jsonObject.put("InRemark", edt_remote_intimeremark.getText().toString());
            jsonObject.put("OutLat", "");
            jsonObject.put("OutLog", "");
            jsonObject.put("OutRemark", "");
            jsonObject.put("LocationType", "Anywhere");
            jsonObject.put("RadialDistance", "0");
            jsonObject.put("Date", current_date);
           // jsonObject.put("NearestLat", nLat);
           // jsonObject.put("NearestLog", nLog);
            jsonObject.put("NearestLocationInTime", nlocation);
            jsonObject.put("NearestLocationOutTime", "");
          //  jsonObject.put("ParameterList", jsonArrayParameter);

            Log.i("Json Param",jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void setStatus(boolean b){
        editor = sharedPreferences.edit();
        editor.putBoolean("boolean", b); // Storing boolean - true/false
        editor.commit(); // commit changes
    }

    private void setIntime(String b,String lat, String log){
        editor = sharedPreferences.edit();
        editor.putString("setintime", b);
        editor.putString("setintimelat", lat);
        editor.putString("setintimelog", log);
        editor.commit(); // commit changes
    }

    private void setIntimePic(String b){

        editor = sharedPreferences.edit();
        editor.putString("setintimepic", b); // Storing boolean - true/false
        editor.commit(); // commit changes
    }
    private String getIntime(){
        return sharedPreferences.getString("setintime","");
    }

    private String getIntimeLat(){
        return sharedPreferences.getString("setintimelat","");
    }
    private String getIntimeLog(){
        return sharedPreferences.getString("setintimelog","");
    }

    private String getIntimePic(){
        return sharedPreferences.getString("setintimepic","");
    }

    private void statusClear(){
        editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    private boolean getStatus(){
       return sharedPreferences.getBoolean("boolean",false);
    }
    //.............................................Find Near Search Location..............................
    private void FindNearest(){

        StringBuilder googlePlacesUrl =  new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + 500);
        googlePlacesUrl.append("&types=" + "school");
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + getResources().getString(R.string.google_maps_key));

       GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask();
        Object[] toPass = new Object[1];
        //   toPass[0] = mMap;
        toPass[0] = googlePlacesUrl.toString();
        googlePlacesReadTask.execute(toPass);
    }

    class GooglePlacesReadTask extends AsyncTask<Object, Integer, String> {
        String googlePlacesData = null;
        @Override
        protected String doInBackground(Object... inputObj) {
            try {

                String googlePlacesUrl = (String) inputObj[0];
               Http http = new Http();
                googlePlacesData = http.read(googlePlacesUrl);
            } catch (Exception e)
            {
                Log.d("Google Place Read Task", e.toString());
            }
            return googlePlacesData;
        }

        @Override
        protected void onPostExecute(String result) {
           PlacesDisplayTask placesDisplayTask = new PlacesDisplayTask();
            Object[] toPass = new Object[1];
            toPass[0] = result;
         //   Log.v("GetAddress1",result);
            placesDisplayTask.execute(toPass);
        }
    }

    class PlacesDisplayTask extends AsyncTask<Object, Integer, List<HashMap<String, String>>> {

        JSONObject googlePlacesJson;
        //GoogleMap googleMap;

        @Override
        protected List<HashMap<String, String>> doInBackground(Object... inputObj) {

            List<HashMap<String, String>> googlePlacesList = null;
           PlacesDisplayTask.Places placeJsonParser = new PlacesDisplayTask.Places();

            try {
                // googleMap = (GoogleMap) inputObj[0];
                googlePlacesJson = new JSONObject((String) inputObj[0]);
                googlePlacesList = placeJsonParser.parse(googlePlacesJson);
            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return googlePlacesList;
        }


        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {
            nearLocationDataArrayList .clear();
            if(list != null) {
                for (int i = 0; i < list.size(); i++) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    HashMap<String, String> googlePlace = list.get(i);
                    double lat = Double.parseDouble(googlePlace.get("lat"));
                    double lng = Double.parseDouble(googlePlace.get("lng"));
                    String placeName = googlePlace.get("place_name");
                    String vicinity = googlePlace.get("vicinity");
                    LatLng latLng = new LatLng(lat, lng);
                    markerOptions.position(latLng);
                    markerOptions.title(placeName + " : " + vicinity);

                    nearLocationDataArrayList.add(new NearLocationData(lat,lng,placeName,vicinity));
                    Log.v("GetAddress",placeName + " : " + vicinity);
                    //   googleMap.addMarker(markerOptions);
                }
                Collections.sort(nearLocationDataArrayList);
                Log.v("Arraylist", nearLocationDataArrayList.toString());
            }
        }

        public class Places {
            public List<HashMap<String, String>> parse(JSONObject jsonObject) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = jsonObject.getJSONArray("results");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return getPlaces(jsonArray);
            }

            private List<HashMap<String, String>> getPlaces(JSONArray jsonArray) {
                int placesCount = jsonArray.length();
                List<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
                HashMap<String, String> placeMap = null;

                for (int i = 0; i < placesCount; i++) {
                    try {
                        placeMap = getPlace((JSONObject) jsonArray.get(i));
                        placesList.add(placeMap);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return placesList;
            }

            private HashMap<String, String> getPlace(JSONObject googlePlaceJson) {
                HashMap<String, String> googlePlaceMap = new HashMap<String, String>();
                String placeName = "-NA-";
                String vicinity = "-NA-";
                String latitude = "";
                String longitude = "";
                String reference = "";
                try {
                    if (!googlePlaceJson.isNull("name")) {
                        placeName = googlePlaceJson.getString("name");
                    }
                    if (!googlePlaceJson.isNull("vicinity")) {
                        vicinity = googlePlaceJson.getString("vicinity");
                    }
                    latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
                    longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
                    reference = googlePlaceJson.getString("reference");
                    googlePlaceMap.put("place_name", placeName);
                    googlePlaceMap.put("vicinity", vicinity);
                    googlePlaceMap.put("lat", latitude);
                    googlePlaceMap.put("lng", longitude);
                    googlePlaceMap.put("reference", reference);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return googlePlaceMap;
            }
        }
    }

    class Http {
        public String read(String httpUrl) throws IOException {
            String httpData = "";
            InputStream inputStream = null;
            HttpURLConnection httpURLConnection = null;
            try {
                URL url = new URL(httpUrl);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                httpData = stringBuffer.toString();
                bufferedReader.close();
            } catch (Exception e) {
                Log.d("Exception-read Http url", e.toString());
            } finally {
                inputStream.close();
                httpURLConnection.disconnect();
            }
            return httpData;
        }
    }

    public  void alertdiaolog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set title
        alertDialogBuilder.setTitle("Exit Page ?");

        // set dialog message
        alertDialogBuilder
                .setMessage("Attendance has been allready taken")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                // if this button is clicked, close
                                // current activity
                                // startActivity(new Intent(CardLogin.this, MainActivity.class));

                                getActivity().finish();

                            }
                        })
                /*.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        })*/;

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }
}
