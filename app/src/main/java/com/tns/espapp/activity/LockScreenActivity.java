package com.tns.espapp.activity;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tns.espapp.AppConstraint;
import com.tns.espapp.R;
import com.tns.espapp.Utility.AppSingleton;
import com.tns.espapp.Utility.SharedPreferenceUtils;
import com.tns.espapp.Utility.Utility;
import com.tns.espapp.database.AttendanceSettingData;
import com.tns.espapp.database.DatabaseHandler;
import com.tns.espapp.database.GPSCheckStatusData;
import com.tns.espapp.database.OPEntryData;
import com.tns.espapp.database.OPEntryLatLog;
import com.tns.espapp.database.Picture;
import com.tns.espapp.database.SettingData;
import com.tns.espapp.database.TaxiFormData;
import com.tns.espapp.service.GPSTracker;
import com.tns.espapp.service.OPEntryGPSTracker;
import com.tns.espapp.service.SendLatiLongiServerIntentService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LockScreenActivity extends AppCompatActivity {
    private EditText employeePasswordEditText;
    private String empPassword;
    private SharedPreferenceUtils sharedPreferences;
    private ImageView verifyBtn;
    private TextView tnsMpinTextView;
    private TextView descriptionTextView;
    private DatabaseHandler db;
    private SharedPreferences sharedPreferencessss;
    private SharedPreferences sharedPreferencessssOP;
    private SharedPreferences.Editor editor;
    String  empIDS;
    private String Lat, Logi, RadialDistance, LocationType ="";
    String[] permissions = {
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.RECEIVE_BOOT_COMPLETED"
    };
    public static final int MULTIPLE_PERMISSIONS = 10;
    Handler  handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        db = new DatabaseHandler(this);

        sharedPreferencessss = getSharedPreferences("SERVICE", Context.MODE_PRIVATE);
        sharedPreferencessssOP = getSharedPreferences("OPENTRYSERVICE", Context.MODE_PRIVATE);

        SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance();
        sharedPreferenceUtils.setContext(LockScreenActivity.this);
        empIDS = sharedPreferenceUtils.getString(AppConstraint.EMPID);
        startService(new Intent(getApplication(), SendLatiLongiServerIntentService.class));
        GetAttendanceSettingDataServer();
        getLayoutsId();
        registerOnClickListener();






        if (checkPermissions()) {
        }

       /* if (Build.BRAND.equalsIgnoreCase("xiaomi")) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            startActivity(intent);
        }*/

       // addAutoStartup();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void getLayoutsId() {
        employeePasswordEditText = (EditText) findViewById(R.id.employeePasswordEditText);
        tnsMpinTextView = (TextView) findViewById(R.id.tnsMpinTextView);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        verifyBtn = (ImageView) findViewById(R.id.verifyBtn);
        setFontFamily();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }
private void setFontFamily()
{
    Typeface face = Typeface.createFromAsset(getAssets(), "arial.ttf");
    tnsMpinTextView.setTypeface(face);
    descriptionTextView.setTypeface(face);

}

    private boolean isMandatoryField() {
        employeePasswordEditText.setError(null);
        empPassword = employeePasswordEditText.getText().toString();

        if (TextUtils.isEmpty(empPassword)) {
            employeePasswordEditText.setError(getString(R.string.employeeIdError));
            return false;
        }
        return true;


    }

    private void registerOnClickListener() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id) {
                    case R.id.verifyBtn:
                        if (Utility.isNetworkAvailable(getApplicationContext())) {
                            if (isMandatoryField()) {
                                compareEmployeeId();
                            }
                        } else {
                            Utility.displayMessage(getApplicationContext(), "Network is not available");
                        }
                }
            }
        };
        verifyBtn.setOnClickListener(clickListener);
    }

    private void compareEmployeeId() {
        sharedPreferences = SharedPreferenceUtils.getInstance();
        sharedPreferences.setContext(getApplicationContext());
        String storedEmpId = sharedPreferences.getString(AppConstraint.EMPID);
        createSettingJsonRequest(storedEmpId, empPassword);

    }

    private JSONObject getUserSettingJsonObject(String empId, String empPassword) {

        JSONObject jsonObject = new JSONObject();
        String IMEINumber = Utility.getIMEINumber(this);
        try {
            jsonObject.put("DatabaseName", "TNS_HR");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");
            jsonObject.put("spName", "USP_GetDeviceSetting");
            jsonObject.put("loginID", empId);
            jsonObject.put("loginPassword", empPassword);
            jsonObject.put("Deviceid", IMEINumber);

            Log.v("lockschreen JSON",jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

    private void createSettingJsonRequest(String empId, String empPassword) {
        Log.d("createSettingJsonRequest", getUserSettingJsonObject(empId, empPassword).toString());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                AppConstraint.VERIFYLOGINURL, getUserSettingJsonObject(empId, empPassword),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        parseSettingResponse(response);
                        pDialog.hide();

                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
            }

        });


        AppSingleton.getInstance(this).addToRequestQueue(jsonObjReq, null);
    }

    private void parseSettingResponse(JSONObject response) {
        Log.d("printSettingResponse", response.toString());
        try {

            int gpsenabled_int;
            int status = response.getInt("status");
            String workingStatus = response.getString("EmpStatus");
            int GpSInterval  = response.getInt("GpSInterval");
            boolean GPSEnabled  = response.getBoolean("GPSEnabled");
            String GPSSpeed  = response.getString("GPSSpeed");

            if(GPSEnabled){

                gpsenabled_int = 1;
            }else{

                gpsenabled_int = 0;
            }

            if(status==0)
            {
                Utility.displayMessage(this,"Your record is not find in DataBase");
                employeePasswordEditText.setText("");
                employeePasswordEditText.setError(getString(R.string.check_employeePasswordError));
                return;
            }
            else
            {
                if(workingStatus.equalsIgnoreCase("NO"))
                {
                    Utility.displayMessage(this,"You are not Currently Working in Tns");
                    return;
                }

                db.deleteGPSsettingData();
                db.insertGPSSettingData(new SettingData(gpsenabled_int,GpSInterval,Integer.parseInt(GPSSpeed),"1"));
                Intent intent = new Intent(LockScreenActivity.this,HomeActivity.class);
                startActivity(intent);

/*
................................startservice..................................................*/

                List<TaxiFormData> datas = db.getLastTaxiformData();
                if (datas.size() > 0) {
                    int flag = datas.get(0).getFlag();
                    if (flag == 0) {

                        Intent   intent2 = new Intent(LockScreenActivity.this, GPSTracker.class);
                        if (!GPSTracker.isRunning) {
                            editor = sharedPreferencessss.edit();
                            editor.putString("formno", datas.get(0).getFormno());
                            editor.putString("getdate", datas.get(0).getSelectdate());
                            editor.putString("empid", empIDS);
                            editor.commit();
                          startService(intent2);

                        }else{

                            Utility.displayMessage(this,"running service");
                        }
                    }
                }

                /*
................................stopservice..................................................*/

                /*
................................startservice..................................................*/

                List<OPEntryData> datasOP = db.getLastOPEntryData();
                if (datasOP.size() > 0) {
                    int flag = datasOP.get(0).getFlag();
                    if (flag == 0) {
                        Intent   intent2 = new Intent(LockScreenActivity.this, OPEntryGPSTracker.class);
                        if (!OPEntryGPSTracker.isRunning) {
                            editor = sharedPreferencessssOP.edit();
                            editor.putString("formno", datasOP.get(0).getProjectNo());
                            editor.putString("getdate", datasOP.get(0).getCurrentdate());
                            editor.putString("empid", empIDS);
                            editor.commit();

                                startService(intent2);


                        }
                    }
                }

                /*
................................stopservice..................................................*/
                finish();
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void GetAttendanceSettingDataServer() {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        //pDialog.show();
        JsonArrayRequest jsObjRequest = new JsonArrayRequest (com.android.volley.Request.Method.POST, AppConstraint.ATTENDANCEREMOTE_SETTING, JsonParameterSendServer(),
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        pDialog.dismiss();
                        parseResult(response);
                        Log.e("getattendanceSetting", response + "");
                        //   Toast.makeText(LockScreenActivity.this, "Get Attendance Setting...", Toast.LENGTH_LONG).show();
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("getattendanceSettingerror", error + "");
                        GetAttendanceSettingDataServer();
                        pDialog.dismiss();
                        Toast.makeText(LockScreenActivity.this, "Getting Failed...", Toast.LENGTH_LONG).show();

                    }
                }) {

        };

        AppSingleton.getInstance(this).addToRequestQueue(jsObjRequest, null);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void parseResult(JSONArray response) {
//[{"Lat":20,"Log":50,"RadialDistance":"200","LocationType":"Fixed"}]
        try {

            JSONArray jsonArray = new JSONArray(response.toString());
            JSONObject obj = jsonArray.getJSONObject(0);
            Lat = obj.getString("Lat");
            Logi = obj.getString("Log");
            RadialDistance = obj.getString("RadialDistance");
            LocationType = obj.getString("LocationType");

            if(db.getAttendance_settingData().size() >0) {
                db.deleteAttendancesettingData();
            }
            db.insertAttendanceSettingData(new AttendanceSettingData(Lat,Logi,RadialDistance,LocationType));
            Log.e("attendance setting", response + "");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("attendance setting parse error", response + "");
        }

    }

    private JSONObject JsonParameterSendServer() {

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

        JSONArray jsonArrayParameter = new JSONArray();
        jsonArrayParameter.put(empIDS);
        try {

            jsonObject.put("DatabaseName", "TNS_HR");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");
            jsonObject.put("spName", "GetAttendanceSettings");
            jsonObject.put("ParameterList", jsonArrayParameter);

            Log.i("Json Param",jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "permission not Granted", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


    private void addAutoStartup() {
        try {
            Intent intent = new Intent();
            String manufacturer = android.os.Build.MANUFACTURER;
            if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
            } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
            } else if ("Letv".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
            } else if ("Honor".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
            }
            List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if  (list.size() > 0) {
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.e("exc" , String.valueOf(e));
        }
    }

    private void checkGpsStatus(){
        final Runnable r = new Runnable() {
            public void run() {
                final LocationManager manager = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE );
                String h = DateFormat.format("MM-dd-yyyy-h-mmssaa", System.currentTimeMillis()).toString();

                if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                    //  Toast.makeText(getApplicationContext(), "GPS is disable!", Toast.LENGTH_LONG).show();
                    db.insertGPSCheckStatusData(new GPSCheckStatusData(h, "0"));
                    Log.v("currenttime",h);
                }
                else {
                    Log.v("currenttime",h);
                    db.insertGPSCheckStatusData(new GPSCheckStatusData(h,"1"));
                    //  Toast.makeText(getApplicationContext(), "GPS is Enable!", Toast.LENGTH_LONG).show();
                }

                handler.postDelayed(this, 5000);
            }
        };

        handler.postDelayed(r, 5000);
    }


}


