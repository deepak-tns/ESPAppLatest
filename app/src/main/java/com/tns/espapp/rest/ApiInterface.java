package com.tns.espapp.rest;

import com.google.gson.JsonObject;
import com.tns.espapp.database.PersonalInfoData;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by TNS on 20-Sep-17.
 */


public interface ApiInterface {

    // http://api.themoviedb.org/3/movie/top_rated?api_key=15570e0891afdee0f3aa3049b6605ace
    @GET("movie/top_rated")
    Call<PersonalInfoData> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<PersonalInfoData> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);

    @FormUrlEncoded
    @POST("/")
    @Headers({
            "Content-Type: application/json;charset=UTF-8"
    })
    Call<JSONObject> Login( @Body JSONObject bean);


}