package com.wjdgus262.ocrtestapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private boolean FinishFlag;
    Button button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        try {

            //초기실행,두번째 실행 판단 false = 초기, true = 두번째
            final SharedPreferences mPref = getSharedPreferences("isFirst", Activity.MODE_PRIVATE);
            Boolean bFirst = mPref.getBoolean("isFirst",false);
            if(bFirst == false)
            {

                button1 = (Button)findViewById(R.id.button1);
                button1.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                       // clickbtn(getSharedPreferences("test",Activity.MODE_PRIVATE),"ko");

                        //1,2,3,4로 언어 구분
                        SharedPreferences pref = getSharedPreferences("test",Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = pref.edit();
                        editor1.putInt("ttt",1);
                        editor1.commit();

                        //언어 데이터 셋팅
                        Locale lang = Locale.KOREA;
                        langsetting(lang);
                    }
                });
                Button button2 = (Button)findViewById(R.id.button2);
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //clickbtn(getSharedPreferences("test",Activity.MODE_PRIVATE),"en");
                        SharedPreferences pref = getSharedPreferences("test",Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = pref.edit();
                        editor1.putInt("ttt",2);
                        editor1.commit();
                        Locale lang = Locale.US;
                        langsetting(lang);
                    }
                });
                Button button3 = (Button)findViewById(R.id.button3);
                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences pref = getSharedPreferences("test",Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = pref.edit();
                        editor1.putInt("ttt",3);
                        editor1.commit();
                        Locale lang = Locale.CHINA;
                        langsetting(lang);
                    }
                });
                Button button4 = (Button)findViewById(R.id.button4);
                button4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences pref = getSharedPreferences("test",Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = pref.edit();
                        editor1.putInt("ttt",4);
                        editor1.commit();
                        Locale lang = Locale.JAPAN;
                        langsetting(lang);
                    }
                });









                final SharedPreferences.Editor editor = mPref.edit();
                editor.putBoolean("isFirst",true);
                editor.commit();
            }
            if(bFirst == true)
            {
                //Toast.makeText(getApplicationContext(),"ttt",Toast.LENGTH_SHORT).show();
                //저장된값 가져오기
                SharedPreferences  pref = getSharedPreferences("test",Activity.MODE_PRIVATE);
                pref = getSharedPreferences("test",Activity.MODE_PRIVATE);
                //언어 구분에따라 switch문으로 구분
                int check = pref.getInt("ttt",0);
                Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
                    switch (check)
                    {
                        case 1:
                            //intent.putExtra("len","ko");
                            startActivity(intent);
                            break;
                        case 2:
                            //intent.putExtra("len","en");
                            startActivity(intent);
                            break;
                        case 3:
//                            intent.putExtra("len","ch");
                            startActivity(intent);
                            break;
                        case 4:
                            //intent.putExtra("len","jp");
                            startActivity(intent);
                            break;
                        default:
                            System.out.print("default");
                    }


            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    //언어셋팅
    public void langsetting(Locale lang)
    {
        Configuration config = new Configuration();
        config.locale = lang;
        getResources().updateConfiguration(config,getResources().getDisplayMetrics());

        Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
        finish();
        startActivity(intent);
    }
//    @Override
//    public void finish()
//    {
//        if(FinishFlag == false)
//        {
//            Toast.makeText(getApplicationContext(),"'뒤로'버튼을 한번 더 누르시면 종료됩니다.",Toast.LENGTH_SHORT).show();
//            FinishFlag = true;
//            handler.sendEmptyMessageDelayed(0,2000);
//            return;
//        }
//        ActivityCompat.finishAffinity(this);
//    }
//
//    Handler handler = new Handler()
//    {
//        @Override
//        public void handleMessage(Message msg) {
//            if(msg.what == 0)
//            {
//                FinishFlag = false;
//            }
//        }
//    };



}
