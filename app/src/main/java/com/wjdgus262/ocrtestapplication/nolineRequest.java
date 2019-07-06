package com.wjdgus262.ocrtestapplication;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class nolineRequest extends StringRequest {
    final static private String URL = "http://112.164.213.165/android_hk/noline.php";
    private Map<String,String> par;
    public nolineRequest(String num,String pars,Response.Listener<String> list){
        super(Method.POST,URL,list,null);
        par = new HashMap<>();
        par.put("number",num);
        par.put("pars",pars);
    }

    @Override
    public Map<String,String> getParams()
    {
        return par;
    }

}
