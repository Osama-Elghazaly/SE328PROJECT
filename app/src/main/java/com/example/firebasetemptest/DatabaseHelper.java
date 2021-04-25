package com.example.firebasetemptest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "se328Project.db";
    public static final String TABLE_NAME = "users_data";
    public static final String COL1 = "ID";
    public static final String COL2 = "Name";
    public static final String COL3 = "Email";
    public static final String COL4 = "Password";

    /* Constructor */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    /* Code runs automatically when the dB is created */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME +
                " (ID TEXT PRIMARY KEY , " +
                " Name TEXT, Email TEXT, Password TEXT)";

        db.execSQL(createTable);
    }

    /* Every time the dB is updated (or upgraded) */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /* Basic function to add data. REMEMBER: The fields
       here, must be in accordance with those in
       the onCreate method above.
    */
    public boolean addData(String id,String name, String email,String password) {
        long result;

        Cursor cur=getSpecifiedResultByID(id);//checking to see if my table has an ID matching to the one passes right now

        if(cur.getCount()==1)//if found a matching ID
            return false;
        else {


            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL1, id);//key,value
            contentValues.put(COL2, name);
            contentValues.put(COL3, email);
            contentValues.put(COL4,password);

            result = db.insert(TABLE_NAME, null, contentValues);
        }


        //if data are inserted incorrectly, it will return -1
        if(result == -1 ) {
            return false;
        } else {
            return true;
        }
    }

    /* Returns only one result */
    public Cursor structuredQuery(int ID) {
        SQLiteDatabase db = this.getReadableDatabase(); // No need to write
        Cursor cursor = db.query(
                TABLE_NAME, new String[]{COL1,
                        COL2, COL3}, COL1 + "=?",
                new String[]{String.valueOf(ID)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }


    public Cursor getSpecificResult(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME +" Where name=? ",new String[]{name});
        return data;
    }

    //return row(s) specified by the ID
    public Cursor getSpecifiedResultByID(String ID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME +" Where  ID= ?  ",new String[]{ID});



        return data;

    }

    // Return everything inside a specific table
    public Cursor getListContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }

    public int dltRow(String name){

        SQLiteDatabase db = this.getWritableDatabase();
        int delete=0;

        long result= DatabaseUtils.queryNumEntries(db,TABLE_NAME,"name=?",new String[]{name});

        if(result>=1)
            delete=db.delete(TABLE_NAME,"name=?",new String[]{name});

        return delete;

    }

    public void update(String id,String email){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL3,email);

        db.update(TABLE_NAME,contentValues,"ID=?",new String[]{id});


    }



}
