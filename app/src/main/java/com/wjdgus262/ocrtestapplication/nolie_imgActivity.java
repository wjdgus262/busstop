package com.wjdgus262.ocrtestapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import uk.co.senab.photoview.PhotoViewAttacher;

public class nolie_imgActivity extends AppCompatActivity {

    ImageView imageView;
    ImageView imageView1,imageView2,imageView3;

    String url_img1,url_img2,url_img3,url_img4;
    Bitmap bitmap1,bitmap2,bitmap3,bitmap4;

    PhotoViewAttacher Attacher1,Attacher2,Attacher3,Attacher4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nolie_img);

        imageView = (ImageView)findViewById(R.id.image1);
        imageView1 = (ImageView)findViewById(R.id.image2);
        imageView2 = (ImageView)findViewById(R.id.image3);
        imageView3= (ImageView)findViewById(R.id.image4);

        Attacher1 = new PhotoViewAttacher(imageView);
        Attacher2 = new PhotoViewAttacher(imageView1);
        Attacher3 = new PhotoViewAttacher(imageView2);
        Attacher4 = new PhotoViewAttacher(imageView3);
        Intent intent = getIntent();
        final String num = intent.getStringExtra("num");


        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    //Toast.makeText(getApplicationContext(),success+"",Toast.LENGTH_SHORT).show();
                    if(success)
                    {
                        //Toast.makeText(getApplicationContext(),jsonResponse.getString("img")+"",Toast.LENGTH_SHORT).show();
                        url_img1 = jsonResponse.getString("img1");
                        url_img2 = jsonResponse.getString("img2");
                        url_img3 = jsonResponse.getString("img3");
                        url_img4 = jsonResponse.getString("img4");
                      //  Toast.makeText(getApplicationContext(),url_img3+"",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                Thread mThread = new Thread()
                {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(url_img1);
                            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                            conn.setDoInput(true);
                            conn.connect();
                            InputStream is = conn.getInputStream();
                            bitmap1 = BitmapFactory.decodeStream(is);

                            URL url1 = new URL(url_img2);
                            HttpURLConnection conn1 = (HttpURLConnection)url1.openConnection();
                            conn1.setDoInput(true);
                            conn1.connect();
                            InputStream is1 = conn1.getInputStream();
                            bitmap2 = BitmapFactory.decodeStream(is1);

                            URL url2 = new URL(url_img3);
                            HttpURLConnection conn2 = (HttpURLConnection)url2.openConnection();
                            conn2.setDoInput(true);
                            conn2.connect();
                            InputStream is2 = conn2.getInputStream();
                            bitmap3 = BitmapFactory.decodeStream(is2);

                            URL url3 = new URL(url_img4);
                            HttpURLConnection conn3= (HttpURLConnection)url3.openConnection();
                            conn3.setDoInput(true);
                            conn3.connect();
                            InputStream is3 = conn3.getInputStream();
                            bitmap4 = BitmapFactory.decodeStream(is3);
                        }catch (IOException ie)
                        {
                            ie.printStackTrace();
                        }
                    }
                };
                mThread.start();
                try {
                    mThread.join();
                    imageView.setImageBitmap(bitmap1);
                    imageView1.setImageBitmap(bitmap2);
                    imageView2.setImageBitmap(bitmap3);
                    imageView3.setImageBitmap(bitmap4);
                }catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        };
        nolieListRequest request = new nolieListRequest(num,listener);
        RequestQueue queue = Volley.newRequestQueue(nolie_imgActivity.this);
        queue.add(request);
    }
}
