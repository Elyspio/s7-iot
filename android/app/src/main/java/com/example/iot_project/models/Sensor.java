package com.example.iot_project.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Sensor {
    final String serial;
    final String label;

    public Sensor(String serial, String label) {
        this.serial = serial;
        this.label = label;
    }

    public String getSerial() {
        return serial;
    }

    public String getLabel() {
        return label;
    }
}
