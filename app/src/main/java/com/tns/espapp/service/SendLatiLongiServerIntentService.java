package com.tns.espapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;


import com.tns.espapp.AppConstraint;
import com.tns.espapp.DisplayCustomToastforService;
import com.tns.espapp.HTTPPostRequestMethod;
import com.tns.espapp.Utility.SharedPreferenceUtils;
import com.tns.espapp.Utility.Utility;
import com.tns.espapp.database.DatabaseHandler;
import com.tns.espapp.database.GPSCheckStatusData;
import com.tns.espapp.database.LatLongData;
import com.tns.espapp.database.OPEntryMilestone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SendLatiLongiServerIntentService extends IntentService {

    private static final ExecutorService pool = Executors.newSingleThreadExecutor();
    private String empid;
    private DatabaseHandler db;

    // Handler handler = new Handler(Looper.getMainLooper());
    // Context context;

    public SendLatiLongiServerIntentService() {
        super("IntentService");
    }

     Handler  handler = new Handler();
    @Override
    protected void onHandleIntent(Intent intent) {

        db = new DatabaseHandler(getApplicationContext());
    /*   SharedPreferences sharedPreferences_setid = getApplicationContext().getSharedPreferences("ID", Context.MODE_PRIVATE);
         empid = sharedPreferences_setid.getString("empid", "");*/

        SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance();
        sharedPreferenceUtils.setContext(this);
        empid = sharedPreferenceUtils.getString(AppConstraint.EMPID);
        checkGpsStatus();
        while (true) {

            List<LatLongData> latLongDataList = db.getAllLatLongStatus();
            int size = latLongDataList.size();
            if (size > 0) {

                LatLongData latLongData = latLongDataList.get(0);
                String result = HTTPPostRequestMethod.postMethodforESP(AppConstraint.TAXITRACKROOT, JsonParameterTaxiTrack(latLongData));
                getResult(result, latLongData);

             /*
            for (LatLongData latLongData : latLongDataList) {
                      //  int id=  latLongData.getId();
                    // String diste=  latLongData.getTotaldis();

               // adddata  = adddata +latLongData.getTotaldis()+","+ latLongData.getId()+ ":::" + size +"\n";
                  // handler.post(new DisplayCustomToastforService(this, adddata));

                  String result = HTTPPostRequestMethod.postMethodforESP(AppConstraint.TAXITRACKROOT, JsonParameterTaxiTrack(latLongData));
                  getResult(result, latLongData);
                }
             */

            } else {
                break;
            }
        }

        OPMileStone();

    }

    private void getResult(String s, LatLongData latLongData) {

        try {
            JSONObject   jsonObject = new JSONObject(s);
             int status = jsonObject.getInt("status");
             Log.v("latlog status",status+"");

            if (status == 1)
            {
                latLongData.setLatlong_flag(1);
                db.updateLatLong(latLongData.getId(),latLongData.getFormno(),latLongData.getDate(),latLongData.getLat(),latLongData.getLongi(),latLongData.getLatlong_flag());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONObject JsonParameterTaxiTrack(LatLongData latLongData) {
        String getDate_latlong = "";
        JSONObject jsonObject = new JSONObject();
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MMM-yyyy");
        try {

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy");
            Date dtt = df.parse(latLongData.getDate());
            Date ds = new Date(dtt.getTime());
            getDate_latlong= dateFormat2.format(ds);
            System.out.println(getDate_latlong);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            String IMEINumber = Utility.getIMEINumber(this);
            JSONArray jsonArrayParameter = new JSONArray();
            jsonArrayParameter.put(latLongData.getFormno());
            jsonArrayParameter.put(empid);
            jsonArrayParameter.put(latLongData.getLat());
            jsonArrayParameter.put(latLongData.getLongi());
            jsonArrayParameter.put(getDate_latlong +" "+latLongData.getCurrent_time_str());
            jsonArrayParameter.put(latLongData.getLatlong_flag());
            jsonArrayParameter.put("0");
            jsonArrayParameter.put(latLongData.getId());
            jsonArrayParameter.put(latLongData.getTotaldis());
            jsonArrayParameter.put(latLongData.getSpeed());
            jsonArrayParameter.put(IMEINumber);

            Log.d("imeino_sendlat", IMEINumber);
            jsonObject.put("DatabaseName", "TNS_GPSTracker_demo");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");
            jsonObject.put("spName", "USP_Taxi_Lat_Log");
            jsonObject.put("ParameterList", jsonArrayParameter);

            Log.d("sendlat", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    private void OPMileStone() {
        while (true) {
            List<OPEntryMilestone> opEntryMilestoneList = db.getAllOPEntryMilestoneStatus();
            int size = opEntryMilestoneList.size();
            if (size > 0) {

                OPEntryMilestone opEntryMilestone = opEntryMilestoneList.get(0);
                String result = HTTPPostRequestMethod.postMethodforESP(AppConstraint.OPENTRYMILESTONE, JSonobjMileStoneParameter(opEntryMilestone));
                getMilestoneResult(result, opEntryMilestone);

            } else {
                break;
            }
        }

    }

    private void getMilestoneResult(String s, OPEntryMilestone opEntryMilestone) {

        try {
            JSONObject   jsonObject = new JSONObject(s);
            boolean status = jsonObject.getBoolean("status");

            if (status == true) {
                opEntryMilestone.setMilestone_flag(1);
                db.updateMilestone(opEntryMilestone.getId(),opEntryMilestone.getMilestone_flag());

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONObject JSonobjMileStoneParameter(OPEntryMilestone opEntryMilestone) {
        String getDate = "";
        JSONObject jsonObject = new JSONObject();
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MMM-yyyy");
        try {

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy");
            Date dtt = df.parse(opEntryMilestone.getDate());
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
            jsonObject.put("ProjectNo", opEntryMilestone.getFormno());
            jsonObject.put("vDate",  getDate);
            jsonObject.put("Lat",  opEntryMilestone.getLat());
            jsonObject.put("Log",  opEntryMilestone.getLongi());
            jsonObject.put("Status", opEntryMilestone.getMilestone_flag()+"");

            Log.v("milestone json",jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void checkGpsStatus(){

         Runnable r = new Runnable() {
            public void run() {
                final LocationManager manager = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE );
                String h = DateFormat.format("MM-dd-yyyy-h-mm:ss:aa", System.currentTimeMillis()).toString();

                if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                    // Toast.makeText(getApplicationContext(), "GPS is disable!", Toast.LENGTH_LONG).show();
                    db.insertGPSCheckStatusData(new GPSCheckStatusData(h, "Off"));
                    Log.v("currenttime",h);
                }
                else {
                    Log.v("currenttime",h);
                    db.insertGPSCheckStatusData(new GPSCheckStatusData(h,"On"));
                    //  Toast.makeText(getApplicationContext(), "GPS is Enable!", Toast.LENGTH_LONG).show();
                }

                handler.postDelayed(this, 5000);
            }
        };

        handler.postDelayed(r, 5000);
    }

    private void saveTextFile( String s){
        try {
            String     h = DateFormat.format("MM-dd-yyyy-h-mmssaa", System.currentTimeMillis()).toString();
            // this will create a new name everytime and unique
            String destinationpath = Environment.getExternalStorageDirectory().toString();
            File root = new File(destinationpath, "Notes");
            // if external memory exists and folder with name Notes
            if (!root.exists()) {
                root.mkdirs(); // this will create folder.
            }
            File filepath = new File(root, h + ".txt");  // file path to save
            FileWriter writer = new FileWriter(filepath);
            writer.append(s);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }


}
