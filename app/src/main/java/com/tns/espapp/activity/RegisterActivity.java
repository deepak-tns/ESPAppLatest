package com.tns.espapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tns.espapp.AppConstraint;
import com.tns.espapp.R;
import com.tns.espapp.Utility.AppSingleton;
import com.tns.espapp.Utility.SharedPreferenceUtils;
import com.tns.espapp.Utility.Utility;
import com.tns.espapp.database.DatabaseHandler;
import com.tns.espapp.database.SettingData;
import com.tns.espapp.database.TaxiFormData;
import com.tns.espapp.push_notification.MyFirebaseInstanceIDService;
import com.tns.espapp.service.GPSTracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegisterActivity extends AppCompatActivity {
    private EditText employeeIdEditText;
    private EditText employeePasswordEditText;
    private Button verifyBtn;
    private String fireBaseToken;
    private String empId;
    private String empPassword;
    private ProgressDialog pDialog;
    private SharedPreferenceUtils sharedPreferences;
    private TextView registerYourDeviceTextView;
    private TextView waitForApprovalTextView;



    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getLayoutsId();
        getFireBaseToken();
        registerOnClickListener();

    }

    private void getLayoutsId() {
        employeeIdEditText = (EditText) findViewById(R.id.employeeIdEditText);
        employeePasswordEditText = (EditText) findViewById(R.id.employeePasswordEditText);
        verifyBtn = (Button) findViewById(R.id.verifyBtn);
        registerYourDeviceTextView = (TextView) findViewById(R.id.registerYourDeviceTextView);
        waitForApprovalTextView = (TextView) findViewById(R.id.waitForApprovalTextView);
        setFontFamily();
    }


    private String getFireBaseToken() {
        fireBaseToken = MyFirebaseInstanceIDService.SharedSave.getInstance(getApplicationContext()).getDeviceToken();
        Log.v("Token No", fireBaseToken);
        return fireBaseToken;

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
                                createVerifyJsonObject();
                            }
                        } else {
                            Utility.displayMessage(getApplicationContext(), "Network is not available");
                        }

                }


            }
        };
        verifyBtn.setOnClickListener(clickListener);
    }

    private boolean isMandatoryField() {
        employeeIdEditText.setError(null);
        employeePasswordEditText.setError(null);
        empId = employeeIdEditText.getText().toString();
        empPassword = employeePasswordEditText.getText().toString();
        if (TextUtils.isEmpty(empId)) {
            employeeIdEditText.setError(getString(R.string.employeeIdError));
            return false;
        } else if (TextUtils.isEmpty(empPassword)) {
            employeePasswordEditText.setError(getString(R.string.employeePasswordError));
            return false;
        }
        return true;


    }

    private Map<String, String> getVerifyParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("DatabaseName", "TNS_HR");
        params.put("ServerName", "bkp-server");
        params.put("UserId", "sanjay");
        params.put("Password", "tnssoft");
        params.put("spName", "USP_login");
        params.put("loginID", empId);
        params.put("loginPassword", empPassword);
        return params;

    }

    private void createVerifyJsonObject() {
        JSONObject parameters = new JSONObject(getVerifyParams());
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                AppConstraint.ESP_LOGIN, parameters,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.v("reg",response.toString());
                        parseVerifyResponse(response);


                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("reg",error.toString());
                pDialog.hide();
            }

        });

        AppSingleton.getInstance(this).addToRequestQueue(jsonObjReq, null);
    }

    private JSONObject getRegistrationJsonObject() {

        JSONObject jsonObject = new JSONObject();
        String IMEINumber = Utility.getIMEINumber(this);
        String modelNumber = Utility.getModelNumber();
        String deviceName = Utility.getDeviceName();
        String deviceModel = deviceName + modelNumber;
        String androidOsVersion = Utility.getDeviceVersion() + "";
        String fireBaseToken = getFireBaseToken();
        Log.d("fireBaseToken", fireBaseToken);
        try {
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(androidOsVersion);
            jsonArray.put(empId);
            jsonArray.put(fireBaseToken);
            jsonArray.put(IMEINumber);
            jsonArray.put(deviceModel);
            jsonObject.put("DatabaseName", "TNS_HR");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");
            jsonObject.put("spName", "USP_EspMobileRegistration");
            jsonObject.put("ParameterList", jsonArray);

        }
         catch (JSONException e)
        {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void createRegistrationJsonObject() {
        Log.d("printRegistrationObject", getRegistrationJsonObject().toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                AppConstraint.TAXITRACKROOT, getRegistrationJsonObject(),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("reg",response.toString());

                        parseRegistrationResponse(response);


                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("reg",error.toString());
                pDialog.hide();
            }

        });


        AppSingleton.getInstance(this).addToRequestQueue(jsonObjReq, null);
    }

    private void parseVerifyResponse(JSONObject response) {
        try {
            int status = response.getInt("status");
            if (status == 1) {
                createRegistrationJsonObject();

            } else {
                Utility.displayMessage(this, "Wrong Credentials");
                pDialog.hide();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void parseRegistrationResponse(JSONObject response) {
        Log.d("RegistrationResponse", response.toString());
        try {
            int status = response.getInt("status");
            sharedPreferences = SharedPreferenceUtils.getInstance();
            sharedPreferences.setContext(getApplicationContext());
            if (status == 1) {

                sharedPreferences.putBoolean(AppConstraint.REGISTERFLAG, true);
                sharedPreferences.putString(AppConstraint.EMPID, empId);
                sharedPreferences.putString(AppConstraint.PASSWORD, empPassword);
                createApprovalJsonObjectRequest();


            } else
                {

                sharedPreferences.putBoolean(AppConstraint.REGISTERFLAG, false);
                Utility.displayMessage(this, "Your Registration was not Successful");
                pDialog.hide();


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void createApprovalJsonObjectRequest() {
        Log.d("printApproveJson", getApprovalJsonObject().toString());


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                AppConstraint.TAXITRACKROOT, getApprovalJsonObject(),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        parseApprovedResponse(response);
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

    private JSONObject getApprovalJsonObject() {

        JSONObject jsonObject = new JSONObject();
        String IMEINumber = Utility.getIMEINumber(this);

        try {

            JSONArray jsonArray = new JSONArray();
            jsonArray.put(IMEINumber);
            jsonObject.put("DatabaseName", "TNS_HR");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");
            jsonObject.put("spName", "SP_mobileaproved");
            jsonObject.put("ParameterList", jsonArray);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

    private void setFontFamily() {
        Typeface face = Typeface.createFromAsset(getAssets(),
                "arial.ttf");

        registerYourDeviceTextView.setTypeface(face);
        waitForApprovalTextView.setTypeface(face);
        employeeIdEditText.setTypeface(face);
        employeePasswordEditText.setTypeface(face);
        verifyBtn.setTypeface(face);
    }

    private void parseApprovedResponse(JSONObject response) {
        Log.d("ApprovedResponse", response.toString());
        try {
            int status = response.getInt("status");
            if (status == 1) {
                sharedPreferences.putBoolean(AppConstraint.APPROVEDFLAG, true);
                startActivity(new Intent(getApplicationContext(), LockScreenActivity.class));
                finish();
                //return;
            } else {
                sharedPreferences.putBoolean(AppConstraint.APPROVEDFLAG, false);
                startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                finish();
                //  Utility.displayMessage(this, "You are Registered Successfully Please Wait for Approval");

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}