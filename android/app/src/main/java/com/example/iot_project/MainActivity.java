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

    //Declaration de mes variables
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

    /*
        Function onCreate
        Override de la méthode OnCreate étant appelé au lancement de l'activité
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                                             //Appel de la méthode parente
        setContentView(R.layout.activity_main);                                         //Appel de la vue

        //Récupération des composants de ma vue dans mes variables
        btnNetwork = findViewById(R.id.bt_network);
        btnConnection = findViewById(R.id.bt_connection);
        bapNetwork = findViewById(R.id.bap_network);
        etIp = findViewById(R.id.et_ip);
        etPort = findViewById(R.id.et_port);
        tvState = findViewById(R.id.tv_state);
        tvTemperature = findViewById(R.id.tv_temperature);
        tvBrigthness = findViewById(R.id.tv_brightness);
        tbOrder = findViewById(R.id.tb_order);


        //liste des sensors
        spRoom = findViewById(R.id.sp_room);
        SensorAdapter adapter = new SensorAdapter(this, new ArrayList<>());
        spRoom.setAdapter(adapter);

        /*
            Listener sur spRoom
            Permet de récupérer le choix de pièce séléctionner (à afficher)
         */
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

        /*
            Listener sur btnNetwork
            Permet d'afficher la BottomAppBar de connexion
         */
        btnNetwork.setOnClickListener((v -> {
            bapNetwork.setVisibility(View.VISIBLE);
            btnNetwork.setVisibility(View.INVISIBLE);
        }));

        /*
            Listener sur btnConnexion
            Permet de lancer un thread de lecture du serveur
         */
        btnConnection.setOnClickListener(v -> {
            Thread thread = new Thread() {                                                                          //Déclaration du thread
                public void run() {

                    final String host = MainActivity.this.etIp.getText().toString();                                //Récupération de l'ip
                    final String port = MainActivity.this.etPort.getText().toString();                              //Récupération du port

                    String serverUrl = "http://" + host + ":" + port + "/api/";                                     //Construction de l'URL
                    MainActivity.this.service = new Retrofit.Builder().baseUrl(serverUrl)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                            .create(API.class);                                                                     //Création de notre objet Retrofit s'occupant de la connexion

                    try {
                        MainActivity.this.service.checkIfServerIsAlive().execute();                                 //Vérification de la connexion
                        try {
                            final Sensor[] sensors = MainActivity.this.service.getSensors().execute().body();       //Récupération de la liste des sensors (du microbit) dans le serveur => On parlera par la siute de pièce pour plus de claireté

                            //Mise à jour des sensors dans la vue
                            MainActivity.this.runOnUiThread(() -> {
                                adapter.clear();                                                                    //Vidage de la liste
                                adapter.addAll(sensors);                                                            //Ajout des données récupérée dans la liste
                                MainActivity.this.selected = adapter.getItem(0);                            //Séléction du premier item
                                MainActivity.this.findViewById(R.id.sp_room).setVisibility(View.VISIBLE);           //Rend visible le composant
                            });
                        } catch (Exception e) {
                            makeToast("Could not fetch sensors' list ", Toast.LENGTH_LONG);                    //Affichage popup d'erreur
                        }

                    } catch (Exception e) {
                        makeToast("There is no server at " + serverUrl, Toast.LENGTH_LONG);                    //Affichage popup d'erreur
                    }

                    while (true) {                                                                                  //Lancement de la boucle de lecture
                        try {
                            if (MainActivity.this.selected != null) {                                               //Si une pièce est séléctionnée
                                final Sensor selected = MainActivity.this.selected;                                 //Récupération de la pièce

                                Data temperature = MainActivity.this.service.getData(selected.getSerial(), Code.DbMap.Temperature).execute().body();    //Récupération de la data
                                Data brightness = MainActivity.this.service.getData(selected.getSerial(), Code.DbMap.brightness).execute().body();      //Récupération de la data

                                //Mise à jour des sensors dans la vue
                                MainActivity.this.runOnUiThread(() -> {
                                    tvTemperature.setText(String.format("Température : %s", temperature.getValue()));                                   //Ajout de la température
                                    tvBrigthness.setText(String.format("Luminosité : %s", brightness.getValue()));                                      //Ajout de la luminosité
                                });
                            }
                            sleep(10 * 1000);                                                                //Pause pour ne pas spam le serveur
                        } catch (InterruptedException e) {                                                         //Récupération des erreurs
                            e.printStackTrace();
                        } catch (IOException e) {                                                                  //Récupération des erreurs
                            e.printStackTrace();
                        }


                    }
                }
            };
            thread.start();                                                             //Lancement du thread

            bapNetwork.setVisibility(View.INVISIBLE);                                   //Rend invisible le BottomAppBar de connexion
            btnNetwork.setVisibility(View.VISIBLE);                                     //Rend visible le bouton Réseau
            tbOrder.setVisibility(View.VISIBLE);                                        //Rend visible le bouton de gestion de l'ordre d'affichage coté microbit

            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();

        });

        /*
            Listener sur btnOrder
            Permet de lancer un thread d'écriture au serveur
         */
        tbOrder.setOnClickListener((v -> {
            Thread threadSend = new Thread() {                                      //Déclaration du thread
                public void run() {
                    try {
                        if (MainActivity.this.selected != null) {                   //Si une pièce est séléctionnée
                            MainActivity.this.service.setOrder(MainActivity.this.selected.getSerial(), new String[]{"T", "L"}).execute();       //Envoie de l'odre au serveur
                        }
                    } catch (IOException e) {                                       //Récupération des erreurs
                        e.printStackTrace();
                    }
                }
            };
            threadSend.start();                                                     //Lancement du thread
        }));

    }

    /*
       Function makeToast
       Permet de générer un Toast
    */
    private void makeToast(String text, int duration) {
        this.runOnUiThread(() -> Toast.makeText(this, text, duration).show());
    }

}