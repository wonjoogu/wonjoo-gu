package com.example.lastone;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static java.sql.DriverManager.println;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION=1;
    private Context context;
    EditText et;
    public static final String DATABASE_NAME = "calendar.db";
    public static final String TABLE_NAME = "calendar_table";
    public static final String col_ID = "id";
    public static final String col_Memo = "memo";
    private TextView result;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
//        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(this.getClass().getName(),"====OnCreate Method Start====");

        Log.d(this.getClass().getName(),"====Create Table" +TABLE_NAME+"====");
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                " (" + col_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + col_Memo + " VARCHAR(255))";
        db.execSQL(CREATE_TABLE);
        Toast.makeText(context, "DB 생성 완료", Toast.LENGTH_LONG).show();
        Log.i("table name : ", TABLE_NAME);
        Log.d(this.getClass().getName(),"====OnCreate Method End====");

    }

    public void onOpen(SQLiteDatabase db){
        println("opened database [" + DATABASE_NAME +"].");
    }

    //버전이 변경될 때마다 호출
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(this.getClass().getName(),"====OnUpgrade Method Start====");

        Log.w(this.getClass().getName(),"Upgrading database from version"
        + oldVersion+"to"+newVersion
        +",which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);

        Log.d(this.getClass().getName(),"====OnUpgrade Method End====");
    }
}
