package com.wjdgus262.ocrtestapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class reviewActivity extends AppCompatActivity {

    private static final String TYPE_IMAGE = "image/*";
    private static final int INPUT_FILE_REQUEST_CODE = 1;

    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        webView = (WebView)findViewById(R.id.reviewweb);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://wjdgus262.cafe24.com/hackerton/index.php");
       // webView.getSettings().setUserAgentString("desktop");
        webView.getSettings().setBuiltInZoomControls(true);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB)
        {
            webView.getSettings().setDisplayZoomControls(false);
        }

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH){
            webView.getSettings().setTextZoom(100);
            webView.setInitialScale(192);
        }


        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onCloseWindow(WebView window) {
                finish();
                super.onCloseWindow(window);
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                final WebSettings settings = view.getSettings();
                settings.setDomStorageEnabled(true);
                settings.setJavaScriptEnabled(true);
                settings.setAllowFileAccess(true);
                settings.setAllowContentAccess(true);

                view.setWebChromeClient(this);
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(view);
                resultMsg.sendToTarget();
                return false;
            }


            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                System.out.println("WebViewActivity");
                if(mFilePathCallback != null)
                {
                    mFilePathCallback.onReceiveValue(null);
                }
                mFilePathCallback = filePathCallback;
                imageChooser();
                return true;
            }

            private void imageChooser()
            {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(takePictureIntent.resolveActivity(getPackageManager()) != null)
                {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath",mCameraPhotoPath);

                    }catch (IOException e)
                    {
                        Log.e(getClass().getName(),"Unable to create Image File",e);
                    }

                    if(photoFile != null)
                    {
                        mCameraPhotoPath = "file:"+photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photoFile));
                    }else{
                        takePictureIntent = null;
                    }
                }

                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType(TYPE_IMAGE);

                Intent[] intentArray;
                if(takePictureIntent != null)
                {
                    intentArray = new Intent[]{takePictureIntent};
                }else{
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT,contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE,"Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,intentArray);

                startActivityForResult(chooserIntent,INPUT_FILE_REQUEST_CODE);

            }
        });
        webView.setWebViewClient(new WebViewClient());

    }

    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+timeStamp+"_";
        File stroageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName,".jpg",stroageDir);
        return imageFile;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == INPUT_FILE_REQUEST_CODE && resultCode == RESULT_OK)
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                if(mFilePathCallback == null)
                {
                    super.onActivityResult(requestCode,resultCode,data);
                    return;
                }
                Uri[] results = new Uri[]{getResultUri(data)};

                mFilePathCallback.onReceiveValue(results);
                mFilePathCallback = null;
            }else {
                if (mUploadMessage == null) {
                    super.onActivityResult(requestCode, resultCode, data);
                    return;
                }
                Uri result = getResultUri(data);

                Log.d(getClass().getName(), "openFileChooser : " + result);
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }else{
            if(mFilePathCallback != null) mFilePathCallback.onReceiveValue(null);
            if(mUploadMessage != null) mUploadMessage.onReceiveValue(null);
            mFilePathCallback = null;
            mUploadMessage = null;
            super.onActivityResult(requestCode,resultCode,data);
        }
    }

    private Uri getResultUri(Intent data)
    {
        Uri result = null;
        if(data == null || TextUtils.isEmpty(data.getDataString()))
        {
            if(mCameraPhotoPath != null)
            {
                result = Uri.parse(mCameraPhotoPath);
            }
        }else{
            String filePath = "";
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                filePath = data.getDataString();
            }else{
                filePath = "file: ";
            }
            result = Uri.parse(filePath);
        }
        return result;
    }

}
