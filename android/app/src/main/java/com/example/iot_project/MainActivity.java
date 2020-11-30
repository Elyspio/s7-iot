package com.example.iot_project;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iot_project.adapter.SensorAdapter;
import com.example.iot_project.models.Code;
import com.example.iot_project.models.Data;
import com.example.iot_project.models.Sensor;
import com.google.android.material.bottomappbar.BottomAppBar;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Button btnNetwork;
    private Button btnConnection;
    private BottomAppBar bapNetwork;
    private EditText etIp;
    private EditText etPort;
    private TextView tvState;
    private TextView tvTemperature;
    private TextView tvBrigthness;
    private ToggleButton tbOrder;
    private Spinner spRoom;
    private API service;
    private Sensor selected = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnNetwork = findViewById(R.id.bt_network);
        btnConnection = findViewById(R.id.bt_connection);
        bapNetwork = findViewById(R.id.bap_network);
        etIp = findViewById(R.id.et_ip);
        etPort = findViewById(R.id.et_port);
        tvState = findViewById(R.id.tv_state);
        tvTemperature = findViewById(R.id.tv_temperature);
        tvBrigthness = findViewById(R.id.tv_brightness);
        tbOrder = findViewById(R.id.tb_order);


        // liste des sensors
        spRoom = findViewById(R.id.sp_room);
        SensorAdapter adapter = new SensorAdapter(this, new ArrayList<>());
        spRoom.setAdapter(adapter);


        spRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.this.selected = adapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                MainActivity.this.selected = null;
            }
        });

        btnNetwork.setOnClickListener((v -> {
            bapNetwork.setVisibility(View.VISIBLE);
            btnNetwork.setVisibility(View.INVISIBLE);
        }));

        btnConnection.setOnClickListener(v -> {
            Thread thread = new Thread() {
                public void run() {

                    final String host = MainActivity.this.etIp.getText().toString();
                    final String port = MainActivity.this.etPort.getText().toString();

                    String serverUrl = "http://" + host + ":" + port + "/api/";
                    MainActivity.this.service = new Retrofit.Builder().baseUrl(serverUrl)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                            .create(API.class);

                    try {
                        MainActivity.this.service.checkIfServerIsAlive().execute();
                        try {

                            final Sensor[] sensors = MainActivity.this.service.getSensors().execute().body();
                            // mise à jour des sensors dans la vue
                            MainActivity.this.runOnUiThread(() -> {
                                adapter.clear();
                                adapter.addAll(sensors);
                                MainActivity.this.selected = adapter.getItem(0);
                                MainActivity.this.findViewById(R.id.sp_room).setVisibility(View.VISIBLE);
                            });


                        } catch (Exception e) {
                            makeToast("Could not fetch sensors' list ", Toast.LENGTH_LONG);
                        }

                    } catch (Exception e) {
                        makeToast("There is no server at " + serverUrl, Toast.LENGTH_LONG);
                    }

                    while (true) {

                        try {

                            if (MainActivity.this.selected != null) {
                                final Sensor selected = MainActivity.this.selected;

                                Data temperature = MainActivity.this.service.getData(selected.getSerial(), Code.DbMap.Temperature).execute().body();
                                Data brightness = MainActivity.this.service.getData(selected.getSerial(), Code.DbMap.brightness).execute().body();

                                MainActivity.this.runOnUiThread(() -> {
                                    tvTemperature.setText(String.format("Température : %s", temperature.getValue()));
                                    tvBrigthness.setText(String.format("Luminosity : %s", brightness.getValue()));
                                });
                            }

                            sleep(10 * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                }
            };
            thread.start();

            bapNetwork.setVisibility(View.INVISIBLE);
            btnNetwork.setVisibility(View.VISIBLE);
            tbOrder.setVisibility(View.VISIBLE);

            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();

        });

        tbOrder.setOnClickListener((v -> {
            Thread threadSend = new Thread() {
                public void run() {
                    try {
                        if (MainActivity.this.selected != null) {
                            MainActivity.this.service.setOrder(MainActivity.this.selected.getSerial(), new String[]{"T", "L"}).execute();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            threadSend.start();
        }));

    }


    private void makeToast(String text, int duration) {
        this.runOnUiThread(() -> Toast.makeText(this, text, duration).show());
    }

}