/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.wjdgus262.ocrtestapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main2Activity extends AppCompatActivity {


    //-----------------변수선언--------------
    private boolean FinishFlag;
    public static String ll;
    public static String leng1 = null;

    private Button mBtnCameraView;
    private EditText mEdit;
    private String datapath = "";
    private String lang = "";

    private int ACTIVITY_REQUEST_CODE = 1;
    static TessBaseAPI sTess;

    public static int intentnumber = 0;


    public static Button button,button1,button3,review,popup;

    public static boolean flagcheck = false;

    public static String numberCheck = null;
    //-----------------변수선언끝--------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //-----위젯 객체화
        button = (Button)findViewById(R.id.pathbutton);
        button1 = (Button)findViewById(R.id.nolinebutton);
        button3 = (Button)findViewById(R.id.selectbutton);
        review = (Button)findViewById(R.id.reviewbutton);
        popup = (Button)findViewById(R.id.popupbutton);
        //---------------위젯 객체화끝----------------

        popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PopupActivity.class);
                startActivity(intent);
            }
        });

        ImageView imageView22 = (ImageView)findViewById(R.id.imageView2);
        imageView22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.kopo.ac.kr/jeju/index.do"));
                startActivity(intent);
            }
        });

        mBtnCameraView = (Button)findViewById(R.id.carbutton);
        //mEdit = (EditText)findViewById(R.id.edit_ocr);
        sTess = new TessBaseAPI();

        lang = "kor";

        datapath = getFilesDir()+ "/tesseract";

        if(checkFile(new File(datapath+"/tessdata")))
        {
            sTess.init(datapath, lang);
        }



        mBtnCameraView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flagcheck == false)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);

                    builder.setTitle(getString(R.string.inputnum));
                    builder.setMessage(getString(R.string.input));
                    final EditText editText = new EditText(Main2Activity.this);
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    builder.setView(editText);
                    builder.setPositiveButton(getString(R.string.arrivecheck), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            numberCheck = editText.getText().toString();
                            Intent Camera = new Intent(getApplicationContext(),CameraView.class);
                            startActivityForResult(Camera,ACTIVITY_REQUEST_CODE);
                        }
                    });
                    builder.setNegativeButton(getString(R.string.canle), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }else{
                    Intent Camera = new Intent(getApplicationContext(),CameraView.class);
                    startActivityForResult(Camera,ACTIVITY_REQUEST_CODE);
                }
            }
        });

        if(button.getText().equals("실시간 도착버스조회"))
        {
            ll = "ko";
        }else if(button.getText().equals("Real-time arrival bus lookup"))
        {
            ll = "en";
            button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 8);


        }else if(button.getText().equals("實時公交車到達查詢"))
        {
            ll = "ch";
        }else if(button.getText().equals("Realtime bus照会"))
        {
            ll = "jp";

        }




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getApplicationContext(),pathActivity.class);
//                intent.putExtra("len1",ll);
               // startActivity(intent);
                new BackgroundTask2().execute();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Main2Activity.this,nolineActvity.class);
//                intent.putExtra("num","a");
//                startActivity(intent);
                new BackgroundTask().execute();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Main2Activity.this,select_Activity.class);
//                intent.putExtra("num","b");
//                startActivity(intent);
                new BackgroundTask1().execute();
            }
        });

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),reviewActivity.class);
                startActivity(intent);
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://112.164.213.180/hackathon/web/insert.php"));
//                startActivity(intent);
            }
        });
//
//        gar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(),garActivity.class);
//                startActivity(intent);
//            }
//        });
    }



    @Override
    public void finish()
    {
        if(FinishFlag == false)
        {
            Toast.makeText(getApplicationContext(),getString(R.string.back),Toast.LENGTH_SHORT).show();
            FinishFlag = true;
            handler.sendEmptyMessageDelayed(0,2000);
            return;
        }
        ActivityCompat.finishAffinity(this);
    }

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0)
            {
                FinishFlag = false;
            }
        }
    };

    boolean checkFile(File dir)
    {
        //디렉토리가 없으면 디렉토리를 만들고 그후에 파일을 카피
        if(!dir.exists() && dir.mkdirs()) {
            copyFiles();
        }
        //디렉토리가 있지만 파일이 없으면 파일카피 진행
        if(dir.exists()) {
            String datafilepath = datapath + "/tessdata/" + lang + ".traineddata";
            File datafile = new File(datafilepath);
            if(!datafile.exists()) {
                copyFiles();
            }
        }
        return true;
    }
    void copyFiles()
    {
        AssetManager assetMgr = this.getAssets();

        InputStream is = null;
        OutputStream os = null;

        try {
            is = assetMgr.open("tessdata/"+lang+".traineddata");

            String destFile = datapath + "/tessdata/" + lang + ".traineddata";

            os = new FileOutputStream(destFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = is.read(buffer)) != -1) {
                os.write(buffer, 0, read);
            }
            is.close();
            os.flush();
            os.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            if(requestCode== ACTIVITY_REQUEST_CODE)
            {
                // 받아온 OCR 결과 출력
             //   mEdit.setText(data.getStringExtra("STRING_OCR_RESULT"));
            }
        }
    }

    class BackgroundTask extends AsyncTask<Void,Void,String>
    {
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://wjdgus262.cafe24.com/list_test.php";
        }

        @Override
        protected void onPostExecute(String s) {
            Intent intent = new Intent(Main2Activity.this,nolineActvity.class);
            intent.putExtra("num",s);
            startActivity(intent);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder builder = new StringBuilder();
                while((temp = reader.readLine()) != null)
                {
                    builder.append(temp +"\n");
                }
                reader.close();;
                inputStream.close();
                httpURLConnection.disconnect();
                return builder.toString().trim();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }


    class BackgroundTask1 extends AsyncTask<Void,Void,String>
    {
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://wjdgus262.cafe24.com/list_test.php";
        }

        @Override
        protected void onPostExecute(String s) {
            Intent intent = new Intent(Main2Activity.this,select_Activity.class);
            intent.putExtra("num",s);
            startActivity(intent);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder builder = new StringBuilder();
                while((temp = reader.readLine()) != null)
                {
                    builder.append(temp +"\n");
                }
                reader.close();;
                inputStream.close();
                httpURLConnection.disconnect();
                return builder.toString().trim();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }
    class BackgroundTask2 extends AsyncTask<Void,Void,String>
    {
        String target;
        @Override
        protected void onPreExecute() {
           target = "http://wjdgus262.cafe24.com/date_test.php";
        }

        @Override
        protected void onPostExecute(String s) {
            Intent intent = new Intent(Main2Activity.this,pathActivity.class);
            intent.putExtra("data",s);
            startActivity(intent);
        }

        @Override
        protected String doInBackground(Void... voids) {
           try {
               URL url = new URL(target);
               HttpURLConnection httpURLConnection  = (HttpURLConnection)url.openConnection();
               InputStream inputStream = httpURLConnection.getInputStream();
               BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
               String temp;
               StringBuilder builder = new StringBuilder();
               while((temp = reader.readLine()) != null)
               {
                   builder.append(temp+"\n");
               }
               reader.close();
               inputStream.close();
               httpURLConnection.disconnect();
               return builder.toString().trim();

           }catch (Exception e)
           {
               e.printStackTrace();
           }
           return null;
        }
    }
}
