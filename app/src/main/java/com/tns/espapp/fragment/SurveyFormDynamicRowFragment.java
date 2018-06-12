package com.tns.espapp.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.provider.Settings;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tns.espapp.AppConstraint;
import com.tns.espapp.ListviewHelper;
import com.tns.espapp.R;
import com.tns.espapp.Utility.AppSingleton;
import com.tns.espapp.Utility.SharedPreferenceUtils;
import com.tns.espapp.Utility.Utility;
import com.tns.espapp.activity.CameraSurfaceViewActivity;
import com.tns.espapp.activity.RealPathUtil;
import com.tns.espapp.adapter.CheckListAdapterListview;
import com.tns.espapp.database.ChecklistData;
import com.tns.espapp.database.DatabaseHandler;
import com.tns.espapp.database.SurveyFormData;
import com.tns.espapp.fragment_new.SurveyFormDynamicRowHistoryActivity;
import com.tns.espapp.fragment_new.SurveyFormFixRowHistoryActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

import static android.app.Activity.RESULT_OK;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.MODE_WORLD_WRITEABLE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SurveyFormDynamicRowFragment extends Fragment implements View.OnClickListener {

    int row;
    private static String INSERT;
    public static final String mypreference = "chk_test";
    private SimpleDateFormat dateFormatter;
    private Calendar cal;
    private int day;
    private int month;
    private int year;

    private TableLayout tl;
    private TableRow tr;

    List<ChecklistData> getFormlist;
    ArrayList<String> listkey;

    private int PICK_ATTACHMENT_REQUEST = 11;
    private int ADD_CAPTURE_IMAGE = 12;


    int count_capture_Image = 0;
    int BTNCONSTANT = 2;

    // Temp save listItem position
    int positions;
    private String realPath;
    private String stgetImage;


    String current_date;
    ArrayList<String> arrayList;
    SurveyFormDynamicRowAdapterListview_Save siteinfodynamicrowAdapterListview_save;


    int t_id = 104;
    Button btn_send;
    List<EditText> allEds ;
    List<TextView> allTvs ;
    ListView finallistview;
    LinearLayout linearLayoutphotos;

    HashMap<String, List<EditText>> hashMap_edt = new HashMap();
    HashMap<Integer, String> hashMap = new HashMap<>();
    SQLiteDatabase myDataBase;
    private SQLiteStatement insertStmt;
    SharedPreferences sharedpreferences;

    TextView ivPhotos1;

    private LocationListener locationListener;
    private LocationManager locationManager;
    private String lats, longi;

    View list_header;
    List<SurveyFormData> fvalue = new ArrayList<>();
    String setFormname;
    CheckListAdapterListview lstadapter;
    private List<ChecklistData> check_list;
    private View getview;

    ArrayList<String> formlist;
    private Button btn_createForm;
    private Button btn_add_row;
    ArrayAdapter aa;
    DatabaseHandler db;
    Spinner spin;

    private String empid;
    private String stTable1 ="";
    private String stTable2 ="";
    private String IMEINumber;

    private String photo1="";
    private String photo2="";
    private String photo3="";
    private String photo4="";
    private String photo5="";



    public SurveyFormDynamicRowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_surveyformdynamicrow, container, false);
        getview = v;
        db = new DatabaseHandler(getActivity());
        spin = (Spinner) v.findViewById(R.id.finalfeedback_spinner1);
        TextView tv_surveyform_Fix_history=(TextView)v.findViewById(R.id.tv_surveyform_dynamic_history);


        tv_surveyform_Fix_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SurveyFormDynamicRowHistoryActivity.class));
            }
        });

        btn_createForm = (Button) v.findViewById(R.id.btn_finalfeedback_crete_form);
        btn_add_row = (Button) v.findViewById(R.id.btn_add_row);
        // TextView textview =(TextView)v.findViewById(R.id.set_formane);
        finallistview = (ListView) v.findViewById(R.id.final_listview);
        list_header = (View) v.findViewById(R.id.lay_header);

        getFormname();
        getLocation();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
        cal = Calendar.getInstance();
        System.out.println(dateFormat.format(cal.getTime()));
        current_date = dateFormat.format(cal.getTime());
        IMEINumber = Utility.getIMEINumber(getActivity());
        SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance();
        sharedPreferenceUtils.setContext(getActivity());
        empid = sharedPreferenceUtils.getString(AppConstraint.EMPID);

        sharedpreferences = getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        btn_send = (Button) v.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        addRow(v);

        return v;
    }

    private void addRow(final View v) {

        btn_add_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row = row++;
               // setView_Multiphotos(v);

           /*     TableRow row = new TableRow(getActivity());
                //add Layouts to your new row
                TextView txt = new TextView(getActivity());
                txt.setText("New Row");
                row.addView(txt);
                //add your new row to the TableLayout:
                TableLayout table = (TableLayout) getActivity().findViewById(R.id.tableLayout1);
                table.addView(row);
           */

      /*       if(fvalue.size()>1)
             {
                 for (int i = 0; i < siteinfodynamicrowAdapterListview_save.getCount(); i++) {
                     Object k = siteinfodynamicrowAdapterListview_save.getItem(i);
                     SurveyFormData finalFeedBackData = (SurveyFormData) k;
                     Log.v("checkdata", finalFeedBackData.getCount()+"");

                     fvalue.add(new SurveyFormData(setFormname, finalFeedBackData.getsNo(), finalFeedBackData.getDesc(), finalFeedBackData.getSts(), finalFeedBackData.getRemark(), finalFeedBackData.getPhotos(), finalFeedBackData.getPath(), finalFeedBackData.getCount(), finalFeedBackData.getFlag()));

                 }
             }else{

             }
             */
                fvalue.add(new SurveyFormData(""));
                siteinfodynamicrowAdapterListview_save = new SurveyFormDynamicRowAdapterListview_Save(getActivity(),R.layout.surveyformdynamicrow_data_adapter,fvalue);
                finallistview.setAdapter(siteinfodynamicrowAdapterListview_save);
                ListviewHelper.getListViewSize(finallistview);
                siteinfodynamicrowAdapterListview_save.notifyDataSetChanged();
            }
        });

    }

    private void getFormname() {

        getVOLLY_FORMNAME();
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectFormSpinner = parent.getItemAtPosition(position).toString();
                setFormname = "D"+selectFormSpinner;

            /*    LinearLayout li1 = (LinearLayout) view.findViewById(R.id.table_main2);
                if(((LinearLayout) li1).getChildCount() > 0)
                    ((LinearLayout) li1).removeAllViews();*/
                getVOLLY_FORMVARYFY(selectFormSpinner);
                //  btn_createForm.setVisibility(View.VISIBLE);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void initLinearBind(View view, List<ChecklistData> checklistDataList) {
        allEds = new ArrayList<EditText>();
        allTvs = new ArrayList<TextView>();
        listkey = new ArrayList<>();
        LinearLayout li1 = (LinearLayout) view.findViewById(R.id.table_main2);
        li1.removeAllViews();
    /*      ViewGroup parentView = (ViewGroup) li1.getParent();
                parentView.removeView(edt_getreferenceNo);*/

        for (int i = 0; i < checklistDataList.size(); i++) {
            LinearLayout LL3 = new LinearLayout(getActivity());
            // LL2.setBackgroundColor(Color.CYAN);
            LL3.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams LLParams3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            // LL3.setWeightSum(2f);
            LL3.setLayoutParams(LLParams3);

            TextView tv3 = new TextView(getActivity());
            LinearLayout.LayoutParams tvParams_3 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            tv3.setLayoutParams(tvParams_3);
            tv3.setText(checklistDataList.get(i).getName_value());
            tv3.setTextSize(12);
            tv3.setTextColor(Color.BLACK);
            tv3.setId(t_id + 1);


            final EditText etv3 = new EditText(getActivity());
            LinearLayout.LayoutParams edtParams_3 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f);
            allEds.add(etv3);
            allTvs.add(tv3);

           // hashMap.put(i, checklistDataList.get(i).getName());
            listkey.add(checklistDataList.get(i).getName());
           // hashMap_edt.put(checklistDataList.get(i).getName(), allEds);

            edtParams_3.setMargins(5, 20, 5, 20);
            etv3.setLayoutParams(edtParams_3);
            etv3.setBackgroundResource(R.drawable.edt_border);
            etv3.setPadding(10, 10, 10, 10);
            //  etv3.setHint(checklistDataList.get(i).getName());
            etv3.setTextSize(12);
            etv3.setTextColor(Color.BLACK);
            etv3.setId(i);
            LL3.addView(tv3);

            if (checklistDataList.get(i).getDataType().equalsIgnoreCase("Numeric")) {
                etv3.setInputType(InputType.TYPE_CLASS_NUMBER);

            }
            if (checklistDataList.get(i).getDataType().equalsIgnoreCase("Date")) {
                //  etv3.setText(dateFormatter.format(cal.getTime()));
                etv3.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.ic_menu_my_calendar, 0);
                etv3.setFocusable(false);
                etv3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setdate(etv3);
                    }
                });
            }

            LL3.addView(etv3);
            li1.addView(LL3);
        }

        listkey.add("Status");
        createDynamicDatabase(getActivity(), setFormname, listkey);
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


    @Override
    public void onClick(View v) {

        boolean b = true;
        final ArrayList<String> listvalue = new ArrayList<>();
        final String[] strings = new String[allEds.size()];

        for (int i = 0; i <allEds.size(); i++) {
            String s = allEds.get(i).getText().toString();
            Log.v("edttext", s+"deee");
            strings[i] = allEds.get(i).getText().toString();

            stTable1 += '"'+listkey.get(i).toString()+'"'+":"+'"'+allEds.get(i).getText().toString()+'"';
            if(i< allEds.size()-1) {
                stTable1 += ",";
            }else{
                stTable1 += "";
            }
            if (strings[i].equals("")) {
                Toast.makeText(getActivity(), "Field can't be empty", Toast.LENGTH_LONG).show();
                b = false;
                break;
            } else {
                b = true;
            }
            // listkey.add( s);
            listvalue.add(strings[i]);

        }

        if (b) {
            listvalue.add("No");
            insert(getActivity(), listvalue, listkey, setFormname);
            for (int i = 0; i < allEds.size(); i++) {
                allEds.get(i).setText("");
            }
            saveForm_pre(setFormname, "1");

       int adaptersize = siteinfodynamicrowAdapterListview_save.getCount();
            Log.v("adptersize", adaptersize+"");
            //  btn_send.setFocusable(false);
            for (int i = 0; i < siteinfodynamicrowAdapterListview_save.getCount(); i++) {
                Object k = siteinfodynamicrowAdapterListview_save.getItem(i);
                SurveyFormData sd = (SurveyFormData) k;
               // Log.v("checkdata", finalFeedBackData.getRemark()+"");

                stTable2 += '"'+"vFeildvalue1"+'"'+":"+'"'+sd.getsNo()+'"';
                stTable2 += ",";
                stTable2 +='"'+"vFeildvalue2"+'"'+":"+'"'+sd.getDesc()+'"';
                stTable2 += ",";
                stTable2 += '"'+"vFeildvalue3"+'"'+":"+'"'+sd.getSts()+'"';
                stTable2 += ",";
                stTable2 += '"'+"vFeildvalue4"+'"'+":"+'"'+sd.getRemark()+'"';
                stTable2 += ",";
                stTable2 +='"'+"Photo1"+'"'+":"+'"'+sd.getImg1()+'"';
                stTable2 += ",";
                stTable2 += '"'+"Photo2"+'"'+":"+'"'+sd.getImg2()+'"';
                stTable2 += ",";
                stTable2 += '"'+"Photo3"+'"'+":"+'"'+sd.getImg3()+'"';
                stTable2 += ",";
                stTable2 += '"'+"Photo4"+'"'+":"+'"'+sd.getImg4()+'"';
                stTable2 += ",";
                stTable2 += '"'+"Photo5"+'"'+":"+'"'+sd.getImg5()+'"';

               // if(finalFeedBackData.getsNo()!=null&&)


              /*  ftpConnect_Capture ftpConnect = new ftpConnect_Capture();
                for(String img : stImageCaptureList){
                    ftpConnect.uploadFile(new File(img));
                }*/

                db.insertFinalCheckListData_Save(new SurveyFormData(setFormname, sd.getsNo(), sd.getDesc(), sd.getSts(), sd.getRemark(), sd.getCount(),sd.getImg1(),sd.getImg2(),sd.getImg3(),sd.getImg4(),sd.getImg5(),sd.getImageText1(),sd.getImageText2(),sd.getImageText3(),sd.getImageText4(),sd.getImageText5(), sd.getFlag()));
                sendAllDataServerTable2(stTable2);

                Log.v("jsontable2", JsonParameterSendServerTable2(stTable2).toString());
                Log.v("printTable2", stTable2);
            }
            sendAllDataServerTable1(stTable1);
            Log.v("jsontable1", JsonParameterSendServerTable1(stTable1).toString());
            Log.v("printTable1", stTable1);
            fvalue.clear();
            siteinfodynamicrowAdapterListview_save.clear();
            siteinfodynamicrowAdapterListview_save.notifyDataSetChanged();
        }




    }


    public void createDynamicDatabase(Context context, String tableName, ArrayList<String> title) {
     //DATABASE_NAME=   "/mnt/sdcard/my.db";

        try {

            int i;
            String querryString = null;
            myDataBase = context.openOrCreateDatabase(db.DATABASE_NAME, MODE_WORLD_WRITEABLE, null);
            if (title.size() == 1) {//Opens database in writable mode.
                querryString = title.get(0) + " text";
            }

           /*
           else if(title.size() ==2) {
                querryString = title.get(0)+" text,";
                querryString+= title.get(1) +" text";
            }
            */

            else {
                querryString = title.get(0) + " text,";
                for (i = 1; i < title.size() - 1; i++) {
                    querryString += title.get(i);
                    querryString += " text";
                    querryString += ",";
                }
                querryString += title.get(i) + " text";
            }

            querryString = "CREATE TABLE IF NOT EXISTS " + tableName + "(" +" id integer primary key autoincrement,"+ querryString + ");";
            //  Toast.makeText(context, querryString, Toast.LENGTH_LONG).show();
            myDataBase.execSQL(querryString);
            // Toast.makeText(context, "Execute Query", Toast.LENGTH_LONG).show();

        } catch (SQLException ex) {
            Log.e(ex.getClass().getName(), ex.getMessage(), ex);
        }
    }

    public void insert(Context context, ArrayList<String> array_vals, ArrayList<String> title, String TABLE_NAME) {

        try {

            myDataBase = context.openOrCreateDatabase(db.DATABASE_NAME, MODE_WORLD_WRITEABLE, null);         //Opens database in writable mode.

            String titleString = null;
            String markString = null;
            int i;
            if (title.size() == 1) {
                titleString = title.get(0);
                markString = "?";
            }
        /*    else if(title.size() ==2)
            {
                titleString = title.get(0) + ",";
                markString = "?,";
                titleString+= title.get(1) +" text";
                markString+="?";
            }*/
            else {

                titleString = title.get(0) + ",";
                markString = "?,";

                for (i = 1; i < title.size() - 1; i++) {
                    titleString += title.get(i);
                    titleString += ",";
                    markString += "?,";
                }
                titleString += title.get(i);
                markString += "?";
            }
            INSERT = "insert into " + TABLE_NAME + "(" + titleString + ")" + "values" + "(" + markString + ")";
            int s = 0;

            while (s < array_vals.size()) {

                System.out.println("Size of array1" + array_vals.size());
                int j = 1;
                this.insertStmt = this.myDataBase.compileStatement(INSERT);
                for (int k = 0; k < title.size(); k++) {

                    insertStmt.bindString(j, array_vals.get(k + s));
                    j++;

                }

                s += title.size();

            }
            insertStmt.executeInsert();
        } catch (SQLException ex) {
            Log.e(ex.getClass().getName(), ex.getMessage(), ex);
        }


    }

    public void saveForm_pre(String key, String value) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getForm_pre(String key) {

        String gets = sharedpreferences.getString(key, "");
        return gets;
    }

    public void clear_form_shared_pre() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_ATTACHMENT_REQUEST) {
                Uri uri = data.getData();
                try {
                    realPath = RealPathUtil.getPath_File_Attacah(getActivity(), uri);
                } catch (NullPointerException e) {
                    Toast.makeText(getActivity(), "Not Found", Toast.LENGTH_LONG).show();
                }

                if (realPath != null) {
                    File file = new File(realPath);
                    stgetImage = file.getName();
                    //  attachment_ImageList.add(stgetImage);
                    //  attachmentDatas.add(new AttachmentData(stgetImage, realPath));

                 /*
                    adapter_attachment = new AttachmentAdapter(getActivity(), R.layout.add_attachment_feedbackfrag_adapter, attachment_ImageList);
                    lst_attachment.setAdapter(adapter_attachment);
                    ListviewHelper.getListViewSize(lst_attachment);
                 */
                    if (stgetImage != null) {
                        //  linearLayout_add_attachment.addView(tableLayout(stgetImage), linearLayout_add_attachment.getChildCount());
                    }

                }
            }
            if (requestCode == ADD_CAPTURE_IMAGE) {

                onCaptureImageResult(data);
            }
        }
            if(requestCode==2) {

                try {
                    String message = data.getStringExtra("text");
                    String path = data.getStringExtra("path");
                    SurveyFormData chk = fvalue.get(positions);
                    String photoss = path;
                    // chk.setPath(path);
                    chk.setPath(photoss);
                    int counter = chk.getCount() + 1;
                    chk.setCount(counter);

                    if (counter == 1) {
                        photo1 = photoss;
                     //   stImageCaptureListCustomPath.add(photoss);
                        chk.setImg1(photoss);
                        chk.setImageText1(message);
                    }
                    if (counter == 2) {
                        photo2 = photoss;
                     //   stImageCaptureListCustomPath.add(photoss);
                        chk.setImg2(photoss);
                        chk.setImageText2(message);

                    }
                    if (counter == 3) {
                        photo3 = photoss;
                        chk.setImg3(photoss);
                        chk.setImageText3(message);
                    }
                    if (counter == 4) {
                        photo4 = photoss;

                        chk.setImg4(photoss);
                        chk.setImageText4(message);
                    }
                    if (counter == 5) {
                        photo5 = photoss;

                        chk.setImg5(photoss);
                        chk.setImageText5(message);
                    }
                    siteinfodynamicrowAdapterListview_save.notifyDataSetChanged();
                    Toast.makeText(getActivity(), message + "," + path, 2000).show();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
        }

    }


    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }


    public class SurveyFormDynamicRowAdapterListview_Save extends ArrayAdapter {
        Context context;
        List<SurveyFormData> getListArray;

        public SurveyFormDynamicRowAdapterListview_Save(Context context, int resource, List<SurveyFormData> list) {
            super(context, resource, list);
            this.context = context;
            this.getListArray = list;

        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {


            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.surveyformdynamicrow_data_adapter, parent, false);
            }

            //  lst_attachment =(ListView)convertView.findViewById(R.id.lstcapture_adapter);

            final TextView tvSno = (TextView) convertView.findViewById(R.id.tv_sno);
            final TextView tvDesc = (TextView) convertView.findViewById(R.id.tv_description);
            final TextView tvSts = (TextView) convertView.findViewById(R.id.tv_status);
            final EditText tvRemark = (EditText) convertView.findViewById(R.id.tv_remark);
            TextView ivPhotos = (TextView) convertView.findViewById(R.id.tv_photos);
            Button buttonrow =(Button)convertView.findViewById(R.id.btn_add_row);
            ivPhotos1 = (TextView) convertView.findViewById(R.id.tv_photos_1);

            final SurveyFormData data = getListArray.get(position);
            // data.setRemark(tvRemark.getText().toString());
            // data.setSts(tvSts.getText().toString());

             tvSno.setText(data.getsNo());
             tvDesc.setText(data.getDesc());
             tvSts.setText(data.getSts());
              tvRemark.setText(data.getRemark());
             ivPhotos.setText(data.getPhotos());

            ivPhotos.setBackgroundResource(android.R.drawable.ic_input_add);
            ivPhotos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (data.getCount()<5) {
                        //  captureImage(position);
                        positions = position;
                        Intent i = new Intent(getActivity(), CameraSurfaceViewActivity.class);
                        startActivityForResult(i, 2);

                    }else{
                        Toast.makeText(getActivity(),"Maximum 5 Photo",Toast.LENGTH_LONG).show();
                    }


                }
            });


            tvSno.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    data.setsNo(tvSno.getText().toString());
                }
            });



            tvDesc.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    getListArray.get(position).setDesc(tvDesc.getText().toString());
                  //  data.setDesc(tvDesc.getText().toString());
                }
            });

            tvSts.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    data.setSts(tvSts.getText().toString());
                }
            });



            tvRemark.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    data.setRemark(tvRemark.getText().toString());
                }
            });


            if (data.getCount() != 0) {

                ivPhotos1.setVisibility(View.VISIBLE);
                ivPhotos1.setText(data.getCount() + "");

                Log.v("AllImage", data.getPath());
                String[] words = data.getPath().split(":::");
                //splits the string based on whitespace
                data.setMultiphotos(Arrays.asList(words));

                for (String w : data.getMultiphotos()) {
                    Log.v("someImage", w);
                }

            }

            ivPhotos1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    custumDialog(getListArray);
                }
            });

       /*
        buttonrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    fvalue.add(new SurveyFormData(data.getsNo(),data.getDesc(),data.getSts(),data.getRemark(),String.valueOf(data.getCount())));

                    siteinfodynamicrowAdapterListview_save = new SurveyFormDynamicRowAdapterListview_Save(getActivity(),R.layout.final_checklist_data_adapter,fvalue);
                    finallistview.setAdapter(siteinfodynamicrowAdapterListview_save);
                    siteinfodynamicrowAdapterListview_save.notifyDataSetChanged();
                }
            });
       */
            siteinfodynamicrowAdapterListview_save.notifyDataSetChanged();

            return convertView;

        }

    }
    public void custumDialog(List<SurveyFormData> as) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.surveyform_preview_img_dialoge);
        //  dialog.setTitle("Title...");
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

   /*   ImageView iv =(ImageView)dialog.findViewById(R.id.iv_surveyform_image);
        TextView tv =(TextView)dialog.findViewById(R.id.tv_surveyform_text);     */
        ListView lst =(ListView)dialog.findViewById(R.id.lst_sur1);

       ImgPreviewAdpater imgadapter = new ImgPreviewAdpater(getActivity(),R.layout.surveyform_preview_img_dialoge,as);
        lst.setAdapter(imgadapter);

    /*    Bitmap bitmap = BitmapFactory.decodeFile(sttext);
        iv.setImageBitmap(bitmap);
        tv.setText(sttext);
   */
        dialog.show();
    }


    private class ImgPreviewAdpater extends ArrayAdapter {
        String s;
        Context context;
        private List<SurveyFormData> list = null;
        //   private CustomOnItemClickListener customOnItemClickListener;
        public ImgPreviewAdpater(Context context, int resource, List<SurveyFormData> lst) {
            super(context, resource, lst);
            this.context = context;
            this.list = lst;
            db = new DatabaseHandler(context);

        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            TextView tv_entry, tv_history;
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.surveyform_preview_img_dialoge, parent, false);
            ImageView iv1 =(ImageView)convertView.findViewById(R.id.iv_surveyform_image1);
            final TextView tv1 =(TextView) convertView.findViewById(R.id.tv_surveyform_text1);
            ImageView iv2 =(ImageView)convertView.findViewById(R.id.iv_surveyform_image2);
            TextView tv2 =(TextView)convertView.findViewById(R.id.tv_surveyform_text2);
            ImageView iv3 =(ImageView)convertView.findViewById(R.id.iv_surveyform_image3);
            TextView tv3 =(TextView)convertView.findViewById(R.id.tv_surveyform_text3);
            ImageView iv4 =(ImageView)convertView.findViewById(R.id.iv_surveyform_image4);
            TextView tv4 =(TextView)convertView.findViewById(R.id.tv_surveyform_text4);
            ImageView iv5 =(ImageView)convertView.findViewById(R.id.iv_surveyform_image5);
            TextView tv5 =(TextView)convertView.findViewById(R.id.tv_surveyform_text5);

            // int colorPos = position % colors.length;
            // convertView.setBackgroundColor(colors[colorPos]);
            SurveyFormData s = list.get(position);

            Bitmap bitmap = BitmapFactory.decodeFile(s.getImg1());
            iv1.setImageBitmap(bitmap);
            tv1.setText(s.getImageText1());
            Bitmap bitmap2 = BitmapFactory.decodeFile(s.getImg2());
            iv2.setImageBitmap(bitmap2);
            tv2.setText(s.getImageText2());
            Bitmap bitmap3 = BitmapFactory.decodeFile(s.getImg3());
            iv3.setImageBitmap(bitmap3);
            tv3.setText(s.getImageText3());
            Bitmap bitmap4 = BitmapFactory.decodeFile(s.getImg4());
            iv4.setImageBitmap(bitmap4);
            tv4.setText(s.getImageText4());
            Bitmap bitmap5 = BitmapFactory.decodeFile(s.getImg5());
            iv5.setImageBitmap(bitmap5);
            tv5.setText(s.getImageText5());

          /*  tv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SurveyFormData s = list.get(position);
                    s .setImageText1(tv1.getText().toString());
                  list.set(position,s);
                  notifyDataSetChanged();
                }
            });*/


            return convertView;
        }

    }


    public void captureImage(int pos) {
        positions = pos;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, ADD_CAPTURE_IMAGE);
    }

    /**
     * Set capture image to database and set to image preview
     *
     * @param data
     */
    private void onCaptureImageResult(Intent data) {
        String capturepath = "";
        Bundle extras = data.getExtras();
        Bitmap thumbnail = (Bitmap) extras.get("data");

        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 80, bytes);

        SimpleDateFormat time_formatter = new SimpleDateFormat("HH:mm:ss");
        String current_time_str = time_formatter.format(System.currentTimeMillis());

        String destinationpath = Environment.getExternalStorageDirectory().toString();
        File destination = new File(destinationpath + "/ESP/SurveyFormDynamic/");
        if (!destination.exists()) {
            destination.mkdirs();
        }

        File file = null;
        FileOutputStream fo;
        try {
            // destination.createNewFile();

            capturepath = current_date + "_" + current_time_str + ".jpg";

            file = new File(destination, capturepath);
            fo = new FileOutputStream(file);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println(file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\") + 1));
        String path = (file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\") + 1));

        String startkmImageEncodeString = encodeToBase64(thumbnail, Bitmap.CompressFormat.JPEG, 100);
        String totalcapturepath = destinationpath + "/ESP/SurveyFormDynamic/" + capturepath;

        //count1++;

        SurveyFormData chk = fvalue.get(positions);
        String photoss = chk.getPath() + path ;
        // chk.setPath(path);
        chk.setPath(photoss);
        int counter = chk.getCount() + 1;
        chk.setCount(counter);

        if(counter==1){
            photo1 = totalcapturepath;
            chk.setImg1(startkmImageEncodeString);
        }
        if(counter==2){
            photo2= totalcapturepath;
            chk.setImg2(startkmImageEncodeString);
        }
        if(counter==3){
            photo3= totalcapturepath;
            chk.setImg3(startkmImageEncodeString);
        }
        if(counter==4){
            photo4= totalcapturepath;
            chk.setImg4(startkmImageEncodeString);
        }
        if(counter==5){
            photo5= totalcapturepath;
            chk.setImg5(startkmImageEncodeString);

        }


        //  setPhotosData(positions,path,fvalue);
        siteinfodynamicrowAdapterListview_save.notifyDataSetChanged();

    }


    private void getLocation() {

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                lats = String.valueOf(location.getLatitude());
                longi = String.valueOf(location.getLongitude());

                //   Toast.makeText(getActivity(),lats+","+longi,300).show();
                // tv.append(s);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }
        };

        locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        //noinspection Missing Permission
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);


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
            jsonObject.put("DatabaseName", "TNS_NT_PMS");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");
            jsonObject.put("spName", "GetDynamicColumns");
            jsonObject.put("ParameterList", jsonArrayParameter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, AppConstraint.DYNAMICGETJSONDATA, jsonObject,
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
        check_list.clear();
        try {
            JSONObject obj1 = new JSONObject(jsonObject);
            JSONArray jsonArray1 = obj1.getJSONArray("Table");
            JSONArray jsonArrayTable2 = obj1.getJSONArray("Table1");
            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject jObj2 = jsonArray1.getJSONObject(i);
                Log.d("FormJSONDetails-->", jObj2.toString());
                String vFormName = jObj2.getString("vFormName");
                String vNameInput = jObj2.getString("fieldName");
                String vNameValue = jObj2.getString("fieldValue");
                String Datatype = jObj2.getString("dataType");

                ChecklistData checklistData = new ChecklistData(vFormName, vNameInput, vNameValue, Datatype, 0);
                check_list.add(checklistData);

                //db.insertCheckListData(checklistData);

          /*      btn_createForm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        for( ChecklistData c : check_list){

                            String formno=   c.getFormno().replaceAll("\\s","");
                            ChecklistData checklistDatas = new ChecklistData(formno, c.getName(), c.getName_value(), c.getDataType(),c.getSize(),c.getDecimal(), 1);
                            db.insertCheckListData(checklistDatas);
                        }
                        btn_createForm.setVisibility(View.GONE);
                    }
                });*/
            }

            for (int i = 0; i < jsonArrayTable2.length(); i++) {
                JSONObject jObj2 = jsonArrayTable2.getJSONObject(i);
                Log.d("FormJSONDetails-->", jObj2.toString());
                // int Srno = jObj2.getInt("Srno");
                //   String vFormName = jObj2.getString("vFormName");
                String vFeildvalue1 = jObj2.getString("vFeildvalue1");
                String vFeildvalue2 = jObj2.getString("vFeildvalue2");
                String vFeildvalue3 = jObj2.getString("vFeildvalue3");
                String vFeildvalue4 = jObj2.getString("vFeildvalue4");
                String Photo = jObj2.getString("Photo");
                // int sno=  Integer.parseInt(vFeildvalue1);

                TextView tv_vFeildvalue1 = (TextView) list_header.findViewById(R.id.header_sno);
                TextView tv_vFeildvalue2 = (TextView) list_header.findViewById(R.id.header_des);
                TextView tv_vFeildvalue3 = (TextView) list_header.findViewById(R.id.header_remark);
                TextView tv_vFeildvalue4 = (TextView) list_header.findViewById(R.id.header_status);
                TextView tv_photo = (TextView) list_header.findViewById(R.id.header_photo);

                tv_vFeildvalue1.setText(vFeildvalue1);
                tv_vFeildvalue2.setText(vFeildvalue2);
                tv_vFeildvalue3.setText(vFeildvalue3);
                tv_vFeildvalue4.setText(vFeildvalue4);
                tv_photo.setText("Photo" + "(" + Photo + ")");

                list_header.setVisibility(View.VISIBLE);

            }

            initLinearBind(getview, check_list);

        /*    fvalue.add(new SurveyFormData(1));
            siteinfodynamicrowAdapterListview_save = new SurveyFormDynamicRowAdapterListview_Save(getActivity(),R.layout.final_checklist_data_adapter,fvalue);
            finallistview.setAdapter(siteinfodynamicrowAdapterListview_save);
            siteinfodynamicrowAdapterListview_save.notifyDataSetChanged();*/


        /*    if(check_list.size() == 0){
                btn_createForm.setEnabled(false);
            }

            lstadapter = new CheckListAdapterListview(getActivity(), R.layout.checklist_data_adapter, check_list) ;

            listView.setAdapter(lstadapter);
            ListviewHelper.getListViewSize(listView);
            lstadapter.notifyDataSetChanged();*/


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
            jsonObject.put("DatabaseName", "TNS_NT_PMS");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");
            jsonObject.put("spName", "GetFormName");
            //  jsonObject.put("ParameterList","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, AppConstraint.DYNAMICFORMNAME, jsonObject,
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
        String formNameno = "";
        formlist = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(jsonObject);
            //   {"FormName":[{"FormName":"Form3"}]}

            JSONArray jsonArray = obj.getJSONArray("FormName");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObj2 = jsonArray.getJSONObject(i);
                formNameno = jObj2.getString("FormName");
                formlist.add(formNameno);
            }
            aa = new ArrayAdapter(getActivity(), R.layout.spinner_layout, formlist);
            aa.setDropDownViewResource(R.layout.spinner_layout);
            spin.setAdapter(aa);
            spin.setSelection(aa.getCount() - 1);
            //  getVOLLY_FORMVARYFY(formNameno);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendAllDataServerTable1(String table1) {
        // String mStringUrl = "http://www.yoururl.com";
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, AppConstraint.DYNAMICINSERTJSONDATA1, JsonParameterSendServerTable1( table1),
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        parseSenddataJsonTable1(response.toString());
                        Log.e("Insert Data", response + "");
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error Insert Data", error + "");
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

    private JSONObject JsonParameterSendServerTable1(String table1) {
      String dat ="";

        int count = db.getLastInsertIdFinalcheklistTable1(setFormname);
        Log.e("printcount", count + "");

        String getDate = "";
        JSONObject jsonObject = new JSONObject();
         // int count = db.getCountFinalChecklist2_Save();
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MMM-yyyy");
        try {

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy");

            Date dtt = df.parse(current_date);
            Date ds = new Date(dtt.getTime());
            dat = df.format(ds);
            getDate= dateFormat2.format(ds);
            System.out.println(getDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            JSONArray jsonArrayParameter = new JSONArray();

            String date = dat.replaceAll("-", "");
            String trnno =  '"'+"vTrnno"+'"'+":"+'"'+empid+"-"+date+"-"+count+'"';
            jsonArrayParameter.put(trnno);
            jsonArrayParameter.put('"'+"DeviceID"+'"'+":"+'"'+IMEINumber+'"');
            jsonArrayParameter.put('"'+"dsysdate"+'"'+":"+'"'+getDate+'"');

            jsonArrayParameter.put(table1);

   /*         JSONArray jsonArrayParameter1 = new JSONArray();
            jsonArrayParameter1.put(trnno);
            jsonArrayParameter1.put('"'+"DeviceID"+'"'+":"+'"'+IMEINumber+'"');
            jsonArrayParameter1.put(stTable2);
*/
            jsonObject.put("DatabaseName", "TNS_NT_PMS");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");
            jsonObject.put("spName", "insertDynamicData11");
            jsonObject.put("Formname", setFormname.substring(1));
            jsonObject.put("ParameterList", jsonArrayParameter);
           // jsonObject.put("ParameterList1", jsonArrayParameter1);

            Log.e("FormName", setFormname + "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void sendAllDataServerTable2(String table2) {
        // String mStringUrl = "http://www.yoururl.com";
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, AppConstraint.DYNAMICINSERTJSONDATA2, JsonParameterSendServerTable2(table2),
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        parseSenddataJsonTable2(response.toString());
                        Log.e("Insert Data", response + "");
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error Insert Data", error + "");
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

    private JSONObject JsonParameterSendServerTable2(String table2) {
        String dat ="";
       /* List<SurveyFormData> getall= db.getAllFinalChecklist2_Save(setFormname);
        for(SurveyFormData data: getall){
            Log.e("printlist", data.getDesc() + "");
        }*/
        int count = db.getLastInsertIdFinalcheklistTable1(setFormname);;
        Log.e("printcount", count + "");

        String getDate = "";
        JSONObject jsonObject = new JSONObject();
        // int count = db.getCountFinalChecklist2_Save();
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MMM-yyyy");
        try {

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy");

            Date dtt = df.parse(current_date);
            Date ds = new Date(dtt.getTime());
            dat = df.format(ds);
            getDate= dateFormat2.format(ds);
            System.out.println(getDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            JSONArray jsonArrayParameter = new JSONArray();

            String date = dat.replaceAll("-", "");
            String trnno =  '"'+"vTrnno"+'"'+":"+'"'+empid+"-"+date+"-"+count+'"';
      /*      jsonArrayParameter.put(trnno);
            jsonArrayParameter.put('"'+"DeviceID"+'"'+":"+'"'+IMEINumber+'"');
            jsonArrayParameter.put('"'+"dsysdate"+'"'+":"+'"'+getDate+'"');

            jsonArrayParameter.put(stTable1);*/

            JSONArray jsonArrayParameter1 = new JSONArray();
            jsonArrayParameter1.put(trnno);
            jsonArrayParameter1.put('"'+"DeviceID"+'"'+":"+'"'+IMEINumber+'"');
            jsonArrayParameter1.put(table2);

            jsonObject.put("DatabaseName", "TNS_NT_PMS");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");
            jsonObject.put("spName", "insertDynamicData2");
            jsonObject.put("Formname", setFormname.substring(1));
           // jsonObject.put("ParameterList", jsonArrayParameter);
            jsonObject.put("ParameterList1", jsonArrayParameter1);

            Log.e("FormName", setFormname + "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    public void parseSenddataJsonTable1(String jsonObject) {


        try {
            JSONObject obj = new JSONObject(jsonObject);
            //   {"FormName":[{"FormName":"Form3"}]}


            int res = obj.getInt("count");
            if(res >0) {
                if(db. getLastInsertIdFinalcheklistTable1(setFormname)>0){
                    db.updateFinalcheklistTable1Flag(db.getLastInsertIdFinalcheklistTable1(setFormname),1,setFormname);
                }

                Toast.makeText(getActivity(), "Upload Successfully" + res, Toast.LENGTH_SHORT).show();
            }

            //  getVOLLY_FORMVARYFY(formNameno);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void parseSenddataJsonTable2(String jsonObject) {




        try {
            JSONObject obj = new JSONObject(jsonObject);
            int res = obj.getInt("count");
            if(res >0) {

                if(db.getLastInsertIdFinalcheklist()>0){
                    db.updateFinalcheklistFlag(db.getLastInsertIdFinalcheklist(),1);
                }

                Toast.makeText(getActivity(), "Upload Successfully" + res, Toast.LENGTH_SHORT).show();
            }


            //  getVOLLY_FORMVARYFY(formNameno);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public class ftpConnect_Capture {

        public void uploadFile(File fileName) {
            FTPClient client = new FTPClient();
            try {
                client.connect(AppConstraint.FTP_HOST, 21);
                client.login(AppConstraint.FTP_USER, AppConstraint.FTP_PASS);
                client.setType(FTPClient.TYPE_BINARY);
                // client.changeDirectory("TNSSOFT Folder");

                try {

                    // client.changeDirectory("/upload/"); //I want to upload picture in MyPictures directory/folder. you can use your own.
                }
                catch (Exception e) {

                    //client.createDirectory("/upload/");
                    // client.changeDirectory("/upload/");
                }

                client.upload(fileName, new MyTransferListener_Capture());

            } catch (Exception e) {
                e.printStackTrace();
                try {
                    client.disconnect(true);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }

        }

    }


    /*******
     * Used to file upload and show progress
     **********/

    private class MyTransferListener_Capture implements FTPDataTransferListener {

        public void started() {

            // Transfer started
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    //    Toast.makeText(getActivity(), " Upload Started ...", Toast.LENGTH_SHORT).show();
                }
            });

            //System.out.println(" Upload Started ...");
        }

        public void transferred(final int length) {


            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    //   Toast.makeText(getActivity(), " transferred ..." + length, Toast.LENGTH_SHORT).show();
                }
            });


        }

        public void completed() {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });

            // Transfer completed


            //System.out.println(" completed ..." );
        }

        public void aborted() {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(getActivity(), " transfer aborted , please try again...", Toast.LENGTH_SHORT).show();

                }
            });

            // Transfer aborted

            //System.out.println(" aborted ..." );
        }

        public void failed() {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), " failed...", Toast.LENGTH_SHORT).show();


                }
            });

            // Transfer failed
            System.out.println(" failed ...");
        }

    }


  /*  public void setView_Multiphotos(View v) {
        tl = (TableLayout) v.findViewById(R.id.tableLayout1);
   *//*     TableRow tr_head = new TableRow(getActivity());
        tr_head.setId(10);
        tr_head.setBackgroundColor(Color.GRAY);
        tr_head.setLayoutParams(new TableRow.LayoutParams(
                ActionBar.LayoutParams.FILL_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT));


        TextView label_sno = new TextView(getActivity());
        LinearLayout.LayoutParams param = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,.4f);
        label_sno.setLayoutParams(param);
        label_sno.setGravity(Gravity.CENTER);
        label_sno.setBackgroundResource(R.drawable.edit_text_design_table);

        label_sno.setPadding(0, 5, 0, 5);
        label_sno.setText("SNo");
        label_sno.setTextColor(Color.WHITE);
        //description........................................................................................
        TextView label_desc = new TextView(getActivity());
        LinearLayout.LayoutParams paramdesc = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1f);
        label_desc.setLayoutParams(paramdesc);
        label_desc.setGravity(Gravity.CENTER);
        label_desc.setBackgroundResource(R.drawable.edit_text_design_table);

        label_desc.setPadding(0, 5, 0, 5);
        label_desc.setText("Description");
        label_desc.setTextColor(Color.WHITE);

        //satus........................................................................................
        TextView label_sts = new TextView(getActivity());
        LinearLayout.LayoutParams paramsts = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,.5f);
        label_sts.setLayoutParams(paramsts);
        label_sts.setGravity(Gravity.CENTER);
        label_sts.setBackgroundResource(R.drawable.edit_text_design_table);
        label_sts.setPadding(0, 5, 0, 5);
        label_sts.setText("Status");
        label_sts.setTextColor(Color.WHITE);

        //status........................................................................................
        TextView label_remark = new TextView(getActivity());
        LinearLayout.LayoutParams paramremark = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,.6f);

        label_remark.setLayoutParams(paramremark);
        label_remark.setGravity(Gravity.CENTER);
        label_remark.setBackgroundResource(R.drawable.edit_text_design_table);
        label_remark.setPadding(0, 5, 0, 5);
        label_remark.setText("Remark");
        label_remark.setTextColor(Color.WHITE);

        //status........................................................................................
        TextView label_photos = new TextView(getActivity());
        LinearLayout.LayoutParams param_photos = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,.7f);

        label_photos.setLayoutParams(param_photos);
        label_photos.setGravity(Gravity.CENTER);
        label_photos.setBackgroundResource(R.drawable.edit_text_design_table);
        label_photos.setPadding(0, 5, 0, 5);
        label_photos.setText("Photos");
        label_photos.setTextColor(Color.WHITE);



        tr_head.addView(label_sno);
        tr_head.addView(label_desc);
        tr_head.addView(label_sts);
        tr_head.addView(label_remark);
        tr_head.addView(label_photos);
*//*
        // add the column to the table row here



   *//*     tl.addView(tr_head, new TableLayout.LayoutParams(
                ActionBar.LayoutParams.FILL_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT));

        ListView lst_attachment = new ListView(getActivity());*//*


        tr = new TableRow(getActivity());
        tr.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        EditText label_sno2 = new EditText(getActivity());
        LinearLayout.LayoutParams param2 = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, .4f);
        label_sno2.setLayoutParams(param2);

        label_sno2.setGravity(Gravity.CENTER);
        label_sno2.setBackgroundResource(R.drawable.edit_text_design);

        label_sno2.setPadding(0, 5, 0, 5);
     //   label_sno2.setText("");
        label_sno2.setTextColor(Color.BLACK);
        label_sno2.setTextSize(14);
        //description........................................................................................
        EditText label_desc2 = new EditText(getActivity());
        LinearLayout.LayoutParams paramdesc2 = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        label_desc2.setLayoutParams(paramdesc2);
        label_desc2.setGravity(Gravity.CENTER);
        label_desc2.setBackgroundResource(R.drawable.edit_text_design);

        label_desc2.setPadding(0, 5, 0, 5);
        //label_desc2.setText("");
        label_desc2.setTextColor(Color.BLACK);
        label_desc2.setTextSize(14);

        //status........................................................................................
        EditText label_sts2 = new EditText(getActivity());
        LinearLayout.LayoutParams paramsts2 = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, .5f);
        label_sts2.setLayoutParams(paramsts2);
        label_sts2.setGravity(Gravity.CENTER);
        label_sts2.setBackgroundResource(R.drawable.edit_text_design);
        label_sts2.setPadding(0, 5, 0, 5);
        // label_sts2.setText(value.get(k).getSts());
        label_sts2.setTextColor(Color.BLACK);
        label_sts2.setTextSize(14);

        //remark........................................................................................
        EditText label_remark2 = new EditText(getActivity());
        LinearLayout.LayoutParams paramremark2 = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, .6f);

        label_remark2.setLayoutParams(paramremark2);
        label_remark2.setGravity(Gravity.CENTER);
        label_remark2.setBackgroundResource(R.drawable.edit_text_design);
        label_remark2.setPadding(0, 5, 0, 5);
        // label_remark2.setText(value.get(k).getRemark());
        label_remark2.setTextColor(Color.BLACK);
        label_remark2.setTextSize(14);

        //photos........................................................................................
        linearLayoutphotos = new LinearLayout(getActivity());
        LinearLayout.LayoutParams paramlinearLayoutphotos = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, .7f);
        linearLayoutphotos.setLayoutParams(paramlinearLayoutphotos);
        linearLayoutphotos.setOrientation(LinearLayout.VERTICAL);
        linearLayoutphotos.setBackgroundResource(R.drawable.edit_text_design);

        TextView label_photos2 = new TextView(getActivity());
        final LinearLayout.LayoutParams param_photos2 = new TableRow.LayoutParams(50, 70);

        label_photos2.setLayoutParams(param_photos2);
        label_photos2.setGravity(Gravity.CENTER);
        label_photos2.setBackgroundResource(R.drawable.edit_text_design);
        label_photos2.setPadding(0, 5, 0, 5);
        // label_photos2.setText(value.get(k).getPhotos());
        label_photos2.setBackgroundResource(android.R.drawable.ic_input_add);
        label_photos2.setTextColor(Color.BLACK);
        label_photos2.setId(1);


        label_photos2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int btnId = v.getId();
                switch (1) {
                    case 2: {
                        arrayList = new ArrayList<String>();
                        //  arrayList.add(String.valueOf(btnId));
                        if (count_capture_Image < 5) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, ADD_CAPTURE_IMAGE);
                            count_capture_Image++;

                        }
                    }
                    break;
                    case 3: {
                        arrayList = new ArrayList<String>();
                        if (count_capture_Image < 5) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, ADD_CAPTURE_IMAGE);
                            count_capture_Image++;
                        }
                        // arrayList.add(String.valueOf(btnId));
                    }
                    case 4: {
                        arrayList = new ArrayList<String>();
                        if (count_capture_Image < 5) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, ADD_CAPTURE_IMAGE);
                            count_capture_Image++;

                        }

                    }
                    break;


                }




            }
        });

        linearLayoutphotos.addView(label_photos2);

        tr.addView(label_sno2);
        tr.addView(label_desc2);
        tr.addView(label_sts2);
        tr.addView(label_remark2);
        tr.addView(linearLayoutphotos);

        tl.addView(tr, new TableLayout.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT));



        tr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableRow tr = (TableRow) v;
                for (int i = 0; i < tr.getChildCount(); i++) {
                    TextView companyTV, valueTV, yearTV;
                    String company = null, value, year;
                    final View view = tr.getChildAt(i);
                    if (view instanceof TextView) {
                        companyTV = (TextView) tr.getChildAt(i);
                    }
                    if (view instanceof CheckBox) {

                    }


                }
            }
        });


    }*/


}
