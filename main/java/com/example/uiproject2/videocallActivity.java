package com.example.uiproject2;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class videocallActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videocall);
    }
    //버튼 클릭시 보여지는 화면에 관련된 코드
    public  void onClick(View v){
        finish();
    }

}
