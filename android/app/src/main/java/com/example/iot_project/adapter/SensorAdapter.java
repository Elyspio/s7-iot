package com.example.iot_project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.iot_project.R;
import com.example.iot_project.models.Sensor;

import java.util.List;

public class SensorAdapter extends ArrayAdapter<Sensor> {


    public SensorAdapter(Context context, List<Sensor> users) {
        super(context, R.layout.sensor, R.id.sensor_label, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Sensor sensor = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sensor, parent, false);
        }
        TextView label = convertView.findViewById(R.id.sensor_label);
        label.setText(String.format("Sensor: %s", sensor.getLabel()));
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        Sensor sensor = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sensor, parent, false);
        }
        convertView.setPadding(0, 12, 0, 12);
        TextView label = convertView.findViewById(R.id.sensor_label);
        label.setText(String.format("%s", sensor.getLabel()));

        return convertView;
    }


}