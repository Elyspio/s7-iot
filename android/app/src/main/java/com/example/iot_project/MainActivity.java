package com.example.iot_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomappbar.BottomAppBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button btNetwork;
    private Button btConnection;
    private BottomAppBar bapNetwork;
    private EditText etIp;
    private EditText etPort;
    private TextView tvTemperature;
    private TextView tvBrigthness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btNetwork = (Button)findViewById(R.id.bt_network);
        btConnection = (Button)findViewById(R.id.bt_connection);
        bapNetwork = (BottomAppBar)findViewById(R.id.bap_network);
        etIp = (EditText)findViewById(R.id.et_ip);
        etPort = (EditText)findViewById(R.id.et_port);
        tvTemperature = (TextView)findViewById(R.id.tv_temperature);
        tvBrigthness = (TextView)findViewById(R.id.tv_brightness);

        btNetwork.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bapNetwork.setVisibility(View.VISIBLE);
                btNetwork.setVisibility(View.INVISIBLE);
            }
        }));

        btConnection.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Thread threadRecep = new Thread(){
                    public void run(){
                        while(true){
                            String temperature = recupStream(1);
                            String brigthness = recupStream(0);

                            if(temperature !=null) temperature = recupData(temperature, "value");
                            if(brigthness !=null) brigthness = recupData(brigthness, "value");

                            String finalTemperature = temperature;
                            String finalBrigthness = brigthness;
                            MainActivity.this.runOnUiThread(()->{
                                tvTemperature.setText(finalTemperature);
                                tvBrigthness.setText(finalBrigthness);
                            });

                            try {
                                Thread.sleep(10000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                threadRecep.start();

                bapNetwork.setVisibility(View.INVISIBLE);
                btNetwork.setVisibility(View.VISIBLE);
            }
        }));

    }
    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }


    private String recupStream(int data){
        String result = null;
        HttpURLConnection urlConnection = null;
        URL url = null;
        try {
            url = new URL("http://10.0.2.2:5000/api/data/last/9/"+data);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            result = readStream(in);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return result;
    }

    private String recupData(String stream, String data){
        JSONArray jsonArray = null;
        JSONObject jsonObject = null;
        String result = null;
        try {
            jsonArray = new JSONArray(stream);
            jsonObject = (JSONObject) jsonArray.get(0);
            result = String.valueOf(jsonObject.get(data));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}