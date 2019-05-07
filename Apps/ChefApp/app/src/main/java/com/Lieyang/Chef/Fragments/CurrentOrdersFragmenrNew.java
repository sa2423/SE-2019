package com.Lieyang.Chef.Fragments;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.Lieyang.Chef.Models.Order;
import com.Lieyang.Chef.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CurrentOrdersFragmenrNew extends RecyclerView.Adapter<CurrentOrdersFragmenrNew.ViewHolder> {

    private Context context;
    private List<ViewHolder> lstHolders;
    public List<Order> lst;

    private Handler mHandler = new Handler();
    private Runnable updateRemainingTimeRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (lstHolders) {
                long currentTime = System.currentTimeMillis();
                for (ViewHolder holder : lstHolders) {
                    holder.updateTimeRemaining(currentTime);
                }
            }
        }
    };

    public CurrentOrdersFragmenrNew(List<Order> lst, Context context){
        super();
        this.lst = lst;
        this.context = context;
        lstHolders = new ArrayList<>();
        startUpdateTimer();
    }

    private void startUpdateTimer() {
        Timer tmr = new Timer();
        tmr.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(updateRemainingTimeRunnable);
            }
        }, 1000, 1000);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_current_orders, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(lst.get(position));
        synchronized (lstHolders) {
            lstHolders.add(holder);
        }
        holder.updateTimeRemaining(System.currentTimeMillis());
    }

    @Override
    public int getItemCount() {
        return lst.size();
    }

    public void clear(){
        lst.clear();
    }

    public Order getItem(int i){
        return lst.get(i);
    }

    public int getCount(){
        return lst.size();
    }

    public void addAll(List<Order> orders){
        lst.addAll(orders);
    }

    public void remove(Order order){
        lst.remove(order);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView orderId, guestId, orderDate, timer;
        Order mModel;

        public void setData(Order item) {
            mModel = item;
            orderId.setText(item.id);
            guestId.setText(item.userid);
            orderDate.setText(item.datetime);
            updateTimeRemaining(System.currentTimeMillis());
        }

        public void updateTimeRemaining(long currentTime) {

            long MillisecondTime = SystemClock.uptimeMillis() - mModel.StartTime;


            int Seconds = (int) (MillisecondTime / 1000);

            int Minutes = Seconds / 60;

            Seconds = Seconds % 60;

            int MilliSeconds = (int) (MillisecondTime % 1000);

            String time = Minutes + ":" + String.format("%02d", Seconds);
            timer.setText(time);
        }

        public ViewHolder(View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.current_order_number);
            guestId = itemView.findViewById(R.id.current_order_guest_id);
            orderDate = itemView.findViewById(R.id.current_order_date);
            timer = itemView.findViewById(R.id.timer);
        }
    }
}