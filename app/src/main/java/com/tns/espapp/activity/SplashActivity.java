package com.tns.espapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

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
import com.tns.espapp.database.NotificationData;
import com.tns.espapp.database.SettingData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {
    private DatabaseHandler db;
    private SharedPreferenceUtils sharedPreferences;
    private boolean isRegistered;
    private boolean isApproved;
    private LinearLayout waitForApprovalLayout_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHandler(this);
        setContentView(R.layout.activity_splash);
        waitForApprovalLayout_splash = (LinearLayout)findViewById(R.id.waitForApprovalLayout_splash);

        if (getIntent().getExtras() != null) {

      /*

            Bundle bundle = getIntent().getExtras();

            if (bundle != null) {
                String tittle = bundle.get("tittle") + "";
                String messages = bundle.get("message") + "";
                String image = bundle.get("image") + "";
                db.add_DB_Notification(new NotificationData(tittle, messages, image, 0));
                Set<String> keys = bundle.keySet();

                for (String a : keys) {

                    Log.e("for Bundle", "[" + a + "=" + bundle.get(a) + "]");

                }
            }

         */

        }

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sharedPreferences = SharedPreferenceUtils.getInstance();
                sharedPreferences.setContext(getApplicationContext());
                isRegistered = sharedPreferences.getBoolean(AppConstraint.REGISTERFLAG);
                isApproved = sharedPreferences.getBoolean(AppConstraint.APPROVEDFLAG);
                if (isRegistered) {
                    if (isApproved) {
                        startActivity(new Intent(getApplicationContext(), LockScreenActivity.class));
                        finish();

                    } else {
                        createApprovalJsonObjectRequest();
                    }


                } else {
                    startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                    finish();

                }


            }
        }, 0000);


    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    private void createApprovalJsonObjectRequest() {
        Log.d("printRegistrationObject", getApprovalJsonObject().toString());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

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

    private void parseApprovedResponse(JSONObject response) {
        try {
            Log.d("ApprovedResponse",response.toString());
            int status = response.getInt("status");
            if(status==1)
            {
                sharedPreferences.putBoolean(AppConstraint.APPROVEDFLAG,true);
                waitForApprovalLayout_splash.setVisibility(View.GONE);
                startActivity(new Intent(getApplicationContext(), LockScreenActivity.class));
                finish();
            }
            else
            {
                sharedPreferences.putBoolean(AppConstraint.APPROVEDFLAG,false);
               waitForApprovalLayout_splash.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}