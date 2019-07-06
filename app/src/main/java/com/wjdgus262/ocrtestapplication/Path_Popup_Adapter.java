package com.wjdgus262.ocrtestapplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class Path_Popup_Adapter extends BaseAdapter {
    private Context context;
    private List<Path_Popup_item> list;

    public Path_Popup_Adapter(Context context, List<Path_Popup_item> list) {
        this.context = context;
        this.list = list;
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
        View v = View.inflate(context,R.layout.path_poup_item,null);
        TextView nametext = (TextView)v.findViewById(R.id.path_popup_name);
        TextView infotext = (TextView)v.findViewById(R.id.path_popup_info);
        TextView idtext = (TextView)v.findViewById(R.id.path_poup_id);
        nametext.setText(list.get(position).getName());
        infotext.setText(list.get(position).getInfo());
        idtext.setText(list.get(position).getId());


        v.setTag(list.get(position).getName());


        return v;
    }
}
