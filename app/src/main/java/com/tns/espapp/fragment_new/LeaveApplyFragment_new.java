package com.tns.espapp.fragment_new;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tns.espapp.AppConstraint;
import com.tns.espapp.R;
import com.tns.espapp.Utility.AppSingleton;
import com.tns.espapp.Utility.SharedPreferenceUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeaveApplyFragment_new extends Fragment {

    private int day;
    private int month;
    private int year;
    private Calendar cal;

    private String current_year;

    private SimpleDateFormat dateFormatter;
    private EditText tv_leaveaply_formto, tv_leaveaply_todate;
    private Spinner spi_leavefor;
    private Button buttonsave;
    private TextView tv_total_days,tv_leave_type,tv_leave_apply_totalavail,tv_leave_apply_contactno,tv_leave_apply_email,reason_leave;
    String  empid ,leavefor;


    String[] leavefor_list = { "Full Day", "First Half", "Second Half" };

    public LeaveApplyFragment_new() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_leave_apply_fragment_new, container, false);
        findIds(v);
        final SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance();
        sharedPreferenceUtils.setContext(getActivity());
         empid = sharedPreferenceUtils.getString(AppConstraint.EMPID);
        createJsonRequest(empid);


        tv_leaveaply_formto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setdate(tv_leaveaply_formto,"");
            }
        });
        tv_leaveaply_todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setdate(tv_leaveaply_todate,"Todate");
            }
        });


        ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,leavefor_list);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spi_leavefor.setAdapter(aa);
        spi_leavefor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                leavefor = (String) parent.getItemAtPosition(position);
                //  Toast.makeText(getActivity(),leavefor_list[position] ,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        return v;
    }

    private void findIds(View v){
        tv_leaveaply_formto =(EditText) v.findViewById(R.id.tv_leaveaply_formto);
        tv_leaveaply_todate =(EditText) v.findViewById(R.id.tv_leaveaply_todate);
        spi_leavefor=(Spinner)v.findViewById(R.id.spi_leaveapply_leavefor);
        tv_leave_type =(TextView) v.findViewById(R.id.tv_leave_type);
        reason_leave =(TextView) v.findViewById(R.id.reason_leave);
        tv_total_days =(TextView)v.findViewById(R.id.tv_total_days);
        buttonsave =(Button)v.findViewById(R.id.btn_leave_apply);
        tv_leave_apply_totalavail = (TextView) v.findViewById(R.id.tv_leave_apply_totalavai);
        tv_leave_apply_contactno = (TextView) v.findViewById(R.id.tv_leave_apply_contactno);
        tv_leave_apply_email = (TextView) v.findViewById(R.id.tv_leave_apply_email);

        Calendar now = Calendar.getInstance();   // Gets the current date and time
        int curryear = now.get(Calendar.YEAR);
        current_year = String.valueOf(curryear);

        buttonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(tv_leaveaply_formto.getText().toString())) {
                    tv_leaveaply_formto.setError("Please Select FromDate");

                    return;
                } else if (TextUtils.isEmpty(tv_leaveaply_todate.getText())) {
                    tv_leaveaply_todate.setError("Please Select ToDate");
                    return;

                } else if (TextUtils.isEmpty(reason_leave.getText())) {
                    reason_leave.setError("Please Enter Leave Reason");
                    reason_leave.requestFocus();
                    return;

                }else {
                    createJsonRequestPost(empid);
                }
            }
        });

    }



    private void setdate(final EditText edt, final String todate) {

        cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        final DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

        //   addmeeting_edt_selectdate.setText(dayOfMonth + "-" + dateFormatter.format (monthOfYear + 1) + "-" + year);

                        cal.set(year, monthOfYear, dayOfMonth);
                        edt.setText(  dateFormatter.format(cal.getTime()));


                        if(!tv_leaveaply_formto.getText().toString().equals("")&& todate. equalsIgnoreCase("TODATE")){
                            substractDate(tv_leaveaply_formto,tv_leaveaply_todate);
                           //

                        }else{
                            Toast.makeText(getActivity(),"Please Select From Date",Toast.LENGTH_LONG).show();
                            tv_leaveaply_todate.getText().clear();
                            tv_total_days.setText("");
                        }

                    }
                }, year, month, day);

    /*    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date d = null;
        try {
            d = sdf.parse("01/01/2016");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dpd.getDatePicker().setMinDate(d.getTime());
*/

    if(todate. equalsIgnoreCase("TODATE")){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Date d = new Date();

        try {
            if(!tv_leaveaply_formto.getText().toString().equals("")){
                d = sdf.parse(tv_leaveaply_formto.getText().toString());
            }else{
                Date da = new Date();
                cal.setTime(da);
                cal.add(Calendar.MONTH, -12);
                da = cal.getTime();
                dpd.getDatePicker().setMinDate(da.getTime());
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        dpd.getDatePicker().setMinDate(d.getTime());

    }else
        {
        Date da = new Date();
        cal.setTime(da);
        cal.add(Calendar.MONTH, -12);
        da = cal.getTime();

        dpd.getDatePicker().setMinDate(da.getTime());
    }

        Calendar calendar = Calendar.getInstance();//get the current day
        calendar.add(Calendar.MONTH,12);
        long afterThreeMonthsinMilli=calendar.getTimeInMillis();
        dpd.getDatePicker().setMaxDate(afterThreeMonthsinMilli);
        dpd.show();

    }


    private void substractDate3month(){
        cal = Calendar.getInstance();
        System.out.println("Current date : " + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE) + "-" + cal.get(Calendar.YEAR));
        cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -3);
        System.out.println("date before 5 months : " + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE) + "-" + cal.get(Calendar.YEAR));
    }

    private void substractDate(TextView fromdate, TextView todate){

        dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        // calculating the difference b/w startDate and endDate
        
        String startDate = fromdate.getText().toString();
        String endDate = todate.getText().toString();
        Date  date1 = null;
        Date  date2 = null;
        try {
            date1 = dateFormatter.parse(startDate);
            Log.v("fromDate",date1+"");
            date2 = dateFormatter.parse(endDate);
            Log.v("toDate",date2+"");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long getDiff = date2.getTime() - date1.getTime();
        // using TimeUnit class from java.util.concurrent package
        long getDaysDiff = TimeUnit.MILLISECONDS.toDays(getDiff);
        tv_total_days.setText(1+getDaysDiff+"");
        Log.v("date getDaysDiff",getDaysDiff+"");

    }


    private void createJsonRequest(String empId) {


        String URL = "";

            URL = AppConstraint.LEAVEFROMDATA;


        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                URL, jsonParam(empId),
                new com.android.volley.Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        handleResponse(response.toString());
                        Log.v("responce", response.toString());


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        });

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjReq, null);
    }

    private JSONObject jsonParam(String empId) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        // jsonArray.put("");
        try {
            jsonObject.put("DatabaseName", "TNS_HR");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");
            jsonObject.put("spName", "GetLeaveFormData");
            jsonArray.put(empId);
            jsonObject.put("ParameterList", jsonArray);

            Log.v("jsonparam", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

    private void handleResponse(String results) {
        if(results.equals("[]")){

            return;
        }
        try {
            JSONArray jsonArray = new JSONArray(results);
            JSONArray jsonArray0 =jsonArray.getJSONArray(0);
            JSONArray jsonArray1 =jsonArray.getJSONArray(1);

            JSONObject jsonObject = jsonArray0.getJSONObject(0);
            String LeaveAvailed = jsonObject.getString("LeaveAvailed");

            JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
            String MobileNo = jsonObject1.getString("MobileNo");
            String EmailId = jsonObject1.getString("EmailId");
            Log.v("ResponseData",LeaveAvailed +","+MobileNo+","+EmailId);

            tv_leave_apply_totalavail.setText(LeaveAvailed);
            tv_leave_apply_contactno.setText(MobileNo);
            tv_leave_apply_email.setText(EmailId);

        } catch (JSONException e) {
            Log.v("ResponseData",e.getMessage());
            e.printStackTrace();
        }


    }


    private void createJsonRequestPost(String empId) {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        String URL = "";
        URL = AppConstraint.LEAVEFROM;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                URL, jsonParamPost(empId),
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        //tv_leave_apply_totalavail.getText().toString());

                        handleResponsePost(response.toString());
                        Log.v("responce", response.toString());
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                pDialog.dismiss();
                if(error.toString().contains("Value Leave Successfully Apply")) {
                    tv_leaveaply_formto.getText().clear();
                    tv_leaveaply_todate.getText().clear();
                    tv_total_days.setText("");
                    reason_leave.setText("");
                    Toast.makeText(getActivity(),"Applied Successfully",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(),"Server Error",Toast.LENGTH_SHORT).show();
                }

                Log.v("responce error", error.toString());
            }

        });

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjReq, null);
    }

    private JSONObject jsonParamPost(String empId)
    {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            jsonObject.put("DatabaseName", "TNS_HR");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");
            jsonObject.put("spName", "SP_InsertLeaveTransaction");
            jsonArray.put(empId);
            jsonArray.put("CL");
            jsonArray.put(tv_leave_apply_totalavail.getText().toString());
            jsonArray.put(tv_leaveaply_formto.getText().toString());
            jsonArray.put(tv_leaveaply_todate.getText().toString());
            jsonArray.put(reason_leave.getText().toString());
            jsonArray.put(tv_leave_apply_email.getText().toString());
            jsonArray.put(tv_leave_apply_contactno.getText().toString());
            jsonArray.put(current_year);
            jsonArray.put(leavefor);

         //   "parameterList":[17281,"CL",10.5,"29-oct-2017","29-oct-2017","Personal","test@gmail.com","123198","2017","Full Day"]}


        jsonObject.put("ParameterList", jsonArray);

            Log.v("jsonparam", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

    private void handleResponsePost(String results) {

        if(results.equals("[]")){
            return;
        }

        try
        {
            JSONObject jsonObject = new JSONObject(results);
            tv_leaveaply_formto.setText("");
            tv_leaveaply_todate.setText("");
             reason_leave.setText("");
            Log.d("result",jsonObject.toString());
        }
        catch (JSONException e) {
            Log.v("ResponseData",e.getMessage());
            e.printStackTrace();
        }
    }



}
