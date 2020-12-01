package com.example.iot_project;

import com.example.iot_project.models.Data;
import com.example.iot_project.models.Sensor;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


//Interface implémentant Retrofit
public interface API {

    /*
        Function setOrder
        Envoie au serveur l'ordre d'affichage sur le microbit
     */
    @POST("./sensors/order")
    @FormUrlEncoded
    Call<Void> setOrder(@Field("sensor") String sensorId, @Field("order") String[] order);

    /*
        Function getData
        Récupère les données des capteurs sur le serveur
     */
    @GET("data/last/{sensorId}/{dataCodeId}")
    Call<Data> getData(
            @Path("sensorId") String sensorId,
            @Path("dataCodeId") Integer dataCodeId
    );

    /*
        Function checkIfServerIsAlive
        Vérifie la bonne connexion au serveur
     */
    @GET("./")
    Call<Void> checkIfServerIsAlive();


    /*
        Function getSensors
        Récupère les différents capteurs utilisé par le microbit
     */
    @GET("sensors")
    Call<Sensor[]> getSensors();

}
