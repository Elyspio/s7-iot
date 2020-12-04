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

/**
 * Cette classe permet de faire en sorte que le composant
 */
public class SensorAdapter extends ArrayAdapter<Sensor> {

    /*
       Constructeur
    */
    public SensorAdapter(Context context, List<Sensor> sensors) {
        super(context, R.layout.sensor, R.id.sensor_label, sensors);
    }



    /**
     * Permet de retourner un item lors de l'affchage standard (valeur choisie)
     * @param position
     * @param convertView
     * @param parent
     */
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


    /**
     * Permet de retourner un item lors de l'affichage du dropdown
     * @param position index 
     * @param convertView
     * @param parent
     */
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