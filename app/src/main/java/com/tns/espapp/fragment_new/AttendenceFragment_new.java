package com.tns.espapp.fragment_new;


import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.tns.espapp.AppConstraint;
import com.tns.espapp.R;
import com.tns.espapp.Utility.AppSingleton;
import com.tns.espapp.Utility.SharedPreferenceUtils;
import com.tns.espapp.database.AttendenceCompleteDataModel;
import com.tns.espapp.database.AttendenceDateTimeModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendenceFragment_new extends Fragment {

    private TextView tv_attendencesummary_norecord,tv_total_min,tv_taken_min,tv_remain_min;
    private LinearLayout linearLayout_mintue_header,linear_recyclerheader;

    private Button   btn_attendence ;



    private String year1;
    private String year2;
    private String empid;

    private Spinner spi_select_year;
    private ArrayAdapter  adapter_year;

    ArrayList<String> selectyear_list = new ArrayList<>();
    private List<AttendenceDateTimeModel> attendenceDatasList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyAdapter mAdapter;


    public AttendenceFragment_new() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_attendence_fragment_new, container, false);
        final SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance();
        sharedPreferenceUtils.setContext(getActivity());
        empid = sharedPreferenceUtils.getString(AppConstraint.EMPID);

        findIDS(v);



        return v;

    }

private  void findIDS(View v){
    spi_select_year =(Spinner) v.findViewById(R.id.spi_attendence_year);
    btn_attendence =(Button)v.findViewById(R.id.btn_attendenceSearch);

    recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_attendencesummary);
    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity()) {


        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
            LinearSmoothScroller smoothScroller = new LinearSmoothScroller(getActivity()) {

                private static final float SPEED = 300f;// Change this value (default=25f)

                @Override
                protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                    return SPEED / displayMetrics.densityDpi;
                }

            };
            smoothScroller.setTargetPosition(position);
            startSmoothScroll(smoothScroller);
        }

    };

    recyclerView.setLayoutManager(mLayoutManager);
    DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
    divider.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider));
    recyclerView.addItemDecoration(divider);

    Calendar now = Calendar.getInstance();   // Gets the current date and time
    int curryear = now.get(Calendar.YEAR);
    int month = now.get(Calendar.MONTH);
    int curryear1= curryear-1;
    year1 = String.valueOf(curryear1);
    year2 =curryear+"";

/*    selectyear_list.add("JAN-"+year1);
    selectyear_list.add("FEB-"+year1);
    selectyear_list.add("MAR-"+year1);
    selectyear_list.add("APR-"+year1);
    selectyear_list.add("MAY-"+year1);
    selectyear_list.add("JUN-"+year1);
    selectyear_list.add("JUL-"+year1);
    selectyear_list.add("AUG-"+year1);
    selectyear_list.add("SEP-"+year1);
    selectyear_list.add("OCT-"+year1);
    selectyear_list.add("NOV-"+year1);*/
    selectyear_list.add("DEC-"+year1);
    selectyear_list.add("JAN-"+year2);
    selectyear_list.add("FEB-"+year2);
    selectyear_list.add("MAR-"+year2);
    selectyear_list.add("APR-"+year2);
    selectyear_list.add("MAY-"+year2);
    selectyear_list.add("JUN-"+year2);
    selectyear_list.add("JUL-"+year2);
    selectyear_list.add("AUG-"+year2);
    selectyear_list.add("SEP-"+year2);
    selectyear_list.add("OCT-"+year2);
    selectyear_list.add("NOV-"+year2);
    selectyear_list.add("DEC-"+year2);

    adapter_year = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, selectyear_list);
    adapter_year.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spi_select_year.setAdapter(adapter_year);
    spi_select_year.setSelection(month+1);

    spi_select_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            year2 = (String) parent.getItemAtPosition(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });


    btn_attendence.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            createJsonRequestAttendenceData(empid);
            mAdapter = new MyAdapter( getHeader(),attendenceDatasList);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    });

    tv_attendencesummary_norecord=(TextView)v.findViewById(R.id.tv_attendencesummary_norecord);
    linearLayout_mintue_header=(LinearLayout)v.findViewById(R.id.lin_attendence_min);
    linear_recyclerheader=(LinearLayout)v.findViewById(R.id.linear_recyclerheader);
    tv_total_min =(TextView)v.findViewById(R.id.tv_total_min);
    tv_taken_min =(TextView)v.findViewById(R.id.tv_taken_min);
    tv_remain_min =(TextView)v.findViewById(R.id.tv_remain_min);

}


    private void createJsonRequestAttendenceData(String empId) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        String URL = AppConstraint.ATTENDANCEDETAILS;


        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                URL, jsonParamAttendenceData(empId),
                new com.android.volley.Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        handleResponseAttendenceData(response.toString());
                        Log.v("responce", response.toString());
                        pDialog.hide();

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleResponseAttendenceData(empid);
                pDialog.hide();
            }

        });

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjReq, null);
    }

    private JSONObject jsonParamAttendenceData(String empId) {
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
            jsonObject.put("spName", "GetAttendanceDetails");

            jsonObject.put("ParameterList", jsonArray);
            Log.v("jsonparam", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

    private void handleResponseAttendenceData(String results) {
        attendenceDatasList.clear();
        JSONArray jsonArrayOuter = null;
        JSONArray jsonArray1 =null;
        Log.v("attendeancejsonresponse", results.toString());
        if (results.equals("[]")||results.equals(empid)) {
            linearLayout_mintue_header.setVisibility(View.GONE);
            linear_recyclerheader.setVisibility(View.GONE);
            tv_attendencesummary_norecord.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
            return;
        }
        AttendenceCompleteDataModel attendenceCompleteDataModel = new AttendenceCompleteDataModel();


            try {
                jsonArrayOuter = new JSONArray(results);
                jsonArray1 = jsonArrayOuter.getJSONArray(0);

            Gson gson = new Gson();
         List<AttendenceDateTimeModel> attendenceDateTimeModelList = Arrays.asList(gson.fromJson(jsonArray1.toString(), AttendenceDateTimeModel[].class));
        JSONArray jsonArray2 = jsonArrayOuter.getJSONArray(1);

        int remainingMinuts = jsonArray2.getJSONObject(0).getInt("RemainingMinuts");

        JSONArray jsonArray3 = jsonArrayOuter.getJSONArray(2);
        int totalTakenMinuts = jsonArray3.getJSONObject(0).getInt("TotalTakenMinuts");


        attendenceCompleteDataModel.setAttendenceDateTimeModelArrayList(attendenceDateTimeModelList);
        attendenceCompleteDataModel.setRemainingMinuts(remainingMinuts);
        attendenceCompleteDataModel.setTotalTakenMinuts(totalTakenMinuts);

                linearLayout_mintue_header.setVisibility(View.VISIBLE);
                tv_attendencesummary_norecord.setVisibility(View.GONE);
            tv_total_min.setText("270");
            tv_taken_min.setText(attendenceCompleteDataModel.getTotalTakenMinuts()+"");
            tv_remain_min.setText(attendenceCompleteDataModel.getRemainingMinuts()+"");


            tv_attendencesummary_norecord.setVisibility(View.GONE);
//...........................................addheader blanklist.............................................
             linear_recyclerheader.setVisibility(View.VISIBLE);
//...........................................addheader blanklist.............................................

            for (AttendenceDateTimeModel data : attendenceDateTimeModelList) {

                attendenceDatasList.add(data);
                Log.v("Listdata",data.getDate());

                //Toast.makeText(getActivity(),data.toString(),Toast.LENGTH_LONG).show(); ;
            }
                mAdapter.notifyDataSetChanged();
            // String json = "{"client":"127.0.0.1","servers":["8.8.8.8","8.8.4.4","156.154.70.1","156.154.71.1"]}";

            } catch (JSONException e) {
                e.printStackTrace();
            }
    }
    public AttendenceDateTimeModel getHeader()
    {
        return new AttendenceDateTimeModel();
    }


    public class  MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        AttendenceDateTimeModel headername;
        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;
        private List<AttendenceDateTimeModel> List;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView date,status,intime,outtime,mintueconsume;
            public MyViewHolder(View view) {
                super(view);
                date = (TextView) view.findViewById(R.id.tv_atten_date);
                status = (TextView) view.findViewById(R.id.tv_atten_status);
                intime = (TextView) view.findViewById(R.id.tv_atten_intime);
                outtime = (TextView) view.findViewById(R.id.tv_atten_outtime);
                mintueconsume = (TextView) view.findViewById(R.id.tv_atten_takenmin);
            }
        }

        public MyAdapter(AttendenceDateTimeModel headername, List<AttendenceDateTimeModel> moviesList) {
            this. headername= headername;
            this.List = moviesList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.leave_transaction_adapter_view, parent, false);
            // return new MyViewHolder(itemView);

            if(viewType == TYPE_HEADER)
            {
              //  View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendenceinfo_adapter_view_header, parent, false);
              //  return  new MyAdapter.VHHeader(v);
            }
            else if(viewType == TYPE_ITEM)
            {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendenceinfo_adapter_view, parent, false);
                return new MyViewHolder(v);
            }

            throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
          /*
           if(position > 1) {


                  if (position % 2 == 1) {
                      // Set a background color for ListView regular row/item
                      holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                      //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                  } else {
                      // Set the background color for alternate row/item
                      holder.itemView.setBackgroundColor(Color.parseColor("#78B266"));
                      //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
                  }
           */
            if (holder instanceof VHHeader) {
                VHHeader VHheader = (VHHeader) holder;
                //    VHheader.txtTitle.setText(headername.getmonth());
                //    VHheader.txtfeb.setText(headername.getopeningEL());


            } else if (holder instanceof MyViewHolder) {
                MyViewHolder VHitem = (MyViewHolder) holder;

                AttendenceDateTimeModel movie = List.get(position);
                VHitem.date.setText(movie.getDate());
                VHitem.status.setText(movie.getStatus());
                VHitem.intime.setText(movie.getInTime());
                VHitem.outtime.setText(movie.getOutTime());
                VHitem.mintueconsume.setText(movie.getMinuteConsume()+"");
                if((movie.getHoliDay()== 1) ||( movie.getSunday()== 1)){
               holder.itemView.setBackgroundColor(getResources().getColor(R.color.lite_orange));
                }
                else
                {
                    holder.itemView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
                if((movie.getStatus().equalsIgnoreCase("P    ")) ){
                    VHitem.status.setTextColor(getResources().getColor(R.color.forestgreen));
                }else
                {
                    VHitem.status.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                }


            }

        }

        @Override
        public int getItemCount() {
            return List.size();
        }
        @Override
        public int getItemViewType(int position) {
         /*   if(isPositionHeader(position))
                return TYPE_HEADER;*/

            return TYPE_ITEM;
        }

        private boolean isPositionHeader(int position)
        {
            return position == 0;
        }



        class VHHeader extends RecyclerView.ViewHolder{
            TextView txtTitle, txtfeb;
            public VHHeader(View itemView) {
                super(itemView);
                // txtTitle = (TextView)itemView.findViewById(R.id.tv_leaveladger_month);
                //  txtfeb = (TextView)itemView.findViewById(R.id.tv_leaveladger_op_el);

            }
        }

    }

}
