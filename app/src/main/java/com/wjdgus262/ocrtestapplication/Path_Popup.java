package com.wjdgus262.ocrtestapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Path_Popup extends AppCompatActivity {
    String name,info,id;
    private ListView listView;
    private Path_Popup_Adapter adapter;
    private List<Path_Popup_item> list;
    public static String id_text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path__popup);
        Intent intent = getIntent();

        listView = (ListView)findViewById(R.id.path_popup_list);
        list = new ArrayList<Path_Popup_item>();
        adapter = new Path_Popup_Adapter(getApplicationContext(),list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView idtext = (TextView)view.findViewById(R.id.path_poup_id);
                TextView nametext = (TextView)view.findViewById(R.id.path_popup_name);
                TextView infotext=  (TextView)view.findViewById(R.id.path_popup_info);
                pathActivity.start.setText(nametext.getText().toString()+"("+infotext.getText().toString()+")");
                pathActivity.path_num_id.setText(idtext.getText().toString());
//                Toast.makeText(getApplicationContext(),"정류소 id"+idtext.getText().toString()+"",Toast.LENGTH_SHORT).show();
                fin();

            }
        });
        try {
            JSONObject jsonObject =new JSONObject(intent.getStringExtra("data"));
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            for(int i = 0; i < jsonArray.length();i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                name = item.getString("name");
                info = item.getString("info");
                id = item.getString("ID");
                Path_Popup_item item1 = new Path_Popup_item(name, info, id);
                list.add(item1);

            }
        }catch (JSONException je)
        {
                je.printStackTrace();
        }


    }
    public void fin()
    {
        this.finish();
    }



}
