package com.tns.espapp.rest;

import com.tns.espapp.AppConstraint;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by TNS on 20-Sep-17.
 */

public class ApiClient {

    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    public static final String login = AppConstraint.ESP_LOGIN +"/";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(login)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}