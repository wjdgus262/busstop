package com.wjdgus262.ocrtestapplication;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class BusRequest extends StringRequest{

    public static String URL = "http://wjdgus262.cafe24.com/busid_for.php";
    private Map<String,String> par;

    public BusRequest(String id, Response.Listener listener)
    {
        super(Method.POST,URL,listener,null);
        par = new HashMap<>();
        par.put("id",id);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return par;
    }
}
