package com.example.iot_project;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface API {

    @POST("./sensors/order")
    @FormUrlEncoded
    Call<Void> setOrder(@Field("sensor") Integer sensorId, @Field("order") String[] order);

}
