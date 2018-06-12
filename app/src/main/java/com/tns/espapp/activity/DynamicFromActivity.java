package com.tns.espapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tns.espapp.AppConstraint;
import com.tns.espapp.R;
import com.tns.espapp.Utility.AppSingleton;
import com.tns.espapp.database.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DynamicFromActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner formListSpinner;
    private LinearLayout parentLinearLayout;
    private ArrayList<String> formNumberList = new ArrayList<>();
    private ArrayList<DynamicForm> dynamicFormArrayList = new ArrayList<>();
    private ArrayList<String> editTextValueList = new ArrayList<>();
    private ArrayList<String> textFieldNameArrayList = new ArrayList<>();
    private static final int MY_BUTTON = 9000;
    private static final int MY_EDITTEXT = 1000;
    private static final int MY_TEXTVIEW = 5000;
    private int editTextCounter = 0;
    private int textViewCounter = 0;

    DatabaseHandler db;
    private SQLiteDatabase myDataBase;
    private SQLiteStatement insertStmt;
    private String INSERT;
    private String selectedFormName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_from);
        getLayoutsId();
        getFormListFromServer();


   }
   private void getLayoutsId()
   {
       parentLinearLayout = (LinearLayout) findViewById(R.id.parentLinearLayout);
       formListSpinner = (Spinner)findViewById(R.id.formListSpinner);
   }
   private void getFormListFromServer()
   {
       final ProgressDialog pDialog = new ProgressDialog(this);
       pDialog.setMessage("Loading...");
       pDialog.show();
      JSONObject jsonObject = new JSONObject();
       try {

           jsonObject.put("DatabaseName", "TNS_HR");
           jsonObject.put("ServerName", "TNSSERVER");
           jsonObject.put("UserId", "sanjay");
           jsonObject.put("Password", "tnssoft");
           jsonObject.put("spName", "USP_GetForm");


       } catch (JSONException e) {
           e.printStackTrace();
       }
       JsonObjectRequest fromNameJsonRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, AppConstraint.FORMNAME, jsonObject,
               new com.android.volley.Response.Listener<JSONObject>() {
                   @Override
                   public void onResponse(JSONObject response) {

                       parseFormNameJSON(response.toString());
                       pDialog.dismiss();


                   }
               },
               new com.android.volley.Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError error) {
                       pDialog.dismiss();

                   }
               }) {
       };
       AppSingleton.getInstance(this).addToRequestQueue(fromNameJsonRequest, null);

   }

    public void onClick(View v) {
        Toast toast;
        switch (v.getId()) {
            case MY_BUTTON:
                for (int i = 0; i < editTextCounter; i++) {
                    editTextValueList.add(((EditText) findViewById(MY_EDITTEXT + i)).getText().toString());

                }
                for (int i = 0; i < textViewCounter; i++) {
                    textFieldNameArrayList.add(((TextView) findViewById(MY_TEXTVIEW + i)).getText().toString());
                }

                String name = editTextValueList.get(0);
                String labelName = textFieldNameArrayList.get(0);
                toast = Toast.makeText(this, name + labelName, Toast.LENGTH_LONG);
                createDynamicDatabase(this, selectedFormName, textFieldNameArrayList);
                insert(this, editTextValueList, textFieldNameArrayList, selectedFormName);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
                break;
        }
    }





    private void createDynamicViews(LinearLayout ll) {
        textViewCounter =0;
        editTextCounter =0;
        for (int i = 0; i < dynamicFormArrayList.size(); i++) {
            DynamicForm dynamicForm = dynamicFormArrayList.get(i);
            if (dynamicForm.getDatatype().equalsIgnoreCase("String")) {
                TextView tv = new TextView(this);
                tv.setText(dynamicForm.getFieldValue());
                tv.setId(MY_TEXTVIEW + i);
                tv.setText(dynamicForm.getFieldName());
                textViewCounter++;
                ll.addView(tv);

                EditText et = new EditText(this);
                et.setHint(dynamicForm.getFieldValue());
                et.setMinLines(1);
                et.setId(MY_EDITTEXT + i);
                et.setMaxLines(3);
                editTextCounter++;
                ll.addView(et);
            } else {

            }
        }
        Button b = new Button(this);
        b.setText("Save");
        b.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        b.setId(MY_BUTTON);
        b.setGravity(Gravity.CENTER);
        b.setOnClickListener(this);
        parentLinearLayout.addView(b);
        parentLinearLayout.setGravity(Gravity.CENTER);
    }

    private void createFormRequest(String selectedFormName) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        dynamicFormArrayList.clear();

        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray jsonArrayParameter = new JSONArray();
            jsonArrayParameter.put(selectedFormName);
            jsonObject.put("DatabaseName", "TNS_HR");
            jsonObject.put("ServerName", "TNSSERVER");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");
            jsonObject.put("spName", "USP_FormMaster");
            jsonObject.put("ParameterList", jsonArrayParameter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, AppConstraint.FORMVERIFY, jsonObject,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Parse Json", response.toString());
                        parseFormDataJSON(response.toString());
                        pDialog.dismiss();


                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();

                    }
                }) {
        };
        Log.d("From Structure Request", jsonObject.toString());
        AppSingleton.getInstance(this).addToRequestQueue(jsObjRequest, null);


    }

    private void parseFormDataJSON(String json) {
        try {

            JSONObject jsonObject = null;
            jsonObject = new JSONObject(json);
            dynamicFormArrayList.clear();
            JSONArray jsonArray = jsonObject.getJSONArray("FormName");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject formObject = (JSONObject) jsonArray.get(i);
                DynamicForm dynamicForm = new DynamicForm();
                dynamicForm.setFormName(formObject.getString("formName"));
                dynamicForm.setFieldName(formObject.getString("fieldName"));
                dynamicForm.setFieldValue(formObject.getString("fieldValue"));
                dynamicForm.setDatatype(formObject.getString("dataType"));
                dynamicForm.setSize(formObject.getInt("size"));
                dynamicForm.setDecimal(formObject.getInt("decimal"));
                dynamicFormArrayList.add(dynamicForm);

            }
            db.insertDynamicFormData(dynamicFormArrayList);
            dynamicFormArrayList.clear();
            dynamicFormArrayList = db.getDynamicFormData(selectedFormName);
            parentLinearLayout.removeAllViews();
            createDynamicViews(parentLinearLayout);


        } catch (JSONException jsonException) {
            Log.e("Execption in Json", jsonException.getMessage());
        }
    }

    public void createDynamicDatabase(Context context, String tableName, ArrayList<String> title) {

        try {

            int i;
            String querryString = null;
            myDataBase = context.openOrCreateDatabase( "/mnt/sdcard/my.db", MODE_WORLD_WRITEABLE, null);
            if(title.size()==1) {//Opens database in writable mode.
                querryString = title.get(0) + " text";
            }
            else if(title.size() ==2) {
                querryString = title.get(0)+" text,";
                querryString+= title.get(1) +" text";
            }
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

            Toast.makeText(context, querryString, Toast.LENGTH_LONG).show();

            myDataBase.execSQL(querryString);
            Toast.makeText(context, "Execute Query", Toast.LENGTH_LONG).show();

        } catch (SQLException ex) {
            Log.e(ex.getClass().getName(), ex.getMessage(), ex);

        }
    }

    public void insert(Context context, ArrayList<String> array_vals, ArrayList<String> title, String TABLE_NAME) {
        try {


            myDataBase = context.openOrCreateDatabase("/mnt/sdcard/my.db", MODE_WORLD_WRITEABLE, null);         //Opens database in writable mode.
            String titleString = null;
            String markString = null;
            int i;
            if(title.size() ==1)
            {
                titleString = title.get(0) ;
                markString = "?";
            }
            else if(title.size() ==2)
            {
                titleString = title.get(0) + ",";
                markString = "?,";
                titleString+= title.get(1) +" text";
                markString+="?";
            }
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
    /*This Function Check the Data in DataBase For Corresponding FormName
    if Data Already exists in local DataBase so its not send request
     to the server else create request for server to get data*/

    private void checkDataInDataBase(String formName)
    {
        db = new DatabaseHandler(this);
        dynamicFormArrayList =   db.getDynamicFormData(formName);
        if(dynamicFormArrayList.isEmpty()) {
            createFormRequest(formName);
        }
        else
        {
            parentLinearLayout.removeAllViews();
            createDynamicViews(parentLinearLayout);

        }
    }
    private void parseFormNameJSON(String json) {
        try {

            JSONObject jsonObject = null;
            jsonObject = new JSONObject(json);
            formNumberList.clear();
            JSONArray jsonArray = jsonObject.getJSONArray("FormName");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject formObject = (JSONObject) jsonArray.get(i);
                String formName = formObject.getString("vFormName");
                formNumberList.add(formName);

            }
            setFromListAdapter();




        } catch (JSONException jsonException) {
            Log.e("Execption in Json", jsonException.getMessage());
        }
    }
    private void setFromListAdapter()
    {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, formNumberList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        formListSpinner.setAdapter(dataAdapter);
        formNameSelectionListener();
    }
    private void formNameSelectionListener()
    {
        formListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFormName = parent.getItemAtPosition(position).toString();
                parentLinearLayout.removeAllViews();
                checkDataInDataBase(selectedFormName);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
