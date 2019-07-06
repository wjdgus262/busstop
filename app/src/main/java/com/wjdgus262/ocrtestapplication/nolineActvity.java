package com.wjdgus262.ocrtestapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.senab.photoview.PhotoViewAttacher;

public class nolineActvity extends AppCompatActivity {
    EditText editText;


    private ListView listView;
    private nolieAdapter adapter;
    private List<noline_item> list;
    private List<noline_item> savelist;
    String number,location;
    TextView route;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noline_actvity);
        Pattern pattern = Pattern.compile("^[a-zA-Z]*$");

        String len = Main2Activity.button1.getText().toString();
        Matcher m = pattern.matcher(len);

        editText = (EditText)findViewById(R.id.nolineEdittext);
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
        if(len.equals("Route/schedule"))
        {

            editText.setHint("Please enter the bus number");
        }

        listView = (ListView)findViewById(R.id.noline_list);
        list = new ArrayList<noline_item>();

        final Intent intent1 = getIntent();
        adapter = new nolieAdapter(getApplicationContext(),list,savelist);
        listView.setAdapter(adapter);
        savelist = new ArrayList<noline_item>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView list_text = view.findViewById(R.id.noline_list_text);
                String text = list_text.getText().toString();
               // Toast.makeText(getApplicationContext(),text+"",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),nolie_imgActivity.class);
                intent.putExtra("num",text);
                startActivity(intent);
            }
        });


        try {
            JSONObject jsonObject = new JSONObject(intent1.getStringExtra("num"));
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            int count = 0;
            while(count < jsonArray.length())
            {
                JSONObject object = jsonArray.getJSONObject(count);
                number = object.getString("num");
                location = object.getString("location");
                noline_item number1 = new noline_item(number,location);
                list.add(number1);
                savelist.add(number1);
                count++;
            }
        }catch (Exception e)
        {
                e.printStackTrace();
        }
    }

    public void serachNumber(String search)
    {
        list.clear();
        for(int i = 0; i < savelist.size(); i++)
        {
            if(savelist.get(i).getNumber().contains(search))
            {
                list.add(savelist.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }
}
