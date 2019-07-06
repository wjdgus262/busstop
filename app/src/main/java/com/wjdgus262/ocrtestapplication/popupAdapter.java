package com.wjdgus262.ocrtestapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class popupAdapter extends BaseAdapter {
    private ArrayList<Popupitem> mlist = new ArrayList<>();

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Popupitem getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context context = parent.getContext();

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.popup_item,parent,false);
        }

        ImageView imageView = (ImageView)convertView.findViewById(R.id.popupimage);
        TextView textView = (TextView)convertView.findViewById(R.id.popuptext);

        Popupitem popupitem = getItem(position);

        imageView.setImageDrawable(popupitem.getIcon());
        textView.setText(popupitem.getText());



        return convertView;
    }

    public void addItem(Drawable icon,String name)
    {
        Popupitem popupitem = new Popupitem();


        popupitem.setIcon(icon);
        popupitem.setText(name);

        mlist.add(popupitem);
    }
}
