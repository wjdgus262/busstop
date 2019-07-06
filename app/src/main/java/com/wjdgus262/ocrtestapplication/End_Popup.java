package com.wjdgus262.ocrtestapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class End_Popup extends AppCompatActivity {
    String name,info,id;
    private ListView listView;
    private End_Popup_Adapter adapter;
    private List<End_Popup_item> list;
    private List<End_Popup_item> savelist;
    public static String id_text = "";

    public static String teststring = "";
    String objectstring = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end__popup);
        Intent intent = getIntent();

        listView = (ListView)findViewById(R.id.end_popup_list);
        list = new ArrayList<End_Popup_item>();
        savelist = new ArrayList<End_Popup_item>();
        adapter = new End_Popup_Adapter(getApplicationContext(),list,savelist);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView nametext = (TextView)view.findViewById(R.id.end_popup_name);
                teststring = nametext.getText().toString();
                pathActivity.end.setText(teststring);
                pathActivity.popcheck = true;
//                pathActivity pathActivity = new pathActivity();
//                pathActivity.shape();
                fin();
            }
        });

        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("num"));
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            int count = 0;
            while(count < jsonArray.length())
            {
                JSONObject object = jsonArray.getJSONObject(count);
                id = object.getString("id");
                name = object.getString("name");
                info = object.getString("info");

                End_Popup_item number1 = new End_Popup_item(name,info,id);
                list.add(number1);
                savelist.add(number1);
                count++;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        EditText editText = (EditText)findViewById(R.id.end_popup_edit);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                serachNumber(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    public void fin()
    {
        this.finish();
    }

    public void serachNumber(String ser)
    {
        list.clear();
        for(int i = 0; i <savelist.size(); i++)
        {
            if(savelist.get(i).getName().contains(ser))
            {
                list.add(savelist.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }
}
