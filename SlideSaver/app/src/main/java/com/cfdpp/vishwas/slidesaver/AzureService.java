package com.cfdpp.vishwas.slidesaver;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.cfdpp.vishwas.slidesaver.model.SensorResponse;
import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.heatmaps.WeightedLatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by vishwas on 26/10/18.
 */

public class AzureService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AzureService(String name) {
        super(name);
    }

    public AzureService() {
        super("AzureService");
    }

    String BASE_URL = "http://13.71.4.119:3000/";

    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    Retrofit retrofit = builder.client(httpClient.build())
            .build();
    SensorServiceInterface client = retrofit.create(SensorServiceInterface.class);

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String data = intent.getDataString();
        while (true) {
            Call<List<SensorResponse>> call = client.getResponse();
            call.enqueue(new Callback<List<SensorResponse>>() {
                @Override
                public void onResponse(Call<List<SensorResponse>> call, Response<List<SensorResponse>> response) {
                    List<SensorResponse> sensorResponseList = response.body();
                    List<WeightedLatLng> list = new ArrayList<>();
                    for (SensorResponse i : sensorResponseList) {
                        Log.e("Service data", String.valueOf(i.getRiskFactor()));
                        list.add(new WeightedLatLng(new LatLng(i.get_lat(), i.get_long()), i.getRiskFactor()));
                    }
                    Toast.makeText(getApplicationContext(), "Toast to the service", Toast.LENGTH_SHORT).show();
                    // TODO: for background
                    /*
                     * Get the location points for hot zones
                     * Calculate the user's risk factor according to the points and user's location
                     * Notify the user if risk factor is more than .5
                     */
                }

                @Override
                public void onFailure(Call<List<SensorResponse>> call, Throwable t) {
                    Log.e("Error", t.toString());
                }
            });

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
//    public static final String TAG = "job_get_azure_data";
//
//
//    @NonNull
//    @Override
//    protected Result onRunJob(@NonNull Params params) {
//        Log.e("AzureJob", "Inside onRunJob");
//        return Result.SUCCESS;
//    }
//
//    public static void scheduleJob() {
//        Set<JobRequest> jobRequests = JobManager.instance().getAllJobRequestsForTag(AzureService.TAG);
//        if (!jobRequests.isEmpty()) {
//            return;
//        }
//        new JobRequest.Builder(AzureService.TAG)
//                .setPeriodic(TimeUnit.MINUTES.toMillis(15), TimeUnit.MINUTES.toMillis(7))
//                .setUpdateCurrent(true) // calls cancelAllForTag(NoteSyncJob.TAG) for you
//                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
//                .setRequirementsEnforced(true)
//                .build()
//                .schedule();
//    }
}
