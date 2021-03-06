package com.tns.espapp.fragment_new;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tns.espapp.AppConstraint;
import com.tns.espapp.DrawBitmapAll;
import com.tns.espapp.HTTPPostRequestMethod;
import com.tns.espapp.ListviewHelper;
import com.tns.espapp.R;
import com.tns.espapp.Utility.SharedPreferenceUtils;
import com.tns.espapp.activity.CurrentLocationActivity;
import com.tns.espapp.database.ChecklistData;
import com.tns.espapp.database.DatabaseHandler;
import com.tns.espapp.database.NearLocationData;
import com.tns.espapp.database.OPEntryData;
import com.tns.espapp.database.OPEntryMilestone;
import com.tns.espapp.service.OPEntryGPSTracker;

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
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/*
* A simple {@link Fragment} subclass.
*/
public class OPEntryFragment_new extends Fragment implements LocationListener, View.OnClickListener {
    private TextView getAddress, tv_opentry_empid, tv_projectNo;
    private Spinner spi_opimode, spi_opentry_optype, spi_resonOP;
    private EditText edt_startkm_text, edt_endkm_text, edt_startkm_image, edt_endkm_image;
    private EditText tv_opentry_date, edtcustoname, edt_opentry_remark, edtmLocation;
    private int REQUEST_CAMERA = 0;
    private String empid;
    private String lat;
    private String longi;
    private double latitude;
    private double longitude;
    private String current_date;
    private Geocoder geocoder;


    private String intimeIvEncodeString = "", outtime_IvEncodeString = "";
    private Handler mHandler;
    private Runnable mRunnable;
    private int mInterval = 300; // milliseconds
    private boolean initialState = true;
    private Button btnStartSerice, btnStopService,btn_save,btn_milestone,btn_view_milestone;

    String paddedkeyid;
    int keyid = 1;
    int incri_id;
    int flag = 0;

    String form_no;
    Intent intent;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    DatabaseHandler db;

    private String stmode = "", stoptype = "", streasonop = "";
    private String stmode1 = "", stoptype1 = "", streasonop1 = "";

    private ListView listview_milestone;
    private boolean flag_location;
    private boolean b_insert;

    private  String housnoAddress;
    private View vv;
    private LinearLayout  listview_milestone_header;
    private OPMileStoneAdapter opMileStoneAdapter;
    private ArrayList<NearLocationData> nearLocationDataArrayList = new ArrayList<>();

    private  LocationManager locationManager;
    public OPEntryFragment_new() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_opentry_new, container, false);
        geocoder = new Geocoder(getActivity(), Locale.ENGLISH);
        SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance();
        sharedPreferenceUtils.setContext(getActivity());
        empid = sharedPreferenceUtils.getString(AppConstraint.EMPID);
        db = new DatabaseHandler(getActivity());
         vv =v;
        findIds(v);
        getLocation();
        intent = new Intent(getActivity(), OPEntryGPSTracker.class);
        sharedPreferences = getActivity().getSharedPreferences("OPENTRYSERVICE", Context.MODE_PRIVATE);

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
        Calendar cal = Calendar.getInstance();
        System.out.println(dateFormat.format(cal.getTime()));
        current_date = dateFormat.format(cal.getTime());

        String formated_Date = new String(current_date);
        formated_Date = formated_Date.replaceAll("-", "");

        paddedkeyid = String.format("%3s", keyid).replace(' ', '0');
        form_no = empid + "/" + formated_Date + "/" + paddedkeyid;
        tv_projectNo.setText(form_no);
        tv_opentry_date.setText(current_date);
        List<OPEntryData> data = db.getAllOPEntry();
        int a = data.size();

        if (a > 0) {
            for (OPEntryData datas : data) {
                flag = datas.getFlag();

                incri_id = datas.getId();
                keyid = datas.getKeyid();
                String getDate2 = datas.getCurrentdate();
                form_no = datas.getProjectNo();
                stmode1 = datas.getMode();
                String startkm = datas.getStartkm();
                String endkm = datas.getEndkm();
                intimeIvEncodeString = datas.getStartkmImage();
                outtime_IvEncodeString = datas.getEndkmImage();
                String customerName = datas.getCustomerName();
                stoptype1 = datas.getOptype();
                streasonop1 = datas.getReasonforop();
                String manuallocation = datas.getManuallocation();
                String currentLocation = datas.getCurrentLocation();
                String remark = datas.getRemark();
                flag = datas.getFlag();
                flag_location = true;

                Log.v("OPEntryflag",flag+"");
                Log.v("OPEntryincri",incri_id+"");



                if (flag == 1 || flag == 2) {

                    if (!current_date.equals(getDate2)) {
                        keyid = 1;
                    } else {
                        keyid++;
                    }
                    paddedkeyid = String.format("%3s", keyid).replace(' ', '0');
                    // int id = Integer.parseInt(paddedkeyid);
                    formated_Date = new String(current_date);
                    formated_Date = formated_Date.replaceAll("-", "");
                    form_no = empid + "/" + formated_Date + "/" + paddedkeyid;

                    tv_projectNo.setText(form_no);
                    tv_opentry_date.setText(current_date);
                    edt_startkm_text.getText().clear();
                    edt_endkm_text.getText().clear();
                    edt_startkm_image.getText().clear();
                    intimeIvEncodeString ="";
                    outtime_IvEncodeString ="";
                    edt_endkm_image.getText().clear();
                    edtcustoname.getText().clear();
                    edtmLocation.getText().clear();
                    edt_opentry_remark.getText().clear();

                } else {

                    tv_projectNo.setText(form_no);
                    tv_opentry_date.setText(current_date);
                    edt_startkm_text.setText(startkm);
                    edt_endkm_text.setText(endkm);
                    edt_startkm_image.setText(intimeIvEncodeString);
                    edt_endkm_image.setText(outtime_IvEncodeString);
                    edtcustoname.setText(customerName);
                    edtmLocation.setText(manuallocation);
                    getAddress.setText(currentLocation);
                    edt_opentry_remark.setText(remark);
                    Log.v("OPEntrystartkmimg",  intimeIvEncodeString+"");
                    ArrayList<String> list = new ArrayList<>();
                    list.add("CAR");
                    list.add("BIKE");
                    list.add("CAB");
                    list.add("OTHERS");

                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.spinner_layout, list);
                    spi_opimode.setAdapter(adapter);
                    spi_opimode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            stmode = (String) adapterView.getItemAtPosition(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }


                    });
                    if (!stmode1.equals("")) {
                        spi_opimode.setSelection(list.indexOf(stmode1));
                    }
                    ArrayList<String> list2 = new ArrayList<>();
                    list2.add("Full Day");
                    list2.add("First Half");
                    list2.add("Second Half");

                    ArrayAdapter adapter2 = new ArrayAdapter(getActivity(), R.layout.spinner_layout, list2);
                    spi_opentry_optype.setAdapter(adapter2);

                    spi_opentry_optype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            stoptype = (String) adapterView.getItemAtPosition(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    if (!stoptype1.equals("")) {
                        spi_opentry_optype.setSelection(list.indexOf(stoptype1));
                    }

                    ArrayList<String> list3 = new ArrayList<>();
                    list3.add("Customer");
                    list3.add("Factory");
                    list3.add("Office Work");
                    list3.add("Meeting");
                    ArrayAdapter adapter3 = new ArrayAdapter(getActivity(), R.layout.spinner_layout, list3);
                    spi_resonOP.setAdapter(adapter3);
                    spi_resonOP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            streasonop = (String) adapterView.getItemAtPosition(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    if (!streasonop1.equals("")) {
                        spi_resonOP.setSelection(list.indexOf(streasonop1));
                    }

                }
            }

        } else {

        }

        allEdittexform();




        return v;
    }
    public void initLinearBind( View v, List<OPEntryMilestone> opEntryMilestones) {


        LinearLayout liFirst = (LinearLayout)v.findViewById(R.id.table_mainssss);
        if(((LinearLayout) liFirst).getChildCount() > 0)
            ((LinearLayout) liFirst).removeAllViews();

        for (int i = 1; i < opEntryMilestones.size(); i++) {
            LinearLayout LL1 = new LinearLayout(getActivity());
            // LL2.setBackgroundColor(Color.CYAN);
            LL1.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams LLParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


            LL1.setLayoutParams(LLParams1);

            TextView tv1 = new TextView(getActivity());
            LinearLayout.LayoutParams tvParams_1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1f);
            tv1.setLayoutParams(tvParams_1);
            tv1.setTextSize(12);
            tv1.setTextColor(Color.BLACK);
            tv1.setText(i+"");



            TextView tv2 = new TextView(getActivity());
            LinearLayout.LayoutParams tvParams_2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1f);
            tv2.setLayoutParams(tvParams_2);
            tv2.setTextSize(12);
            tv2.setTextColor(Color.BLACK);
            tv2.setText(opEntryMilestones.get(i).getDate());

            TextView tv3 = new TextView(getActivity());
            LinearLayout.LayoutParams tvParams_3 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1f);
            tv3.setLayoutParams(tvParams_3);;
            tv3.setTextSize(12);
            tv3.setTextColor(Color.BLACK);
            tv3.setText(opEntryMilestones.get(i).getLocation());


            LL1.addView(tv1);
            LL1.addView(tv2);
            LL1.addView(tv3);
            liFirst.addView(LL1);


        }





    }

    private void findIds(View v) {
        listview_milestone =(ListView)v.findViewById(R.id.listview_milestone) ;
        edt_startkm_text = (EditText) v.findViewById(R.id.edt_startkm_text);
        edt_endkm_text = (EditText) v.findViewById(R.id.edt_endkmtext);
        edt_startkm_image = (EditText) v.findViewById(R.id.edt_startkm_image);
        edt_endkm_image = (EditText) v.findViewById(R.id.edt_endkm_image);
        btnStartSerice = (Button) v.findViewById(R.id.btn_start_service);
        btnStopService = (Button) v.findViewById(R.id.btn_stop_service);
        tv_projectNo = (TextView) v.findViewById(R.id.tv_project_no);

    /*    edt_attandence_intime =(ImageView)v.findViewById(R.id.edt_attandence_intime);
        edt_attandence_outtime =(ImageView)v.findViewById(R.id.edt_attandence_outtime);
        tv_intime_get =(TextView)v.findViewById(R.id.tv_intime_get);
        tv_outtime_get=(TextView) v.findViewById(R.id.tv_outtime_get);
        btn_in_preview =(Button)v.findViewById(R.id.btn_in_preview) ;*/

        edt_startkm_image.setOnClickListener(this);
        edt_endkm_image.setOnClickListener(this);
        btnStartSerice.setOnClickListener(this);
        btnStopService.setOnClickListener(this);


        tv_opentry_date = (EditText) v.findViewById(R.id.tv_opentry_date);
        tv_opentry_date.setOnClickListener(this);

        tv_opentry_empid = (TextView) v.findViewById(R.id.tv_opentry_empid);
        edtcustoname = (EditText) v.findViewById(R.id.edt_cust_name);
        edt_opentry_remark = (EditText) v.findViewById(R.id.edt_opentry_remark);
        edtmLocation = (EditText) v.findViewById(R.id.edt_mlocation);
        getAddress = (TextView) v.findViewById(R.id.tv_opentry_currlocation);
        spi_opimode = (Spinner) v.findViewById(R.id.spi_opimode);
        btn_save = (Button) v.findViewById(R.id. btn_save);
        btn_save.setOnClickListener(this);
        btn_milestone = (Button) v.findViewById(R.id. btn_milestone);
        btn_milestone.setOnClickListener(this);
        btn_view_milestone= (Button) v.findViewById(R.id. btn_view_milestone);
        btn_view_milestone.setOnClickListener(this);
        listview_milestone_header=(LinearLayout)v.findViewById(R.id.listview_milestone_header);

        ArrayList<String> list = new ArrayList<>();
        list.add("CAR");
        list.add("BIKE");
        list.add("CAB");
        list.add("OTHERS");

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.spinner_layout, list);
        spi_opimode.setAdapter(adapter);
        spi_opimode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                stmode = (String) adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        });

        if (!stmode1.equals("")) {
            spi_opimode.setSelection(list.indexOf(stmode1));
        }

        spi_opentry_optype = (Spinner) v.findViewById(R.id.spi_opentry_optype);
        spi_resonOP = (Spinner) v.findViewById(R.id.spi_reasonop);

        ArrayList<String> list2 = new ArrayList<>();
        list2.add("Full Day");
        list2.add("First Half");
        list2.add("Second Half");

        ArrayAdapter adapter2 = new ArrayAdapter(getActivity(), R.layout.spinner_layout, list2);
        spi_opentry_optype.setAdapter(adapter2);

        spi_opentry_optype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                stoptype = (String) adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        if (!stoptype1.equals("")) {
            spi_opentry_optype.setSelection(list.indexOf(stoptype1));
        }

        ArrayList<String> list3 = new ArrayList<>();
        list3.add("Customer");
        list3.add("Factory");
        list3.add("Office Work");
        list3.add("Meeting");
        ArrayAdapter adapter3 = new ArrayAdapter(getActivity(), R.layout.spinner_layout, list3);
        spi_resonOP.setAdapter(adapter3);

        spi_resonOP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                streasonop = (String) adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (!streasonop1.equals("")) {
            spi_resonOP.setSelection(list.indexOf(streasonop1));
        }
        tv_opentry_empid.setText(empid);

    }

    @Override
    public void onLocationChanged(Location location) {
        lat = String.valueOf(location.getLatitude());
        longi = String.valueOf(location.getLongitude());
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        FindNearest();
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null)
            {
                 housnoAddress = addresses.get(0).getPremises() +","+addresses.get(0).getSubLocality()+","+addresses.get(0).getLocality();
                Address returnedAddress = addresses.get(0);
                //Toast.makeText(getActivity(),housno , Toast.LENGTH_LONG).show();
                StringBuilder strReturnedAddress = new StringBuilder("Address:");
                // for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
               // strReturnedAddress = strReturnedAddress.append(returnedAddress.getAddressLine(0)).append("\n");
                strReturnedAddress = strReturnedAddress.append(housnoAddress);
              //  strReturnedAddress = strReturnedAddress.append(city).append("\n");
                //}
                if (!strReturnedAddress.equals(null)) {

                    if (!flag_location) {
                        getAddress.setText(strReturnedAddress);
                    }

                } else {
                    getAddress.setText("Test Location");
                }
                Log.v("getLocation OPEntry", strReturnedAddress.toString());
                // Toast.makeText(getActivity(), strReturnedAddress.toString() , Toast.LENGTH_LONG).show();
                // tv_first.setText("Your Current Address is : "+strReturnedAddress.toString());

            } else {
                //  tv_first.setText("No Address returned!");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            // tv_first.setText("Canont get Address!");
        }

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
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider, 5000, 0, this);
            if (location != null)
                onLocationChanged(location);
            else
                Toast.makeText(getActivity(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getActivity(), "No Provider Found", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.edt_startkm_image) {
            selectImage("start");

        }
        if (view.getId() == R.id.edt_endkm_image) {
            selectImage("end");
        }
        if (view.getId() == R.id.btn_start_service) {
            if (!OPEntryGPSTracker.isRunning) {
                editor = sharedPreferences.edit();
                editor.putString("formno", form_no);
                editor.putString("getdate", current_date);
                editor.putString("empid", empid);
                editor.commit();
                getActivity().startService(intent);

            }

        }

        if (view.getId() == R.id.btn_stop_service) {
            getActivity().stopService(intent);
        }

        if (view.getId() == R.id.tv_opentry_date) {
            setdate(tv_opentry_date);
        }
        if (view.getId() == R.id.btn_save) {

            if (TextUtils.isEmpty(edt_startkm_text.getText().toString())) {
                edt_startkm_text.setError("Please Enter StartKM");
                edt_endkm_text.getText().clear();
                edt_startkm_text.requestFocus();
                return;
            } else if (TextUtils.isEmpty(edt_endkm_text.getText())) {
                edt_endkm_text.setError("Please Enter EndKM");
                edt_endkm_text.requestFocus();
                return;
            }else{

                  new sendDataAsnycTask().execute(AppConstraint.OPENTRY);
            }
        }

        if (view.getId() == R.id.btn_milestone) {
          //  String formno, String date, String lat, String longi, int milestone_flag
            try {
                Log.v("mile stone","insert milestone");
                db.insertOPEntryMilestone(new OPEntryMilestone(form_no,current_date,lat,longi,housnoAddress,nearLocationDataArrayList.get(0).getName(),0));

            }catch (NullPointerException e){
                Log.v("mile stone","insert milestone exception");
                db.insertOPEntryMilestone(new OPEntryMilestone(form_no,current_date,lat,longi,housnoAddress,nearLocationDataArrayList.get(0).getName(),0));

            }
          //  db.insertOPEntryMilestone(new OPEntryMilestone(form_no,current_date,lat,longi,housnoAddress,nearLocationDataArrayList.get(0).getName(),flag));
            ArrayList<OPEntryMilestone> arrayList = new ArrayList<>();

            List<OPEntryMilestone> opEntryMilestoneList = db.getOPEntryMilestone(tv_projectNo.getText().toString());
            arrayList.addAll(opEntryMilestoneList);

            Log.v("mile stone","size "+opEntryMilestoneList.size());
            if(opEntryMilestoneList.size()>0)
            {
                listview_milestone_header.setVisibility(View.VISIBLE);
                 opMileStoneAdapter = new OPMileStoneAdapter(getActivity(),R.layout.milestone_adapter,arrayList);
                listview_milestone.setAdapter(opMileStoneAdapter);
                ListviewHelper.setListViewHeightBasedOnItems(listview_milestone);
                opMileStoneAdapter.notifyDataSetChanged();
                new sendDataMilestoneAsnycTask().execute(AppConstraint.OPENTRYMILESTONE);
            }
        }

        if(view.getId() == R.id.btn_view_milestone){
            ArrayList<OPEntryMilestone> arrayList = new ArrayList<>();
            List<OPEntryMilestone> opEntryMilestoneList = db.getOPEntryMilestone(tv_projectNo.getText().toString());
            arrayList.addAll(opEntryMilestoneList);

            if(opEntryMilestoneList.size()>0){
                listview_milestone_header.setVisibility(View.VISIBLE);
                opMileStoneAdapter = new OPMileStoneAdapter(getActivity(),R.layout.milestone_adapter,arrayList);
                listview_milestone.setAdapter(opMileStoneAdapter);
                ListviewHelper.setListViewHeightBasedOnItems(listview_milestone);
                opMileStoneAdapter.notifyDataSetChanged();
                //initLinearBind(vv,arrayList);
            }

        }

    }

    private void onCaptureImageResult(Intent data, String name) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        if (name.equals("start")) {
            SimpleDateFormat time_formatter = new SimpleDateFormat("HH:mm:ss");
            String current_time_str = time_formatter.format(System.currentTimeMillis());

            if (lat == null) {
                Toast.makeText(getActivity(), "please wait gps location not found", Toast.LENGTH_LONG).show();
            } else {
                latitude = Double.parseDouble(lat);
                longitude = Double.parseDouble(longi);
                String totalString = current_date + current_time_str + "\nLat :" + String.format("%.4f", latitude) + ",  Long :" + String.format("%.4f", longitude) + "\nStartKM Image ";
                // Bitmap setTextwithImage =    ProcessingBitmap(thumbnail,totalString);
                Bitmap setTextwithImage = DrawBitmapAll.drawTextToBitmap(getContext(), thumbnail, totalString);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                setTextwithImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                String destinationpath = Environment.getExternalStorageDirectory().toString();
                File destination = new File(destinationpath + "/ESPOPENTRY/");
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
                intimeIvEncodeString = encodeToBase64(setTextwithImage, Bitmap.CompressFormat.JPEG, 100);
                edt_startkm_image.setText(intimeIvEncodeString);

                new sendDataAsnycTaskFirstTime().execute(AppConstraint.OPENTRY);
            }
        }

        if (name.equals("end")) {
            SimpleDateFormat time_formatter = new SimpleDateFormat("HH:mm:ss");
            String current_time_str = time_formatter.format(System.currentTimeMillis());

            if (lat == null) {
                Toast.makeText(getActivity(), "please wait gps location not found", Toast.LENGTH_LONG).show();
            }
            else
                {
                latitude = Double.parseDouble(lat);
                longitude = Double.parseDouble(longi);
                String totalString = current_date + current_time_str + "\nLat:" + String.format("%.4f", latitude) + ",Long:" + String.format("%.4f", longitude) + "\nEndKM Image ";
                //  Bitmap setTextwithImage =    ProcessingBitmap(thumbnail,totalString);
                Bitmap setTextwithImage = DrawBitmapAll.drawTextToBitmap(getContext(), thumbnail, totalString);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                setTextwithImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String destinationpath = Environment.getExternalStorageDirectory().toString();
                File destination = new File(destinationpath + "/ESPOPENTRY/");
                if (!destination.exists()) {
                    destination.mkdirs();
                }
                FileOutputStream fo;
                try {
                    //  file.createNewFile();
                    File file = new File(destination, current_date + "_" + current_time_str + ".jpg");
                    fo = new FileOutputStream(file);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                //  Uri tempUri = getImageUri(getActivity(), thumbnail);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                // File finalFile = new File(getRealPathFromURI(tempUri));
                System.out.println(destination.getAbsolutePath().substring(destination.getAbsolutePath().lastIndexOf("\\") + 1));
                String path = (destination.getAbsolutePath().substring(destination.getAbsolutePath().lastIndexOf("\\") + 1));
                // endkmImageEncodeString= encodeToBase64(thumbnail, Bitmap.CompressFormat.JPEG,50);
                outtime_IvEncodeString = encodeToBase64(setTextwithImage, Bitmap.CompressFormat.JPEG, 100);
                edt_endkm_image.setText(outtime_IvEncodeString);
            }
            // setImg.setImageBitmap(thumbnail);
        }


        // setImg.setImageBitmap(thumbnail);

    }

    private void selectImage(String Value) {
        if (Value.equals("start")) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CAMERA);
        }
        if (Value.equals("end")) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 2);
        }

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data, "start");

            if (requestCode == 2)
                onCaptureImageResult(data, "end");

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


/*    private void sendAllDataServer() {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, AppConstraint.COMMONURL, JsonParameterSendServer(),
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        Log.e("!_@@_SUCESS", response + "");
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("!_@@_Errors--", error + "");
                        pDialog.dismiss();
                    }
                }) {
        *//*
          @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
               // params.put("Authorization", "key=" + Legacy_SERVER_KEY);
                params.put("Content-Type", "application/json");
                return params;
            }  *//*
        };
        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest, null);
    }

    private JSONObject JsonParameterSendServer() {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray jsonArrayParameter = new JSONArray();

            String trnno = "vTrnno";
            jsonArrayParameter.put(trnno);
            jsonObject.put("DatabaseName", "TNS_NT_PMS");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");
            jsonObject.put("spName", "insertDynamicData");
            jsonObject.put("ParameterList", jsonArrayParameter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }*/

    protected void blinkTextTask(TextView mTextView) {
        if (initialState) {
            // Reverse the boolean
            initialState = false;
            // Set the TextView color to red
            mTextView.setTextColor(Color.RED);
        } else {
            // Reverse the boolean
            initialState = true;
            // Change the TextView color to initial State
            mTextView.setTextColor(Color.BLACK);
        }
        // Schedule the task
        mHandler.postDelayed(mRunnable, mInterval);
    }

    private void setdate(final EditText edt) {

        final Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        final DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        //   addmeeting_edt_selectdate.setText(dayOfMonth + "-" + dateFormatter.format (monthOfYear + 1) + "-" + year);

                        cal.set(year, monthOfYear, dayOfMonth);
                        edt.setText(dateFormatter.format(cal.getTime()));


                    }
                }, year, month, day);


        dpd.show();

    }

    private void allEdittexform() {

            edt_startkm_text.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!b_insert) {
                        if (s.length() == 1) {
                            if (flag == 1 || flag == 2) {
                                flag = 0;
                            }
                            db.insertOPEntry(new OPEntryData(keyid, tv_projectNo.getText().toString(), stmode, edt_startkm_text.getText().toString(),
                                    edt_endkm_text.getText().toString(), edt_startkm_image.getText().toString(), edt_endkm_image.getText().toString(), current_date,
                                    edtcustoname.getText().toString(), stoptype, streasonop, edtmLocation.getText().toString(), getAddress.getText().toString(), edt_opentry_remark.getText().toString(), flag));
                            incri_id = db.getLastOPEntryInsertId();
                        } else {
                            db.updateOPEntryDetail(incri_id, tv_projectNo.getText().toString(), stmode, edt_startkm_text.getText().toString(),
                                    edt_endkm_text.getText().toString(), edt_startkm_image.getText().toString(), edt_endkm_image.getText().toString(), current_date,
                                    edtcustoname.getText().toString(), stoptype, streasonop, edtmLocation.getText().toString(), getAddress.getText().toString(), edt_opentry_remark.getText().toString(), flag);

                        }

                    }
                }
            });

        edt_endkm_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b_insert) {
                    db.updateOPEntryDetail(incri_id, tv_projectNo.getText().toString(), stmode, edt_startkm_text.getText().toString(),
                            edt_endkm_text.getText().toString(), edt_startkm_image.getText().toString(), edt_endkm_image.getText().toString(), current_date,
                            edtcustoname.getText().toString(), stoptype, streasonop, edtmLocation.getText().toString(), getAddress.getText().toString(), edt_opentry_remark.getText().toString(), flag);
                }
            }
        });
        edtmLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b_insert) {
                    db.updateOPEntryDetail(incri_id, tv_projectNo.getText().toString(), stmode, edt_startkm_text.getText().toString(),
                            edt_endkm_text.getText().toString(), edt_startkm_image.getText().toString(), edt_endkm_image.getText().toString(), current_date,
                            edtcustoname.getText().toString(), stoptype, streasonop, edtmLocation.getText().toString(), getAddress.getText().toString(), edt_opentry_remark.getText().toString(), flag);

                }
            }
        });
        edtcustoname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b_insert) {
                    db.updateOPEntryDetail(incri_id, tv_projectNo.getText().toString(), stmode, edt_startkm_text.getText().toString(),
                            edt_endkm_text.getText().toString(), edt_startkm_image.getText().toString(), edt_endkm_image.getText().toString(), current_date,
                            edtcustoname.getText().toString(), stoptype, streasonop, edtmLocation.getText().toString(), getAddress.getText().toString(), edt_opentry_remark.getText().toString(), flag);
                }

            }
        });
        edt_opentry_remark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!b_insert) {
                    db.updateOPEntryDetail(incri_id, tv_projectNo.getText().toString(), stmode, edt_startkm_text.getText().toString(),
                            edt_endkm_text.getText().toString(), edt_startkm_image.getText().toString(), edt_endkm_image.getText().toString(), current_date,
                            edtcustoname.getText().toString(), stoptype, streasonop, edtmLocation.getText().toString(), getAddress.getText().toString(), edt_opentry_remark.getText().toString(), flag);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private class sendDataAsnycTask extends AsyncTask<String, Void, String> {

        ProgressDialog pd = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setTitle("Please Wait...");
            pd.setMessage("Uploaded Records");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            Log.v("opentry json",JSonobjParameter().toString());
            String s = HTTPPostRequestMethod.postMethodforESP(params[0], JSonobjParameter());
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            getActivity().stopService(intent);
            //  iv_status.setVisibility(View.GONE);
            // GPSTracker.isRunning= false;


            String re = s;
            try {

                JSONObject jsonObject = new JSONObject(s);
                String status = jsonObject.getString("status");
                String id = jsonObject.getString("id");

                Log.v("id",id);
                if (status.equals("true")) {
                    flag = 1;
                    // db.updatedetails(keyid, edt_settaxiform_date.getText().toString(), edtproject_type.getText().toString(), edtstartkmtext.getText().toString(), edt_startkmImage.getText().toString(), edtendkmtext.getText().toString(), edt_endkm_Image.getText().toString(), flag);
                    db.updateOPEntryDetail(incri_id, tv_projectNo.getText().toString(), stmode, edt_startkm_text.getText().toString(),
                            edt_endkm_text.getText().toString(), edt_startkm_image.getText().toString(), edt_endkm_image.getText().toString(), current_date,
                            edtcustoname.getText().toString(), stoptype, streasonop, edtmLocation.getText().toString(), getAddress.getText().toString(), edt_opentry_remark.getText().toString(), flag);
                    Toast.makeText(getActivity(), "Uploaded Successfully...", Toast.LENGTH_LONG).show();

                    if (!b_insert) {
                        b_insert = true;

                        edt_startkm_text.getText().clear();
                        edt_endkm_text.getText().clear();
                        edt_startkm_image.getText().clear();
                        intimeIvEncodeString ="";
                        outtime_IvEncodeString ="";
                        edt_endkm_image.getText().clear();
                        edtcustoname.getText().clear();
                        edtmLocation.getText().clear();
                        edt_opentry_remark.getText().clear();

                    }
                    getFragmentManager().beginTransaction().replace(R.id.li_opentry, new OPEntryFragment_new()).addToBackStack(null).commit();
                   /* FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(TaxiFormFragment.this).attach(TaxiFormFragment.this).commit();*/
                } else {

                    Toast.makeText(getActivity(), "Internet is not working", Toast.LENGTH_LONG).show();
                    flag = 2;
                    // db.updatedetails(keyid, edt_settaxiform_date.getText().toString(), edtproject_type.getText().toString(), edtstartkmtext.getText().toString(), edt_startkmImage.getText().toString(), edtendkmtext.getText().toString(), edt_endkm_Image.getText().toString(), flag);
                    db.updateOPEntryDetail(incri_id, tv_projectNo.getText().toString(), stmode, edt_startkm_text.getText().toString(),
                            edt_endkm_text.getText().toString(), edt_startkm_image.getText().toString(), edt_endkm_image.getText().toString(), current_date,
                            edtcustoname.getText().toString(), stoptype, streasonop, edtmLocation.getText().toString(), getAddress.getText().toString(), edt_opentry_remark.getText().toString(), flag);

                    if (!b_insert) {
                        b_insert = true;

                        edt_startkm_text.getText().clear();
                        edt_endkm_text.getText().clear();
                        edt_startkm_image.getText().clear();
                        intimeIvEncodeString ="";
                        outtime_IvEncodeString ="";
                        edt_endkm_image.getText().clear();
                        edtcustoname.getText().clear();
                        edtmLocation.getText().clear();
                        edt_opentry_remark.getText().clear();

                    }
                    getFragmentManager().beginTransaction().replace(R.id.li_opentry, new OPEntryFragment_new()).addToBackStack(null).commit();

                 /*   FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(TaxiFormFragment.this).attach(TaxiFormFragment.this).commit();*/
                }


            } catch (JSONException e) {

                Toast.makeText(getActivity(), "internet is very slow please try again", Toast.LENGTH_LONG).show();
                flag = 2;
                // db.updatedetails(keyid, edt_settaxiform_date.getText().toString(), edtproject_type.getText().toString(), edtstartkmtext.getText().toString(), edt_startkmImage.getText().toString(), edtendkmtext.getText().toString(), edt_endkm_Image.getText().toString(), flag);
                db.updateOPEntryDetail(incri_id, tv_projectNo.getText().toString(), stmode, edt_startkm_text.getText().toString(),
                        edt_endkm_text.getText().toString(), edt_startkm_image.getText().toString(), edt_endkm_image.getText().toString(), current_date,
                        edtcustoname.getText().toString(), stoptype, streasonop, edtmLocation.getText().toString(), getAddress.getText().toString(), edt_opentry_remark.getText().toString(), flag);

                if (!b_insert) {
                    b_insert = true;

                    edt_startkm_text.getText().clear();
                    edt_endkm_text.getText().clear();
                    edt_startkm_image.getText().clear();
                    intimeIvEncodeString ="";
                    outtime_IvEncodeString ="";
                    edt_endkm_image.getText().clear();
                    edtcustoname.getText().clear();
                    edtmLocation.getText().clear();
                    edt_opentry_remark.getText().clear();

                }


                getFragmentManager().beginTransaction().replace(R.id.li_opentry, new OPEntryFragment_new()).addToBackStack(null).commit();

             /*   FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(TaxiFormFragment.this).attach(TaxiFormFragment.this).commit();
             */
                e.printStackTrace();
            }


        }
    }

    private JSONObject JSonobjParameter() {
        String getDate = "";
        JSONObject jsonObject = new JSONObject();

        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MMM-yyyy");

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

        try {

            jsonObject.put("DatabaseName", "TNS_HR");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");
            jsonObject.put("EmpId", empid);
            jsonObject.put("ProjectNo", tv_projectNo.getText().toString());
            jsonObject.put("Mode", stmode);
            jsonObject.put("StartKM", edt_startkm_text.getText().toString());
            jsonObject.put("StartKmImg", intimeIvEncodeString);
            jsonObject.put("EndKM", edt_endkm_text.getText().toString());
            jsonObject.put("EndKMImg", outtime_IvEncodeString);
            jsonObject.put("vDate", getDate);
            jsonObject.put("CustomerName", edtcustoname.getText().toString());
            jsonObject.put("OPType", stoptype);
            jsonObject.put("ReasonForOP", streasonop);
            jsonObject.put("UserLocation",  edtmLocation.getText().toString());
            jsonObject.put("GPSLocation",  getAddress.getText().toString());
            jsonObject.put("Remarks", edt_opentry_remark.getText().toString());
            jsonObject.put("Lat", lat);
            jsonObject.put("Log", longi);
            jsonObject.put("Status", "1");

            Log.v("startkmImg json",intimeIvEncodeString.toString());
            Log.v("endkmImg json",outtime_IvEncodeString.toString());
             Log.v("jsonopsend",jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    private JSONObject JSonobjParameterFirsttime() {
        String getDate = "";
        JSONObject jsonObject = new JSONObject();

        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MMM-yyyy");

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

        try {

            jsonObject.put("DatabaseName", "TNS_HR");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");
            jsonObject.put("EmpId", empid);
            jsonObject.put("ProjectNo", tv_projectNo.getText().toString());
            jsonObject.put("Mode", stmode);
            jsonObject.put("StartKM", edt_startkm_text.getText().toString());
            jsonObject.put("StartKmImg", intimeIvEncodeString);
            jsonObject.put("EndKM", "");
            jsonObject.put("EndKMImg", "");
            jsonObject.put("vDate", getDate);
            jsonObject.put("CustomerName", edtcustoname.getText().toString());
            jsonObject.put("OPType", stoptype);
            jsonObject.put("ReasonForOP", streasonop);
            jsonObject.put("UserLocation",  edtmLocation.getText().toString());
            jsonObject.put("GPSLocation",  getAddress.getText().toString());
            jsonObject.put("Remarks", edt_opentry_remark.getText().toString());
            jsonObject.put("Lat", lat);
            jsonObject.put("Log", longi);
            jsonObject.put("Status", "1");

            Log.v("startkmImg json",intimeIvEncodeString.toString());
            Log.v("endkmImg json",outtime_IvEncodeString.toString());
            Log.v("jsonopfirsttime",jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private class sendDataAsnycTaskFirstTime extends AsyncTask<String, Void, String> {

        ProgressDialog pd = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setTitle("Please Wait...");
            pd.setMessage("Uploaded Records");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String s = HTTPPostRequestMethod.postMethodforESP(params[0], JSonobjParameterFirsttime());
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();


            String re = s;
            try {

                JSONObject jsonObject = new JSONObject(s);
                String status = jsonObject.getString("status");
                //   String id = jsonObject.getString("id");


                if (status.equals("true")) {
                    flag = 0;
                    // db.updatedetails(keyid, edt_settaxiform_date.getText().toString(), edtproject_type.getText().toString(), edtstartkmtext.getText().toString(), edt_startkmImage.getText().toString(), edtendkmtext.getText().toString(), edt_endkm_Image.getText().toString(), flag);
                    // db.updatedetails(keyid, edt_settaxiform_date.getText().toString(), edtproject_type.getText().toString(), edtstartkmtext.getText().toString(), edt_startkmImage.getText().toString(), edtendkmtext.getText().toString(), edt_endkm_Image.getText().toString(), flag);
                    db.updateOPEntryDetail(incri_id, tv_projectNo.getText().toString(), stmode, edt_startkm_text.getText().toString(),
                            edt_endkm_text.getText().toString(), edt_startkm_image.getText().toString(), edt_endkm_image.getText().toString(), current_date,
                            edtcustoname.getText().toString(), stoptype, streasonop, edtmLocation.getText().toString(), getAddress.getText().toString(), edt_opentry_remark.getText().toString(), flag);
                    Toast.makeText(getActivity(), "Uploaded Successfully...", Toast.LENGTH_LONG).show();                    //     Toast.makeText(getActivity(), "Uploaded FirstTime Successfully...", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(getActivity(), "Internet is not working", Toast.LENGTH_LONG).show();
                    flag = 0;
                    // db.updatedetails(keyid, edt_settaxiform_date.getText().toString(), edtproject_type.getText().toString(), edtstartkmtext.getText().toString(), edt_startkmImage.getText().toString(), edtendkmtext.getText().toString(), edt_endkm_Image.getText().toString(), flag);
                    // db.updatedetails(keyid, edt_settaxiform_date.getText().toString(), edtproject_type.getText().toString(), edtstartkmtext.getText().toString(), edt_startkmImage.getText().toString(), edtendkmtext.getText().toString(), edt_endkm_Image.getText().toString(), flag);
                    db.updateOPEntryDetail(incri_id, tv_projectNo.getText().toString(), stmode, edt_startkm_text.getText().toString(),
                            edt_endkm_text.getText().toString(), edt_startkm_image.getText().toString(), edt_endkm_image.getText().toString(), current_date,
                            edtcustoname.getText().toString(), stoptype, streasonop, edtmLocation.getText().toString(), getAddress.getText().toString(), edt_opentry_remark.getText().toString(), flag);
                    Toast.makeText(getActivity(), "Uploaded Successfully...", Toast.LENGTH_LONG).show();


                }


            } catch (JSONException e) {

                Toast.makeText(getActivity(), "internet is very slow please try again", Toast.LENGTH_LONG).show();
                flag = 0;
                // db.updatedetails(keyid, edt_settaxiform_date.getText().toString(), edtproject_type.getText().toString(), edtstartkmtext.getText().toString(), edt_startkmImage.getText().toString(), edtendkmtext.getText().toString(), edt_endkm_Image.getText().toString(), flag);
                // db.updatedetails(keyid, edt_settaxiform_date.getText().toString(), edtproject_type.getText().toString(), edtstartkmtext.getText().toString(), edt_startkmImage.getText().toString(), edtendkmtext.getText().toString(), edt_endkm_Image.getText().toString(), flag);
                db.updateOPEntryDetail(incri_id, tv_projectNo.getText().toString(), stmode, edt_startkm_text.getText().toString(),
                        edt_endkm_text.getText().toString(), edt_startkm_image.getText().toString(), edt_endkm_image.getText().toString(), current_date,
                        edtcustoname.getText().toString(), stoptype, streasonop, edtmLocation.getText().toString(), getAddress.getText().toString(), edt_opentry_remark.getText().toString(), flag);
                Toast.makeText(getActivity(), "Uploaded Successfully...", Toast.LENGTH_LONG).show();

                e.printStackTrace();
            }


        }
    }


    private class OPMileStoneAdapter extends ArrayAdapter {
        OPEntryMilestone milestonedata;
        int deepColor = Color.parseColor("#FFFFFF");
        int deepColor2 = Color.parseColor("#DCDCDC");
        //  int deepColor3 = Color.parseColor("#B58EBF");
        private int[] colors = new int[]{deepColor, deepColor2};
        private List<OPEntryMilestone> searchlist = null;



        public OPMileStoneAdapter(Context context, int resource, ArrayList<OPEntryMilestone> latLongDatas) {
            super(context, resource, latLongDatas);
            this.searchlist = latLongDatas;

        }

        private class ViewHolder {
            TextView id;
            TextView formno;
            TextView date;
            TextView lat;
            TextView log;
            TextView location;
            TextView status;
            TextView delete;




        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

           ViewHolder viewHolder = null;

            if (convertView == null) {
                 viewHolder = new ViewHolder();
                LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.milestone_adapter, null);
                //int colorPos = position % colors.length;
                // convertView.setBackgroundColor(colors[colorPos]);

                viewHolder.id = (TextView) convertView.findViewById(R.id.srno_milestone);
                viewHolder.formno = (TextView) convertView.findViewById(R.id.projectno_milestone);
                viewHolder.date = (TextView) convertView.findViewById(R.id.date_milestone);
                viewHolder.lat = (TextView) convertView.findViewById(R.id.lat_milestone);
                viewHolder.log = (TextView) convertView.findViewById(R.id.log_milestone);
                viewHolder.location = (TextView) convertView.findViewById(R.id.location_milestone);
                viewHolder.status = (TextView) convertView.findViewById(R.id.status_milestone);

                convertView.setTag(viewHolder);

            }
                else {
                    int colorPos = position % colors.length;
                    convertView.setBackgroundColor(colors[colorPos]);
                    viewHolder = (ViewHolder) convertView.getTag();
                }

               milestonedata = searchlist.get(position);

               /*  for(int i = 1; i<=searchlist.size();i++){
                     viewHolder.id.setText(i+"");
                }*/
                 viewHolder.id.setText(position+1 +"");
                viewHolder.formno.setText(milestonedata.getFormno());
                viewHolder.date.setText(milestonedata.getDate());
                viewHolder.lat.setText(milestonedata.getLat());
                viewHolder.log.setText(milestonedata.getLongi());
                viewHolder.location.setText(milestonedata.getLocation());
                viewHolder.status.setText(milestonedata.getMilestone_flag()+"");

            return convertView;

        }

      /*  private void deleteitemDialog(final int p){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(true);
            builder.setTitle("Are you sure delete this item?");
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // How to remove the selected item?
                    // adapter.remove(adapter.getItem(p));
                    dialog.dismiss();
                }


            });
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // How to remove the selected item?
                    // adapter.remove(adapter.getItem(p));
                    milestonedata = searchlist.get(p);
                    //  db.deleteSingleRowTaxiformData_ByID(milestonedata.getId());
                    db.deleteAllRowLatLongData(milestonedata.getFormno());

                    searchlist.remove(p);
                   notifyDataSetChanged();
                }


            });

            AlertDialog alert = builder.create();
            alert.show();
        }
*/


        public void remove(OPEntryMilestone object) {
            searchlist.remove(object);
            notifyDataSetChanged();
        }

        public List<OPEntryMilestone> getTaxiForm_DataArrayList() {
            return searchlist;
        }


    }

    private class sendDataMilestoneAsnycTask extends AsyncTask<String, Void, String> {

        ProgressDialog pd = new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setTitle("Please Wait...");
            pd.setMessage("Uploaded Records");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            Log.v("opmilestone json",JSonobjMileStoneParameter().toString());
            String s = HTTPPostRequestMethod.postMethodforESP(params[0], JSonobjMileStoneParameter());
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            int idss= db.  getLastInsertIdMilestone();
            String re = s;
            try {

                JSONObject jsonObject = new JSONObject(s);
                boolean status = jsonObject.getBoolean("status");
                String id = jsonObject.getString("id");

                Log.v("id",id);
                if (status == true) {


                    db.updateMilestone(idss,1);
                    Toast.makeText(getActivity(), "Uploaded Successfully...", Toast.LENGTH_LONG).show();
                    opMileStoneAdapter.notifyDataSetChanged();

                } else {
                    db.updateMilestone(idss,0);
                    Toast.makeText(getActivity(), "Internet is not working", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                db.updateMilestone(idss,0);
                Toast.makeText(getActivity(), "internet is very slow please try again", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    private JSONObject JSonobjMileStoneParameter() {
        NearLocationData nd=  nearLocationDataArrayList.get(0);
        String nLat = String.valueOf( nd.getLat());
        String nLog = String.valueOf( nd.getLog());
        String nlocation = String.valueOf( nd.getName());

        String getDate = "";
        JSONObject jsonObject = new JSONObject();
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MMM-yyyy");
        try {

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy");
            Date dtt = df.parse(current_date);
            Date ds = new Date(dtt.getTime());
            getDate= dateFormat2.format(ds);
            System.out.println(getDate);
            Log.v("datess",getDate);

        } catch (ParseException e) {
            Log.v("exception",e.getMessage());
            e.printStackTrace();
        }
        try {
       /*     incri_id, tv_projectNo.getText().toString(), stmode, edt_startkm_text.getText().toString(),
                    edt_endkm_text.getText().toString(), edt_startkm_image.getText().toString(),
                    edt_endkm_image.getText().toString(), current_date,
                    edtcustoname.getText().toString(), stoptype, streasonop, edtmLocation.getText().toString(),
                    getAddress.getText().toString(), edt_opentry_remark.getText().toString(), flag);
       */
            jsonObject.put("DatabaseName", "TNS_HR");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");
            jsonObject.put("EmpId", empid);
            jsonObject.put("ProjectNo", tv_projectNo.getText().toString());
            jsonObject.put("vDate", getDate);
            jsonObject.put("Lat", lat);
            jsonObject.put("Log", longi);
            jsonObject.put("Status", "0");
            jsonObject.put("NearestLocation", nlocation);

             Log.v("milestone json",jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    //.............................................Find Near Search Location..............................

    private void FindNearest( ){

        StringBuilder googlePlacesUrl =  new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + 500);
        googlePlacesUrl.append("&types=" + "school");
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + getString(R.string.google_maps_key));

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
            Log.v("GetAddress1",result);
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

    @Override
    public void onStart() {
        super.onStart();
        locationManager.removeUpdates(this);
    }
}

