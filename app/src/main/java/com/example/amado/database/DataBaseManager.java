package com.example.amado.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Amado on 03/03/2015.
 */
public class DataBaseManager {
    public static final String TABLE_NAME = "Contacts";


    public static final String CN_ID= "_id";
    public static final String CN_NAME = "name";
    public static final String CN_PHONE = "phone";

    public static final String CREATE_TABLE = "create table "+ TABLE_NAME+" ("
            +CN_ID+" integer primary key autoincrement,"
            +CN_NAME+" text not null,"
            +CN_PHONE+" text);";


    private DbHelper mHelper;
    private SQLiteDatabase mDb;
    private String[] mColumns=new String[]{CN_ID,CN_NAME,CN_PHONE};

    public DataBaseManager(Context context) {

       mHelper  = new DbHelper(context);
       mDb  = mHelper.getWritableDatabase();
    }

    public void insert(String name, String phone){

        mDb.insert(TABLE_NAME, null,generateContentValues(name, phone));

    }

    private ContentValues generateContentValues(String name, String phone) {
        ContentValues values= new ContentValues();
        values.put(CN_NAME, name);
        values.put(CN_PHONE, phone);
        return values;
    }

    public void deleteFromDB(String name){
        mDb.delete(TABLE_NAME,CN_NAME+"=?", new String[]{name});
    }

     public void modifyPhone(String name, String newphone){
         mDb.update(TABLE_NAME, generateContentValues(name, newphone), CN_NAME+"=?", new String[]{name});
     }

    public Cursor loadContactsCursor(){


        return mDb.query(TABLE_NAME, mColumns, null, null, null, null, null);

    }

    public Cursor searchContact(String name){
        try {//added this try and catch to simulate a search in a large database that would take time and freeze the app without background task
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mDb.query(TABLE_NAME, mColumns, CN_NAME+"=?", new String[]{name}, null, null, null);
    }


}
