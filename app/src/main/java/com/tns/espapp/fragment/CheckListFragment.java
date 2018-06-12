package com.tns.espapp.fragment;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tns.espapp.AppConstraint;
import com.tns.espapp.ListviewHelper;
import com.tns.espapp.R;
import com.tns.espapp.Utility.AppSingleton;
import com.tns.espapp.adapter.CheckListAdapterListview;
import com.tns.espapp.adapter.ChecklistAdapter;
import com.tns.espapp.database.ChecklistData;
import com.tns.espapp.database.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */

public class CheckListFragment extends Fragment {
    private ChecklistAdapter checklistAdapter;
    private CheckListAdapterListview lstadapter;

    private View V_view;

    private SimpleDateFormat dateFormatter;
    private Calendar cal;
    private int day;
    private int month;
    private int year;
    private List<ChecklistData> check_list ;
    private RecyclerView recyclerView;

    private ListView listView;
    private Button btn_createForm;


    String name[] = {"one", "two", "three", "four", "five", "six", "seven", "eight"};
    String[] form = {"Form1", "Form2", "Form3", "Form4", "Form5", "Form6", "Form7", "Form8", "Form9", "Form10"};

    ArrayList <String> formlist ;
    ArrayAdapter aa;
    static String frg_name;
    static int getFrg_postion;
    DatabaseHandler db;
    Spinner spin;
    String selectFormSpinner;
    RelativeLayout relative_attachment;

    public static CheckListFragment newInstance_CheckListFragment(int index, String value) {
        CheckListFragment f = new CheckListFragment();
        Bundle args = new Bundle();
        getFrg_postion = args.getInt("index", index);
        frg_name = args.getString("value", value);
        f.setArguments(args);
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_check_list, container, false);
        V_view = v;

        TextView frg_from = (TextView) v.findViewById(R.id.frg_from);
        db = new DatabaseHandler(getActivity());
        spin = (Spinner) v.findViewById(R.id.spinner1);
        btn_createForm =(Button)v.findViewById(R.id.btn_crete_form);
        //spin.setOnItemSelectedListener(this);
        listView = (ListView)v.findViewById(R.id.listview_chk);

        relative_attachment =(RelativeLayout) v.findViewById(R.id.relative_attachment);

        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        cal.set(year, month, day);
        frg_from.setText(frg_name + " " + getFrg_postion);

         // db.deletecheckListData();

        getVOLLY_FORMNAME();
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectFormSpinner =   parent.getItemAtPosition(position).toString();
                List<ChecklistData> getFormlist = db.getAllChecklistwithFormno(selectFormSpinner);
                lstadapter = new CheckListAdapterListview(getActivity(), R.layout.checklist_data_adapter, getFormlist);
                listView.setAdapter(lstadapter);
                lstadapter.notifyDataSetChanged();

                if(getFormlist.size() >0) {

                    btn_createForm.setVisibility(View.GONE);
                }else {
                   getVOLLY_FORMVARYFY(selectFormSpinner);
                    btn_createForm.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       // initLinearBind(v);
       // loadJSONFromAsset();
/*
    recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        checklistAdapter = new ChecklistAdapter(getActivity(),check_list, new ChecklistAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ChecklistData item, int pos) {
                Toast.makeText(getActivity(), item.getName() + "," + pos, Toast.LENGTH_LONG).show();
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        // recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(checklistAdapter);
        checklistAdapter.notifyDataSetChanged();
*/

        //  prepareMovieData();

        return v;

    }


    public void initLinearBind(View view, List<ChecklistData> checklistDataList) {

        int size = checklistDataList.size();
        LinearLayout li1 = (LinearLayout) view.findViewById(R.id.table_main);
        for (int i = 0; i < name.length; i++) {
            LinearLayout LL2 = new LinearLayout(getActivity());
            // LL2.setBackgroundColor(Color.CYAN);
            LL2.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            //  LL2.setWeightSum(1f);
            LL2.setLayoutParams(LLParams);

            TextView tv2 = new TextView(getActivity());
            LinearLayout.LayoutParams tvParams_2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            tv2.setLayoutParams(tvParams_2);
            tv2.setText(name[i]);
            tv2.setTextSize(10);
            tv2.setTextColor(Color.BLACK);

            EditText etv2 = new EditText(getActivity());
            LinearLayout.LayoutParams edtParams_3 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f);
            etv2.setLayoutParams(edtParams_3);
            etv2.setHint(name[i]);
            etv2.setTextSize(10);
            etv2.setTextColor(Color.BLACK);

            LinearLayout LL3 = new LinearLayout(getActivity());
            // LL2.setBackgroundColor(Color.CYAN);
            LL3.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams LLParams3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            // LL3.setWeightSum(2f);
            LL3.setLayoutParams(LLParams3);

            TextView tv3 = new TextView(getActivity());
            LinearLayout.LayoutParams tvParams_3 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            tv3.setLayoutParams(tvParams_3);
            tv3.setText(form[i]);
            tv3.setTextSize(10);
            tv3.setTextColor(Color.BLACK);


            final EditText etv3 = new EditText(getActivity());
            LinearLayout.LayoutParams edtParams_4 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f);

            etv3.setLayoutParams(edtParams_4);
            etv3.setHint(form[i]);
            etv3.setTextSize(10);
            etv3.setTextColor(Color.BLACK);
            etv3.setText(dateFormatter.format(cal.getTime()));
            etv3.setFocusable(false);


            LL2.addView(tv2);
            LL2.addView(etv2);

            LL3.addView(tv3);
            LL3.addView(etv3);

            // RelativeLayout rl=((RelativeLayout) findViewById(R.id.screenRL));
            li1.addView(LL2);
            li1.addView(LL3);

            etv3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setdate(etv3);
                }
            });
        }
    }



    private void setdate(final EditText edt) {
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        DatePickerDialog dpd = new DatePickerDialog(getActivity(),
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

    private void getVOLLY_FORMVARYFY(String getfname) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
       // String mStringUrl = "http://www.yoururl.com";
       JSONObject jsonObject = new JSONObject();
        try {
            JSONArray jsonArrayParameter = new JSONArray();
            jsonArrayParameter.put(getfname);
            jsonObject.put("DatabaseName", "TNS_HR");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");
            jsonObject.put("spName", "USP_FormMaster");
            jsonObject.put("ParameterList",jsonArrayParameter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, AppConstraint.FORMVERIFY, jsonObject,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseFormVeryfyJSON(response.toString());
                        pDialog.dismiss();
                        Log.e("!_@@_SUCESS", response + "");
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Log.e("!_@@_Errors--", error + "");
                    }
                }) {
        /*    @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
               // params.put("Authorization", "key=" + Legacy_SERVER_KEY);
                params.put("Content-Type", "application/json");
                return params;
            }*/
        };
        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest, null);

    }

    public void parseFormVeryfyJSON(String jsonObject) {

        check_list = new ArrayList<>();
        try {
            JSONObject obj1 = new JSONObject(jsonObject);
            JSONArray jsonArray = obj1.getJSONArray("FormName");

            for(int i = 0; i<jsonArray.length(); i++) {
                JSONObject jObj2 = jsonArray.getJSONObject(i);
                Log.d("FormverifyDetails-->", jObj2.toString());
                String vFormName = jObj2.getString("formName");
                String vNameInput = jObj2.getString("fieldName");
                String vNameValue = jObj2.getString("fieldValue");
                String Datatype = jObj2.getString("dataType");
                String Size = jObj2.getString("size");
                String Decimal = jObj2.getString("decimal");

              ChecklistData checklistData = new ChecklistData(vFormName, vNameInput, vNameValue, Datatype,Size,Decimal, 0);
              check_list.add(checklistData);

                  //db.insertCheckListData(checklistData);

            btn_createForm.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {

                         for( ChecklistData c : check_list){
                             String formno=   c.getFormno().replaceAll("\\s","");
                              ChecklistData checklistDatas = new ChecklistData(formno, c.getName(), c.getName_value(), c.getDataType(),c.getSize(),c.getDecimal(), 1);
                             db.insertCheckListData(checklistDatas);
                         }
                         btn_createForm.setVisibility(View.GONE);
                     }
                 });
            }

            if(check_list.size() == 0){
                btn_createForm.setEnabled(false);
            }

            lstadapter = new CheckListAdapterListview(getActivity(), R.layout.checklist_data_adapter, check_list) ;
            listView.setAdapter(lstadapter);
            ListviewHelper.getListViewSize(listView);
            lstadapter.notifyDataSetChanged();

    } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getVOLLY_FORMNAME() {
        // String mStringUrl = "http://www.yoururl.com";
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            // jsonObject.put("DatabaseName", "TNS_NT_PMS");
            jsonObject.put("DatabaseName", "TNS_HR");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");
            jsonObject.put("spName", "USP_GetForm ");
          //  jsonObject.put("spName", "GetFormName");

          //  jsonObject.put("ParameterList","0");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, AppConstraint.FORMNAME, jsonObject,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseFormNAMEJSON(response.toString());
                        pDialog.dismiss();
                        Log.e("!_@@_SUCESS", response + "");
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("!_@@_Errors--", error + "");
                        pDialog.dismiss();
                    }
                }) {
        /*    @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
               // params.put("Authorization", "key=" + Legacy_SERVER_KEY);
                params.put("Content-Type", "application/json");
                return params;
            }*/
        };
        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest, null);

    }

    public void parseFormNAMEJSON(String jsonObject) {

        formlist = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(jsonObject);
            JSONArray jsonArray = obj.getJSONArray("FormName");
            for(int i = 0; i<jsonArray.length(); i++) {
                JSONObject jObj2 = jsonArray.getJSONObject(i);
                String formNameno = jObj2.getString("vFormName");
                formlist.add(formNameno);
                //getVOLLY_FORMVARYFY(formNameno);
                aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, formlist);
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin.setAdapter(aa);
                spin.setSelection(aa.getCount() - 1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
















































































    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("JSONFile.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

            parseJSON(json);

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    public void parseJSON(String jsonObject) {

        try {
            JSONObject obj = new JSONObject(jsonObject);
            JSONArray m_jArry = obj.getJSONArray("sitedata");
            ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> m_li;

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                Log.d("Details-->", jo_inside.getString("siteid"));

                String formno = jo_inside.getString("formno");
                String siteid = jo_inside.getString("siteid");
                JSONObject jsonobjsiteType = m_jArry.getJSONObject(i);
                JSONObject jsonobjsite2 = jsonobjsiteType.getJSONObject("sitetype");
                String sitetype = jsonobjsite2.getString("value");


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void sendFCMPush() {

        final String Legacy_SERVER_KEY = "AIzaSyBM-G98OFEuJ7CcDMbwk4M3Z4oWEJ4Vcj4";
        String msg = "this is test message,.,,.,.";
        String title = "my title";
        String token = "caAXHgnPD2c:APA91bEkcbZFSMpMsDIEzjem7FHvzxvy6g0Argkk0uyOWr0UxxVgT4vaEbONfAEgXfAOAdJBrpclX-j0GqaJYWgYjJnjm79Mhk3GhKI5G14PAs9mLDtJ8uzXLgJ8UZ7zWXmvle2uAXWN";

        JSONObject obj = null;
        JSONObject objData = null;
        JSONObject dataobjData = null;

        try {
            obj = new JSONObject();
            objData = new JSONObject();

            objData.put("body", msg);
            objData.put("title", title);
            objData.put("sound", "default");
            objData.put("icon", "icon_name"); //   icon_name image must be there in drawable
            objData.put("tag", token);
            objData.put("priority", "high");

            dataobjData = new JSONObject();
            dataobjData.put("text", msg);
            dataobjData.put("title", title);

            obj.put("to", token);
            //obj.put("priority", "high");

            obj.put("notification", objData);
            obj.put("data", dataobjData);

            Log.e("!_@rj@_@@_PASS:>", obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, AppConstraint.FCM_PUSH_URL, obj,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("!_@@_SUCESS", response + "");
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("!_@@_Errors--", error + "");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "key=" + Legacy_SERVER_KEY);
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        int socketTimeout = 1000 * 60;// 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        requestQueue.add(jsObjRequest);
    }


}

