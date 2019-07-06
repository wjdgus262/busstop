package com.wjdgus262.ocrtestapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;

import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import uk.co.senab.photoview.PhotoViewAttacher;

public class pathActivity extends AppCompatActivity{

    //위젯
    ImageView imageView;
    PhotoViewAttacher Attachter;
    public static EditText start, end;
    public static String subdata = null;
    TextView num_text,minuteslater_text,station_text,tranistpoint_text,tranist_end,tranist_num,tranist_start,tranist_end_setfr,real_text,tranist_text,tranistlater_text,tranist_end_set;
    TextView here_text;
    View lineView;
    public static int subnum;
    TextView number;
    LinearLayout linearLayout;
    LayoutInflater inflate;


    String ss, ee;
    String leng;
    public static String number_data_city = null;
    public static TextView path_num_id;

    //Gps
    private final int PERMISSION_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSION_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;

    private GpsInfo gps;
    private GpsInfo gps1;
    Vibrator vibrator;

    //위도,경도
    double latitude;
    double longitude;
    BigDecimal biglat;
    BigDecimal biglong;
    BigDecimal blati;
    BigDecimal blong;
    BigDecimal selong,selat,thirdlong,thirlat,fourthlong,fourthlat;

    boolean handlerCheck = false;
    String tiem[] = new String[16];
    Intent intent;



    BigDecimal sumdd;//범위좌표
    BigDecimal sumtt;//현재좌표
    BigDecimal sumkk;//다랑쉬좌표

    public static String num = "";


    //정류장검색
    double lon_db[],lat_db[];
   // BigDecimal lat[];
    BigDecimal stationid[];
    String name[];
    String address[];
    BigDecimal maer[];
    BigDecimal mear_lat[];
    int hed_count = 0;
    String name_ap;


    private Handler proHandler;
    private ProgressDialog progressDialog;


    String endstaionid[] = new String[800];
    String publicid[];
    String buscheck = "";

    public String endid[] = new String[300];
    public static boolean popcheck = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);
        StrictMode.enableDefaults();
        proHandler = new Handler();
        popcheck = false;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog = progressDialog.show(pathActivity.this,"",getString(R.string.station),true);
                proHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(progressDialog!=null&&progressDialog.isShowing())
                            {
                                progressDialog.dismiss();
                            }
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                },3900);
            }
        });


        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        intent = getIntent();
       leng = intent.getExtras().getString("len1");
       path_num_id = (TextView)findViewById(R.id.path_num_id);
       // leng = "ko";
        start = (EditText)findViewById(R.id.start);
        end = (EditText)findViewById(R.id.end);
        final Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        lon_db = new double[3346];
        lat_db = new double[3346];
        stationid = new BigDecimal[3346];
        name = new String [3346];
        address = new String[3346];
        maer = new BigDecimal[3346];
        mear_lat = new BigDecimal[3346];
        for(int i = 0; i < lat_db.length; i++)
        {
            name[i] = "";
            address[i] = "";
        }
        end.setInputType(0);
        //정류장 검색
        end.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    new endTask().execute();
                }
            }
        });

       //     Toast.makeText(getApplicationContext(),lon_lat[10]+"테스트",Toast.LENGTH_SHORT).show();
            //----------정류장검색끝----------


        linearLayout  = (LinearLayout)findViewById(R.id.linear);
        end.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                switch (i)
                {
                    case EditorInfo.IME_ACTION_SEARCH:

                            shape();


                        //
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "기본", Toast.LENGTH_LONG).show();
                        return false;
                }
                return true;
            }
        });
        Button select  = (Button)findViewById(R.id.select);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = end.getText().toString();
                if(name.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"도착지를 입력해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                        shape();

                    Response.Listener<String> listener = new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                            try {

                                JSONObject jsonResponse=  new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                biglong = new BigDecimal(String.valueOf(jsonResponse.getDouble("longi")));
                                biglat = new BigDecimal(String.valueOf(jsonResponse.getDouble("lat")));
                                selong = new BigDecimal(String.valueOf(jsonResponse.getDouble("selong")));
                                selat = new BigDecimal(String.valueOf(jsonResponse.getDouble("selat")));
                                thirdlong = new BigDecimal(String.valueOf(jsonResponse.getDouble("thirdlong")));
                                thirlat = new BigDecimal(String.valueOf(jsonResponse.getDouble("thirlat")));
                                fourthlong = new BigDecimal(String.valueOf(jsonResponse.getDouble("fourthlong")));
                                fourthlat = new BigDecimal(String.valueOf(jsonResponse.getDouble("fourthlat")));
                                gpsHandler.sendEmptyMessage(0);
                                sumdd = fourthlat.add(fourthlong);
                                sumkk = thirdlong.add(thirlat);

                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    };
                    PathRequest pathRequest = new PathRequest(name,listener);
                    RequestQueue queue = Volley.newRequestQueue(pathActivity.this);
                    queue.add(pathRequest);
                }
            }
        });
        //도착지설정
        new numTask().execute();




        callPermission();
        new Background_tr().execute();
        mHandler.sendEmptyMessage(0);
    }


    @Override
    public void onBackPressed() {
        if(handlerCheck == false)
        {
            this.finish();
        }else{

            mHandler.removeMessages(0);
//            mHandler1.removeMessages(0);
            gpsHandler.removeMessages(0);
            busHandelr.removeMessages(0);
            realHandler.removeMessages(0);
        }

        super.onBackPressed();
    }
    //도착알림 handler
        Handler gpsHandler = new Handler()
        {
            @SuppressLint("DefaultLocale")
            public void handleMessage(Message msg) {
                gps1 = new GpsInfo(pathActivity.this);
                if(gps1.isGetLocation)
                {
                    latitude = Double.parseDouble(String.format("%.6f",gps1.getLatitude()));
                    longitude =Double.parseDouble(String.format("%.5f",gps1.getLongitude()));



                    blati = new BigDecimal(String.valueOf(latitude));
                    blong= new BigDecimal(String.valueOf(longitude));
                    sumtt = blati.add(blong);
//                    Toast.makeText(getApplicationContext(),"GpsHa",Toast.LENGTH_SHORT).show();
 if(sumtt.compareTo(sumdd) == 1 && sumtt.compareTo(sumkk) == -1)
                     {
                         vibrator.vibrate(1000);
                        AlertDialog.Builder dialog = new AlertDialog.Builder(pathActivity.this);
                        dialog.setTitle(getString(R.string.arrive))
                                .setMessage(getString(R.string.arrivethis))
                                .setPositiveButton(getString(R.string.arrivecheck), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //mHandler.removeMessages(0);
                                    }
                                })
                                .create()
                                .show();
                     }
                    if(blati.compareTo(selong) == 1 && blati.compareTo(biglong) == -1 && blong.compareTo(selat) == -1 && blong.compareTo(biglat) == 1)
                    {
                        vibrator.vibrate(1000);
                        AlertDialog.Builder dialog = new AlertDialog.Builder(pathActivity.this);
                        dialog.setTitle(getString(R.string.arrive))
                                .setMessage(getString(R.string.arrivethis))
                                .setPositiveButton(getString(R.string.arrivecheck), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //mHandler.removeMessages(0);
                                    }
                                })
                                .create()
                                .show();
                    }


                }
                handlerCheck = true;
                gpsHandler.sendEmptyMessageDelayed(0,7000);
            }
        };
        //주변정류장 handler
        Handler mHandler = new Handler()
        {

            @SuppressLint("DefaultLocale")
            public void handleMessage(Message msg)
            {
                if(hed_count == 0)
                {
                   // Toast.makeText(getApplicationContext(),)
                    gps = new GpsInfo(pathActivity.this);
                    if(gps.isGetLocation())
                    {

                        latitude = Double.parseDouble(String.format("%.6f",gps.getLatitude()));
                        longitude =Double.parseDouble(String.format("%.5f",gps.getLongitude()));



                        blati = new BigDecimal(String.valueOf(latitude));
                        blong= new BigDecimal(String.valueOf(longitude));
                        sumtt = blati.add(blong);

                    }else
                    {
                        gps.showSettingsAlert();
                    }
                }else if(hed_count == 1)
                {
                    gps = new GpsInfo(pathActivity.this);
                    if(gps.isGetLocation())
                    {
                        latitude = Double.parseDouble(String.format("%.6f",gps.getLatitude()));
                        longitude =Double.parseDouble(String.format("%.5f",gps.getLongitude()));

                    //    Toast.makeText(getApplicationContext(),"현재좌표 : "+latitude+"  "+longitude,Toast.LENGTH_SHORT).show();


                        blati = new BigDecimal(String.valueOf(latitude));
                        blong= new BigDecimal(String.valueOf(longitude));
                        if(blati.compareTo(new BigDecimal("0.0")) == 0 && blati.compareTo(new BigDecimal("0.0")) == 0)
                        {
                            start.setText("gps 연결 안됨");
                        }else{
                            Location locationA = new Location("point A");
                            locationA.setLatitude(latitude);
                            locationA.setLongitude(longitude);

                            Location locationB = new Location("point B");
                            double distance[] = new double[name.length];
                            int meter[] = new int[name.length];
                            for(int i = 0; i < lat_db.length; i++)
                            {
                                locationB.setLongitude(lon_db[i]);
                                locationB.setLatitude(lat_db[i]);


                                distance[i] = locationB.distanceTo(locationA);
                                meter[i] = (int) distance[i];
                            }

                            int count = 0;
                            float min = meter[0];
                            for(int i = 0; i < meter.length;i++)
                            {
                                if(min > meter[i])
                                {
                                    min = meter[i];
                                    count = i;
                                }
                            }
                            name_ap = name[count];

                            locationTask task = new locationTask();
                            task.execute(name_ap);
                        }
                    }else{
                        gps.showSettingsAlert();
                    }

                }else{
                    mHandler.removeMessages(0);
                }

                handlerCheck = true;

                hed_count++;
                mHandler.sendEmptyMessageDelayed(0,4000);
            }
        };

    class locationTask extends AsyncTask<String,Void,String>
    {

        String errorString = null;
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            Intent intent = new Intent(pathActivity.this,Path_Popup.class);
            intent.putExtra("data",s);
            startActivity(intent);
        }

        @Override
        protected String doInBackground(String... strings) {

            String searchkeyword = strings[0];
            String URL = "http://wjdgus262.cafe24.com/location2.php";
            String parameters = "name=" +searchkeyword;

            try {

                URL url = new URL(URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(parameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int Code = httpURLConnection.getResponseCode();
                Log.d("log","response Code - " + Code);

                InputStream inputStream;
                if(Code == HttpURLConnection.HTTP_OK)
                {
                    inputStream = httpURLConnection.getInputStream();
                }else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null)
                {
                    sb.append(line);
                }

                bufferedReader.close();


                return sb.toString().trim();
            }catch (Exception e)
            {
                Log.e("Error","Error",e);
                errorString = e.toString();
                return null;
            }
        }
    }

//    public void test()
//    {
//        Log.v("PathActivit",End_Popup.teststring);
//    }


    class endTask extends AsyncTask<Void,Void,String>
    {
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://wjdgus262.cafe24.com/location_list.php";
        }

        @Override
        protected void onPostExecute(String s) {
            Intent intent = new Intent(pathActivity.this,End_Popup.class);
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



    public void shape()
    {
        Main2Activity.flagcheck = true;
        String today = null;
        Date date = new Date();

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

        Calendar cal =Calendar.getInstance();

        cal.setTime(date);

        cal.add(Calendar.MINUTE,39);
        today = format.format(cal.getTime());
        inflate = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflate.inflate(R.layout.activity_subpath_,linearLayout,true);
        imageView = (ImageView)findViewById(R.id.pathmap);
        Attachter = new PhotoViewAttacher(imageView);
        Attachter.setScaleType(ImageView.ScaleType.FIT_XY);
        num_text = (TextView)findViewById(R.id.num_text);
        real_text = (TextView)findViewById(R.id.real_text);
        tranist_text = (TextView)findViewById(R.id.tranist_text);
        minuteslater_text = (TextView)findViewById(R.id.minuteslater_text);
        station_text = (TextView)findViewById(R.id.station_text);
        tranistpoint_text = (TextView)findViewById(R.id.tranistpoint_text);
        tranist_end = (TextView)findViewById(R.id.tranist_end);
        tranist_num = (TextView)findViewById(R.id.tranist_num);
        tranist_start = (TextView)findViewById(R.id.tranist_start);
        tranist_end_setfr = (TextView)findViewById(R.id.tranist_end_setfr);
        tranist_end_set = (TextView)findViewById(R.id.tranist_end_set);
        tranistlater_text = (TextView)findViewById(R.id.tranistlater_text);
        here_text = (TextView)findViewById(R.id.here_text);
        lineView = (View)findViewById(R.id.lineview);
//        mHandler1.sendEmptyMessage(0);
        busHandelr.sendEmptyMessage(0);
        tranist_end.setText(today+" "+getString(R.string.pat_text8));
            subdata = num_text.getText().toString();
 //           number_data = num_text.getText().toString();


        //환승
        try {
            String subtime[] = new String[tiem.length];
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("data"));
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            int count = 0;
            while(count < jsonArray.length())
            {
                JSONObject object = jsonArray.getJSONObject(count);
                tiem[count] = object.getString("one");
                subtime[count] = object.getString("two");
                count++;
            }

            long reqDateTime[] = new long[tiem.length];
            long minute[] = new long[tiem.length];
            Date curDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            curDate = dateFormat.parse(dateFormat.format(curDate));
            long curDateTime = curDate.getTime();
            String a= "";
            for(int i = 0; i < tiem.length; i++)
            {
                reqDateTime[i] = dateFormat.parse(tiem[i]).getTime();
                long num = (reqDateTime[i] - curDateTime) / 60000;
                minute[i] = num;
            }
            long min = 1000;
            int count1 = 0;
            for(int i = 0; i < minute.length; i++)
            {
                if(min > minute[i] && minute[i] > 30)
                {
                    min = minute[i];
                    count1 = i;
                }
            }
           // Toast.makeText(getApplicationContext(),count1+"   "+min+"   ",Toast.LENGTH_SHORT).show();
            if(count1 == 0 || count1 == 1 || count1 == 2 || count1 == 3 || count1 == 4 || count1 == 5)
            {

                tranist_start.setText("AM"+tiem[count1].substring(0,5)+ " "+getString(R.string.pat_text10));
                tranist_end_setfr.setText("AM"+subtime[count1].substring(0,5)+ " "+getString(R.string.pat_text12));
            }else{

                tranist_start.setText("PM"+tiem[count1].substring(0,5)+ " "+getString(R.string.pat_text10));
                tranist_end_setfr.setText("PM"+subtime[count1].substring(0,5)+ " "+getString(R.string.pat_text12));
            }



           // Toast.makeText(getApplicationContext(),tiem[count1].substring(0,5)+"분출발",Toast.LENGTH_SHORT).show();
        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    //--환승끝


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_ACCESS_FINE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            isAccessFineLocation = true;
        }else if(requestCode == PERMISSION_COARSE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            isAccessCoarseLocation = true;
        }

        if(isAccessFineLocation && isAccessCoarseLocation)
        {
            isPermission = true;
        }
    }
    private void callPermission()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_ACCESS_FINE_LOCATION);
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_COARSE_LOCATION);
        }else
        {
            isPermission = true;
        }
    }



    //DB 버스 아이디 불러오기
    class numTask extends AsyncTask<Void,Void,String>{
        String target;
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

        @Override
        protected void onPreExecute() {
            target = "http://wjdgus262.cafe24.com/select_busid.php";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                while(count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);
                    endstaionid[count] = object.getString("id");
                    count++;
                }
//                Toast.makeText(getApplicationContext(),endstaionid[10],Toast.LENGTH_SHORT).show();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }


    class busStationTask extends AsyncTask<String,Integer,Document>
    {
        Document doc;
        String s = "";
        ProgressDialog dialog = new ProgressDialog(pathActivity.this);
        int pos_dialog = 0;
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Loading...");
//            dialog.setCancelable(true);
//            dialog.addContentView(new ProgressBar(getApplicationContext()),new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);
            dialog.dismiss();
            try {
                StrictMode.enableDefaults();
                int count = 0;

                NodeList nodeList = doc.getElementsByTagName("item");

                for(int i = 0; i < nodeList.getLength(); i++)
                {
                    Node node = nodeList.item(i);
                    Element element = (Element)node;

                    NodeList predictTravTm = element.getElementsByTagName("predictTravTm");
                    if(Integer.parseInt(predictTravTm.item(0).getChildNodes().item(0).getNodeValue()) > 1)
                    {
                        count++;
                    }
                }
                String test[] = new String[count];
                int tcount = 0;
                for(int i = 0; i < nodeList.getLength(); i++)
                {
                    Node node = nodeList.item(i);
                    Element element = (Element)node;

                    NodeList predictTravTm = element.getElementsByTagName("predictTravTm");
                    if(Integer.parseInt(predictTravTm.item(0).getChildNodes().item(0).getNodeValue()) > 1)
                    {
                        NodeList routeid = element.getElementsByTagName("routeId");
                        test[tcount] = routeid.item(0).getChildNodes().item(0).getNodeValue()+"   ";
                        s += test[tcount];
                        tcount++;
                    }
                }

//                textView.setText(s);
                if(!s.equals(null))
                {
                    parser(test);
                }else {
                    busHandelr.sendEmptyMessage(0);
                }

            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        public void parser(String string[])
        {
            publicid = new String[800];
            boolean instation = false,initem = false,forch = false;

            int ccc = 0;
            String aa = "";
            try {
               Loop1 : for(int i = 0; i < string.length; i++)
                {
                    if(!forch)
                    {
                        URL url = new URL("http://busopen.jeju.go.kr/OpenAPI/service/bis/BusLocation?route=" + string[i]);

                        XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                        XmlPullParser parser = parserFactory.newPullParser();

                        parser.setInput(url.openStream(),"utf-8");

                        int prserEvent = parser.getEventType();
                        while (prserEvent != XmlPullParser.END_DOCUMENT)
                        {
                            switch (prserEvent){
                                case XmlPullParser.START_TAG:
                                    if(parser.getName().equals("stationNm")){
                                        instation = true;
                                    }
                                    break;
                                case XmlPullParser.TEXT:
                                    if(instation)
                                    {
                                        if(parser.getText().equals(end.getText().toString())){
                                            aa += string[i].replaceAll(" ","")+"/";
                                            forch = true;
                                        }
                                        instation = false;
                                    }
                                    break;
                                case XmlPullParser.END_TAG:
                                    if(parser.getName().equals("item"))
                                    {
                                        initem = false;
                                    }
                                    break;
                            }
                            prserEvent = parser.next();
                        }
                    }else{
                        break;
                    }
                }
                String a = "";
                String idcheck = "";
                int arracount = 0;
                boolean fcheck = false;

                String sub[] = aa.split("/");
                String bb[] = new String[sub.length];
                String oh = "";
                for(int i = 0; i < sub.length; i++)
                {
                    bb[i] = sub[i];
                    oh += bb[i]+"//";
                }

                boolean ttcheck = false;
                for(int i = 0; i < bb.length; i++)
                {
                    for(int j = 0; j < endstaionid.length; j++)
                    {
                        if(bb[i].equals(endstaionid[j]))
                        {
                            buscheck = bb[i];
                        }
                    }
                }

                if(!buscheck.equals(null))
                {
                    realHandler.sendEmptyMessage(0);
                    busHandelr.removeMessages(0);
                }else{
                    Toast.makeText(getApplicationContext(),"정확한 도착지를 입력해주세요",Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e)
            {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsStrting = sw.toString();

                Log.e("error", exceptionAsStrting);
            }
        }
        @Override
        protected Document doInBackground(String... strings) {
            URL url;
            try {
                for (int i = 0; i < 5; i++) {
                    dialog.setProgress(i * 20);

                    Thread.sleep(500);

                }
                StringBuilder sb = new StringBuilder();
                sb.append("http://busopen.jeju.go.kr/OpenAPI/service/bis/BusArrives?station=");
                sb.append(path_num_id.getText().toString());
                url = new URL(sb.toString());
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();

            }catch (Exception e)
            {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsStrting = sw.toString();

                Log.e("error", exceptionAsStrting);
            }
            return doc;
        }
    }
    Handler realHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            new realBackgroundTask().execute(buscheck);
            realHandler.sendEmptyMessageDelayed(0,5000);
        }
    };
    private class realBackgroundTask extends AsyncTask<String,Void,Document>
    {
        Document doc;
        int bustime[] = new int[300];
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);
            try {

//                Toast.makeText(getApplicationContext(),buscheck,Toast.LENGTH_SHORT).show();
                String id = "";
                String pre = "";
                String leftStation = "";
                NodeList nodeList = doc.getElementsByTagName("item");
                int check = 1;
                int checkcount = 0;
                for(int i = 0; i < nodeList.getLength(); i++)
                {
                    Node node = nodeList.item(i);
                    Element element = (Element)node;
                    NodeList routeid = element.getElementsByTagName("routeId");
                    NodeList predivTm = element.getElementsByTagName("predictTravTm");
                    NodeList left = element.getElementsByTagName("leftStation");
                    if(routeid.item(0).getChildNodes().item(0).getNodeValue().equals(buscheck))
                    {
                        if(predivTm.item(0).getChildNodes().item(0).getNodeValue().equals("0"))
                        {
                            pre = "정보없음";
                            leftStation = "정보없음";
                        }else if(Integer.parseInt(predivTm.item(0).getChildNodes().item(0).getNodeValue()) <= 2)
                        {
                            pre = "잠시후도착";
                        }else{
                            pre = predivTm.item(0).getChildNodes().item(0).getNodeValue() + "분뒤도착";
                        }
                        leftStation = left.item(0).getChildNodes().item(0).getNodeValue() + "번째전 정류장";
                        id = routeid.item(0).getChildNodes().item(0).getNodeValue();
                        check = 1;
                        break;
                    }else if(!routeid.item(0).getChildNodes().item(0).getNodeValue().equals(buscheck)){
                        check = 2;
                        if(Integer.parseInt(predivTm.item(0).getChildNodes().item(0).getNodeValue()) >=3)
                        {
                            bustime[i] = Integer.parseInt(predivTm.item(0).getChildNodes().item(0).getNodeValue());
                        }
                    }
                }

                here_text.setTextColor(Color.parseColor("#747374"));
                if(check == 2)
                {
                    tranistpoint_text.setTextColor(Color.parseColor("#747374"));
                    tranist_end.setTextColor(Color.parseColor("#747374"));
                    tranist_end_setfr.setTextColor(Color.parseColor("#747374"));
                    tranist_start.setTextColor(Color.parseColor("#FFE400"));
                    tranist_num.setTextColor(Color.parseColor("#FFE400"));
                    tranist_text.setTextColor(Color.parseColor("#747374"));
                    tranistlater_text.setTextColor(Color.parseColor("#747374"));
                    tranist_end_set.setTextColor(Color.parseColor("#747374"));
                    lineView.setBackgroundColor(Color.parseColor("#BDBDBD"));

                    int min = 1000;
                    int count1 = 0;
                    for(int i = 0; i < bustime.length; i++)
                    {
                        if(min > bustime[i] && bustime[i] > 1)
                        {
                            min = bustime[i];
                            count1 = i;
                        }
                    }
                    Node node = nodeList.item(count1);
                    Element element = (Element)node;
                    NodeList routeid = element.getElementsByTagName("routeId");
                    NodeList predivTm = element.getElementsByTagName("predictTravTm");
                    NodeList left = element.getElementsByTagName("leftStation");
                    leftStation = left.item(0).getChildNodes().item(0).getNodeValue() + "번째전 정류장";
                    id = routeid.item(0).getChildNodes().item(0).getNodeValue();
                    pre = predivTm.item(0).getChildNodes().item(0).getNodeValue() + "분뒤도착";
                }else if(check == 1)
                {
                    tranistpoint_text.setTextColor(Color.parseColor("#FFFFFF"));
                    tranist_end.setTextColor(Color.parseColor("#FFFFFF"));
                    tranist_end_setfr.setTextColor(Color.parseColor("#FFFFFF"));
                    tranist_start.setTextColor(Color.parseColor("#FFFFFF"));
                    tranist_num.setTextColor(Color.parseColor("#FFFFFF"));
                    tranist_text.setTextColor(Color.parseColor("#FFFFFF"));
                    tranistlater_text.setTextColor(Color.parseColor("#FFFFFF"));
                    tranist_end_set.setTextColor(Color.parseColor("#FFFFFF"));
                    lineView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                minuteslater_text.setText(pre);
                station_text.setText(leftStation);
                result(id);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        @Override
        protected Document doInBackground(String... strings) {
            URL url;
            try {
                StringBuffer sb = new StringBuffer();
                sb.append("http://busopen.jeju.go.kr/OpenAPI/service/bis/BusArrives?station=");
                sb.append(path_num_id.getText().toString());
                Log.v("SBSBSBBS",sb.toString());
                url = new URL(sb.toString());
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return doc;
        }
 public void result(String id)
        {
            Response.Listener<String > listener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if(success)
                        {
                            //Toast.makeText(getApplicationContext(),"성공",Toast.LENGTH_SHORT).show();
                            num = jsonResponse.getString("num");
                        }
                        num_text.setText(num);
                        subnum = Integer.parseInt(num);
                        int num_it = Integer.parseInt(num);
                        if(num_it >= 101 && num_it < 200 )
                        {
                            num_text.setTextColor(Color.parseColor("#E71F1E"));
                            minuteslater_text.setTextColor(Color.parseColor("#E71F1E"));
                        }else if(num_it >= 200 && num_it < 400)
                        {
                            num_text.setTextColor(Color.parseColor("#51C1EF"));
                            minuteslater_text.setTextColor(Color.parseColor("#51C1EF"));
                        }else if(num_it >= 400 && num_it < 500)
                        {
                            num_text.setTextColor(Color.parseColor("#2FB08F"));
                            minuteslater_text.setTextColor(Color.parseColor("#2FB08F"));
                        }else if(num_it >= 500 && num_it < 600)
                        {
                            num_text.setTextColor(Color.parseColor("#51C1EF"));
                            minuteslater_text.setTextColor(Color.parseColor("#51C1EF"));
                        }else if(num_it >= 600 & num_it < 800)
                        {
                            num_text.setTextColor(Color.parseColor("#2FB08F"));
                            minuteslater_text.setTextColor(Color.parseColor("#2FB08F"));
                        }else
                        {
                            num_text.setTextColor(Color.parseColor("#F9B200"));
                            minuteslater_text.setTextColor(Color.parseColor("#F9B200"));
                        }

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            };
            BusRequest request = new BusRequest(id,listener);
            RequestQueue queue = Volley.newRequestQueue(pathActivity.this);
            queue.add(request);
        }
    }
    Handler busHandelr = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            new busStationTask().execute();
            busHandelr.sendEmptyMessageDelayed(0,10000);
        }
    };


    class Background_tr extends AsyncTask<Void,Void,String>
    {
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://wjdgus262.cafe24.com/tt.php";
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                while(count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);
                    stationid[count] = new BigDecimal(object.getString("ID"));
                    name[count] = object.getString("name");
                    address[count] = object.getString("info");
                    lon_db[count] = object.getDouble("long");
                    lat_db[count] = object.getDouble("lat");
                    count++;
                }
            }catch (Exception e)
            {
                System.out.println(Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT));
                e.printStackTrace();
            }
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
