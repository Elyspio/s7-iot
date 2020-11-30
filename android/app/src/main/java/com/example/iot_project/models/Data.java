package com.example.iot_project.models;

public class Data {
    final Integer id;
    final String value;
    final String date;
    final Sensor sensor;
    final Code code;

    public Data(Integer id, String value, String date, Sensor sensor, Code code) {
        this.id = id;
        this.value = value;
        this.date = date;
        this.sensor = sensor;
        this.code = code;
    }

    public Integer getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public String getDate() {
        return date;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public Code getCode() {
        return code;
    }
}
