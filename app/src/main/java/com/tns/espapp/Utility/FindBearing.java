package com.tns.espapp.Utility;

/**
 * Created by TNS on 06-Mar-18.
 */

public class FindBearing {
    public static String DIRECTIONNAME;

    public static void main(String[] args) {
        System.out.println(" Your Result >>> "+FindBearing.bearing(19.2859590, 73.4966430, 19.2861020, 73.4988090));
    }

    public static double bearing(double lat1, double lon1, double lat2, double lon2){
        double longitude1 = lon1;
        double longitude2 = lon2;
        double latitude1 = Math.toRadians(lat1);
        double latitude2 = Math.toRadians(lat2);
        double longDiff= Math.toRadians(longitude2-longitude1);
        double y= Math.sin(longDiff)*Math.cos(latitude2);
        double x=Math.cos(latitude1)*Math.sin(latitude2)-Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(longDiff);
        double resultDegree= (Math.toDegrees(Math.atan2(y, x))+360)%360;
        String coordNames[] = {"N","NNE", "NE","ENE","E", "ESE","SE","SSE", "S","SSW", "SW","WSW", "W","WNW", "NW","NNW", "N"};
        double directionid = Math.round(resultDegree / 22.5);
        // no of array contain 360/16=22.5
        if (directionid < 0) {
            directionid = directionid + 16;
            //no. of contains in array
        }
        String compasLoc=coordNames[(int) directionid];
        DIRECTIONNAME = compasLoc;
       // return resultDegree+" ,"+compasLoc;
        return resultDegree ;
    }
}