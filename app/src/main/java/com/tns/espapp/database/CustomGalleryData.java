package com.tns.espapp.database;

/**
 * Created by TNS on 20-Mar-18.
 */

public class CustomGalleryData {
    String video_id;
    String video_name;

    public CustomGalleryData(String video_id,String video_name) {
        this.video_id = video_id;
        this.video_name = video_name;
    }

    public String getVideo_id() {
        return video_id;
    }

    public String getVideo_name() {
        return video_name;
    }

    public void setVideo_name(String video_name) {
        this.video_name = video_name;
    }
}
