package com.example.firebasetemptest;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class plzWork extends AppCompatActivity {

   // ArrayList<String> listKeys = new ArrayList<String>();//store keys of the values
    List<String> args = new ArrayList<String>();//store values of the array
    ArrayAdapter<String> adapter;



    ListView dataListView;

   // TextView textView;

    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plz_work);

        myDB = new DatabaseHelper(this);
        //textView=(TextView)findViewById(R.id.txtViewForListView2);

        dataListView=(ListView)findViewById(R.id.list_view);
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,args);
        dataListView.setAdapter(adapter);
        dataListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);







        Cursor cur = myDB.getListContents();
        StringBuffer buffer = new StringBuffer();

        dataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String[] s= args.get(position).split("\n",5);

                Log.d("Osama","Value of s: "+s[0]+"\n"+s[1]+"\n"+s[2]);
                Toast.makeText(plzWork.this,s[0]+" "+s[1],Toast.LENGTH_LONG).show();






            }
        });


        while (cur.moveToNext()) { //populating the buffer
            for (int i = 0; i < cur.getColumnCount(); i++) {
                buffer.append(cur.getColumnName(i) + ": " + cur.getString(i) + "\n");
            }
            buffer.append("\n");
        }



        int y = 0;
        int z = 0;

        for (int i = 0; i < buffer.length(); i++) {//go over all charcters in the buffer

            if (buffer.charAt(i) == '\n') {//checking where the \n is located
                z = z + 1;
            }

            try {
                if (z % 5 == 0 && z > 0) { //here its 5 since we will have \n repeated four times per record
                    //will enter here if the code find \n
                    args.add(buffer.substring(y, i - 1));
                    z = 0; /*reset value of z everytime we add an employee, cuz if we dont reset the value of Z,u
                     the code will think that we have found four \n and prints and new list item
                    */

                    y = i + 1;
                }
            } catch (Exception e) {
                Toast.makeText(plzWork.this, "Error in z method", Toast.LENGTH_LONG).show();
            }

        }



    }
}