package com.tns.espapp.fragment;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.tns.espapp.R;
import com.tns.espapp.database.SurveyFormData;
import com.tns.espapp.database.ChecklistData;
import com.tns.espapp.database.DatabaseHandler;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_WORLD_WRITEABLE;


/**
 * A simple {@link Fragment} subclass.
 */
public class CheckListSavedFragment extends Fragment implements View.OnClickListener {
    private static  String INSERT  ;
    public static final String mypreference = "chk_test";
    SharedPreferences sharedpreferences;
    private SimpleDateFormat dateFormatter;
    private Calendar cal;
    private int day;
    private int month;
    private int year;
    private DatabaseHandler db;

    private TableLayout tl;
    private TableRow tr;

    List<ChecklistData> getFormlist;
    ArrayList <String>  listkey;

    // Temp save listItem position
    int positions;
     String current_date;
     ArrayList<String> arrayList;
    int t_id = 104;
    Button   btn_send;
    List<EditText> allEds = new ArrayList<EditText>();
     ListView finallistview;
     LinearLayout linearLayoutphotos;

    HashMap<String,List<EditText>> hashMap_edt = new HashMap();
    HashMap<Integer,String> hashMap = new HashMap<>();
    SQLiteDatabase  myDataBase;
    private SQLiteStatement insertStmt;





    View list_header;
    List<SurveyFormData> fvalue;
    String setFormname;
    public CheckListSavedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_check_list_saved, container, false);

        TextView textview =(TextView)v.findViewById(R.id.set_formane);
        finallistview=(ListView)v.findViewById(R.id.final_listview);
         list_header = (View)v. findViewById(R.id.lay_header);
        sharedpreferences = getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
        cal = Calendar.getInstance();
        System.out.println(dateFormat.format(cal.getTime()));
        current_date = dateFormat.format(cal.getTime());



        btn_send =(Button)v.findViewById(R.id.btn_send) ;

        btn_send.setOnClickListener(this);

        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        cal.set(year, month, day);


        db = new DatabaseHandler(getActivity());
        setFormname = getArguments().getString("PARAM1");


        setFormname = setFormname.replaceAll("\\s","");
        getFormlist = db.getAllChecklistwithFormno(setFormname);
        textview.setText(setFormname);

     //  db. deleteFormcheckListData(setFormname);

        initLinearBind(v, getFormlist);

/*

    List<SurveyFormData> dataget=  db.getAllFinalChecklist2_Save(setFormname);
        for(SurveyFormData d : dataget)
        {
            Log.v("Tagget",d.getRemark());


        }

 */



        return v;
    }


    public void initLinearBind( View view, List<ChecklistData> checklistDataList) {
           listkey = new ArrayList<>();

        LinearLayout li1 = (LinearLayout) view.findViewById(R.id.table_main2);
        for (int i = 0; i < checklistDataList.size(); i++) {
            LinearLayout LL3 = new LinearLayout(getActivity());
            // LL2.setBackgroundColor(Color.CYAN);
            LL3.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams LLParams3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            // LL3.setWeightSum(2f);
            LL3.setLayoutParams(LLParams3);

            TextView tv3 = new TextView(getActivity());
            LinearLayout.LayoutParams tvParams_3 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1f);
            tv3.setLayoutParams(tvParams_3);
            tv3.setText(checklistDataList.get(i).getName_value());
            tv3.setTextSize(16);
            tv3.setTextColor(Color.BLACK);
            tv3.setId(t_id + 1);


            final EditText etv3 = new EditText(getActivity());
            LinearLayout.LayoutParams edtParams_3 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,2f);
            allEds.add(etv3);
            hashMap.put(i, checklistDataList.get(i).getName());
            listkey.add( checklistDataList.get(i).getName());
            hashMap_edt.put(checklistDataList.get(i).getName(),allEds);

            edtParams_3.setMargins(5, 20, 5, 20);
            etv3.setLayoutParams(edtParams_3);
            etv3.setBackgroundResource(R.drawable.edt_border);
            etv3.setPadding(10,10,10,10);
          //  etv3.setHint(checklistDataList.get(i).getName());
            etv3.setTextSize(16);
            etv3.setTextColor(Color.BLACK);
            etv3.setId( i);

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
        createDynamicDatabase(getActivity(),setFormname,listkey);


    }
    public void saveForm_pre(String key,String value ){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();

    }

    public String getForm_pre(String key ){

        String gets =  sharedpreferences.getString(key, "");
        return gets;
    }

    public void clear_form_shared_pre(){
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.clear();
        editor.commit();
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
    public void onClick( View v) {

        boolean b = true;
        final ArrayList<String>  listvalue = new ArrayList<>();
        final String[] strings = new String[allEds.size()];

        for(int i=0; i < allEds.size(); i++){
            String s = hashMap.get(i);
            strings[i] = allEds.get(i).getText().toString();

            if(strings[i].equals("")){
                Toast.makeText(getActivity(),"Field can't be empty",Toast.LENGTH_LONG).show();
                b = false;
                break;
            }else {
                b = true;
            }


           // listkey.add( s);
            listvalue.add(strings[i]);


        }

       /* for(int i=0 ; i<allRGs.size() ; i++)
        {
          final String finalS =hashMap.get(i);
            int radioBtnChecked = allRGs.get(i).getCheckedRadioButtonId();
            RadioButton rb=(RadioButton)getActivity(). findViewById(radioBtnChecked);
           allRGs.get(i).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                    RadioButton rb=(RadioButton)getActivity(). findViewById(checkedId);
                    String radioText=rb.getText().toString();
                //   Toast.makeText(getActivity(),radioText +","+ finalS,Toast.LENGTH_LONG).show();

                }
            });


          //  Toast.makeText(getActivity(),rb.getText().toString() +","+ finalS,Toast.LENGTH_LONG).show();

        }
*/
       if(b)
       {
           listvalue.add("No");
           insert(getActivity(), listvalue, listkey, setFormname);
           for(int i=0; i < allEds.size(); i++)
           {
               allEds.get(i).setText("");
           }

           saveForm_pre(setFormname,"1");
             btn_send.setFocusable(false);



       }





    }


    public void createDynamicDatabase(Context context, String tableName, ArrayList<String> title) {
      //  "/mnt/sdcard/my.db"
        try {

            int i;
            String querryString = null;
            myDataBase = context.openOrCreateDatabase( db.DATABASE_NAME, MODE_WORLD_WRITEABLE, null);
            if(title.size()==1) {//Opens database in writable mode.
                querryString = title.get(0) + " text";
            }
           /* else if(title.size() ==2) {
                querryString = title.get(0)+" text,";
                querryString+= title.get(1) +" text";
            }*/
            else{
                querryString = title.get(0)+" text,";
                for(i=1;i<title.size()-1;i++)
                {
                    querryString += title.get(i);
                    querryString +=" text";
                    querryString +=",";
                }
                querryString+= title.get(i) +" text";
            }

            querryString = "CREATE TABLE IF NOT EXISTS " + tableName + "(" + querryString + ");";

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
            if(title.size() ==1)
            {
                titleString = title.get(0) ;
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



    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }







}