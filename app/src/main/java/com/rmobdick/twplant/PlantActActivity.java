package com.rmobdick.twplant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by rodrigo on 14/04/18.
 */

public class PlantActActivity extends AppCompatActivity {

    private static final String TAG = "PlantActActivity";
    private ProgressDialog dialog;


    private TextView plant_name;
    private TextView plant_id;

    private Switch sw_act_heat;
    private Switch sw_act_wat;
    private Switch sw_act_luz;


    private final String SERVER = "192.168.10.50";
    private final String PORT = "8000";

    RequestQueue queue;
    //String url ="https://my-json-server.typicode.com/rodrigoejcm/fakeapi/actuators?id=";
    //http://192.168.10.173:8000/plant/act?plant_id=3
    //http://192.168.10.173:8000/plant/act?plant_id=1&act_temp=true&act_lum=true&act_hum=true

    String url ="http://"+SERVER+":"+PORT+"/plant/act";
    String url_get ="http://"+SERVER+":"+PORT+"/plant/act?plant_id=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_act);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Fetching plant info...");
        dialog.setCancelable(false);

        showDialog();

        initializeVariables();

        hideDialog();

        // BOTAO SAVE ACT RETURN INTENT
        Button button_save_act = (Button) findViewById(R.id.btn_save_act);
        button_save_act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getIntent().hasExtra("plant_name") && getIntent().hasExtra("plant_id") ){
                    ////////////////////////////////
                    // API CALL INFORMING NEW VALUES FOR ACTUATORS
                    String msgTeste = "API: PLANT_TH $POST : Heat " +
                            sw_act_heat.isChecked() + " - Water " +
                            sw_act_wat.isChecked() + " - Light " +
                            sw_act_luz.isChecked();
                    sendDataToApi(getIntent().getStringExtra("plant_id"));
                    //Toast.makeText(getApplicationContext(), msgTeste, Toast.LENGTH_LONG).show();
                    ////////////////////////////////
                    ///////////////////////////////
                }


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



        if (getIntent().hasExtra("plant_name") && getIntent().hasExtra("plant_id") ){
            // FROM THE API
            queue = Volley.newRequestQueue(this);
            Log.e(TAG, "initializeVariables: Criou queue");
            getDataFromApi(getIntent().getStringExtra("plant_id"));
        }
        //getDataFromApi();
        // setVariablesValues();
    }

    private void setVariablesValues(Boolean act_temp, Boolean act_luz, Boolean act_hum) {

        Log.d(TAG, "setVariablesValues: Settinf Var e chackeing extras");

        if (getIntent().hasExtra("plant_name") && getIntent().hasExtra("plant_id") ){

            //Log.d(TAG, "setVariablesValues: " + getIntent().getExtras());

            // Insert NameID
            //Log.d(TAG, "setVariablesValues: Iniciar set var" );
            plant_name.setText(getIntent().getStringExtra("plant_name"));
            plant_id.setText(getIntent().getStringExtra("plant_id"));
            //Log.d(TAG, "setVariablesValues: setado nome e id");

            // COMO O STATUS DOS ACTUATORS PODE MUDAR RPIDAMENTE. é MELHRO SOLICITAR VIA API
            // PARA DADOS ATUALIZADOS
            // HERE WE CAN CALL THE API TO GET ACTUATORS STATUS
            //Toast.makeText(this, "API: PLANTACT $GET - Parametro ID "+ plant_id.getText().toString()  , Toast.LENGTH_SHORT).show();
            //Log.d(TAG, "setVariablesValues: fez o toast");
            // RESPONSE

            // DUMMY PLANT ACT STATUS
            //Random random = new Random();
            Log.e(TAG, "setVariablesValues: " + act_temp + act_hum + act_luz);

            sw_act_heat.setChecked(act_temp);
            if(sw_act_heat.isChecked()){ sw_act_heat.setText("Heat ( Heater is ON )");}

            sw_act_wat.setChecked(act_hum);
            if(sw_act_wat.isChecked()){ sw_act_wat.setText("Watering ( Water is ON )");}

            sw_act_luz.setChecked(act_luz);
            if(sw_act_luz.isChecked()){ sw_act_luz.setText("Light ( Light is ON )");}



            //
        }

    }

    public void getDataFromApi(String plid){

        this.url_get = url_get+plid;

        // prepare the Request
        Log.e(TAG, "getDataFromApi: testando  "  + url_get);



        Log.e(TAG, "getDataFromApi: é aqui  "  + url_get);

        JsonArrayRequest getRequest = new JsonArrayRequest(url_get,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responseArray) {
                        Log.e(TAG, "onResponse: testando  ");

                        try {

                            JSONObject response = responseArray.getJSONObject(0);
                            if(response.has("act_temp") &
                                    response.has("act_hum") &
                                    response.has("act_lum")){
                                Log.e(TAG, "onResponse: vai try testando  ");
                                try {
                                    Log.e(TAG, "onResponse: DEntro do try  ");

                                    Boolean b_heat = Boolean.valueOf(response.getString("act_temp"));
                                    Boolean b_light = Boolean.valueOf(response.getString("act_lum"));
                                    Boolean b_wat = Boolean.valueOf(response.getString("act_hum"));

                                    setVariablesValues(
                                            Boolean.valueOf(response.getString("act_temp")),
                                            Boolean.valueOf(response.getString("act_lum")),
                                            Boolean.valueOf(response.getString("act_hum")));


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }else if(response.length() == 0){
                                Log.d(TAG, "onResponse: CONECTOU E PEGOU JSON VAZIO");
                            }
                        } catch (JSONException e) {
                            Log.d(TAG, "onResponse: ta no primeiro catch");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        setVariablesValues(false,false,false);
                        //hideDialog();
                        //Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onErrorResponse: PUTS");
                    }
                });
        Log.d(TAG, "getDataFromApi: ?");
        queue.add(getRequest);
        Log.d(TAG, "getDataFromApi: SERA?");
    }

    private void showDialog() {
        if (!dialog.isShowing())
            dialog.show();
    }

    private void hideDialog() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

    public void sendDataToApi(String plid){
        this.url = url+plid;
        final String plant_id = plid;

        Log.e(TAG, "sendDataToApi: TA AQUI");
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {

                    @Override
                    public void onResponse(String response) {
                        // response

                        Log.e(TAG, "sendDataToApi: TA AQUI2" + response);
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        //Log.d("Error.Response");
                        Log.e(TAG, "sendDataToApi: TA AQUI3");
                        Log.e(TAG, "onErrorResponse: error ");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                String new_act_heat = String.valueOf(sw_act_heat.isChecked());
                String new_act_wat = String.valueOf(sw_act_wat.isChecked());
                String new_act_luz = String.valueOf(sw_act_luz.isChecked());

                Map<String, String>  params = new HashMap<String, String>();
                params.put("plant_id", plant_id );
                params.put("act_lum", new_act_luz);
                params.put("act_hum", new_act_wat);
                params.put("act_temp", new_act_heat);

                return params;
            }
        };
        queue.add(postRequest);
    }

}
