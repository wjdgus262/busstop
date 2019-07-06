package com.wjdgus262.ocrtestapplication;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    private ArrayList<item> itemArrayList = new ArrayList<item>();

    @Override
    public int getCount() {
        return itemArrayList.size();
    }

    @Override
    public item getItem(int i) {
        return itemArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();

        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_list_item,viewGroup,false);
        }

        ImageView imageView = (ImageView) view.findViewById(R.id.icon);
        TextView textView = (TextView)view.findViewById(R.id.title);

        item item = getItem(i);

        imageView.setImageDrawable(item.getIcon());
        textView.setText(item.getText());

        if(item.getText().equals("Geugdong-yeogaeg 064-753-0310") || item.getText().equals("Dongjin-yeogaeg 064-757-5714") || item.getText().equals("Samhwayeogaeg 064-753-1620") || item.getText().equals("Sam-yeong-gyotong 064-713-7000") || item.getText().equals("Geumnam-yeogaeg 064-753-4423"))
        {
            textView.setTextSize(15);
        }
        return view;
    }

    public void addItem(Drawable img, String title)
    {
        item item = new item();

        item.setIcon(img);
        item.setText(title);

        itemArrayList.add(item);
    }
}
