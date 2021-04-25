package com.example.firebasetemptest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    MainActivity2Weather weather=new MainActivity2Weather();

    ImageView imgWeather;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imgWeather=(ImageView)findViewById(R.id.imageViewMainActivity);//linking to UI
        imgWeather.setImageResource(weather.getImgResource());//changing image resource

        Button goToFireBase=(Button)findViewById(R.id.bttnJumpToFireBase);
        Button goToLocalDB=(Button)findViewById(R.id.bttnJumpToLocalDB);
        Button goToWeather=(Button)findViewById(R.id.bttnJumpToWeather);


        goToFireBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,yu.class));
            }
        });

        goToLocalDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,InteractWithLocalDB.class));
            }
        });

        goToWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MainActivity2Weather.class));
            }
        });

    }
}