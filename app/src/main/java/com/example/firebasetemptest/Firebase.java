package com.example.firebasetemptest;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class Firebase  extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;

    private ListView dataListView;
    private EditText userID;
    private EditText userName;
    private EditText userEmail;
    private EditText userPassword;
    private Button findButton;
    private Button deleteButton;
    private Button addButton;
    private Boolean searchMode = false;
    private Boolean itemSelected = false;
    private int selectedPosition = 0;

     StringBuffer buffer=new StringBuffer();
    int x=0;

    Firebase(String ref) {
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(ref);
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String key = ds.getKey();
                        Log.d("Osama", "Key: " + key);


                        String name = ds.child("name").getValue().toString();
                        Log.d("Osama", "Name: " + name);
                        buffer.append("Name: "+name+"\n");

                        String email = ds.child("email").getValue().toString();
                        Log.d("Osama", "Email: " + email);
                        buffer.append("Email: "+email+"\n");


                        String password=ds.child("password").getValue().toString();
                        Log.d("Osama","Password: "+password);
                        buffer.append("Password: "+password+"\n");


                        String ID=ds.child("id").getValue().toString();
                        Log.d("Osama","ID: "+ID);
                        buffer.append("ID: "+ID+"\n");
                        buffer.append("\n");

                    }
                    Log.d("Osama","Done from for loop in displayAllAsList");

                }catch (Exception e) {
                    Log.d("Osama", "for loop exception " + e.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


    public void writeNewUser(String ID, String name, String email, String password) {

        User user = new User();
        user.setName(name);
        user.setID(ID);
        user.setEmail(email);
        user.setPassword(password);

        myRef.child(ID).setValue(user);
    }

    public void writeWithSuccess(String ID, String name, String email, String password) {
        User user = new User();

        myRef.child("users").child(ID).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("George", "SUCCESS writing...");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("George", "Error: " + e);
            }
        });
    }

    public void delete(String ID){

        myRef.child(ID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Osama","Sucessfully deleted a user "+ID);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("George", "Error: " + e);
            }
        });

    }

    public void update(String ID,String key2update,String newValue){

        myRef.child(ID).child(key2update).setValue(newValue);
    }

    public void viewSpecificUser(String key2filter, String valueToFilter){

       Query query=myRef.orderByChild(key2filter).equalTo(valueToFilter);

       query.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               String s=snapshot.toString();
               Log.d("Osama","snapshot.data: "+s);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {
               Log.d("Osama","error: "+error);

           }
       });


    }

    public void viewAll(){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String key = ds.getKey();
                        Log.d("Osama", "Key: " + key);
                        String name = ds.child("name").getValue().toString();
                        Log.d("Osama", "Name: " + name);
                        String email = ds.child("email").getValue().toString();
                        Log.d("Osama", "Email: " + email);
                    }
                }catch (Exception e){
                    Log.d("Osama","for loop exception "+e.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getAllinJSon(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object object=snapshot.getValue(Object.class);
                String json=new Gson().toJson(object);
                Log.d("Osama","json: "+json);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



    public void displayAsList(){

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String key = ds.getKey();
                        Log.d("Osama", "Key: " + key);


                        String name = ds.child("name").getValue().toString();
                        Log.d("Osama", "Name: " + name);
                        buffer.append("Name: "+name+"\n");

                        String email = ds.child("email").getValue().toString();
                        Log.d("Osama", "Email: " + email);
                        buffer.append("Email: "+email+"\n");


                        String password=ds.child("password").getValue().toString();
                        Log.d("Osama","Password: "+password);
                        buffer.append("Password: "+password+"\n");


                        String ID=ds.child("id").getValue().toString();
                        Log.d("Osama","ID: "+ID);
                        buffer.append("ID: "+ID+"\n");
                        buffer.append("\n");

                    }
                    Log.d("Osama","Done from for loop in displayAllAsList");

                }catch (Exception e) {
                    Log.d("Osama", "for loop exception " + e.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }





    public StringBuffer getBuffer(){
        Log.d("Osama","In strignBuffer get");
        return buffer;
    }




}
