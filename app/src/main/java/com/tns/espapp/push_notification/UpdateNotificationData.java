package com.tns.espapp.push_notification;

/**
 * Created by TNS on 18-Jul-17.
 */

public class UpdateNotificationData {

   private static UpdateNotificationData UpdatenotificationData;


   public   interface  updateListner{

      public void m_updateListner(String text);
    }



      private  static  updateListner i_updatelistner;

    public static UpdateNotificationData get_UpdateNotificationData(){

        if(UpdatenotificationData == null){

            UpdatenotificationData = new UpdateNotificationData();
        }

        return UpdatenotificationData;

    }




    public static void setListner(updateListner listner){
        i_updatelistner = listner;
    }

    public String setdata(String s){

        if(i_updatelistner != null)
        {

            i_updatelistner.m_updateListner(s);
        }

        return s;
    }

}
