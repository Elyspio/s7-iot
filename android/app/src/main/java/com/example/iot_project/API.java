package com.example.iot_project;

import com.example.iot_project.models.Data;
import com.example.iot_project.models.Sensor;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface API {

    @POST("./sensors/order")
    @FormUrlEncoded
    Call<Void> setOrder(@Field("sensor") String sensorId, @Field("order") String[] order);

    @GET("data/last/{sensorId}/{dataCodeId}")
    Call<Data> getData(
            @Path("sensorId") String sensorId,
            @Path("dataCodeId") Integer dataCodeId
    );

    @GET("./")
    Call<Void> checkIfServerIsAlive();


    @GET("sensors")
    Call<Sensor[]> getSensors();

}
