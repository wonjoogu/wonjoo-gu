package com.example.lastone;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class calenderActivity extends AppCompatActivity {
    SQLiteDatabase db;
    String sql;
    DatabaseHelper myDb;
    public static String DATABASE_NAME = "Calendar.db";
    public static String TABLE_NAME = "calendar_table";
    public static final int DATABASE_VERSION=1;
    private LinearLayout header1;
    private TextView monthText;
    private GridView monthView;
    private Calendar calendar;
    private CalendarAdapter items;
    private CalendarAdapter itemView;
    private CalendarAdapter MonthItem;
    private CalendarAdapter mContext;
    private CalendarAdapter monthViewAdapter;
    int curYear;
    int curMonth;
    int curDay;
    private String year;
    private String month;
    private String date;
    private String memo;
    private TextView result;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        monthView = (GridView) findViewById(R.id.monthView);
        monthViewAdapter = new CalendarAdapter(this);
        itemView = new CalendarAdapter(this);
        items = new CalendarAdapter(this);
        mContext = new CalendarAdapter(this);
        monthView.setAdapter(monthViewAdapter);
        //디비
        myDb = new DatabaseHelper(this);
        db = myDb.getWritableDatabase();
        result = (TextView)findViewById(R.id.result);

        // 리스너 설정
        monthView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                // 현재 선택한 일자 정보 표시
                final MonthItem curItem = (MonthItem) monthViewAdapter.getItem(position);
                int day = curItem.getDay();

                DATABASE_NAME = monthView.getContext().toString();

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                Log.d("CalendarActivity", "Selected : " + day);
                final EditText et = new EditText(calenderActivity.this); //getContext()
                AlertDialog.Builder dlg = new AlertDialog.Builder(calenderActivity.this);
                dlg.setMessage("일정을 적으세요").setCancelable(false).setView(et)
                        .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 일자에 일정 추가
                              //  items.addItem(et.getText().toString());
                              ///  String memo = et.getText().toString();

                                sql = String.format("INSERT INTO memo VALUES (NULL, '%s');", memo);
                                result.append("\nInsert Success");
                                Intent intent = new Intent(getApplicationContext(),calenderActivity.class);
                                String value = et.getText().toString();

                                ContentValues values = new ContentValues();
                                values.put(DatabaseHelper.col_Memo, et.getText().toString());
                                //values.put("memo", value);
                                db.insert(DatabaseHelper.TABLE_NAME, DatabaseHelper.col_Memo, values); //입력할 테이블, 비어 있는 ContentValues을 전달 받았을 경우 null값 처리할 컬럼, ContentValues 객체
                                String insertQuery = "INSERT INTO " + DatabaseHelper.TABLE_NAME+"("+DatabaseHelper.col_Memo+") VALUES('values')";
                                db.execSQL(insertQuery);
                                items.addItem(String.valueOf(values));
                                Log.i("이날의 일정은 ! ", String.valueOf(items));
                                Log.i("일정은!", String.valueOf(values));

                                SQLiteDatabase db = myDb.getReadableDatabase();
                                Toast.makeText(getApplicationContext(), curYear + "년" + (curMonth + 1) + "월 " +
                                        curItem.getDay() + "일 \n" + "일정: " + values, Toast.LENGTH_LONG).show();
                                //curItem.getContext(et.getText());
                            }

                            /** private void createTable(String memo) {
                                println("creating table [" + memo + "].");
                                db.execSQL("create table " + memo + "("
                                        + "_id integer PRIMARY KEY autoincrement,"
                                        + " memo text);");
                            }

                            private int insertRecord() {
                                println("inserting records.");
                                int count = 3;
                                db.execSQL("ins
                             ert into memo(memo) values (et);");
                                return count;
                            }**/

                        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = dlg.create();
                alert.setTitle(curYear + "년 " + (curMonth + 1) + "월 " + curItem.getDay() + "일");
                alert.setIcon(R.drawable.mcv_action_next);
                alert.show();
            }
        });

        monthText = (TextView) findViewById(R.id.monthText);
        setMonthText();

        // 이전 월로 넘어가는 이벤트 처리
        Button monthPrevious = (Button) findViewById(R.id.monthPrevious);
        monthPrevious.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                monthViewAdapter.setPreviousMonth();
                monthViewAdapter.notifyDataSetChanged();

                setMonthText();
            }
        });

        // 다음 월로 넘어가는 이벤트 처리
        Button monthNext = (Button) findViewById(R.id.monthNext);
        monthNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                monthViewAdapter.setNextMonth();
                monthViewAdapter.notifyDataSetChanged();

                setMonthText();
            }
        });

        Button query_btn = (Button) findViewById(R.id.query_btn);
        query_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i("버튼에 추가된 메모!", String.valueOf(DatabaseHelper.col_Memo));
                Cursor cursor = db.query(DatabaseHelper.TABLE_NAME, new String[] {DatabaseHelper.col_ID,DatabaseHelper.col_Memo},null,null,null,
                        null,null);
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.col_ID));
                    String memo = cursor.getString(cursor.getColumnIndex(DatabaseHelper.col_Memo));
                    result.append(id+memo);
                    Log.i("select","id : "+ id +" Has memo "+memo);
                    }
                cursor.close();

                String query = "SELECT "+DatabaseHelper.col_ID+","+DatabaseHelper.col_Memo+"FROM "+DatabaseHelper.TABLE_NAME;
             /**
                Cursor cursor2 = db.rawQuery(query, null);
                while (cursor2.moveToNext()) {
                    int id = cursor2.getInt(cursor2.getColumnIndex(DatabaseHelper.col_ID));
                    String memo = cursor2.getString(cursor2.getColumnIndex(DatabaseHelper.col_Memo));
                    Log.i(this.getClass().getName(),"ROW "+ id + "HAS memo "+ memo);
                }
               // cursor2.close();**/

                db.close();
                myDb.close();
            }
        });
    }

/**
    private void createDatabase(String memo) {
        println("creating database [" + memo + "].");
        db = openOrCreateDatabase(memo, MODE_WORLD_WRITEABLE, null);
    }**/
/**
    private void executeRawQuery() {
        println("\nexecuteRawQuery called.\n");
        Cursor c1 = db.rawQuery("select count(*) as Total from " + TABLE_NAME, null);
        println("cursor count : " + c1.getCount());
        c1.moveToNext();
        println("record count : "+c1.getInt(0));

        c1.close();
    }
    private void executeRawQueryParam() {
        println("\nexecuteRawQueryParam called.\n");
        String SQL = "select memo"
                + " from " + TABLE_NAME
                + " where memo";
        String[] args = {"30"}; // ???

        Cursor c1 = db.rawQuery(SQL, args);
        int recordCount = c1.getCount();
        println("cursor count : "+ recordCount + "\n");

        for (int i = 0; i < recordCount; i++){
            c1.moveToNext();
            String memo = c1.getString(0);
            println("Record #" + i + ":"+ memo);
        }
        c1.close();
    }
**/
    /**
     * 월 표시 텍스트 설정
     */
    private void setMonthText() {
        curYear = monthViewAdapter.getCurYear();
        curMonth = monthViewAdapter.getCurMonth();
        curDay = monthViewAdapter.getCurDay();

        monthText.setText(curYear + "년 " + (curMonth + 1) + "월");
    }
}
