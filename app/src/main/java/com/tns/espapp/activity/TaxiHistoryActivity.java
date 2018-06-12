package com.tns.espapp.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tns.espapp.AppConstraint;
import com.tns.espapp.HTTPPostRequestMethod;
import com.tns.espapp.R;
import com.tns.espapp.Utility.SharedPreferenceUtils;
import com.tns.espapp.Utility.Utility;
import com.tns.espapp.database.DatabaseHandler;
import com.tns.espapp.database.TaxiFormData;
import com.tns.espapp.fragment.MapWebViewFragment;
import com.tns.espapp.fragment.TaxiFormRecordFragment;
import com.tns.espapp.service.SendLatiLongiServerIntentService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TaxiHistoryActivity extends AppCompatActivity {
    private DatabaseHandler db;
    private TaxiFormRecordHistoryAdapter adapter;
    private ArrayList<TaxiFormData> taxiFormDataArrayList ;
    private ListView listview_taxirecord_history;
    private String empid,getstatus;
    private EditText editsearch;
    private Typeface face;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxi_history);
        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        db = new DatabaseHandler(TaxiHistoryActivity.this);
     /*   SharedPreferences sharedPreferences_setid = TaxiHistoryActivity.this.getSharedPreferences("ID", Context.MODE_PRIVATE);
        empid = sharedPreferences_setid.getString("empid", "");*/
        SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance();
        sharedPreferenceUtils.setContext(TaxiHistoryActivity.this);
        empid = sharedPreferenceUtils.getString(AppConstraint.EMPID);

        taxiFormDataArrayList = new ArrayList<>();
        listview_taxirecord_history=(ListView)findViewById(R.id.listview_taxiform_history);
        List<TaxiFormData> taxiformrecordDataList = db.getAllTaxiformData();
        int size = taxiformrecordDataList.size();

        face  = Typeface.createFromAsset(TaxiHistoryActivity.this.getAssets(),
                "arial.ttf");




        if(size >0){
            for(TaxiFormData taxiFormData : taxiformrecordDataList){
                taxiFormDataArrayList.add(taxiFormData);
            }
        }


        adapter = new TaxiFormRecordHistoryAdapter(TaxiHistoryActivity.this,R.layout.taxiform_record_history_adapter,taxiFormDataArrayList);
        listview_taxirecord_history.setAdapter(adapter);
        View view = LayoutInflater.from(TaxiHistoryActivity.this).inflate(R.layout.header_listview_taxiform_record,null);
        listview_taxirecord_history.addHeaderView(view);

        listview_taxirecord_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



            }
        });



        editsearch = (EditText) findViewById(R.id.search_taxirecord);
        editsearch.setTypeface(face);

        // Capture Text in EditText
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString();
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });



    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    private   class  TaxiFormRecordHistoryAdapter extends ArrayAdapter {
        TaxiFormData  taxiFormData;
        int deepColor = Color.parseColor("#FFFFFF");
        int deepColor2 = Color.parseColor("#DCDCDC");
        //  int deepColor3 = Color.parseColor("#B58EBF");
        private int[] colors = new int[]{deepColor, deepColor2};
        private List<TaxiFormData> searchlist = null;
        List<TaxiFormData> taxiForm_DataArrayList;

        public TaxiFormRecordHistoryAdapter(Context context, int resource, ArrayList<TaxiFormData> latLongDatas) {
            super(context, resource, latLongDatas);
            this.searchlist = latLongDatas;
            this.taxiForm_DataArrayList = new ArrayList<>();
            taxiForm_DataArrayList.addAll(searchlist);
        }

        private class ViewHolder{

            TextView formno ;
            TextView date ;
            TextView id ;
            TextView projecttype;
            TextView vihecleno;
            ImageView status ;
            TextView startkm ;
            TextView endkm;
            TextView delete;

        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            taxiFormData = searchlist.get(position);
            TaxiFormRecordHistoryAdapter.ViewHolder viewHolder = new TaxiFormRecordHistoryAdapter.ViewHolder();

            if (convertView == null) {

                LayoutInflater layoutInflater = (LayoutInflater) TaxiHistoryActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.taxiform_record_history_adapter, parent, false);
                //int colorPos = position % colors.length;
                // convertView.setBackgroundColor(colors[colorPos]);

                viewHolder. formno = (TextView) convertView.findViewById(R.id.tv_formno_taxiadapter);
                viewHolder.  date = (TextView) convertView.findViewById(R.id.tv_date_taxiadapter);
                viewHolder.  id = (TextView) convertView.findViewById(R.id.tv_id_taxiadapter);
                viewHolder.  projecttype = (TextView) convertView.findViewById(R.id.tv_project_taxiadapter);
                viewHolder.  vihecleno = (TextView) convertView.findViewById(R.id.tv_vechicle_taxiadapter);
                viewHolder.  status = (ImageView) convertView.findViewById(R.id.iv_status_taxiadapter);
                viewHolder.  startkm = (TextView) convertView.findViewById(R.id.tv_startkm_taxiadapter);
                viewHolder.  endkm = (TextView) convertView.findViewById(R.id.tv_endkm_taxiadapter);
                viewHolder.delete = (TextView) convertView.findViewById(R.id.tv_delete_history);

                viewHolder.formno.setTypeface(face);
                viewHolder.date.setTypeface(face);
                viewHolder.id.setTypeface(face);
                viewHolder.projecttype.setTypeface(face);
                viewHolder.vihecleno.setTypeface(face);
                viewHolder.endkm.setTypeface(face);

                convertView.setTag(viewHolder);

            }else{


                viewHolder = (TaxiFormRecordHistoryAdapter.ViewHolder) convertView.getTag();

            }



            getstatus = taxiFormData.getFlag()+"";
          //  String fr = "<u>"+taxiFormData.getFormno()+"</u>";
           // viewHolder.  formno.setText(Html.fromHtml(fr));
            viewHolder.  formno.setText(taxiFormData.getFormno());
            viewHolder.  date.setText(taxiFormData.getSelectdate());
            viewHolder. id.setText(taxiFormData.getId()+"");
            viewHolder. projecttype.setText(taxiFormData.getProjecttype());
            viewHolder. vihecleno.setText(taxiFormData.getVechicleno());
            viewHolder.  startkm.setText(taxiFormData.getStartkm());
            viewHolder. endkm.setText(taxiFormData.getEndkm());


            if(getstatus.equals("1")){
                viewHolder. status.setBackgroundResource(R.drawable.success);
                //status.setText("Success");
            }else
            if(getstatus.equals("0")){
                viewHolder. status.setBackgroundResource(R.drawable.pending);
                //status.setText("Pending");
            }else if(getstatus.equals("2")){
                viewHolder. status.setBackgroundResource(R.drawable.upload);

                //status.setText("Retry");
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (position <= taxiFormDataArrayList.size()) {

                     custumDialog(position);
                    }
                    if (getstatus.equals("1")) {

                        try {

                        } catch (Exception e) {
                            Log.e("Exception", e.getMessage());
                        }
                    }
                    else
                    {
                        Toast.makeText(getContext(),"Please Wait",Toast.LENGTH_LONG).show();
                    }

                }
            });



            viewHolder. status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    taxiFormData = searchlist.get(position);
                    getstatus = taxiFormData.getFlag()+"";

                    boolean b = getstatus.equals("2");

                    if(b){
                        new getDataAsnycTask(taxiFormData).execute(AppConstraint.TAXIFORMURL);
                        TaxiHistoryActivity.this.startService(new Intent(TaxiHistoryActivity.this, SendLatiLongiServerIntentService.class));
                    }


                }
            });

            viewHolder.formno.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    taxiFormData = searchlist.get(position);

                    // Intent intent = new Intent(TaxiHistoryActivity.this, MapWebViewActivity.class);
                    // intent.putExtra("formno",taxiFormData.getFormno());
                    // startActivity(intent);

                    MapWebViewFragment ldf = new MapWebViewFragment ();
                    Bundle args = new Bundle();
                    args.putString("formno",taxiFormData.getFormno());
                    ldf.setArguments(args);

//Inflate the fragment
                    //getFragmentManager().beginTransaction().add(R.id.frameLayout_home_frag, ldf).commit();

                }
            });

            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteitemDialog(position);

                    // getActivity(). getSupportFragmentManager().beginTransaction().add(R.id.frameLayout_home_frag, new TaxiFormRecordFragment()).commit();
              /*       FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(TaxiFormRecordFragment.this).attach(TaxiFormRecordFragment.this).commit();
*/
                }
            });


            return convertView;
        }

        public void filter(String charText) {
            // charText = charText.toLowerCase(Locale.getDefault());
            searchlist.clear();
            if (charText.length() == 0) {
                searchlist.addAll(taxiForm_DataArrayList);
            }
            else
            {
                for (TaxiFormData wp : taxiForm_DataArrayList)
                {
                    if (wp.getSelectdate().contains(charText))
                    {

                        searchlist.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }

        private void deleteitemDialog(final int p){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setCancelable(true);
            builder.setTitle("Are you sure delete this item?");
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // How to remove the selected item?
                    // adapter.remove(adapter.getItem(p));
                    dialog.dismiss();
                }


            });
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // How to remove the selected item?
                    // adapter.remove(adapter.getItem(p));
                    taxiFormData = searchlist.get(p);
                    db.deleteSingleRowTaxiformData(taxiFormData.getFormno());
                    db.deleteAllRowLatLongData(taxiFormData.getFormno());
                    searchlist.remove(p);
                   TaxiFormRecordHistoryAdapter.this.notifyDataSetChanged();
                }


            });

            AlertDialog alert = builder.create();
            alert.show();
        }


    }

    public class getDataAsnycTask extends AsyncTask<String, Void, String> {
        TaxiFormData taxiFormData;

        getDataAsnycTask(TaxiFormData taxiFormData){
            this.taxiFormData = taxiFormData;

        }

        ProgressDialog pd = new ProgressDialog(TaxiHistoryActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setTitle("Please Wait...");
            pd.setMessage("Uploaded Records");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String s = HTTPPostRequestMethod.postMethodforESP(params[0], JSonobjParameter(taxiFormData));
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();

            // GPSTracker.isRunning= false;


            String re = s;
            try {

                JSONObject jsonObject = new JSONObject(s);
                String status = jsonObject.getString("status");
                String id = jsonObject.getString("id");


                if (status.equals("true")) {
                    int  flag = 1;
                    db.updatedetails(taxiFormData.getId(), taxiFormData.getSelectdate(),taxiFormData.getFormno(), taxiFormData.getProjecttype(),taxiFormData.getVechicleno(), taxiFormData.getStartkm(), taxiFormData.getStartkm_image(), taxiFormData.getEndkm(), taxiFormData.getEndkmimage(), flag,taxiFormData.getSiteno(),taxiFormData.getRemark());
                    //  db.updatedetails(incri_id, edt_settaxiform_date.getText().toString(), form_no, edtproject_type.getText().toString(), edt_vehicle_no.getText().toString(), edtstartkmtext.getText().toString(), edt_startkmImage.getText().toString(), edtendkmtext.getText().toString(), edt_endkm_Image.getText().toString(), flag);
                    // db.deleteSingleRow_LatLong();

                    Toast.makeText(TaxiHistoryActivity.this, "Uploaded Successfully...", Toast.LENGTH_LONG).show();
                    /*FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(TaxiFormRecordFragment.this).attach(TaxiFormRecordFragment.this).commit();*/
                    adapter.notifyDataSetChanged();
                    Intent intent = getIntent();
                    finish();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);

                } else {

                    Toast.makeText(TaxiHistoryActivity.this, "internet is not working", Toast.LENGTH_LONG).show();
                    // flag = 0;
                    // db.updatedetails(keyid, edt_settaxiform_date.getText().toString(), edtproject_type.getText().toString(), edtstartkmtext.getText().toString(), edt_startkmImage.getText().toString(), edtendkmtext.getText().toString(), edt_endkm_Image.getText().toString(), flag);
                    //  db.updatedetails(incri_id, edt_settaxiform_date.getText().toString(), form_no, edtproject_type.getText().toString(), edt_vehicle_no.getText().toString(), edtstartkmtext.getText().toString(), edt_startkmImage.getText().toString(), edtendkmtext.getText().toString(), edt_endkm_Image.getText().toString(), flag);
                    // db.deleteSingleRow_LatLong();

                }

            } catch (JSONException e) {
                Toast.makeText(TaxiHistoryActivity.this, "please check internet is not working", Toast.LENGTH_LONG).show();
            }


        }
    }

    public JSONObject JSonobjParameter(TaxiFormData taxiFormData) {

        String IMEINumber = Utility.getIMEINumber(this);
        JSONObject jsonObject = new JSONObject();
        try {

  /*    JSONArray jsonArrayParameter = new JSONArray();
         jsonArrayParameter.put("nov_2016");
         jsonArrayParameter.put("2016");
         jsonArrayParameter.put("11");

            List<TaxiFormData> da = db.getAllTaxiformData();
            JSONObject jsonObject2;
            for(int i =0;i<da.size();i++){
              jsonObject2 = new JSONObject();
                jsonObject2.put("in",da.get(i).getFormno());
                  jsonArrayParameter.put(jsonObject2);
            }
*/

            jsonObject.put("DatabaseName", "TNS_HR");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");
            jsonObject.put("ftProject", taxiFormData.getProjecttype());
            jsonObject.put("fnStartkm", taxiFormData.getStartkm());
            jsonObject.put("ftStartkmImgUrl", taxiFormData.getStartkm_image());
            jsonObject.put("fnEndkm", taxiFormData.getEndkm());
            jsonObject.put("ftEndkmImgUrl", taxiFormData.getEndkmimage());
            jsonObject.put("Empid", empid);
            jsonObject.put("ftvehicleNo", taxiFormData.getVechicleno());
            jsonObject.put("ftTaxiFormNo", taxiFormData.getFormno());
            jsonObject.put("Remarks", taxiFormData.getRemark());
            jsonObject.put("SiteNumber", taxiFormData.getSiteno());
            jsonObject.put("taxi_IMEI", IMEINumber);




            // jsonObject.put("spName","USP_Get_Attendance");
            //  jsonObject.put("ParameterList",jsonArrayParameter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;

    }


    /*private void setValue(){
        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> id = new ArrayList<>();
        ArrayList<String> data = new ArrayList<>();
        data.add("1");
        data.add("2");
        data.add("3");
        data.add("4");
        data.add("5");
        data.add("6");

        ArrayList<ModelData> value = new ArrayList<>();

        for(int i =0; i< data.size();i++){
            name.add(data.get(i));
            id.add(data.get(i));

            ModelData data1 = new ModelData();
            data1.setName(name.get(i));
            value.add(data1);



        }
    }


    public class ModelData {
        String id;
        String name ;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public void custumDialog(final int position){
        // custom dialog

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.custom_routemap_latloghis_dialog);
        dialog.setCancelable(true);
        //  dialog.setTitle("Title...");
        dialog.getWindow().setLayout(400, LinearLayout.LayoutParams.WRAP_CONTENT);
        // set the custom dialog components - text, image and button
        TextView routeMap = (TextView) dialog.findViewById(R.id.routemap);
        TextView routeHistory = (TextView) dialog.findViewById(R.id.routehistory);
        routeMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaxiFormData taxiFormData = (TaxiFormData) adapter.getItem(position);
                String fromNumber = taxiFormData.getFormno();
                Intent intent = new Intent(TaxiHistoryActivity.this, RouteMapActivity.class);
                intent.putExtra(AppConstraint.SELECTEDFORMNUMBER, fromNumber);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        routeHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaxiFormData taxiFormData = (TaxiFormData) adapter.getItem(position);
                String fromNumber = taxiFormData.getFormno();
                Intent intent = new Intent(TaxiHistoryActivity.this, LocationHistoryActivity.class);
                intent.putExtra(AppConstraint.SELECTEDFORMNUMBER, fromNumber);
                intent.putExtra("form", "form");
                startActivity(intent);
                dialog.dismiss();
            }
        });


        dialog.show();
       // Window window = dialog.getWindow();
      //  window.setLayout(300, 300);
    }





}

