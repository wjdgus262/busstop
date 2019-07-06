package com.wjdgus262.ocrtestapplication;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class nolieAdapter extends BaseAdapter{
    private Context context;
    private List<noline_item> list;
    private List<noline_item> savelist;
    public nolieAdapter(Context context,List<noline_item> list, List<noline_item> savelist)
    {
        this.context = context;
        this.list = list;
        this.savelist = savelist;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context,R.layout.noline_item,null);
        TextView textView = (TextView)v.findViewById(R.id.noline_list_text);
        TextView textView1 = (TextView)v.findViewById(R.id.noline_list_text1);
        textView.setText(list.get(position).getNumber());
        textView1.setText(list.get(position).getLocation());

        int tv1 = Integer.parseInt(textView.getText().toString());
        if(tv1 >= 101 && tv1 < 200 )
        {
            textView.setTextColor(Color.parseColor("#E71F1E"));
        }else if(tv1 >= 200 && tv1 < 400)
        {
            textView.setTextColor(Color.parseColor("#51C1EF"));
        }else if(tv1 >= 400 && tv1 < 500)
        {
            textView.setTextColor(Color.parseColor("#2FB08F"));
        }else if(tv1 >= 500 && tv1 < 600)
        {
            textView.setTextColor(Color.parseColor("#51C1EF"));
        }else if(tv1 >= 600 & tv1 < 800)
        {
            textView.setTextColor(Color.parseColor("#2FB08F"));
        }else
        {
            textView.setTextColor(Color.parseColor("#F9B200"));
        }




        v.setTag(list.get(position).getNumber());
        return v;
    }
}
