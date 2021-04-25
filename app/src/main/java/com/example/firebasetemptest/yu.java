package com.example.firebasetemptest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class yu extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;

    DatabaseHelper myDB;

    private ListView dataListView;
    private EditText userID;
    private EditText userName;
    private EditText userEmail;
    private EditText userPassword;
    private EditText userUpdate;
    private EditText userValueToUpdate;
    private Button findButton;
    private Button deleteButton;
    private Button addButton;
    private Button updateButton;
    private Button goToLocalDB;

    private Button showUpdateSection;
    private Boolean searchMode = false;
    private Boolean itemSelected = false;
    private int selectedPosition = 0;

    int x=0;

    ImageView imgView;
    Button bttnGoToWeather;
    MainActivity2Weather weather=new MainActivity2Weather();

    int g;//hide unhide update section

    ArrayList<String> listItems = new ArrayList<String>();
    ArrayList<String> listKeys = new ArrayList<String>();
    ArrayAdapter<String> adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yu);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        myDB=new DatabaseHelper(this);


        goToLocalDB=(Button)findViewById(R.id.bttnGoToLocalDBFromFireBase);

        bttnGoToWeather=(Button)findViewById(R.id.goToMain2FromYu);
        imgView=(ImageView)findViewById(R.id.imageViewYu);
        imgView.setImageResource(weather.getImgResource());

        goToLocalDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateDB(myDB);
                startActivity(new Intent(yu.this,InteractWithLocalDB.class));
            }
        });

        bttnGoToWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(yu.this,MainActivity.class));

            }
        });


        showUpdateSection=(Button)findViewById(R.id.bttnUpdate);//show section
        updateButton=(Button)findViewById(R.id.bttnSubmitDataToUpDate);
        updateButton.setEnabled(false);


        dataListView = (ListView) findViewById(R.id.list);
        userID = (EditText) findViewById(R.id.inputID);
        userName = (EditText) findViewById(R.id.inputName);
        userEmail = (EditText) findViewById(R.id.inputEmail);
        userPassword = (EditText) findViewById(R.id.inputPassword);
        userUpdate=(EditText) findViewById(R.id.inputToUpdate);
        userValueToUpdate=(EditText)findViewById(R.id.inputValueToUpdate) ;
        //findButton = (Button) findViewById(R.id.findButton);
        deleteButton = (Button) findViewById(R.id.bttnDelete);
        addButton=(Button)findViewById(R.id.bttnAdd);
        deleteButton.setEnabled(false);

        showUpdateSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(g==0){//means its hidden
                    userValueToUpdate.setVisibility(View.VISIBLE);
                    userUpdate.setVisibility(View.VISIBLE);
                    updateButton.setVisibility(View.VISIBLE);
                    g=1;
                }
                else{
                    userValueToUpdate.setVisibility(View.INVISIBLE);
                    userUpdate.setVisibility(View.INVISIBLE);
                    updateButton.setVisibility(View.INVISIBLE);
                    g=0;
                }

            }
        });




        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataListView.setItemChecked(selectedPosition, false);//TO
                Log.d("Osama","Selected positon is: "+selectedPosition);
                myRef.child(listKeys.get(selectedPosition)).removeValue();
            }
        });


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // String ID,String key2update,String newValue
                String key2update=userUpdate.getText().toString();
                String value=userValueToUpdate.getText().toString();

                myRef.child(listKeys.get(selectedPosition)).child(key2update).setValue(value).addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(yu.this,"Updated",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(yu.this,"Not updated",Toast.LENGTH_LONG).show();
                        Log.e("Osama", "Error: " + e);
                    }
                });;//update row in database

                userValueToUpdate.setVisibility(View.INVISIBLE);
                userUpdate.setVisibility(View.INVISIBLE);
                updateButton.setVisibility(View.INVISIBLE);
                g=0;
                userUpdate.setText("");
                userValueToUpdate.setText("");


                //this works by deleting old row and adding the updated row
                //when the user clicks "update" the list will add the updated row
                //so far the old row is still there
                //notice that both old and updated rows will have the same key
                //listKeys["RowKey1","RowKey2","RowKey2",""RowKey3] old and new rows rspectively
                //all we do is delete the item the first repeated key
                //see method for onChildChnaged it adds a row in the list and deleted the old row
                //thus listKeys["RowKey1","RowKey2",""RowKey3] leaving only one instance of the updated row key

            }
        });



        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    String name = userName.getText().toString();
                    String email = userEmail.getText().toString();
                    String ID = userID.getText().toString();
                    String password = userPassword.getText().toString();

                    if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(ID) || TextUtils.isEmpty(password)){
                        Toast.makeText(yu.this,"Fill all fields",Toast.LENGTH_LONG).show();
                    }
                    else
                        writeNewUser(ID, name, email, password);





                adapter.notifyDataSetChanged();
            }
        });

        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listItems);

        dataListView.setAdapter(adapter);
        dataListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        dataListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent,
                                            View view, int position, long id) {
                        selectedPosition = position;
                        itemSelected = true;
                        deleteButton.setEnabled(true);
                        updateButton.setEnabled(true);
                    }
                });

        addChildEventListener();
    }




    public void writeNewUser(String ID, String name, String email, String password) {

        String key = myRef.push().getKey();//genrated a random key for each child added in the database
        Log.d("Osama","myRef.push(): "+ key);
        User user = new User();
        user.setName(name);
        user.setID(ID);
        user.setEmail(email);
        user.setPassword(password);

       // myRef.child(ID).

        myRef.child(ID).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
               Toast.makeText(yu.this,"Added",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(yu.this,"Not added",Toast.LENGTH_LONG).show();
                Log.e("Osama", "Error: " + e);
            }
        });

        adapter.notifyDataSetChanged();//tells the view to refresh itself
    }

    private void addChildEventListener() {
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("Osama","onCreate onChild added invoked"+snapshot.child("name").getValue().toString());


                String ID=snapshot.child("id").getValue().toString();

                String name=snapshot.child("name").getValue().toString();

                String email=snapshot.child("email").getValue().toString();

                String password=snapshot.child("password").getValue().toString();


                String row="ID: "+ID+"\nName: "+name+"\nEmail: "+email+"\nPassword: "+password;



                adapter.add(row);
                listKeys.add(snapshot.getKey());
                Log.d("Osama","Key: "+snapshot.getKey());//gets key of added element
                Log.d("Osama","listKeys: "+listKeys.toString());
                //adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                String key=snapshot.getKey();
                Log.d("Osama","Key of changed child: "+key);
                int index =listKeys.indexOf(key);
                Log.d("Osama","Index of changed child: "+index);

                String ID=snapshot.child("id").getValue().toString();
                String name=snapshot.child("name").getValue().toString();
                String email=snapshot.child("email").getValue().toString();
                String password=snapshot.child("password").getValue().toString();

                String row="ID: "+ID+"\nName: "+name+"\nEmail: "+email+"\nPassword: "+password;


                adapter.add(row);
                listKeys.add(snapshot.getKey());

                listItems.remove(index);//remove old row from listView
                listKeys.remove(index);//remove the key of the old row from list of keys


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String key = snapshot.getKey();//gives the random genrated key for the deleted child
                int index = listKeys.indexOf(key);//locates the location of the key in the listKey (array containing random keys)

                if (index != -1) {
                    listItems.remove(index);//responsible of deleting the row from the adapter
                    listKeys.remove(index);//removes the key for the deleted row
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //myRef.addChildEventListener(ChildEventListener);
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

    public void populateDB(DatabaseHelper myDB){

        myRef.addValueEventListener(new ValueEventListener() {
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



                        String password=ds.child("password").getValue().toString();
                        Log.d("Osama","Password: "+password);



                        String ID=ds.child("id").getValue().toString();
                        Log.d("Osama","ID: "+ID);

                        myDB.addData(ID,name,email,password);

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
}

