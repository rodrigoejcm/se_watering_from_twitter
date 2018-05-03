package com.rmobdick.twplant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PlantListActivity extends AppCompatActivity {

    private static final String TAG = "PlantListActivity";

    //Vars
    private ArrayList<String> mPlantNames;
    private ArrayList<String> mPlantIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_list);

        iniciaListaDePlantas();

        Button button_add_plant = (Button) findViewById(R.id.btn_add_plant);
        button_add_plant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlantListActivity.this, NewPlantActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }

    protected void onResume() {
        super.onResume();
        iniciaListaDePlantas();
    }


    private void iniciaListaDePlantas(){

        mPlantNames = new ArrayList<>();
        mPlantIds = new ArrayList<>();

        Log.d(TAG, "iniciaListaDePlantas: Inicializndo lista");

        SharedPreferences prefs = getSharedPreferences("shraredPrefs" , MODE_PRIVATE);
        String plantlist = prefs.getString("plantlist", "NO_PLANTS");

        Gson gson = new Gson();

        HashMap<String, String> plant_list_hash = new HashMap<String, String>();

        if (plantlist != "NO_PLANTS"){
            Log.d(TAG, "Lista atual " + plantlist );
            java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>(){}.getType();
            plant_list_hash = gson.fromJson(plantlist, type);

            Iterator it = plant_list_hash.entrySet().iterator();
            Log.d(TAG, "Criou Iterator" );

            while (it.hasNext()) {
                Log.d(TAG, "Dentro do While" );
                Map.Entry plant = (Map.Entry)it.next();
                mPlantIds.add(plant.getKey().toString());
                mPlantNames.add(plant.getValue().toString());
            }

            inicializaRecyclerView();

        }else{
            Log.d("prefs", "Nenhuma planta na lista");
        }

    }

    private void inicializaRecyclerView(){
        Log.d(TAG, "Inicializando Recycler");
        RecyclerView recyclerview = findViewById(R.id.plant_list);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mPlantNames,mPlantIds,this);
        recyclerview.setAdapter(adapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

    }


}
