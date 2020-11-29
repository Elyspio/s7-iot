package com.example.iot_project;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface API {

    @POST("./sensors/order")
    @FormUrlEncoded
    Call<Void> setOrder(@Field("sensor") Integer sensorId, @Field("order") String[] order);

    @GET("data/last/{roomId}/{sensorId}")
    Call<Data[]> getData(
            @Path("roomId") Integer roomId,
            @Path("sensorId") Integer sensorId
    );

}
