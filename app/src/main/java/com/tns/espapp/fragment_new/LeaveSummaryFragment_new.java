package com.tns.espapp.fragment_new;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tns.espapp.AppConstraint;
import com.tns.espapp.R;
import com.tns.espapp.Utility.AppSingleton;
import com.tns.espapp.Utility.SharedPreferenceUtils;
import com.tns.espapp.database.BillInfoData;
import com.tns.espapp.database.FinanceYearData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeaveSummaryFragment_new extends Fragment {
    private String year1;
    private String year2;
    private Spinner spi_select_year;
    private TextView tv_setyear, tv_leavesummary_startdate,
            tv_leavesummary_openingleave,
            tv_leavesummary_totalavailableleave,
            tv_leavesummary_jan, tv_leavesummary_feb, tv_leavesummary_march, tv_leavesummary_apr, tv_leavesummary_may, tv_leavesummary_jun, tv_leavesummary_july, tv_leavesummary_aug, tv_leavesummary_sep, tv_leavesummary_oct, tv_leavesummary_nov, tv_leavesummary_dec,
            tv_leavesummary_leaveavail,
            tv_leavesummary_totalleavededucted,
            tv_leavesummary_totalleavepermitted,
            tv_leavesummary_balance, tv_leavesummary_balance_el, tv_leavesummary_balance_cl;
    private ArrayAdapter adapter;
    ArrayList<String> selectyear_list = new ArrayList<>();
    private String empid;

    public LeaveSummaryFragment_new() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_leave_summary_fragment_new, container, false);
        findIds(v);
        final SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance();
        sharedPreferenceUtils.setContext(getActivity());
        empid = sharedPreferenceUtils.getString(AppConstraint.EMPID);
        createJsonRequestYear(empid);

        Calendar now = Calendar.getInstance();   // Gets the current date and time
        int curryear = now.get(Calendar.YEAR);
        year2 = String.valueOf(curryear);

        createJsonRequestLeaveInfoData(empid);

        return v;
    }

    private void findIds(View v) {
        spi_select_year = (Spinner) v.findViewById(R.id.spi_leavesummary_selectyear);
        tv_setyear = (TextView) v.findViewById(R.id.tv_leavesummary_setyear);

        tv_leavesummary_startdate = (TextView) v.findViewById(R.id.tv_leavesummary_startdate);
        tv_leavesummary_openingleave = (TextView) v.findViewById(R.id.tv_leavesummary_openingleave);
        tv_leavesummary_totalavailableleave = (TextView) v.findViewById(R.id.tv_leavesummary_totalavailableleave);
        tv_leavesummary_jan = (TextView) v.findViewById(R.id.tv_leavesummary_jan);
        tv_leavesummary_feb = (TextView) v.findViewById(R.id.tv_leavesummary_feb);
        tv_leavesummary_march = (TextView) v.findViewById(R.id.tv_leavesummary_march);
        tv_leavesummary_apr = (TextView) v.findViewById(R.id.tv_leavesummary_apr);
        tv_leavesummary_may = (TextView) v.findViewById(R.id.tv_leavesummary_may);
        tv_leavesummary_jun = (TextView) v.findViewById(R.id.tv_leavesummary_jun);
        tv_leavesummary_july = (TextView) v.findViewById(R.id.tv_leavesummary_july);
        tv_leavesummary_aug = (TextView) v.findViewById(R.id.tv_leavesummary_aug);
        tv_leavesummary_sep = (TextView) v.findViewById(R.id.tv_leavesummary_sep);
        tv_leavesummary_oct = (TextView) v.findViewById(R.id.tv_leavesummary_oct);
        tv_leavesummary_nov = (TextView) v.findViewById(R.id.tv_leavesummary_nov);
        tv_leavesummary_dec = (TextView) v.findViewById(R.id.tv_leavesummary_dec);
        tv_leavesummary_leaveavail = (TextView) v.findViewById(R.id.tv_leavesummary_leaveavail);
        tv_leavesummary_totalleavepermitted = (TextView) v.findViewById(R.id.tv_leavesummary_totalleavepermitted);
        tv_leavesummary_totalleavededucted = (TextView) v.findViewById(R.id.tv_leavesummary_totalleavededucted);
        tv_leavesummary_balance = (TextView) v.findViewById(R.id.tv_leavesummary_balance);
        tv_leavesummary_balance_el = (TextView) v.findViewById(R.id.tv_leavesummary_balance_el);
        tv_leavesummary_balance_cl = (TextView) v.findViewById(R.id.tv_leavesummary_balance_cl);




        spi_select_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year2 = (String) parent.getItemAtPosition(position);
             /*   String[] separated = sItem.split("-");
                year1=  separated[0];
                year2=  separated[1]*/
                ;


                createJsonRequestLeaveInfoData(empid);
                tv_setyear.setText(year2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void createJsonRequestYear(String empId) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        String URL = AppConstraint.FINANCIALYEARFORDRP;


        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                URL, jsonParamYear(empId),
                new com.android.volley.Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        handleResponseYear(response.toString());
                        Log.v("responce", response.toString());
                        pDialog.hide();

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
            }

        });

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjReq, null);
    }

    private JSONObject jsonParamYear(String empId) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        // jsonArray.put("");

        try {
            jsonObject.put("DatabaseName", "TNS_HR");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");


            jsonObject.put("spName", "GetYearForDrp");

            jsonObject.put("ParameterList", jsonArray);

            Log.v("jsonparam", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

    private void handleResponseYear(String results) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.create();

        if (results.equals("[]")) {
            return;
        }


        FinanceYearData billdata[] = gson.fromJson(results, FinanceYearData[].class);
        for (FinanceYearData data : billdata) {

           /* String[] separated = data.getFinyr().split("-");
            year1 = separated[0];
            year2 = separated[1];*/
           year2 = data.getFinyr();
            selectyear_list.add(year2);
            adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, selectyear_list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spi_select_year.setAdapter(adapter);


            //Toast.makeText(getActivity(),data.toString(),Toast.LENGTH_LONG).show(); ;
        }

    }





    private void createJsonRequestLeaveInfoData(String empId) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        String URL = AppConstraint.LEAVEINFO;


        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                URL, jsonParamLeaveData(empId),
                new com.android.volley.Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        handleResponseLeaveData(response.toString());
                        Log.v("responce", response.toString());
                        pDialog.hide();

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // createJsonRequestLeaveInfoData(empid);
                pDialog.hide();
            }

        });

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjReq, null);
    }

    private JSONObject jsonParamLeaveData(String empId) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        // jsonArray.put("");

        try {
            jsonObject.put("DatabaseName", "TNS_HR");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");


            jsonArray.put(empId);
            jsonArray.put(year2);
            jsonObject.put("spName", "GetEmployeeLeaveInfo");

            jsonObject.put("ParameterList", jsonArray);

            Log.v("jsonparam", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

    private void handleResponseLeaveData(String results) {


        if (results.equals("[]")) {
            return;
        }
        try {
            JSONArray jsonArray = new JSONArray(results);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = null;
                jsonObject = jsonArray.getJSONObject(i);
                tv_leavesummary_startdate.setText(jsonObject.getString("LvStartDate"));
                tv_leavesummary_openingleave.setText(jsonObject.getString("LvOpening"));
                tv_leavesummary_totalavailableleave.setText(jsonObject.getString("LvAvailable"));

                if (jsonObject.getString("1").equals("null")) {
                    tv_leavesummary_jan.setText(0+"");
                } else {
                    tv_leavesummary_jan.setText(jsonObject.getString("1"));
                }

                if (jsonObject.getString("2").equals("null")) {
                    tv_leavesummary_feb.setText(0+"");
                } else {
                    tv_leavesummary_feb.setText(jsonObject.getString("2"));
                }
                if (jsonObject.getString("3").equals("null")) {
                    tv_leavesummary_march.setText(0+"");
                } else {
                    tv_leavesummary_march.setText(jsonObject.getString("3"));
                }
                if (jsonObject.getString("4").equals("null")) {
                    tv_leavesummary_apr.setText(0+"");
                } else {
                    tv_leavesummary_apr.setText(jsonObject.getString("4"));
                }
                if (jsonObject.getString("5").equals("null")) {
                    tv_leavesummary_may.setText(0+"");
                } else {
                    tv_leavesummary_may.setText(jsonObject.getString("5"));
                }
                if (jsonObject.getString("6").equals("null")) {
                    tv_leavesummary_jun.setText(0+"");
                } else {
                    tv_leavesummary_jun.setText(jsonObject.getString("6"));
                }
                if (jsonObject.getString("7").equals("null")) {
                    tv_leavesummary_july.setText(0+"");
                } else {
                    tv_leavesummary_july.setText(jsonObject.getString("7"));
                }
                if (jsonObject.getString("8").equals("null")) {
                    tv_leavesummary_aug.setText(0+"");
                } else {
                    tv_leavesummary_aug.setText(jsonObject.getString("8"));
                }
                if (jsonObject.getString("9").equals("null")) {
                    tv_leavesummary_sep.setText(0+"");
                } else {
                    tv_leavesummary_sep.setText(jsonObject.getString("9"));
                }
                if (jsonObject.getString("10").equals("null")) {
                    tv_leavesummary_oct.setText(0+"");
                } else {
                    tv_leavesummary_oct.setText(jsonObject.getString("10"));
                }
                if (jsonObject.getString("11").equals("null")) {
                    tv_leavesummary_nov.setText(0+"");
                } else {
                    tv_leavesummary_nov.setText(jsonObject.getString("11"));
                }
                if (jsonObject.getString("12").equals("null")) {
                    tv_leavesummary_dec.setText(0+"");
                } else {
                    tv_leavesummary_dec.setText(jsonObject.getString("12"));
                }
                if (jsonObject.getString("TotLvDeducted").equals("null")) {
                    tv_leavesummary_totalleavededucted.setText(0+"");
                } else {
                    tv_leavesummary_totalleavededucted.setText(jsonObject.getString("TotLvDeducted"));
                }
                if (jsonObject.getString("TotLvPermitted").equals("null")) {
                    tv_leavesummary_totalleavepermitted.setText(0+"");
                } else {
                    tv_leavesummary_totalleavepermitted.setText(jsonObject.getString("TotLvPermitted"));
                }
                if (jsonObject.getString("BalanceLeave").equals("null")) {
                    tv_leavesummary_balance.setText(0+"");
                } else {
                    tv_leavesummary_balance.setText(jsonObject.getString("BalanceLeave"));
                }
                if (jsonObject.getString("BalanceEL").equals("null")) {
                    tv_leavesummary_balance_el.setText(0+"");
                } else {
                    tv_leavesummary_balance_el.setText(jsonObject.getString("BalanceEL"));
                }
                if (jsonObject.getString("BalanceCL").equals("null")) {
                    tv_leavesummary_balance_cl.setText(0+"");
                } else {
                    tv_leavesummary_balance_cl.setText(jsonObject.getString("BalanceCL"));
                }


                tv_leavesummary_leaveavail.setText(jsonObject.getString("TotalLvAvailed"));
              /*  tv_leavesummary_totalleavededucted.setText(jsonObject.getString("TotLvDeducted"));
                tv_leavesummary_totalleavepermitted.setText(jsonObject.getString("TotLvPermitted"));
                tv_leavesummary_balance.setText(jsonObject.getString("BalanceLeave"));
                tv_leavesummary_balance_el.setText(jsonObject.getString("BalanceEL"));
                tv_leavesummary_balance_cl.setText(jsonObject.getString("BalanceCL"));*/
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

//[{"EmpId":"16865","deptCode":"TS","LvStartDate":"2017-01-01T00:00:00","LvOpening":0.73,"LvAvailable":20.25,"1":null,"2":1,"3":5,"4":null,"5":3,"6":null,"7":1,"8":0.5,"9":3,"10":null,"11":null,"12":null,"TotLvDeducted":null,"TotLvPermitted":null,"TotalLvAvailed":13.5,"ActLvAvailed":13.5,"TotalELAvailed":4.5,"TotalCLAvailed":9,"TotalELCredit":11.25,"TotalCLCredit":9,"BalanceLeave":7.48,"BalanceEL":7.48,"BalanceCL":0,"bUpdated":true}]


}




