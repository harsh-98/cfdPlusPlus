package com.cfdpp.vishwas.slidesaver;

import android.util.Log;

import com.cfdpp.vishwas.slidesaver.model.SensorResponse;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by vishwas on 26/10/18.
 */

public class Updater {
//    private static String BASE_URL = "http://13.71.4.119:3000/";
//    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//    private static Retrofit.Builder builder = new Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create());
//    private static Retrofit retrofit = builder.client(httpClient.build())
//            .build();
//    private static SensorServiceInterface client = retrofit.create(SensorServiceInterface.class);
//
//    public static List<SensorResponse> getData() {
//        List<SensorResponse> response;
//        Call<List<SensorResponse>> call = client.getResponse();
//        call.enqueue(new Callback<List<SensorResponse>>() {
//            @Override
//            public void onResponse(Call<List<SensorResponse>> call, Response<List<SensorResponse>> response) {
//                List<SensorResponse> sensorResponseList = response.body();
//                for (SensorResponse i : sensorResponseList) {
//                    Log.e(i.getDeviceID(), String.valueOf(i.getRiskFactor()));
//
//                }
//            }
//            @Override
//            public void onFailure(Call<List<SensorResponse>> call, Throwable t) {
//                Log.e("Error", t.toString());
//            }
//        });
//        return null;
//    }

}
