package com.tns.espapp.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateFormat;
import android.util.Log;

import com.tns.espapp.AppConstraint;
import com.tns.espapp.HTTPPostRequestMethod;
import com.tns.espapp.Utility.SharedPreferenceUtils;
import com.tns.espapp.Utility.Utility;
import com.tns.espapp.database.DatabaseHandler;
import com.tns.espapp.database.LatLongData;
import com.tns.espapp.database.OPEntryLatLog;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SendOPEntryLatiLongiServerIntentService extends IntentService {
    private static final ExecutorService pool = Executors.newSingleThreadExecutor();
    private String empid;
    private DatabaseHandler db;
    Handler handler = new Handler(Looper.getMainLooper());
    Context context;


    public SendOPEntryLatiLongiServerIntentService() {
        super("IntentopentrylatlongService");


    }



    @Override
    protected void onHandleIntent(Intent intent) {
        db = new DatabaseHandler(getApplicationContext());
    /*    SharedPreferences sharedPreferences_setid = getApplicationContext().getSharedPreferences("ID", Context.MODE_PRIVATE);
        empid = sharedPreferences_setid.getString("empid", "");*/

        SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance();
        sharedPreferenceUtils.setContext(this);
        empid = sharedPreferenceUtils.getString(AppConstraint.EMPID);

        while (true) {
            List<OPEntryLatLog> latLongDataList = db.getAllOPEntryLatLongStatus();
            int size = latLongDataList.size();
            if (size > 0) {

                OPEntryLatLog latLongData = latLongDataList.get(0);

                String result = HTTPPostRequestMethod.postMethodforESP(AppConstraint.OPENTRYLATLOG, JsonParameterTaxiTrack(latLongData));
                getResult(result, latLongData);

            /*    for (LatLongData latLongData : latLongDataList) {
                      //  int id=  latLongData.getId();
                    // String diste=  latLongData.getTotaldis();



               // adddata  = adddata +latLongData.getTotaldis()+","+ latLongData.getId()+ ":::" + size +"\n";

                  // handler.post(new DisplayCustomToastforService(this, adddata));



                  String result = HTTPPostRequestMethod.postMethodforESP(AppConstraint.TAXITRACKROOT, JsonParameterTaxiTrack(latLongData));
                  getResult(result, latLongData);

                }*/


            } else {
                break;
            }
        }
    }

    private void getResult(String s, OPEntryLatLog latLongData) {

        try {
             JSONObject   jsonObject = new JSONObject(s);
             boolean status = jsonObject.getBoolean("status");
             Log.d("senddata_opentry", status+"");

            if (status == true) {
                latLongData.setLatlong_flag(1);
                db.updateOPEntryLatLong(latLongData.getId(),latLongData.getLatlong_flag());

        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONObject JsonParameterTaxiTrack(OPEntryLatLog opEntryLatLog) {
        String getDate_latlong = "";
        JSONObject jsonObject = new JSONObject();
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MMM-yyyy");

        try {

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy");
            Date dtt = df.parse(opEntryLatLog.getDate());
            Date ds = new Date(dtt.getTime());
            getDate_latlong= dateFormat2.format(ds);
            System.out.println(getDate_latlong);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            String IMEINumber = Utility.getIMEINumber(this);

            JSONArray jsonArrayParameter = new JSONArray();
           // jsonArrayParameter.put(IMEINumber);
            Log.d("imeino_sendlat", IMEINumber);
            jsonObject.put("DatabaseName", "TNS_GPSTracker_demo");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");

            jsonObject.put("FormNo",opEntryLatLog.getFormno());
            jsonObject.put("EmpId",empid);
            jsonObject.put("Lat",opEntryLatLog.getLat());
            jsonObject.put("Log",opEntryLatLog.getLongi());
            jsonObject.put("vDate",getDate_latlong +" "+opEntryLatLog.getCurrent_time_str());
            jsonObject.put("CurrentTimeStart",opEntryLatLog.getCurrent_time_str());
            jsonObject.put("LatLogFlag",opEntryLatLog.getLatlong_flag());
            jsonObject.put("TotalDis",opEntryLatLog.getTotaldis());
            jsonObject.put("Speed",opEntryLatLog.getSpeed());
            jsonObject.put("ImeiNo",IMEINumber);
            jsonObject.put("Status","true");

            //   jsonObject.put("ParameterList", jsonArrayParameter);

            Log.d("sendjson_opentry", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    private void saveTextFile( String s){

        try {
            String     h = DateFormat.format("MM-dd-yyyyy-h-mmssaa", System.currentTimeMillis()).toString();
            // this will create a new name everytime and unique
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
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
