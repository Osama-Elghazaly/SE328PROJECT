package com.example.firebasetemptest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class InteractWithLocalDB extends AppCompatActivity {

    DatabaseHelper myDB;

    yu myRef=new yu();

    MainActivity2Weather weather=new MainActivity2Weather();


    //make an instance of FireBase here
    //Call a method in the FireBase class to populate my LocalDB
    //Make an instance of the LocalDB in the FireBase class
    //Access methods of the LocalDB class from the FireBase class

    EditText employeeID,employeeEmail,employeeName,employeePassword,key2update,newValue;

    ImageView img;

    LinearLayout updatingLayout;

    Button fetch;


    ArrayList<String> listKeys = new ArrayList<String>();
    ArrayList<String> listItems = new ArrayList<String>();

    int x=1;// to make my UPDATE layout visble/unvisible

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interact_with_local_d_b);

        Button bttnAdd=(Button)findViewById(R.id.bttnAdd);
        Button bttnUpdate=(Button)findViewById(R.id.bttnUpdate);
        Button bttnDelete=(Button)findViewById(R.id.bttnDelete);
        Button bttnView=(Button)findViewById(R.id.bttnView);
        Button bttnSubmitUpdatedSalray=(Button)findViewById(R.id.bttnSubmitUpdatedQuantity);
        Button bttnViewDataOnList=(Button)findViewById(R.id.bttnViewDataOnList);

         fetch=(Button)findViewById(R.id.bttnToFetchFB);

         img=(ImageView)findViewById(R.id.imageViewLocalDB);
         img.setImageResource(weather.getImgResource());


        myDB=new DatabaseHelper(this);





        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(InteractWithLocalDB.this,MainActivity.class));

            }
        });
        employeeID=(EditText)findViewById(R.id.employeeID);
        employeeEmail=(EditText)findViewById(R.id.employeeEmail);
        employeeName=(EditText)findViewById(R.id.employeeName);
        employeePassword=(EditText)findViewById(R.id.employeePassword);
        key2update=(EditText)findViewById(R.id.employeeKey2Update);


        newValue=(EditText)findViewById(R.id.updatedValue);


        updatingLayout=(LinearLayout)findViewById(R.id.layout);// the UPDATE layout

        bttnViewDataOnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InteractWithLocalDB.this,plzWork.class));
            }
        });



        bttnAdd.setOnClickListener(new View.OnClickListener() {
            //adds a record to the table
            @Override
            public void onClick(View v) {

                if(myDB.addData(employeeID.getText().toString(),
                        employeeName.getText().toString(),employeeEmail.getText().toString(),employeePassword.getText().toString())==false) {
                    Toast.makeText(InteractWithLocalDB.this, "Data was not entered into the table \nPlease check your input!", Toast.LENGTH_LONG).show();
                    listItems.add(employeeName.getText().toString());
                    listKeys.add(employeeID.getText().toString());
                }
                else
                    Toast.makeText(InteractWithLocalDB.this,"Data was successfully entered into the table",Toast.LENGTH_LONG).show();



            }
        });

        bttnUpdate.setOnClickListener(new View.OnClickListener() {
            //updates the SALARY of a record specified by the id
            // press the update button to make the layout visible, press it again to make it invisble
            @Override
            public void onClick(View v) {
                x++;

                if(x%2==0){
                    updatingLayout.setVisibility(View.VISIBLE);

                    bttnSubmitUpdatedSalray.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myDB.update(key2update.getText().toString(),newValue.getText().toString());

                        }
                    });

                }
                else
                    updatingLayout.setVisibility(View.INVISIBLE);







            }
        });

        bttnDelete.setOnClickListener(new View.OnClickListener() {
            //deletes a row specified  by the employee_NAME
            //then display a toast with the count of rows deleted
            //if no rows are found, display a toast saying that nothing was deleted
            @Override
            public void onClick(View v) {

                int result=myDB.dltRow(employeeName.getText().toString()); //returns number of deleted rows

                if(result>=1)
                    Toast.makeText(InteractWithLocalDB.this,+result+"Row(s) were susscessfully deleted",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(InteractWithLocalDB.this,"No rows were deleted \nPlease try again",Toast.LENGTH_LONG).show();


            }
        });


        //bttnCount

        bttnView.setOnClickListener(new View.OnClickListener() {
            // if nothing is entered in the employee NAME it will view all enteries in the table
            // if a string was written in the employee NAME it will show the record
            //an error will appear if no results were found

            Cursor cur;
            StringBuffer buffer=new StringBuffer();

            @Override
            public void onClick(View v) {
                if (employeeName.getText().toString().equals("")) {
                    cur = myDB.getListContents();
                } else if (!(employeeName.getText().toString().equals(""))) {
                    cur = myDB.getSpecificResult(employeeName.getText().toString());
                }

                if (cur.getCount()==0)
                    Toast.makeText(InteractWithLocalDB.this, "No results found !", Toast.LENGTH_LONG).show();
                else {//add table rows to the buffer

                    while (cur.moveToNext()) {
                        for (int i = 0; i < cur.getColumnCount(); i++) {
                            buffer.append(cur.getColumnName(i) + ": " + cur.getString(i) + "\n");
                        }
                        buffer.append("\n");

                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(InteractWithLocalDB.this);
                    builder.setCancelable(true);
                    builder.setTitle("Results");
                    builder.setMessage(buffer.toString());
                    builder.show();

                    buffer.delete(0, buffer.capacity());

                }
            }
        });



    }

    protected Cursor getDataList(){

        return myDB.getListContents();

    }
}
