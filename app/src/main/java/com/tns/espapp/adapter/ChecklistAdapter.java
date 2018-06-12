package com.tns.espapp.adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tns.espapp.R;
import com.tns.espapp.database.ChecklistData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by TNS on 23-Jun-17.
 */

public class ChecklistAdapter  extends RecyclerView.Adapter<ChecklistAdapter.MyViewHolder> {
    private SimpleDateFormat dateFormatter;
    private Calendar cal;
    private int day;
    private int month;
    private int year;
    private List<ChecklistData> checkList;
    Context context;

   OnItemClickListener onItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name_key,tv_dob,reporting_head;
        public EditText name_value, edt_dob;
        public CheckBox cb;
        public RadioGroup radioSexGroup;

        public MyViewHolder(View view) {
            super(view);

            radioSexGroup = (RadioGroup)view. findViewById(R.id.radio_boolean);
            name_value = (EditText) view.findViewById(R.id.edt_name);
            name_key = (TextView)view .findViewById(R.id.tv_name);

        }


        public void bind(final ChecklistData checklistData, final OnItemClickListener listener, final int pos) {

            cal = Calendar.getInstance();
            day = cal.get(Calendar.DAY_OF_MONTH);
            month = cal.get(Calendar.MONTH);
            year = cal.get(Calendar.YEAR);
            dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            cal.set(year, month, day);


            name_key.setText(checklistData.getName());
            tv_dob.setText(checklistData.getName_value());

         /*   if(checklistData.getDataType().equals("Numeric")){


               name_value.setInputType(InputType.TYPE_CLASS_NUMBER);
            }

            if(checklistData.getDataType().equals("Date")){

                name_value.setText(dateFormatter.format(cal.getTime()));
                name_value.setFocusable(false);
                name_value.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setdate(name_value);
                    }
                });

            }*/


            // dob.setText(dateFormatter.format(cal.getTime()));
           // reporting_head.setText(checklistData.getDataType());


           // radioSexGroup.setTag(pos);


       /*     cb.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //is chkIos checked?
                    if (((CheckBox) v).isChecked()) {
                        Toast.makeText(context,name_value.getText()+"Selected", Toast.LENGTH_LONG).show();
                    }

                }
            });

            name_value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Position: "+ checklistData.getName(), Toast.LENGTH_LONG).show();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override public void onClick(View v) {

                    listener.onItemClick(checklistData,pos);

                }

            });

            dob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setdate(dob);
                }
            });


            radioSexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton rb = (RadioButton) group.findViewById(checkedId);
                    if (null != rb && checkedId > -1) {
                        Toast.makeText(context, rb.getText()+"&&"+ radioSexGroup.getTag(), Toast.LENGTH_SHORT).show();
                    }

                }
            });*/

        }

    }


    public ChecklistAdapter(Context context,List<ChecklistData> checkList, OnItemClickListener onItemClickListener) {
       this. context = context;
        this.checkList = checkList;
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.checklist_data_adapter, parent, false);

        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(checkList.get(position),onItemClickListener,position);


    }

    @Override
    public int getItemCount() {
        return checkList.size();
    }

    public static interface OnItemClickListener {

        void onItemClick(ChecklistData item, int pos);

    }


    private void setdate(final EditText edt) {
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        DatePickerDialog dpd = new DatePickerDialog(context,
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






}
