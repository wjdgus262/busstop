package com.wjdgus262.ocrtestapplication;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;


public class LongPressRepeatListner implements View.OnTouchListener{

    private Handler handler = new Handler();

    private int initalInterval;
    private int normalInterval;
    private View.OnClickListener clickListener;

    private Runnable handlerRunnable = new Runnable() {
        @Override
        public void run() {
                  handler.postDelayed(this,normalInterval);
                  clickListener.onClick(downview);
        }
    };

    View downview;

    public LongPressRepeatListner(int initalInterval, int normalInterval, View.OnClickListener clickListener)
    {
        if(clickListener == null)
        {
            throw new IllegalArgumentException("listener null");
        }
        if(initalInterval < 0 || normalInterval < 0)
        {
            throw new IllegalArgumentException("interval error");
        }
        this.initalInterval = initalInterval;
        this.normalInterval = normalInterval;
        this.clickListener = clickListener;
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                handler.removeCallbacks(handlerRunnable);
                handler.postDelayed(handlerRunnable,initalInterval);
                downview  = view;
                clickListener.onClick(view);
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                handler.removeCallbacks(handlerRunnable);
                downview = null;
                return true;
        }
        return false;
    }
}
