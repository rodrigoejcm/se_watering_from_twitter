package com.rmobdick.twplant;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

public class NewPlantActivity extends AppCompatActivity {

    //public static final string SHARED_PREFS_PLANT = "shraredPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_plant);


        Button button_create_plant = (Button) findViewById(R.id.btn_create_plant);
        button_create_plant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText plant_id =  findViewById(R.id.form_plant_id);
                EditText plant_name = findViewById(R.id.form_plant_name);

                SharedPreferences prefs = getSharedPreferences("shraredPrefs" , MODE_PRIVATE);
                String plantlist = prefs.getString("plantlist", "NO_PLANTS");

                Gson gson = new Gson();

                HashMap<String, String> plant_list_hash = new HashMap<String, String>();

                if (plantlist != "NO_PLANTS"){
                    Log.d("prefs", "Lista atual " + plantlist );
                    java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>(){}.getType();
                    plant_list_hash = gson.fromJson(plantlist, type);
                }else{
                    Log.d("prefs", "Nenhuma planta na lista");
                }


                plant_list_hash.put(plant_id.getText().toString(),plant_name.getText().toString());
                String hashMapString = gson.toJson(plant_list_hash);
                prefs.edit().putString("plantlist", hashMapString).apply();
                Log.d("prefs", hashMapString);

                //Toast.makeText(getApplicationContext(), "New plant added sucessfully!", Toast.LENGTH_LONG).show();

                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
    }
}
