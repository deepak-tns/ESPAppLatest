package com.tns.espapp.database;

/**
 * Created by TNS on 23-Jun-17.
 */

public class ChecklistData {

    private int id;
    private String formno;
    private String name;
    private String name_value;
    private String dataType;
    private String size;
    private String decimal;
    private int flag;

    public ChecklistData() {
    }

    public ChecklistData(String name, String name_value, String dataType, String decimal) {
        this.name = name;
        this.name_value = name_value;
        this.dataType = dataType;
        this.decimal = decimal;

    }

    public ChecklistData( String name_value) {

        this.name_value = name_value;

    }


    public ChecklistData(String formno, String name, String name_value, String dataType, String size, String decimal, int flag) {
        this.formno = formno;
        this.name = name;
        this.name_value = name_value;
        this.dataType = dataType;
        this.size = size;
        this.decimal = decimal;
        this.flag = flag;
    }

    public ChecklistData(String formno, String name, String name_value, String dataType,  int flag) {
        this.formno = formno;
        this.name = name;
        this.name_value = name_value;
        this.dataType = dataType;

        this.flag = flag;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFormno() {
        return formno;
    }

    public void setFormno(String formno) {
        this.formno = formno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_value() {
        return name_value;
    }

    public void setName_value(String name_value) {
        this.name_value = name_value;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDecimal() {
        return decimal;
    }

    public void setDecimal(String decimal) {
        this.decimal = decimal;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
