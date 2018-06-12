package com.tns.espapp.database;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TNS on 09-Aug-17.
 */

public class SurveyFormData {

    private String formNo;
    private String sNo;
    private String desc;
    private String sts;
    private String remark;
    private String photos;
    private String path;
    private  int count;
    private  int flag;
    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private String img5;
    private String imageText1;
    private String imageText2;
    private String imageText3;
    private String imageText4;
    private String imageText5;
    private List<String> multiphotos = new ArrayList<>();

    public SurveyFormData(String formNo, String sNo, String desc, String sts, String remark, String photos, String path, int count, int flag)
    {
        this.formNo=formNo;
        this.sNo = sNo;
        this.desc = desc;
        this.sts = sts;
        this.remark = remark;
        this.photos = photos;
        this. path= path;
        this.count= count;
        this.flag =flag;
    }
    public SurveyFormData(String formNo, String sNo, String desc, String sts, String remark,  int count,String img1,String img2,String img3,String img4,String img5,String imageText1,String imageText2,String imageText3,String imageText4,String imageText5, int flag)
    {
        this.formNo=formNo;
        this.sNo = sNo;
        this.desc = desc;
        this.sts = sts;
        this.remark = remark;
        this.count= count;
        this.flag =flag;
        this.img1=img1;
        this.img2=img2;
        this.img3=img3;
        this.img4=img4;
        this.img5=img5;
        this.imageText1 =imageText1;
        this.imageText2 =imageText2;
        this.imageText3 =imageText3;
        this.imageText4 =imageText4;
        this.imageText5 =imageText5;
    }

    public SurveyFormData(String sNo, String desc, String sts, String remark, String photos)
    {
        this.sNo = sNo;
        this.desc = desc;
        this.sts = sts;
        this.remark = remark;
        this.photos = photos;
    }

    public SurveyFormData() {
    }

    public SurveyFormData(String sNo) {
        this.sNo = sNo;
    }

    public SurveyFormData(ArrayList<String> multiphotos) {
        this.multiphotos = multiphotos;
    }

    public String getFormNo() {
        return formNo;
    }

    public void setFormNo(String formNo) {
        this.formNo = formNo;
    }

    public String getsNo() {
        return sNo;
    }

    public void setsNo(String sNo) {
        this.sNo = sNo;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSts() {
        return sts;
    }

    public void setSts(String sts) {
        this.sts = sts;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public List<String> getMultiphotos() {
        return multiphotos;
    }

    public void setMultiphotos(List<String> multiphotos) {
        this.multiphotos = multiphotos;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public String getImg4() {
        return img4;
    }

    public void setImg4(String img4) {
        this.img4 = img4;
    }

    public String getImg5() {
        return img5;
    }

    public void setImg5(String img5) {
        this.img5 = img5;
    }



    public String getImageText1() {
        return imageText1;
    }

    public void setImageText1(String imageText1) {
        this.imageText1 = imageText1;
    }

    public String getImageText2() {
        return imageText2;
    }

    public void setImageText2(String imageText2) {
        this.imageText2 = imageText2;
    }

    public String getImageText3() {
        return imageText3;
    }

    public void setImageText3(String imageText3) {
        this.imageText3 = imageText3;
    }

    public String getImageText4() {
        return imageText4;
    }

    public void setImageText4(String imageText4) {
        this.imageText4 = imageText4;
    }

    public String getImageText5() {
        return imageText5;
    }

    public void setImageText5(String imageText5) {
        this.imageText5 = imageText5;
    }
}
