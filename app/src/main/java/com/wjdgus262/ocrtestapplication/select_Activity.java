package com.wjdgus262.ocrtestapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class select_Activity extends AppCompatActivity {

    ImageView cash,money,card;
    private ListView listView;
    private selectAdapter adapter;
    private List<select_item> list;
    private List<select_item> savelist;
    String number,lon,tel,company;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_);
            listView = (ListView)findViewById(R.id.listview_select);
            list = new ArrayList<select_item>();
            savelist = new ArrayList<select_item>();

        Intent intent = getIntent();
        adapter = new selectAdapter(getApplicationContext(),list,savelist);
        listView.setAdapter(adapter);

        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("num"));
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            int count = 0;
            while(count < jsonArray.length())
            {
                JSONObject object = jsonArray.getJSONObject(count);
                number = object.getString("num");
                lon = object.getString("location");
                select_item number1 = new select_item(number,lon);
                list.add(number1);
                savelist.add(number1);
                count++;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView select = view.findViewById(R.id.select_list_text);
                String text = select.getText().toString();
                tel(text);
            }
        });

        EditText editText = (EditText)findViewById(R.id.selectEdit);
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
        String len = Main2Activity.button1.getText().toString();
        if(len.equals("Route/schedule"))
        {

            editText.setHint("Please enter the bus number");
        }
    }




    public void serachNumber(String ser)
    {
        list.clear();
        for(int i = 0; i <savelist.size(); i++)
        {
            if(savelist.get(i).getNumber().contains(ser))
            {
                list.add(savelist.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }

    void tel(String num)
    {

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        //Toast.makeText(getApplicationContext(),success+"",Toast.LENGTH_SHORT).show();
                        if(success){
                            tel = jsonResponse.getString("tel");
                            String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
                            tel =tel.replaceAll(match, "");
                            company = jsonResponse.getString("company");
                            //Toast.makeText(getApplicationContext(),tel+"",Toast.LENGTH_SHORT).show();



                        }
                        Toast.makeText(getApplicationContext(),company,Toast.LENGTH_SHORT).show();

                    }catch (Exception e)
                    {
                            e.printStackTrace();
                    }
                startActivity(new Intent("android.intent.action.DIAL",Uri.parse("tel:"+tel)));
            }
        };

        nolieListRequest request = new nolieListRequest(num,listener);
        RequestQueue queue = Volley.newRequestQueue(select_Activity.this);
        queue.add(request);
    }
}
