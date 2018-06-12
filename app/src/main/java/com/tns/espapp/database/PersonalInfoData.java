package com.tns.espapp.database;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TNS on 20-Sep-17.
 */

public class PersonalInfoData {
 private  String empid;
 private  String EmpCardID;
 private  String emp_status;
 private  String emp_status_date;
 private  String doj;
 private  String name;
 private  String Fname;
 private  String Mname;
 private  String Sex;
 private  String GrdType;
 private  String CatType;
 private  String BUnitName;
 private  String DeptCode;
 private  String SubDeptCode;
 private  String DesgType;
 private  String Location;
 private  String ReportingHeadID;
 private  String EmpType;
 private  String MaritalStatus;
 private  String DOA;
 private  String DOB;
 private  String P_Address;
 private  String P_Street;
 private  String P_City;
 private  String P_Zip;
 private  String P_State;
 private  String P_Country;
 private  String C_Address;
 private  String C_Street;
 private  String C_City;
 private  String C_State;
 private  String C_Zip;
 private  String C_Country;
 private  String Phone1;
 private  String Phone2;
 private  String Mobile1;
 private  String Mobile2;
 private  String Email1;
 private  String Email2;
/*
 private  String E_Qualification1;
 private  String E_University1;
 private  String E_PassingYear1;
 private  String E_Div1;
 private  String E_Per1;
 private  String E_Qualification2;
 private  String E_University2;
 private  String E_PassingYear2;
 private  String E_Div2;
 private  String E_Per2;
 private  String E_Qualification3;
 private  String E_University3;
 private  String E_PassingYear3;
 private  String E_Div3;
 private  String E_Per3;
 private  String P_Qualification1;
 private  String P_University1;
 private  String P_PassingYear1;
 private  String P_Div1;
 private  String P_Per1;
 private  String P_Qualification2;
 private  String P_University2;
 private  String P_PassingYear2;
 private  String P_Div2;
 private  String P_Per2;
 private  String P_Qualification3;
 private  String P_University3;
 private  String P_PassingYear3;
 private  String chkCorrespondence;
 private  String Total_Yr_Experience;
 private  String Total_Month_Experience;
 private  String EyeSight;
*/
 private  String BloodGroup;
 private  String PreHistoryDisease;
 private  String EmergencyContactTo;
 private  String EmergencyContactNo;
/* private  String References1;
 private  String References1Ph;
 private  String References1Email;
 private  String References2;
 private  String References2Ph;
 private  String References2Email;
 private  String References3;
 private  String References3Ph;
 private  String References3Email;
 private  String LastEmpName;
 private  String LastEmpAddress1;
 private  String LastEmpAddress2;
 private  String LastEmpPhone;
 private  String LastEmpEmail;
 private  String LastEmpFax;
 private  String LastReportingTo;
 private  String LastDeptName;*/
 private  String SalaryModeOfPayment;
 private  String SalaryBankName;
 private  String SalaryBankAccNo;
 private  String PFAccountNo;
 private  String PFjoinDate;
 private  String ESICInsuranceNo;
 private  String ESICJoinDate;
 private  String ESICBranch;
 private  String ESICDespencery;
 private  String ID_Proof;
 private  String ID_No;
 private String vPanNo;
 private String vAadharCardNo;
 private String vOprID;
 private String ReportingHead;


 public PersonalInfoData() {
 }

 public String getEmpid() {
  return empid;
 }

 public void setEmpid(String empid) {
  this.empid = empid;
 }

 public String getEmpCardID() {
  return EmpCardID;
 }

 public void setEmpCardID(String empCardID) {
  EmpCardID = empCardID;
 }

 public String getEmp_status() {
  return emp_status;
 }

 public void setEmp_status(String emp_status) {
  this.emp_status = emp_status;
 }

 public String getEmp_status_date() {
  return emp_status_date;
 }

 public void setEmp_status_date(String emp_status_date) {
  this.emp_status_date = emp_status_date;
 }

 public String getDoj() {
  return doj;
 }

 public void setDoj(String doj) {
  this.doj = doj;
 }

 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public String getFname() {
  return Fname;
 }

 public void setFname(String fname) {
  Fname = fname;
 }

 public String getMname() {
  return Mname;
 }

 public void setMname(String mname) {
  Mname = mname;
 }

 public String getSex() {
  return Sex;
 }

 public void setSex(String sex) {
  Sex = sex;
 }

 public String getGrdType() {
  return GrdType;
 }

 public void setGrdType(String grdType) {
  GrdType = grdType;
 }

 public String getCatType() {
  return CatType;
 }

 public void setCatType(String catType) {
  CatType = catType;
 }

 public String getBUnitName() {
  return BUnitName;
 }

 public void setBUnitName(String BUnitName) {
  this.BUnitName = BUnitName;
 }

 public String getDeptCode() {
  return DeptCode;
 }

 public void setDeptCode(String deptCode) {
  DeptCode = deptCode;
 }

 public String getSubDeptCode() {
  return SubDeptCode;
 }

 public void setSubDeptCode(String subDeptCode) {
  SubDeptCode = subDeptCode;
 }

 public String getDesgType() {
  return DesgType;
 }

 public void setDesgType(String desgType) {
  DesgType = desgType;
 }

 public String getLocation() {
  return Location;
 }

 public void setLocation(String location) {
  Location = location;
 }

 public String getReportingHeadID() {
  return ReportingHeadID;
 }

 public void setReportingHeadID(String reportingHeadID) {
  ReportingHeadID = reportingHeadID;
 }

 public String getEmpType() {
  return EmpType;
 }

 public void setEmpType(String empType) {
  EmpType = empType;
 }

 public String getMaritalStatus() {
  return MaritalStatus;
 }

 public void setMaritalStatus(String maritalStatus) {
  MaritalStatus = maritalStatus;
 }

 public String getDOA() {
  return DOA;
 }

 public void setDOA(String DOA) {
  this.DOA = DOA;
 }

 public String getDOB() {
  return DOB;
 }

 public void setDOB(String DOB) {
  this.DOB = DOB;
 }

 public String getP_Address() {
  return P_Address;
 }

 public void setP_Address(String p_Address) {
  P_Address = p_Address;
 }

 public String getP_Street() {
  return P_Street;
 }

 public void setP_Street(String p_Street) {
  P_Street = p_Street;
 }

 public String getP_City() {
  return P_City;
 }

 public void setP_City(String p_City) {
  P_City = p_City;
 }

 public String getP_Zip() {
  return P_Zip;
 }

 public void setP_Zip(String p_Zip) {
  P_Zip = p_Zip;
 }

 public String getP_State() {
  return P_State;
 }

 public void setP_State(String p_State) {
  P_State = p_State;
 }

 public String getP_Country() {
  return P_Country;
 }

 public void setP_Country(String p_Country) {
  P_Country = p_Country;
 }

 public String getC_Address() {
  return C_Address;
 }

 public void setC_Address(String c_Address) {
  C_Address = c_Address;
 }

 public String getC_Street() {
  return C_Street;
 }

 public void setC_Street(String c_Street) {
  C_Street = c_Street;
 }

 public String getC_City() {
  return C_City;
 }

 public void setC_City(String c_City) {
  C_City = c_City;
 }

 public String getC_State() {
  return C_State;
 }

 public void setC_State(String c_State) {
  C_State = c_State;
 }

 public String getC_Zip() {
  return C_Zip;
 }

 public void setC_Zip(String c_Zip) {
  C_Zip = c_Zip;
 }

 public String getC_Country() {
  return C_Country;
 }

 public void setC_Country(String c_Country) {
  C_Country = c_Country;
 }

 public String getPhone1() {
  return Phone1;
 }

 public void setPhone1(String phone1) {
  Phone1 = phone1;
 }

 public String getPhone2() {
  return Phone2;
 }

 public void setPhone2(String phone2) {
  Phone2 = phone2;
 }

 public String getMobile1() {
  return Mobile1;
 }

 public void setMobile1(String mobile1) {
  Mobile1 = mobile1;
 }

 public String getMobile2() {
  return Mobile2;
 }

 public void setMobile2(String mobile2) {
  Mobile2 = mobile2;
 }

 public String getEmail1() {
  return Email1;
 }

 public void setEmail1(String email1) {
  Email1 = email1;
 }

 public String getEmail2() {
  return Email2;
 }

 public void setEmail2(String email2) {
  Email2 = email2;
 }

 public String getBloodGroup() {
  return BloodGroup;
 }

 public void setBloodGroup(String bloodGroup) {
  BloodGroup = bloodGroup;
 }

 public String getPreHistoryDisease() {
  return PreHistoryDisease;
 }

 public void setPreHistoryDisease(String preHistoryDisease) {
  PreHistoryDisease = preHistoryDisease;
 }

 public String getEmergencyContactTo() {
  return EmergencyContactTo;
 }

 public void setEmergencyContactTo(String emergencyContactTo) {
  EmergencyContactTo = emergencyContactTo;
 }

 public String getEmergencyContactNo() {
  return EmergencyContactNo;
 }

 public void setEmergencyContactNo(String emergencyContactNo) {
  EmergencyContactNo = emergencyContactNo;
 }

 public String getSalaryModeOfPayment() {
  return SalaryModeOfPayment;
 }

 public void setSalaryModeOfPayment(String salaryModeOfPayment) {
  SalaryModeOfPayment = salaryModeOfPayment;
 }

 public String getSalaryBankName() {
  return SalaryBankName;
 }

 public void setSalaryBankName(String salaryBankName) {
  SalaryBankName = salaryBankName;
 }

 public String getSalaryBankAccNo() {
  return SalaryBankAccNo;
 }

 public void setSalaryBankAccNo(String salaryBankAccNo) {
  SalaryBankAccNo = salaryBankAccNo;
 }

 public String getPFAccountNo() {
  return PFAccountNo;
 }

 public void setPFAccountNo(String PFAccountNo) {
  this.PFAccountNo = PFAccountNo;
 }

 public String getPFjoinDate() {
  return PFjoinDate;
 }

 public void setPFjoinDate(String PFjoinDate) {
  this.PFjoinDate = PFjoinDate;
 }

 public String getESICInsuranceNo() {
  return ESICInsuranceNo;
 }

 public void setESICInsuranceNo(String ESICInsuranceNo) {
  this.ESICInsuranceNo = ESICInsuranceNo;
 }

 public String getESICJoinDate() {
  return ESICJoinDate;
 }

 public void setESICJoinDate(String ESICJoinDate) {
  this.ESICJoinDate = ESICJoinDate;
 }

 public String getESICBranch() {
  return ESICBranch;
 }

 public void setESICBranch(String ESICBranch) {
  this.ESICBranch = ESICBranch;
 }

 public String getESICDespencery() {
  return ESICDespencery;
 }

 public void setESICDespencery(String ESICDespencery) {
  this.ESICDespencery = ESICDespencery;
 }

 public String getID_Proof() {
  return ID_Proof;
 }

 public void setID_Proof(String ID_Proof) {
  this.ID_Proof = ID_Proof;
 }

 public String getID_No() {
  return ID_No;
 }

 public void setID_No(String ID_No) {
  this.ID_No = ID_No;
 }

 public String getvPanNo() {
  return vPanNo;
 }

 public void setvPanNo(String vPanNo) {
  this.vPanNo = vPanNo;
 }

 public String getvAadharCardNo() {
  return vAadharCardNo;
 }

 public void setvAadharCardNo(String vAadharCardNo) {
  this.vAadharCardNo = vAadharCardNo;
 }

 public String getvOprID() {
  return vOprID;
 }

 public void setvOprID(String vOprID) {
  this.vOprID = vOprID;
 }

 public String getReportingHead() {
  return ReportingHead;
 }

 public void setReportingHead(String reportingHead) {
  ReportingHead = reportingHead;
 }
}
