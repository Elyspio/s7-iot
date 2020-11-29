package com.example.iot_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

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
import java.net.URL;

import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private Button btNetwork;
    private Button btConnection;
    private BottomAppBar bapNetwork;
    private EditText etIp;
    private EditText etPort;
    private TextView tvState;
    private TextView tvTemperature;
    private TextView tvBrigthness;
    private ToggleButton tbOrder;
    private Spinner spRoom;
    private API service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.service = new Retrofit.Builder().baseUrl("http://10.0.2.2:5000/api/").build().create(API.class);

        btNetwork = (Button)findViewById(R.id.bt_network);
        btConnection = (Button)findViewById(R.id.bt_connection);
        bapNetwork = (BottomAppBar)findViewById(R.id.bap_network);
        etIp = (EditText)findViewById(R.id.et_ip);
        etPort = (EditText)findViewById(R.id.et_port);
        tvState = (TextView)findViewById(R.id.tv_state);
        tvTemperature = (TextView)findViewById(R.id.tv_temperature);
        tvBrigthness = (TextView)findViewById(R.id.tv_brightness);
        tbOrder = (ToggleButton)findViewById(R.id.tb_order);
        spRoom = findViewById(R.id.sp_room);


//create a list of items for the spinner.
        String[] items = new String[]{"Salon", "Test"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        spRoom.setAdapter(adapter);

        btNetwork.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bapNetwork.setVisibility(View.VISIBLE);
                btNetwork.setVisibility(View.INVISIBLE);
            }
        }));

        btConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread threadRecep = new Thread(){
                    public void run(){
                        while (true) {
                            if(testConnection(etIp.getText().toString(), etPort.getText().toString())) {

                                Log.d("test", String.valueOf(spRoom.getSelectedItem()));

                                int room;
                                switch(String.valueOf(spRoom.getSelectedItem())){
                                    case "Salon":
                                        room = 9;
                                        break;
                                    case "Test":
                                        room = 10;
                                        break;
                                    default:
                                        throw new IllegalStateException("Unexpected value: " + String.valueOf(spRoom.getSelectedItem()));
                                }


                                String temperature = recupStream(etIp.getText().toString(), etPort.getText().toString(), room,1);
                                String brigthness = recupStream(etIp.getText().toString(), etPort.getText().toString(), room,0);

                                if (temperature != null)
                                    temperature = recupData(temperature, "value");
                                if (brigthness != null) brigthness = recupData(brigthness, "value");

                                String finalTemperature = temperature;
                                String finalBrigthness = brigthness;
                                MainActivity.this.runOnUiThread(() -> {
                                    tvState.setText("Connecté");
                                    tvTemperature.setText(finalTemperature);
                                    tvBrigthness.setText(finalBrigthness);
                                });

                                try {
                                    Thread.sleep(10000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            else{
                                MainActivity.this.runOnUiThread(() -> {
                                    tvState.setText("Déconnecté");
                                    tvTemperature.setText("Température");
                                    tvBrigthness.setText("Luminosité");
                                });
                            }
                        }
                    }
                };
                threadRecep.start();

                bapNetwork.setVisibility(View.INVISIBLE);
                btNetwork.setVisibility(View.VISIBLE);
                tbOrder.setVisibility(View.VISIBLE);
            }
        });

        tbOrder.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("check", String.valueOf(tbOrder.isChecked()));

                Thread threadSend = new Thread(){
                    public void run() {
                        try {
                            MainActivity.this.service.setOrder(9, new String[]{"T", "L"}).execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                threadSend.start();

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


    private String recupStream(String ip, String port, int room, int data){
        String result = null;
        HttpURLConnection urlConnection = null;
        URL url = null;
        try {
            url = new URL("http://"+ ip +":"+ port +"/api/data/last/"+ room +"/"+ data);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            result = readStream(in);
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

    private Boolean testConnection(String ip, String port){
        Boolean connected = false;
        HttpURLConnection urlConnection = null;
        URL url = null;
        try {
            url = new URL("http://"+ ip +":"+ port +"/api/data/9");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.getResponseCode();
            connected = true;
            Log.d("connected","true");
        }catch(java.net.UnknownHostException e) {
            Log.d("connected", "false");
            connected = false;
        } catch (IOException e){

        } finally {
            urlConnection.disconnect();
        }
        return connected;
    }
}