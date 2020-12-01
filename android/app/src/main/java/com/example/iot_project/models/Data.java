package com.example.iot_project.models;



public class Data {
    final Integer id;
    final String value;
    final String date;
    final Sensor sensor;
    final Code code;

    /*
        Function Data
        Constructeur
     */
    public Data(Integer id, String value, String date, Sensor sensor, Code code) {
        this.id = id;
        this.value = value;
        this.date = date;
        this.sensor = sensor;
        this.code = code;
    }

    /*
        Function getId
        Getter
     */
    public Integer getId() {
        return id;
    }

    /*
        Function getValue
        Getter
     */
    public String getValue() {
        return value;
    }

    /*
        Function getDate
        Getter
     */
    public String getDate() {
        return date;
    }

    /*
        Function getSensor
        Getter
     */
    public Sensor getSensor() {
        return sensor;
    }

    /*
        Function getCode
        Getter
     */
    public Code getCode() {
        return code;
    }
}
