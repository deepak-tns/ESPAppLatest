package com.tns.espapp.adapter;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by TNS on 13-Oct-17.
 */

public interface RetrofitInterface {

    @GET
    @Streaming
    Call<ResponseBody> downloadFile(@Url String url);


}