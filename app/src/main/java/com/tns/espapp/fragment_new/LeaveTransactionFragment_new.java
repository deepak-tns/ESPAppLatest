package com.tns.espapp.fragment_new;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tns.espapp.AppConstraint;
import com.tns.espapp.R;
import com.tns.espapp.Utility.AppSingleton;
import com.tns.espapp.Utility.SharedPreferenceUtils;
import com.tns.espapp.database.FinanceYearData;
import com.tns.espapp.database.LeaveTransactionData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeaveTransactionFragment_new extends Fragment {

    private  TextView tv_leavetransaction_norecord ;
    private String year1;
    private String year2, selectMonth;
    private String empid;
    private Spinner spi_select_year, spiMonth;
    private ArrayAdapter adapter, adaptermonth;

    ArrayList<String> selectyear_list = new ArrayList<>();
    ArrayList<String> selectMonthList = new ArrayList<>();

    private List<LeaveTransactionData> leaveTransactionList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyAdapter mAdapter;

    public LeaveTransactionFragment_new()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_leave_transaction_fragment_new, container, false);

        final SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance();
        sharedPreferenceUtils.setContext(getActivity());
        empid = sharedPreferenceUtils.getString(AppConstraint.EMPID);
        findIDS(v);
        createJsonRequestYear(empid);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_transactio);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        /*
        LayoutInflater inflaters = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View headerPlaceHolder = inflaters.inflate(R.layout.leave_ladger_adapter_view_header, null, false);
        mLayoutManager.addView(headerPlaceHolder, 0);
        */


        mAdapter = new MyAdapter( getHeader(),leaveTransactionList);
        recyclerView.setAdapter(mAdapter);
       // prepareMovieData();
        return v;
    }

    private void findIDS(View v) {
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


        spi_select_year = (Spinner) v.findViewById(R.id.spi_leavrtransactrion_year);
        spiMonth = (Spinner) v.findViewById(R.id.spi_leavrtransactrion_month);

        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, selectyear_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spi_select_year.setAdapter(adapter);

        spi_select_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year2 = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        adaptermonth = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, selectMonthList);
        adaptermonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiMonth.setAdapter(adaptermonth);

        spiMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectMonth = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Button btn_salaryifoSearch =(Button)v.findViewById(R.id.btn_salarytranSearch);
        btn_salaryifoSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createJsonRequestLeaveTransactionDetailData(empid);
            }
        });
        tv_leavetransaction_norecord=(TextView)v.findViewById(R.id.tv_leavetransaction_norecord);

    }


    private void createJsonRequestYear(final String empId) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
      //  pDialog.setMessage("Loading...");
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
              //  createJsonRequestYear(empId);
                Log.v("responce_error", error.toString());
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
            tv_leavetransaction_norecord.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
            return;
        }

        tv_leavetransaction_norecord.setVisibility(View.GONE);
        FinanceYearData billdata[] = gson.fromJson(results, FinanceYearData[].class);
        for (FinanceYearData data : billdata) {

         /*   String[] separated = data.getFinyr().split("-");
            year1 = separated[0];
            year2 = separated[1];*/
            year2 = data.getFinyr();
            selectyear_list.add(year2);
            adapter.notifyDataSetChanged();

            //Toast.makeText(getActivity(),data.toString(),Toast.LENGTH_LONG).show(); ;
        }
    }


    private void createJsonRequestLeaveTransactionDetailData(String empId) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        String URL = AppConstraint.LEAVETRANSACTION;
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                URL, jsonParamLeaveTransDetailData(empId),
                new com.android.volley.Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        handleResponseLeaveDeatailData(response.toString());
                        Log.v("responce", response.toString());
                        pDialog.hide();

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                createJsonRequestLeaveTransactionDetailData(empid);
                pDialog.hide();
            }

        });

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjReq, null);
    }

    private JSONObject jsonParamLeaveTransDetailData(String empId) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        // jsonArray.put("");
        try {
            jsonObject.put("DatabaseName", "TNS_HR");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");
            jsonArray.put(empId);
            jsonArray.put(selectMonth);
            jsonArray.put(year2);
            jsonObject.put("spName", "GetEmployeeLeaveTransection");
            jsonObject.put("ParameterList", jsonArray);
            Log.v("jsonparam", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

    private void handleResponseLeaveDeatailData(String results) {
        leaveTransactionList.clear();
        leaveTransactionList.add(new LeaveTransactionData());

        Log.v("jsonresponse", results.toString());
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.create();

        if (results.equals("[]")) {
            tv_leavetransaction_norecord.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
            return;
        }

        tv_leavetransaction_norecord.setVisibility(View.GONE);
        LeaveTransactionData leaveDetailDatasdata[] = gson.fromJson(results, LeaveTransactionData[].class);

        for (LeaveTransactionData data : leaveDetailDatasdata) {

            leaveTransactionList.add(data);
            mAdapter.notifyDataSetChanged();
            //Toast.makeText(getActivity(),data.toString(),Toast.LENGTH_LONG).show(); ;
        }
    }

    public  LeaveTransactionData getHeader()
    {
        return new LeaveTransactionData("DEEPAK");
    }

    public class  MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private LeaveTransactionData headername;
        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;
        private List<LeaveTransactionData> List;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView empname,dept, repotingHead,leaveForm,leaveTo,applyDays,secfrom,secto,approvedDays,approved,action;
            public MyViewHolder(View view) {
                super(view);
                empname = (TextView) view.findViewById(R.id.tv_leavetransaction_empname);
                dept = (TextView) view.findViewById(R.id.tv_leavetransaction_dept);
                repotingHead = (TextView) view.findViewById(R.id.tv_leavetransaction_reportinghead);
                leaveForm = (TextView) view.findViewById(R.id.tv_leavetransaction_leaveform);
                leaveTo = (TextView) view.findViewById(R.id.tv_leavetransaction_leaveTo);
                applyDays = (TextView) view.findViewById(R.id.tv_leavetransaction_applydays);
                secfrom = (TextView) view.findViewById(R.id.tv_leavetransaction_secfrom);
                secto = (TextView) view.findViewById(R.id.tv_leavetransaction_secto);
                approvedDays = (TextView) view.findViewById(R.id.tv_leavetransaction_approvedays);
                approved = (TextView) view.findViewById(R.id.tv_leavetransaction_approved);
                action = (TextView) view.findViewById(R.id.tv_leavetransaction_action);
            }
        }



        public MyAdapter(LeaveTransactionData headername,List<LeaveTransactionData> moviesList) {
            this. headername= headername;
            this.List = moviesList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.leave_transaction_adapter_view, parent, false);
            // return new MyViewHolder(itemView);

            if(viewType == TYPE_HEADER)
            {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.leave_transaction_adapter_view_header, parent, false);
                return  new VHHeader(v);
            }
            else if(viewType == TYPE_ITEM)
            {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.leave_transaction_adapter_view, parent, false);
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
            if (holder instanceof MyAdapter.VHHeader)
            {
                 VHHeader VHheader = (VHHeader) holder;
                //    VHheader.txtTitle.setText(headername.getmonth());
                //    VHheader.txtfeb.setText(headername.getopeningEL());
            }
            else if (holder instanceof MyViewHolder)
            {
                MyViewHolder VHitem = (MyViewHolder) holder;
                LeaveTransactionData movie = List.get(position);
                VHitem.empname.setText(movie.getEmpname());
                VHitem.dept.setText(movie.getDept());
                VHitem.repotingHead.setText(movie.getRepotingHead());
                VHitem.leaveForm.setText(movie.getLeaveForm());
                VHitem.leaveTo.setText(movie.getLeaveTo());
                VHitem.applyDays.setText(movie.getApplyDays());
                VHitem.secfrom.setText(movie.getSecfrom());
                VHitem.secto.setText(movie.getSecto());
                VHitem.approvedDays.setText(movie.getApprovedDays());
                VHitem.approved.setText(movie.getApproved());
                VHitem.action.setText(movie.getAction());
            }
        }

        @Override
        public int getItemCount() {
            return List.size();
        }
        @Override
        public int getItemViewType(int position) {
            if(isPositionHeader(position))
                return TYPE_HEADER;

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




    private void prepareMovieData() {

        LeaveTransactionData movie = new LeaveTransactionData();
        leaveTransactionList.add(movie);
        movie = new LeaveTransactionData("Deepak Sachan (16865)", "TS","Randhir Kumar (11732)","13-Sep-2017","13-Sep-2017","1","secto","approved","No","1","1",1);
        leaveTransactionList.add(movie);
        movie = new  LeaveTransactionData("Feb", "1","1","1","1","1","1","1","1","1","1",1);
        leaveTransactionList.add(movie);
        movie = new LeaveTransactionData("March", "1","1","1","1","1","1","1","1","1","1",1);
        leaveTransactionList.add(movie);
        movie = new  LeaveTransactionData("Apr", "1","1","1","1","1","1","1","1","1","1",1);
        leaveTransactionList.add(movie);
        movie = new LeaveTransactionData();
        leaveTransactionList.add(movie);
        mAdapter.notifyDataSetChanged();

    }
}
