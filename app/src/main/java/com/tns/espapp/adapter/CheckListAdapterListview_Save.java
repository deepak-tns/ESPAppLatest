package com.tns.espapp.adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tns.espapp.R;
import com.tns.espapp.database.ChecklistData;
import com.tns.espapp.database.DatabaseHandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by TNS on 25-Jul-17.
 */

public class CheckListAdapterListview_Save extends ArrayAdapter {

    public  static   String getTEXT;

    private SimpleDateFormat dateFormatter;
    private Calendar cal;
    private int day;
    private int month;
    private int year;
    Context context;
    ChecklistData checklistData;
    int deepColor = Color.parseColor("#FFFFFF");
    int deepColor2 = Color.parseColor("#DCDCDC");
    //  int deepColor3 = Color.parseColor("#B58EBF");
    private int[] colors = new int[]{deepColor, deepColor2};
    private List<ChecklistData> searchlist = null;
    DatabaseHandler db;

    //   private CustomOnItemClickListener customOnItemClickListener;


    public CheckListAdapterListview_Save(Context context, int resource, List<ChecklistData> latLongDatas) {
        super(context, resource, latLongDatas);
        this.context = context;
        this.searchlist = latLongDatas;
        db = new DatabaseHandler(context);

    }



    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final TextView name_key, tv_dob, reporting_head;
        final EditText name_value, edt_dob;
        Button btN;

        CheckBox cb;
        LinearLayout linearLayout_boolean;
        RadioGroup radioSexGroup;

        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        cal.set(year, month, day);


        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = layoutInflater.inflate(R.layout.checklist_data_adapter, parent, false);
         btN = (Button)convertView.findViewById(R.id.btn_save) ;

        //int colorPos = position % colors.length;
        // convertView.setBackgroundColor(colors[colorPos]);

        radioSexGroup = (RadioGroup) convertView.findViewById(R.id.radio_boolean);
        linearLayout_boolean =(LinearLayout) convertView.findViewById(R.id.linear_type_boolean);
        name_value = (EditText) convertView.findViewById(R.id.edt_name);
        name_key = (TextView) convertView.findViewById(R.id.tv_name);




        checklistData = searchlist.get(position);


        name_key.setText(checklistData.getName());
        //  name_value.setText(checklistData.getName());
        String getdatatype =checklistData.getDataType();




        if (checklistData.getDataType().equalsIgnoreCase("Numeric")) {

            name_value.setInputType(InputType.TYPE_CLASS_NUMBER);

        }


        if (checklistData.getDataType().equalsIgnoreCase("Date")) {

            name_value.setText(dateFormatter.format(cal.getTime()));
            name_value.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.ic_menu_my_calendar, 0);
            name_value.setFocusable(false);
            name_value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setdate(name_value);
                }
            });
        }

          /*  if (checklistData.getDataType().equalsIgnoreCase("Boolean")) {

                name_value.setVisibility(View.GONE);
                linearLayout_boolean.setVisibility(View.VISIBLE);

                final View finalConvertView = convertView;
                radioSexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        // int id=group.getCheckedRadioButtonId();
                        //  RadioButton rb=(RadioButton) findViewById(id);
                        RadioButton rb=(RadioButton) finalConvertView. findViewById(checkedId);
                        String radioText=rb.getText().toString();


                    }
                });
            }
*/

            if( name_value != null){
                name_value.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        getTEXT = name_value.getText().toString();
                    }
                });

            }

        /*    btN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"hello"+getTEXT,Toast.LENGTH_LONG).show();
                }
            });
*/



        return convertView;
    }


  /*  public static interface CustomOnItemClickListener {

        void onItemClick(ChecklistData item, int pos);

    }*/

    private void setdate(final EditText edt) {
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        DatePickerDialog dpd = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                   /*
                    addmeeting_edt_selectdate.setText(dayOfMonth + "-"+ dateFormatter.format (monthOfYear + 1) + "-" + year);

                              */
                        cal.set(year, monthOfYear, dayOfMonth);

                        edt.setText(dateFormatter.format(cal.getTime()));

                    }
                }, year, month, day);
        dpd.show();
    }



}
