package com.wjdgus262.ocrtestapplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class End_Popup_Adapter extends BaseAdapter {
    private Context context;
    private List<End_Popup_item> list;
    private List<End_Popup_item> savelist;
    public End_Popup_Adapter(Context context, List<End_Popup_item> list,List<End_Popup_item> savelist) {
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
        View v = View.inflate(context,R.layout.end_popup_item,null);
        TextView nametext = (TextView)v.findViewById(R.id.end_popup_name);
        TextView infotext = (TextView)v.findViewById(R.id.end_popup_info);
        TextView idtext = (TextView)v.findViewById(R.id.end_poup_id);
        nametext.setText(list.get(position).getName());
        infotext.setText(list.get(position).getInfo());
        idtext.setText(list.get(position).getId());


        v.setTag(list.get(position).getName());


        return v;
    }
}
