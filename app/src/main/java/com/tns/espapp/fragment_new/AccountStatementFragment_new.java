package com.tns.espapp.fragment_new;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tns.espapp.AppConstraint;
import com.tns.espapp.HTTPPostRequestMethod;
import com.tns.espapp.R;
import com.tns.espapp.Utility.AppSingleton;
import com.tns.espapp.Utility.SharedPreferenceUtils;
import com.tns.espapp.database.AccountStatementData;
import com.tns.espapp.database.LeaveTransactionData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountStatementFragment_new extends Fragment {
    private TextView tv_accountstatement_norecord;
    private SimpleDateFormat dateFormatter;
    private Calendar cal;
    private int day;
    private int month;
    private int year;
    int curryear;
     private String year2;
    private EditText edt_dateform;
    private EditText edt_dateto;
    private Button  search;

    private String empid;
    private List<AccountStatementData> accountStatementDataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyAdapter mAdapter;


    public AccountStatementFragment_new() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=  inflater.inflate(R.layout.fragment_account_statement_new, container, false);
        findIDS(v);

        return v;
    }


    private void findIDS(View v) {

        final SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance();
        sharedPreferenceUtils.setContext(getActivity());
        empid = sharedPreferenceUtils.getString(AppConstraint.EMPID);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_accountinfo);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);



        Calendar now = Calendar.getInstance();   // Gets the current date and time
         curryear = now.get(Calendar.YEAR);
        year2 = String.valueOf(curryear);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        Date d = null;
        try {
            d = dateFormatter.parse("01-04-"+year2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
       String datefrom = dateFormatter.format(d.getTime());
//dateTO...........................................................

        String year3 = String.valueOf(curryear+1);
        Date dto = null;
        try {
            dto = dateFormatter.parse("31-03-"+year3);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dateto = dateFormatter.format(dto.getTime());



        edt_dateform = (EditText) v.findViewById(R.id.edt_accountinfo_dateform);
        edt_dateto = (EditText) v.findViewById(R.id.edt_accountinfo_dateto);
       edt_dateform.setText( datefrom);
        edt_dateto.setText( dateto);
        edt_dateform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFormdate(edt_dateform);
            }
        });
        edt_dateto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTodate(edt_dateto);
            }
        });



        Button btn_Search =(Button)v.findViewById(R.id.btn_accountinfoSearch);
        btn_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               new getDataAshnctask().execute(AppConstraint.ACCOUNTSTATEMENTINFO);
                mAdapter = new MyAdapter( getHeader(),accountStatementDataList);
                recyclerView.setAdapter(mAdapter);
              //  createJsonRequestAccountStatementData(empid);
            }
        });
        tv_accountstatement_norecord=(TextView)v.findViewById(R.id.tv_accountstatement_norecord);

    }

    private void setFormdate(final EditText edt) {

       cal = Calendar.getInstance();
        cal.set(Calendar.MONTH,Calendar.APRIL);
        cal.set(Calendar.DATE,1);
        year = cal.get(Calendar.YEAR );
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(year, month, day);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        final DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                   /*   addmeeting_edt_selectdate.setText(dayOfMonth + "-"
                              + dateFormatter.format (monthOfYear + 1) + "-" + year);*/

                        cal.set(year, monthOfYear, dayOfMonth);

                        edt.setText(dateFormatter.format(cal.getTime()));

                    }
                }, year, month, day);






/*

        Date da = new Date();
        cal.setTime(d);
        cal.add(Calendar.MONTH, -12);
        d = cal.getTime();
        dpd.getDatePicker().setMinDate(d.getTime());

        Calendar calendar = Calendar.getInstance();//get the current day
        calendar.add(Calendar.MONTH,12);
        long afterThreeMonthsinMilli=calendar.getTimeInMillis();
        dpd.getDatePicker().setMaxDate(afterThreeMonthsinMilli);
*/

        dpd.show();

    }

    private void setTodate(final EditText edt) {

        cal = Calendar.getInstance();
        cal.set(Calendar.MONTH,Calendar.MARCH);
        cal.set(Calendar.DATE,31);
        year = cal.get(Calendar.YEAR  );
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(year+1, month, day);

        // String datefrom = dateFormatter.format(d.getTime());
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        final DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                   /*   addmeeting_edt_selectdate.setText(dayOfMonth + "-"
                              + dateFormatter.format (monthOfYear + 1) + "-" + year);*/

                        cal.set(year, monthOfYear, dayOfMonth);

                        edt.setText(dateFormatter.format(cal.getTime()));

                    }
                }, year, month, day);


        dpd.show();

    }



    private  class getDataAshnctask extends AsyncTask<String,Void,String>{
       ProgressDialog  pDialogs = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialogs.setMessage("Loading...");
            pDialogs.show();
        }

        @Override
        protected String doInBackground(String[] params) {

            String  response = HTTPPostRequestMethod.postMethodforESP(params[0],jsonParamSalaryDetailData(empid));

        return response;
        }

        @Override
        protected void onPostExecute(String s) {
            pDialogs.dismiss();
            super.onPostExecute(s);
            handleResponseSalaryDeatailData(s.toString());
            Log.v("httpresponce", s.toString());
        }
    }
  /*  private void createJsonRequestAccountStatementData(String empId) {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

         String URL = AppConstraint.ACCOUNTSTATEMENTINFO;

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                URL, jsonParamSalaryDetailData(empId),
                new com.android.volley.Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        handleResponseSalaryDeatailData(response.toString());
                        Log.v("responce", response.toString());
                        pDialog.hide();

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("responce", error.toString());
              // createJsonRequestAccountStatementData(empid);
                pDialog.hide();
            }

        });

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjReq, null);
    }*/

    private JSONObject jsonParamSalaryDetailData(String empId) {
             String   getDateform ="";
              String  getDateto ="";


        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");


        try {

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

            Date dtt = df.parse(edt_dateform.getText().toString());
            Date ds = new Date(dtt.getTime());
            getDateform = dateFormat2.format(ds);
            System.out.println(getDateform );

        } catch (ParseException e) {
            e.printStackTrace();
        }


        try {

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

            Date dtt = df.parse(edt_dateto.getText().toString());
            Date ds = new Date(dtt.getTime());
              getDateto = dateFormat2.format(ds);
            System.out.println(getDateto );

        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        // jsonArray.put("");

        try {
            jsonObject.put("DatabaseName", "TNS_FINANCE1112");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");
            jsonArray.put(empId);
            jsonArray.put(getDateform);
            jsonArray.put(getDateto);
            jsonArray.put(curryear);
            jsonObject.put("spName", "GetAccountStatementInfo");

            jsonObject.put("ParameterList", jsonArray);

            Log.v("jsonparam", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

    private void handleResponseSalaryDeatailData(String results) {
        accountStatementDataList.clear();
        Log.v("jsonresponse", results.toString());
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.create();

        if (results.equals("[]"))
        {
            tv_accountstatement_norecord.setVisibility(View.VISIBLE);
            return;
        }
        tv_accountstatement_norecord.setVisibility(View.GONE);

        AccountStatementData accountStatementDataas[] = gson.fromJson(results, AccountStatementData[].class);

        for (AccountStatementData data : accountStatementDataas) {

            accountStatementDataList.add(data);
            mAdapter.notifyDataSetChanged();
            //Toast.makeText(getActivity(),data.toString(),Toast.LENGTH_LONG).show(); ;
        }

    }


    public  AccountStatementData getHeader()
    {
        return new AccountStatementData("DEEPAK");
    }


    public class  MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        AccountStatementData headername;
        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;
        private List<AccountStatementData> list;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView tvdate,tvdetail,tvdebit,tvcredit,tvbalance;
            public MyViewHolder(View view) {
                super(view);
                tvdate = (TextView) view.findViewById(R.id.tv_accountst_date);
                tvdetail = (TextView) view.findViewById(R.id.tv_accountst_details);
                tvdebit = (TextView) view.findViewById(R.id.tv_accountst_debit);
                tvcredit = (TextView) view.findViewById(R.id.tv_account_credit);
                tvbalance = (TextView) view.findViewById(R.id.tv_accountst_balance);


            }
        }



        public MyAdapter(AccountStatementData headername,List<AccountStatementData> moviesList) {
            this. headername= headername;
            this.list = moviesList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.leave_transaction_adapter_view, parent, false);
            // return new MyViewHolder(itemView);

            if(viewType == TYPE_HEADER)
            {

               View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_transaction_adapter_view_header, parent, false);
                return new  MyAdapter.VHHeader(v);


            }
            else if(viewType == TYPE_ITEM)
            {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_transaction_adapter_view, parent, false);
                return new MyAdapter.MyViewHolder(v);
            }

            throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
          /*    if(position > 1) {


                  if (position % 2 == 1) {
                      // Set a background color for ListView regular row/item
                      holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                      //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                  } else {
                      // Set the background color for alternate row/item
                      holder.itemView.setBackgroundColor(Color.parseColor("#78B266"));
                      //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
                  }*/
            if (holder instanceof MyAdapter.VHHeader) {
                VHHeader VHheader = (VHHeader) holder;
                    //VHheader.txtTitle.setText(headername.getDate());
                   // VHheader.txtfeb.setText(headername.getDetails());
            }
            else if (holder instanceof MyAdapter.MyViewHolder) {
                MyViewHolder VHitem = (MyViewHolder) holder;
                AccountStatementData movie = list.get(position);
                VHitem.tvdate.setText(movie.getDate());
                VHitem.tvdetail.setText(movie.getDetails());
                VHitem.tvcredit.setText(movie.getCredit());
                VHitem.tvdebit.setText(movie.getDebit());
                VHitem.tvbalance.setText(movie.getBalance());
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
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

}
