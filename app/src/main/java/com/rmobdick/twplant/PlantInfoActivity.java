package com.rmobdick.twplant;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.math.BigDecimal;
import java.math.RoundingMode;



public class PlantInfoActivity extends AppCompatActivity {

    private static final String TAG = "PlantInfoActivity";
    private ProgressDialog dialog;


    private final String SERVER = "192.168.10.50";
    private final String PORT = "8000";

    RequestQueue queue;

    String url ="http://"+SERVER+":"+PORT+"/plant/info?plant_id=";
    Double intent_th_temp = 0.0;
    Double intent_th_hum = 0.0;
    Double intent_th_luz = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        dialog = new ProgressDialog(this);
        dialog.setMessage("Fetching plant info...");
        dialog.setCancelable(false);

        setContentView(R.layout.activity_plant_info);

        // FROM THE API
        queue = Volley.newRequestQueue(this);
        getPlantInfoFromApi();

    }

    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ONNNNN RESTART");
        //iniciaListaDePlantas();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Fetching plant info...");
        dialog.setCancelable(false);

        //setContentView(R.layout.activity_plant_info);

        // FROM THE API
        queue = Volley.newRequestQueue(this);
        getPlantInfoFromApi();
    }



    public void getPlantInfoFromApi(){
        Log.d(TAG, "getIncomingIntent: Pegando as infos do intet");
        if (getIntent().hasExtra("plant_name") && getIntent().hasExtra("plant_ID")){
            Log.d(TAG, "getIncomingIntent: achou os extras" + getIntent().getStringExtra("plant_ID"));

            final String plant_name = getIntent().getStringExtra("plant_name");
            final String plant_ID = getIntent().getStringExtra("plant_ID");

            TextView tv_plant_name = findViewById(R.id.plant_info_nome_planta);
            TextView tv_plant_id = findViewById(R.id.plant_info_id_planta);

            tv_plant_name.setText(plant_name);
            tv_plant_id.setText(plant_ID);



            // HERE WE CAN CALL THE API WIT HTE EXTRA INFO
            //Toast.makeText(this, "API: PLANTINFO GET - Parametro ID "+ plant_ID  , Toast.LENGTH_SHORT).show();
            getDataFromApi(getIntent().getStringExtra("plant_ID"));
            //

                // DUMMY PLANT INFO SET
                //double temp_value = ThreadLocalRandom.current().nextDouble(20, 30);
                //final double temp_th = temp_value + ThreadLocalRandom.current().nextDouble(1, 5);

                //double hum_value = ThreadLocalRandom.current().nextDouble(20, 40);
                //final double hum_th = hum_value + ThreadLocalRandom.current().nextDouble(1, 2);

                //double luz_value = ThreadLocalRandom.current().nextDouble(10, 20);
                //final double luz_th = luz_value + ThreadLocalRandom.current().nextDouble(1, 5);
                //

                //SET DOS COMPOENTES
                //setPlantInfo(plant_name,plant_ID,temp_value,temp_th,hum_value,hum_th,luz_value,luz_th);


            // CREATE INTENTS WITH EXTRA FOR THE BUTTONS
            Button button_config_th = (Button) findViewById(R.id.btn_config_th);
            Log.d(TAG, "getPlantInfoFromApi: criando listerner para botao th");
            button_config_th.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: Botao clicado");

                    Intent intent = new Intent(PlantInfoActivity.this, PlantThActivity.class);
                    intent.putExtra("th_temp",round(intent_th_temp,1));
                    intent.putExtra("th_hum",round(intent_th_hum,1));
                    intent.putExtra("th_lum",round(intent_th_luz,1));
                    intent.putExtra("plant_id",plant_ID);
                    intent.putExtra("plant_name",plant_name);
                    //Log.d(TAG, "getPlantInfoFromApi: botao apertado parametros: " + round(temp_th,2) );
                    startActivityForResult(intent,1);
                }
            });

            Button button_config_act = (Button) findViewById(R.id.btn_config_act);
            button_config_act.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(PlantInfoActivity.this, PlantActActivity.class);
                    intent.putExtra("plant_id",plant_ID);
                    intent.putExtra("plant_name",plant_name);
                    startActivityForResult(intent,1);
                }
            });

            Button button_config_refresh = (Button) findViewById(R.id.btn_config_refresh);
            button_config_refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });

            Button button_twitter = (Button) findViewById(R.id.btn_tweet_feed);
            button_twitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(PlantInfoActivity.this, TwitterFeedActivity.class);
                    intent.putExtra("plant_name",plant_name);
                    startActivity(intent);

                }
            });


            Log.e(TAG, "getPlantInfoFromApi: Iniciou botoes");

        }
    }

    public void setPlantInfo(String plantName, String plantId,
                             double plantTempVal, double plantTempTh,
                             double plantHumVal, double plantHumTh,
                             double plantLuzVal, double plantLuzTh){


        Log.d(TAG, "setPlantInfo: " + plantTempTh +" -  "+ plantTempVal);

        TextView tv_plant_temp_val = findViewById(R.id.value_atual_temp);
        TextView tv_plant_temp_th = findViewById(R.id.value_th_temp);
        TextView tv_plant_luz_val = findViewById(R.id.value_atual_lum);
        TextView tv_plant_luz_th = findViewById(R.id.value_th_lum);
        TextView tv_plant_hum_val = findViewById(R.id.value_atual_hum);
        TextView tv_plant_hum_th = findViewById(R.id.value_th_hum);



        tv_plant_temp_val.setText(String.valueOf(round(plantTempVal,1)));
        tv_plant_temp_th.setText(String.valueOf(round(plantTempTh,1)));

        tv_plant_hum_val.setText(String.valueOf(round(plantHumVal,1)));
        tv_plant_hum_th.setText(String.valueOf(round(plantHumTh,1)));

        tv_plant_luz_val.setText(String.valueOf(round(plantLuzVal,1)));
        tv_plant_luz_th.setText(String.valueOf(round(plantLuzTh,1)));

    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    public void getDataFromApi(String plid){

        String thisurl = url+plid;

        // prepare the Request
        Log.e(TAG, "getDataFromApi: testando " + thisurl);

        showDialog();

        JsonArrayRequest getRequest = new JsonArrayRequest(thisurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responseArray) {
                        try {
                            hideDialog();
                            JSONObject response = responseArray.getJSONObject(0);

                            if(response.has("pl_temperature") &
                                    response.has("pl_humidity") &
                                    response.has("pl_luminosity") &
                                    response.has("th_temperature") &
                                    response.has("th_humidity") &
                                    response.has("th_luminosity")){

                                try {
                                        Double pl_T =  Double.parseDouble(response.getString("pl_temperature"));
                                        Double pl_H =  Double.parseDouble(response.getString("pl_humidity"));
                                        Double pl_L =  Double.parseDouble(response.getString("pl_luminosity"));

                                        Log.d(TAG, "onResponse: " + pl_T);
                                        Log.d(TAG, "onResponse: " + pl_H);
                                        Log.d(TAG, "onResponse: " + pl_L);

                                        Double th_T =  Double.parseDouble(response.getString("th_temperature"));
                                        Double th_H =  Double.parseDouble(response.getString("th_humidity"));
                                        Double th_L =  Double.parseDouble(response.getString("th_luminosity"));

                                        setPlantInfo(
                                            response.getString("name"),
                                            response.getString("plant_id"),
                                            pl_T,
                                            th_T,
                                            pl_H,
                                            th_H,
                                            pl_L,
                                            th_L
                                      );

                                    intent_th_temp = th_T;
                                    intent_th_hum = th_H;
                                    intent_th_luz = th_L;

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d(TAG, "onResponse: ;erro");
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
                        setPlantInfo("","",0.0,0.0,0.0,0.0,0.0,0.0);
                        hideDialog();
                        //Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onErrorResponse: PUTS");
                    }
                });

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


}
