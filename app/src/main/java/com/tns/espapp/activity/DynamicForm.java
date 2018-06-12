package com.tns.espapp.activity;

/**
 * Created by GARIMA on 7/25/2017.
 */

public class DynamicForm {
    private String formName;
    private String fieldName;
    private String fieldValue;
    private String Datatype;
    private int Size;
    private double Decimal;

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }





    public String getDatatype() {
        return Datatype;
    }

    public void setDatatype(String datatype) {
        Datatype = datatype;
    }

    public int getSize() {
        return Size;
    }

    public void setSize(int size) {
        Size = size;
    }

    public double getDecimal() {
        return Decimal;
    }

    public void setDecimal(double decimal) {
        Decimal = decimal;
    }


}
