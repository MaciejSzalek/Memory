package com.memory.memory;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Maciej Szalek on 2019-03-13.
 */

public interface Api {

    @POST("/user/{new}.json")
    Call<User> setData(@Path("new") String s1, @Body User user);

    @GET("/user.json")
    Call<User> getData();

    @PUT("/user/{new}.json")
    Call<User> setDataWithoutRandomness(@Path("new") String s1, @Body User user);
}
