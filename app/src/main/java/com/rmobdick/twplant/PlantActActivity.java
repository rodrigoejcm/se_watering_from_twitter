package com.rmobdick.twplant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by rodrigo on 14/04/18.
 */

public class PlantActActivity extends AppCompatActivity {

    private static final String TAG = "PlantActActivity";


    private TextView plant_name;
    private TextView plant_id;

    private Switch sw_act_heat;
    private Switch sw_act_wat;
    private Switch sw_act_luz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_act);

        initializeVariables();

        // BOTAO SAVE ACT RETURN INTENT
        Button button_save_act = (Button) findViewById(R.id.btn_save_act);
        button_save_act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ////////////////////////////////
                // API CALL INFORMING NEW VALUES FOR ACTUATORS
                String msgTeste = "API: PLANT_TH $POST : Heat " +
                        sw_act_heat.isChecked() + " - Water " +
                        sw_act_wat.isChecked() + " - Light " +
                        sw_act_luz.isChecked();
                Toast.makeText(getApplicationContext(), msgTeste, Toast.LENGTH_LONG).show();
                ////////////////////////////////
                ///////////////////////////////

                // Back to plantinfo activity
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
    }

    private void initializeVariables() {
        Log.d(TAG, "initializeVariables: Inicializando vars");

        plant_name = (TextView) findViewById(R.id.plant_act_nome_planta);
        plant_id = (TextView) findViewById(R.id.plant_act_id_planta);

        sw_act_heat = findViewById(R.id.act_heat);
        sw_act_wat = findViewById(R.id.act_wat);
        sw_act_luz = findViewById(R.id.act_luz);

        setVariablesValues();
    }

    private void setVariablesValues() {

        Log.d(TAG, "setVariablesValues: Settinf Var e chackeing extras");

        if (getIntent().hasExtra("plant_name") && getIntent().hasExtra("plant_id") ){

            Log.d(TAG, "setVariablesValues: " + getIntent().getExtras());

            // Insert NameID
            Log.d(TAG, "setVariablesValues: Iniciar set var" );
            plant_name.setText(getIntent().getStringExtra("plant_name"));
            plant_id.setText(getIntent().getStringExtra("plant_id"));
            Log.d(TAG, "setVariablesValues: setado nome e id");

            // COMO O STATUS DOS ACTUATORS PODE MUDAR RPIDAMENTE. é MELHRO SOLICITAR VIA API
            // PARA DADOS ATUALIZADOS
            // HERE WE CAN CALL THE API TO GET ACTUATORS STATUS
            Toast.makeText(this, "API: PLANTACT $GET - Parametro ID "+ plant_id.getText().toString()  , Toast.LENGTH_SHORT).show();
            Log.d(TAG, "setVariablesValues: fez o toast");
            // RESPONSE

            // DUMMY PLANT ACT STATUS
            Random random = new Random();
            sw_act_heat.setChecked(random.nextBoolean());
            if(sw_act_heat.isChecked()){ sw_act_heat.setText("Heat ( Heater is ON )");}

            sw_act_wat.setChecked(random.nextBoolean());
            if(sw_act_heat.isChecked()){ sw_act_heat.setText("Watering ( Water is ON )");}

            sw_act_luz.setChecked(random.nextBoolean());
            if(sw_act_heat.isChecked()){ sw_act_heat.setText("Light ( Light is ON )");}

            //
        }
    }


}
