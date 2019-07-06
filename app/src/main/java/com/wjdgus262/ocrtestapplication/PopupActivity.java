package com.wjdgus262.ocrtestapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class PopupActivity extends AppCompatActivity {

    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        listView = (ListView)findViewById(R.id.popuplistview);

        dataSetting();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               switch (position)
               {
                   case 0:
                       Locale locale = Locale.KOREA;
                       langsetting(locale);
                       break;
                   case 1:
                       Locale locale1 = Locale.ENGLISH;
                       langsetting(locale1);
                       break;
                   case 2:
                       Locale locale2 = Locale.CHINA;
                       langsetting(locale2);
                       break;
                   case 3:
                       Locale locale3 = Locale.JAPAN;
                       langsetting(locale3);
                       break;
               }
            }
        });
    }



    private void dataSetting()
    {
        popupAdapter adapter = new popupAdapter();

        adapter.addItem(ContextCompat.getDrawable(getApplicationContext(),R.drawable.kr1),"한국어");
        adapter.addItem(ContextCompat.getDrawable(getApplicationContext(),R.drawable.en),"English");
        adapter.addItem(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ch),"中國語");
        adapter.addItem(ContextCompat.getDrawable(getApplicationContext(),R.drawable.jp),"日本語");

        listView.setAdapter(adapter);
    }

    public void langsetting(Locale lang)
    {

        Configuration config = new Configuration();
        config.locale = lang;
        getResources().updateConfiguration(config,getResources().getDisplayMetrics());

        Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
        finish();
        startActivity(intent);
    }


}
