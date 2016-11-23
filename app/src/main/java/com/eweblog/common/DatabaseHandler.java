package com.eweblog.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.eweblog.model.CaseListModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "erginus_lawyer";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {



        String CREATE_ADD_CASE_TABLE = "CREATE TABLE IF NOT EXISTS " + "cases" + "(" + "case_id"
                + " INTEGER PRIMARY KEY," + "users_id" + " TEXT" +  "case_number"
                + " TEXT," + "case_title" + " TEXT" + "case_type"
                + " TEXT," + "case_position_status" + " TEXT" +"case_retained_name"
                + " TEXT," + "case_retained_contact" + " TEXT" +"case_opposite_counselor_name"
                + " TEXT," + "case_opposite_counselor_contact" + " TEXT" +"case_court_name"
                + " TEXT," + "case_started" + " DATETIME" +"case_status"
                + " INTEGER," + "case_created" + " DATETIME" +"case_modified"
                + " DATETIME," + ")";

        db.execSQL(CREATE_ADD_CASE_TABLE);

        String CREATE_CASE_DETAIL = "CREATE TABLE IF NOT EXISTS " + "case_details" + "(" + "case_detail_id"
                + " INTEGER NOT NULL AUTO_INCREMENT," + "cases_id"
                + " INTEGER PRIMARY KEY," + "users_id" + " TEXT" +  "case_number"
                + " TEXT," + "case_title" + " TEXT" + "case_type"
                + " TEXT," + "case_position_status" + " TEXT" +"case_retained_name"
                + " TEXT," + "case_retained_contact" + " TEXT" +"case_opposite_counselor_name"
                + " TEXT," + "case_opposite_counselor_contact" + " TEXT" +"case_court_name"
                + " TEXT," + "case_started" + " DATETIME" +"case_status"
                + " INTEGER," + "case_created" + " DATETIME" +"case_modified"
                + " DATETIME," + ")";

        db.execSQL(CREATE_CASE_DETAIL);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
  //     db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST);

        // Create tables again
        onCreate(db);
    }



  public  void addListItem(List<CaseListModel> listItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        for (int i = 0; i < listItem.size(); i++) {

            Log.e("vlaue inserting==", "" + listItem.get(i));
           // values.put(KEY_ListItem, listItem.get(i));
        //    db.insert(TABLE_LIST, null, values);

        }

        db.close(); // Closing database connection
    }

/*
public Cursor getListItem() {
       String selectQuery = "SELECT  * FROM " + TABLE_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        return cursor;
    }

*/

}
