package com.Lieyang.Chef.Models;

import android.os.Handler;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.TextView;

import com.Lieyang.Chef.Fragments.OrderDetailsFragment;

import java.util.ArrayList;
import java.util.List;

public class Order {

    public String id;
    public String datetime;
    public String userid;
    public List<OrderItem> mOrderItems = new ArrayList<>();
    public boolean paid;
    public String payment_method;
    public boolean completed;
    public boolean served;


    /*************/

    public TextView textView ;

    public Button start, pause, reset;

    public long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;



    public int Seconds, Minutes, MilliSeconds ;

    public Handler handler;

    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Minutes = Seconds / 60;

            Seconds = Seconds % 60;

            MilliSeconds = (int) (UpdateTime % 1000);

            textView.setText("" + Minutes + ":"
                    + String.format("%02d", Seconds));

            handler.postDelayed(this, 0);
        }
    };


    /********/

}
