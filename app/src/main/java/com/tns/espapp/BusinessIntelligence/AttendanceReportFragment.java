package com.tns.espapp.BusinessIntelligence;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;


import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.StackedValueFormatter;

import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tns.espapp.AppConstraint;
import com.tns.espapp.R;
import com.tns.espapp.Utility.AppSingleton;
import com.tns.espapp.database.FinanceYearData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceReportFragment extends Fragment implements OnChartValueSelectedListener ,View.OnClickListener {
    private ArrayAdapter adapter;

    private static final int EXTRACOLOR[] = new int[]{R.color.green, R.color.red, R.color.lite_orange, R.color.colorFAB2, R.color.sky, R.color.orange, R.color.limegreen};
    private String year2;
    private ArrayList<String> selectyear_list;
    private List selectMonthList;

    private String selectYear ="";
    private String selectMonth;
    private String selectdays;
    private Button btn_search;
    PieChart pieChart;
    private  View vv;
    int curryear;
    public AttendanceReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_attendance_report, container, false);
        btn_search =(Button)v.findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);
        vv = v;

        selectYearData(v);
        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.


        return v;

    }
private void settingPieChart(AttendanceReportPiechart reportPiechart){
  LinearLayout li_custom_legend2 =(LinearLayout) vv.findViewById(R.id.li_custom_legend2);
  LinearLayout li_custom_legend1 =(LinearLayout) vv.findViewById(R.id.li_custom_legend1);
    pieChart = (PieChart)vv.findViewById(R.id.piechart);

    //  pieChart.setUsePercentValues(true);
    List<PieEntry> yvalues = new ArrayList<PieEntry>();
    yvalues.add(new PieEntry( Float.valueOf(reportPiechart.getAbsent()),"Absent(A)"));
    yvalues.add(new PieEntry( Float.valueOf(reportPiechart.getAbsentAB()),"Absent(AB)"));
    yvalues.add(new PieEntry( Float.valueOf(reportPiechart.getAbsentLWP()),"AbsentLWP"));
    yvalues.add(new PieEntry( Float.valueOf(reportPiechart.getLoaded()),"Loaded"));
    yvalues.add(new PieEntry( Float.valueOf(reportPiechart.getIdle()),"Idle"));
    yvalues.add(new PieEntry( Float.valueOf(reportPiechart.getUnloaded()),"Unloaded"));
    yvalues.add(new PieEntry( Float.valueOf(reportPiechart.getTransfer()),"Transfer"));

    PieDataSet dataSet = new PieDataSet(yvalues, "Attendance Results");

  /*  ArrayList<String> xVals = new ArrayList<String>();
    xVals.add("Absent(A)");
    xVals.add("Absent(AB)");
    xVals.add("AbsentLWP");
    xVals.add("Loaded");
    xVals.add("Idle");
    xVals.add("Unloaded");
    xVals.add("Transfer");
*/
    PieData data = new PieData(dataSet);
    // In Percentage
   // data.setValueFormatter(new MyValueFormatter());
     data.setValueFormatter(new DefaultValueFormatter(0));
    // data.setValueFormatter(new PercentFormatter());

/*    Legend legend = pieChart.getLegend();
    legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
    legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
    legend.setOrientation(Legend.LegendOrientation.VERTICAL);
    legend.setDrawInside(false);*/

    pieChart.setData(data);
    li_custom_legend1.setVisibility(View.VISIBLE);
    li_custom_legend2.setVisibility(View.VISIBLE);
  //  pieChart.setDescription("Attendance Data");
    pieChart.getDescription().setText("Attendance Data");
    pieChart.setDrawHoleEnabled(true);
    pieChart.setTransparentCircleRadius(10f);
    pieChart.getLegend().setEnabled(false);
    // pieChart.setDrawCenterText(false);
    // pieChart.setCenterText("Data");

   // pieChart.setDrawSliceText(false);
    //dataSet.setDrawValues(false);

   // pieChart.setEntryLabelColor(Color.DKGRAY);
     pieChart.setEntryLabelTextSize(0f);

    dataSet.setValueLinePart1OffsetPercentage(70.f);
    dataSet.setValueLinePart1Length(.9f);
    dataSet.setValueLinePart2Length(.1f);
    dataSet.setValueTextColor(Color.DKGRAY);
    dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
    dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

    pieChart.setHoleRadius(10f);
    dataSet.setColors(EXTRACOLOR,getActivity());
    data.setValueTextSize(10f);
    data.setValueTextColor(Color.DKGRAY);

    pieChart.invalidate();

    pieChart.setOnChartValueSelectedListener(this);

}
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getData()  );
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }


    @Override
    public void onClick(View view) {
        if(view == btn_search){
            createJsonRequestSearchResult();
        }

    }


    private void selectYearData(View v) {
        final Calendar now = Calendar.getInstance();   // Gets the current date and time
          curryear = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);

        selectyear_list = new ArrayList<>();
        selectMonthList = new ArrayList<>();

        selectMonthList.add("1");
        selectMonthList.add("2");
        selectMonthList.add("3");
        selectMonthList.add("4");
        selectMonthList.add("5");
        selectMonthList.add("6");
        selectMonthList.add("7");
        selectMonthList.add("8");
        selectMonthList.add("9");
        selectMonthList.add("10");
        selectMonthList.add("11");
        selectMonthList.add("12");

        Spinner spi_select_year = (Spinner) v.findViewById(R.id.spi_year);
        Spinner spiMonth = (Spinner) v.findViewById(R.id.spi_month);
        final Spinner spi_days = (Spinner) v.findViewById(R.id.spi_days);
        createJsonRequestYear();

        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, selectyear_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spi_select_year.setAdapter(adapter);

        spi_select_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectYear = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
       ArrayAdapter adaptermonth = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, selectMonthList);
        adaptermonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiMonth.setAdapter(adaptermonth);

        spiMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectMonth = (String) parent.getItemAtPosition(position);

                if(selectMonth.equals("4")||selectMonth.equals("6")||selectMonth.equals("9")||selectMonth.equals("11")){
                    ArrayAdapter adapterdays = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, daysList30());
                    adapterdays.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spi_days.setAdapter(adapterdays);
                    spi_days.setSelection(daysList30().size()-1);
                }
               else if(selectMonth.equals("2")){
                    ArrayAdapter adapterdays = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, daysList28());
                    adapterdays.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spi_days.setAdapter(adapterdays);
                    spi_days.setSelection(daysList28().size()-1);
                }else {
                    ArrayAdapter adapterdays = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, daysList31());
                    adapterdays.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spi_days.setAdapter(adapterdays);
                    spi_days.setSelection(daysList31().size()-1);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spiMonth.setSelection(month);


        spi_days.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectdays = (String) parent.getItemAtPosition(position);
                Log.v("piechartspinerdays", selectdays.toString());
                createJsonRequestSearchResult();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void createJsonRequestYear() {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        //  pDialog.setMessage("Loading...");
        pDialog.show();

        String URL = AppConstraint.FINANCIALYEARFORDRP;

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                URL, jsonParamYear(),
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
                //  createJsonRequestYear(empId);
                Log.v("responce_error", error.toString());
                pDialog.hide();
            }

        });
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjReq, null);
    }

    private JSONObject jsonParamYear() {
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
        selectyear_list.clear();
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.create();

        if (results.equals("[]")) {
            return;
        }

        FinanceYearData billdata[] = gson.fromJson(results, FinanceYearData[].class);
        for (FinanceYearData data : billdata) {

            year2 = data.getFinyr();
            selectyear_list.add(year2);
            adapter.notifyDataSetChanged();

        }
    }

  //  <>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>JSON Reguset Search Button>>>>>>>>>>>>>>>>>>>>>>>>>

  private void createJsonRequestSearchResult() {
      final ProgressDialog pDialog = new ProgressDialog(getActivity());
      //  pDialog.setMessage("Loading...");
      pDialog.setCancelable(false);
      pDialog.show();

      String URL = AppConstraint.ATTENDANCEREPORTMONTH;
      JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
              URL, jsonParamSearch(),
              new com.android.volley.Response.Listener<JSONArray>() {

                  @Override
                  public void onResponse(JSONArray response) {
                      handleResponseSearch(response.toString());
                      Log.v("responcesearch", response.toString());
                      pDialog.hide();
                  }
              }, new com.android.volley.Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
              //  createJsonRequestYear(empId);
              Log.v("responce_error", error.toString());
              pDialog.hide();
          }

      });
      jsonObjReq.setRetryPolicy(new RetryPolicy() {
          @Override
          public int getCurrentTimeout() {
              return 50000;
          }

          @Override
          public int getCurrentRetryCount() {
              return 50000;
          }

          @Override
          public void retry(VolleyError error) throws VolleyError {

          }
      });
      AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjReq, null);
  }

    private JSONObject jsonParamSearch() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        if(selectYear.equalsIgnoreCase("")) {
            jsonArray.put(curryear);
        }else {
            jsonArray.put(selectYear);
        }
         jsonArray.put(selectMonth);
        if(selectdays.equals("select")) {
             jsonArray.put(0);
         }else {
             jsonArray.put(selectdays);
         }
        try {
            jsonObject.put("DatabaseName", "TNS_NT_PMS");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");
            jsonObject.put("spName", "GetApiAttendanceReportMonthWise");
            jsonObject.put("ParameterList", jsonArray);
            Log.v("jsonparamSearch", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

    private void handleResponseSearch(String results) {
        if (results.equals("[]")) {
            return;
        }
        else{
            try {
               JSONArray jsonArray = new JSONArray(results);
               JSONObject jsonObject = jsonArray.getJSONObject(0);
               String tdays = jsonObject.getString("ADays");
               String AbsentA = jsonObject.getString("AbsentA");
               String AbsentAB = jsonObject.getString("AbsentAB");
               String AbsentLWP = jsonObject.getString("AbsentLWP");
               String Loaded = jsonObject.getString("Loaded");
               String Idle = jsonObject.getString("Idle");
               String Unloaded = jsonObject.getString("Unloaded");
               String Transfers = jsonObject.getString("Transfers");

               AttendanceReportPiechart piechart = new AttendanceReportPiechart(AbsentA,AbsentAB,AbsentLWP,Loaded,Idle,Unloaded,Transfers);
               settingPieChart(piechart);
               Log.v("piechart",tdays);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //  <>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> End JSON Reguset Search Button>>>>>>>>>>>>>>>>>>>>>>>>>
    private ArrayList<String> daysList31(){
        ArrayList<String> days = new ArrayList<>();
         days.add("1");
         days.add("2");
         days.add("3");
         days.add("4");
         days.add("5");
         days.add("6");
         days.add("7");
         days.add("8");
         days.add("9");
         days.add("10");
         days.add("11");
         days.add("12");
         days.add("13");
         days.add("14");
         days.add("15");
         days.add("16");
         days.add("17");
         days.add("18");
         days.add("19");
         days.add("20");
         days.add("21");
         days.add("22");
         days.add("23");
         days.add("24");
         days.add("25");
         days.add("26");
         days.add("27");
         days.add("28");
         days.add("29");
         days.add("30");
         days.add("31");
         days.add("select");

        return days;

    }
    private ArrayList<String> daysList30(){
        ArrayList<String> days = new ArrayList<>();

        days.add("1");
        days.add("2");
        days.add("3");
        days.add("4");
        days.add("5");
        days.add("6");
        days.add("7");
        days.add("8");
        days.add("9");
        days.add("10");
        days.add("11");
        days.add("12");
        days.add("13");
        days.add("14");
        days.add("15");
        days.add("16");
        days.add("17");
        days.add("18");
        days.add("19");
        days.add("20");
        days.add("21");
        days.add("22");
        days.add("23");
        days.add("24");
        days.add("25");
        days.add("26");
        days.add("27");
        days.add("28");
        days.add("29");
        days.add("30");

        days.add("select");


        return days;

    }
    private ArrayList<String> daysList28(){
        ArrayList<String> days = new ArrayList<>();

        days.add("1");
        days.add("2");
        days.add("3");
        days.add("4");
        days.add("5");
        days.add("6");
        days.add("7");
        days.add("8");
        days.add("9");
        days.add("10");
        days.add("11");
        days.add("12");
        days.add("13");
        days.add("14");
        days.add("15");
        days.add("16");
        days.add("17");
        days.add("18");
        days.add("19");
        days.add("20");
        days.add("21");
        days.add("22");
        days.add("23");
        days.add("24");
        days.add("25");
        days.add("26");
        days.add("27");
        days.add("28");

        days.add("select");


        return days;

    }


  /*  public class MyValueFormatter implements ValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0"); // use one decimal if needed
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return mFormat.format(value) + ""; // e.g. append a dollar-sign
        }


    }*/

}


