package com.memory.memory;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Maciej Szalek on 2019-03-18.
 */

public class FirebaseManager {

    private static final String URL_USER = "https://memory-1ac66.firebaseio.com" ;
    private static final String URL_MESSAGE =  "https://fcm.googleapis.com/fcm/send";


    private Context context;
    private ArrayList<String> list = new ArrayList<>();
    private String userId;
    private String userNumber;

    public FirebaseManager(Context context, String userId, String userNumber){
        this.context = context;
        this.userId = userId;
        this.userNumber = userNumber;
    }

    public void createNewUser(){
        Retrofit retrofitUser = new Retrofit.Builder()
                .baseUrl(URL_USER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api apiUser = retrofitUser.create(Api.class);
        Call<User> callUser = apiUser.setData(userId, new User(userId, userNumber));
        callUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Toast.makeText(context,"New user created", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(context,"Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
