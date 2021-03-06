package com.rmobdick.twplant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rodrigo on 13/04/18.
 * OBS>>>
 * 1 - COMO o seekbar so funciona com inteiros,
 * para simular um float dividimos por 10 ( o valor do seek )
 * e acrescentamos a virgula para exibir visualmente
 *
 * 2 - De forma análoga, guardamos no seek o valor inteiro multiplicado por 10, e quando
 * for necessario enviar, dividimos e trnsformamos em double
 *
 * EX: valor 23.4 > x10 Int -> SEEK 234 > mudançca 221 > Transforma 221.0 >>  divide 22.1
 */

public class PlantThActivity extends AppCompatActivity {

    private static final String TAG = "PlantThActivity";

    private SeekBar seekBarTemp;
    private SeekBar seekBarHum;
    private SeekBar seekBarLuz;

    
    private TextView tv_display_temp;
    private TextView tv_display_hum;
    private TextView tv_display_luz;

    private TextView plant_name;
    private TextView plant_id;


    // MAX VALUES AS PLANTINFOACT RANDON GENERTOR

    private final double DEF_TEMP = 20.0;
    private final int MAX_TEMP = 400; // Explicado em cima.
    private final double DEF_HUM = 20.0;
    private final int MAX_HUM = 1000; // Explicado em cima.
    private final double DEF_LUM = 10.0;
    private final int MAX_LUM = 1000; // Explicado em cima.

    private final String SERVER = "192.168.10.50";
    private final String PORT = "8000";

    RequestQueue queue;
    String url ="http://"+SERVER+":"+PORT+"/plant/th";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_plant_th);
        Log.d(TAG, "onCreate: Criado");

        //Inicializa todas as variáveis
        initializeVariables();

        // OnChange Listener for Temp to display changes on textview display_temp
        seekBarTemp.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                //Log.d(TAG, "onProgressChanged: Progress Mudado " + progresValue);
                Double progress =  (double) progresValue/10;
                tv_display_temp.setText(String.valueOf(progress));
                //Toast.makeText(getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // as we only want to grab the changes, that s no need to implement this
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // as we only want to grab the changes, that s no need to implement this
            }
        });

        // OnChange Listener for Humidity to display changes on textview display_hum
        seekBarHum.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                //Log.d(TAG, "onProgressChanged: Progress Mudado " + progresValue);
                Double progress =  (double) progresValue/10;
                tv_display_hum.setText(String.valueOf(progress));
                //Toast.makeText(getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // as we only want to grab the changes, that s no need to implement this
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // as we only want to grab the changes, that s no need to implement this
            }
        });

        // OnChange Listener for luminosity to display changes on textview display_lum
        seekBarLuz.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                Log.d(TAG, "onProgressChanged: Progress Mudado " + progresValue);
                Double progress =  (double) progresValue/10;
                tv_display_luz.setText(String.valueOf(progress));
                //Toast.makeText(getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // as we only want to grab the changes, that s no need to implement this
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // as we only want to grab the changes, that s no need to implement this
            }
        });


        queue = Volley.newRequestQueue(this);
        Log.e(TAG, "initializeVariables: Criou queue");

        // BOTAO SAVE TH RETURN INTER
        Button button_save_th = (Button) findViewById(R.id.btn_save_th);
        button_save_th.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





                Double new_temp = (double) seekBarTemp.getProgress()/10;
                Double new_hum = (double) seekBarHum.getProgress()/10;
                Double new_luz = (double) seekBarLuz.getProgress()/10;



                sendDataToApi(getIntent().getStringExtra("plant_id"), new_temp, new_hum, new_luz);
                ////////////////////////////////
                // API CALL INFORMING NEW VALUES
                String msgTeste = "API: PLANT_TH $POST : Temp " +
                        (double) seekBarTemp.getProgress()/10 + " - Hum " +
                        (double) seekBarHum.getProgress()/10 + " - Luz " +
                        (double) seekBarLuz.getProgress()/10;
                //Toast.makeText(getApplicationContext(), msgTeste, Toast.LENGTH_LONG).show();
                ////////////////////////////////
                ///////////////////////////////



                // Back to plantinfo activity
                Intent returnIntent = new Intent();
                returnIntent.putExtra("plant_id",getIntent().getStringExtra("plant_id"));
                returnIntent.putExtra("plant_name",getIntent().getStringExtra("plant_name"));
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });


    }

    private void initializeVariables() {
        Log.d(TAG, "initializeVariables: Inicializando");

        // Set views pelo ID

        plant_name = findViewById(R.id.plant_th_nome_planta);
        plant_id = findViewById(R.id.plant_th_id_planta);

        seekBarTemp = findViewById(R.id.seekbar_temp);
        seekBarTemp.setMax(MAX_TEMP);
        seekBarHum = findViewById(R.id.seekbar_hum);
        seekBarHum.setMax(MAX_HUM);
        seekBarLuz = findViewById(R.id.seekbar_luz);
        seekBarLuz.setMax(MAX_LUM);

        tv_display_temp = findViewById(R.id.value_th_temp);
        tv_display_hum = findViewById(R.id.value_th_hum);
        tv_display_luz = findViewById(R.id.value_th_lum);

        //Log.d(TAG, "initializeVariables: Inicializado");

        getIntentInfo();


    }

    public void getIntentInfo() {

        if (getIntent().hasExtra("th_temp") &&
                getIntent().hasExtra("th_hum") &&
                    getIntent().hasExtra("th_lum") &&
                        getIntent().hasExtra("plant_name") &&
                            getIntent().hasExtra("plant_id")) {


            //Log.d(TAG, "getIntentInfo: " + getIntent().getExtras());

            // Insert Name/ID
            plant_name.setText(getIntent().getStringExtra("plant_name"));
            plant_id.setText(getIntent().getStringExtra("plant_id"));

            Double th_temp = getIntent().getDoubleExtra("th_temp", DEF_TEMP);
            Double th_hum = getIntent().getDoubleExtra("th_hum", DEF_HUM);
            Double th_lum = getIntent().getDoubleExtra("th_lum", DEF_LUM);

            Log.e(TAG, "getIntentInfo: lumi " + th_lum );

            // Set initial Th values on the display
            tv_display_temp.setText(String.valueOf(th_temp));
            tv_display_hum.setText(String.valueOf(th_hum));
            tv_display_luz.setText(String.valueOf(th_lum));

            // THE INT CONVERSION FOR THE SEEKBAR
            int th_temp_int = (int) (th_temp * 10);
            int th_hum_int = (int) (th_hum * 10);
            int th_lum_int = (int) (th_lum * 10);

            // Set converted values on the seekbars
            seekBarTemp.setProgress(th_temp_int);
            seekBarHum.setProgress(th_hum_int);
            seekBarLuz.setProgress(th_lum_int);

        }

    }

    // FONTE
    //public double getConvertedValue(int intVal){
    //    double floatVal = 0.0;
    //    floatVal = .5f * intVal;
    //   return round(floatVal,2);
    //}

    public void sendDataToApi(final String plid, final Double tempT, final Double humT, final Double lumT){
        //this.url = url+plid;
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
                //String new_act_heat = String.valueOf(sw_act_heat.isChecked());
                //String new_act_wat = String.valueOf(sw_act_wat.isChecked());
                //String new_act_luz = String.valueOf(sw_act_luz.isChecked());

                Log.d(TAG, "getParams: " + plid + "-" + lumT + "-" + tempT + " - " + humT);

                Map<String, String>  params = new HashMap<String, String>();
                params.put("plant_id", String.valueOf(plid));
                params.put("luminosityT", String.valueOf(lumT));
                params.put("humidityT", String.valueOf(humT));
                params.put("temperatureT", String.valueOf(tempT));
                Log.d(TAG, "getParams: "+ params);
                return params;
            }
        };
        Log.d(TAG, "sendDataToApi: " + postRequest);
        queue.add(postRequest);
    }

}
