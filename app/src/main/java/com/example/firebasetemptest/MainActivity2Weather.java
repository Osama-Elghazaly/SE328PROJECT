package com.example.firebasetemptest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity2Weather extends AppCompatActivity {

    private double temp;
    private String weather;//weather--> main
    private String city;
    public  ImageView img;

    public static int imgResource;

    String weatherWebserviceURL = "http://api.openweathermap.org/data/2.5/weather?q=calgary&appid=e029856a870d493857afdb9a4b423d5e&units=metric";


    // Textview to show temperature and description
    TextView temperature, humidity;
    TextView tempratureVal,humidityVal;

    EditText inputCity;

    Button submit;
    Button goToMainActivity;


    JSONObject jsonObj;


//GET METHOD WITH IF STATMENTS INSIDE
    //IF e.g., snow --> return R.drwable.snow
    //else if(Rain)
    //return R.drwable.rain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2_weather);
        //link graphical items to variables
        temperature = (TextView) findViewById(R.id.temperature);
        humidity = (TextView) findViewById(R.id.humidity);
        tempratureVal=(TextView)findViewById(R.id.temperatureValue);
        humidityVal=(TextView)findViewById(R.id.humidityValue);
        inputCity=(EditText)findViewById(R.id.userCity);
        goToMainActivity=(Button)findViewById(R.id.goToActivityMain);

        humidity.setVisibility(View.INVISIBLE);
        temperature.setVisibility(View.INVISIBLE);


        submit=(Button)findViewById(R.id.bttnSubmitWheather);
        img = (ImageView) findViewById(R.id.weatherbackground);




        goToMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity2Weather.this,MainActivity.class));
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                humidity.setVisibility(View.VISIBLE);
                temperature.setVisibility(View.VISIBLE);

                String city=inputCity.getText().toString();
                String par="http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=e029856a870d493857afdb9a4b423d5e&units=metric";

                weather(par);


            }
        });
    }

    public void weather(String url) {
        // we will send a json formatted request and we will recieve a formatted json formatted reply
        JsonObjectRequest jsonObj =
                new JsonObjectRequest(Request.Method.GET,
                        url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // result of the request

                        Log.d("Osama",response.toString());

                        try {
                            //please use the json validator and formatter to see what u r dealing with

                            JSONObject jsonMain = response.getJSONObject("main");//for all jsons to be saved they need a try and catch
                            Log.d("Osama","subObject"+jsonMain.toString());

                            double temp=jsonMain.getDouble("temp");//getting the temp in the main json object
                            Log.d("Osama","temp is: "+temp);
                            tempratureVal.setText(String.valueOf(temp));

                            double humidity=jsonMain.getDouble("humidity");//getting the humidity in the main json object
                            Log.d("Osama","Humidity is: "+humidity);
                            humidityVal.setText(String.valueOf(humidity));

                            whetherPic(response.getJSONArray("weather"));//calling the weatherPic and passing it the weather element);
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //log errors here
                        Log.d("Osama","Error in url");

                    }

                });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObj);
    }


    public void whetherPic(JSONArray jArray) {
        //accept an array as an input and go through the array and do work on each object in the array
        // we accept it as an array since the wether element is an array
        // anything that starts with a [] is an array

        Log.d("Osama","Inside the whetherPic method");

        for (int i = 0; i < jArray.length(); i++) {
            try {
                JSONObject oneObject = jArray.getJSONObject(i);
                Log.d("Osama", "jArray(i): " + oneObject.toString());

                String weatherCondition=oneObject.getString("main");
                Log.d("Osama","Weather condition: "+weatherCondition);




                    img.setImageResource(R.drawable.cloudy);

                    if(weatherCondition.equals("Clouds")){

                        img.setImageResource(R.drawable.cloudy);
                        img.setTag(R.drawable.cloudy);
                        imgResource=(Integer)img.getTag();
                    }
                    else if(weatherCondition.equals("Clear")){
                        img.setImageResource(R.drawable.clear);
                        img.setTag(R.drawable.clear);
                        imgResource=(Integer)img.getTag();
                    }
                    else if(weatherCondition.equals("Rain")){
                        img.setImageResource(R.drawable.rainy);
                        img.setTag(R.drawable.rainy);
                        imgResource=(Integer)img.getTag();
                    }
                    else if(weatherCondition.equals("Snow")){
                        img.setImageResource(R.drawable.snowy);
                        img.setTag(R.drawable.snowy);
                        imgResource=(Integer)img.getTag();
                }



            } catch (Exception e) {
                Log.d("Osama","Error JSONarray "+ jArray.toString());

            }
        }
    }

    public int getImgResource(){
        return imgResource;
    }

}