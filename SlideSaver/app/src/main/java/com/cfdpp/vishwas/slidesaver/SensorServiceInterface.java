package com.cfdpp.vishwas.slidesaver;

import com.cfdpp.vishwas.slidesaver.model.SensorResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by vishwas on 26/10/18.
 */

public interface SensorServiceInterface {
    @GET("api")
    Call<List<SensorResponse>> getResponse();
}
