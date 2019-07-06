package com.wjdgus262.ocrtestapplication;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PathRequest extends StringRequest {

    public static String URL = "http://wjdgus262.cafe24.com/line.php";
    private Map<String,String > par;
    public PathRequest(String name, Response.Listener listener)
    {
        super(Method.POST,URL,listener,null);
        par = new HashMap<>();
        par.put("name",name);
    }
    @Override
    public Map<String,String> getParams()
    {
        return par;
    }
}
