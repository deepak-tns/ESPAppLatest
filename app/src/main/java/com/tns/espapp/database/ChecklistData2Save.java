package com.tns.espapp.database;

/**
 * Created by TNS on 25-Jul-17.
 */

public class ChecklistData2Save {

    private int id;
    private  String v_form_name;
    private  String v_control_name;
    private  String v_control_value;
    private  int flag;



    public ChecklistData2Save()
    {

    }

    public ChecklistData2Save(String v_form_name, String v_control_name, String v_control_value, int flag) {
        this.v_form_name = v_form_name;
        this.v_control_name = v_control_name;
        this.v_control_value = v_control_value;
        this.flag = flag;
    }

    public String getV_form_name() {
        return v_form_name;
    }

    public void setV_form_name(String v_form_name) {
        this.v_form_name = v_form_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getV_control_name() {
        return v_control_name;
    }

    public void setV_control_name(String v_control_name) {
        this.v_control_name = v_control_name;
    }

    public String getV_control_value() {
        return v_control_value;
    }

    public void setV_control_value(String v_control_value) {
        this.v_control_value = v_control_value;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
