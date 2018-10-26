package com.cfdpp.vishwas.slidesaver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.cfdpp.vishwas.slidesaver.model.SensorResponse;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String BASE_URL = "http://13.71.4.119:3000/";

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(httpClient.build())
                .build();

        SensorServiceInterface client = retrofit.create(SensorServiceInterface.class);

        Call<List<SensorResponse>> call = client.getResponse();

        call.enqueue(new Callback<List<SensorResponse>>() {
            @Override
            public void onResponse(Call<List<SensorResponse>> call, Response<List<SensorResponse>> response) {
                List<SensorResponse> sensorResponseList = response.body();
//                Log.e()
                for (SensorResponse i : sensorResponseList) {
                    Log.e(i.getDeviceID(), String.valueOf(i.getRiskFactor()));
                }
            }

            @Override
            public void onFailure(Call<List<SensorResponse>> call, Throwable t) {
                Log.e("Error", t.toString());
            }
        });
    }
}
