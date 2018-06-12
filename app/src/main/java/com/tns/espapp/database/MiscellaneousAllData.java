package com.tns.espapp.database;

import java.util.List;

/**
 * Created by TNS on 12-Oct-17.
 */

public class MiscellaneousAllData {

    private String path;
    private List<MiscellaneousData> miscellaneousDatas;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<MiscellaneousData> getMiscellaneousDatas() {
        return miscellaneousDatas;
    }

    public void setMiscellaneousDatas(List<MiscellaneousData> miscellaneousDatas) {
        this.miscellaneousDatas = miscellaneousDatas;
    }
}
