//package com.Lieyang.Chef.Models;
//import android.os.Looper;
//import android.os.Message;
//
//import com.Lieyang.Chef.Fragments.OrderDetailsFragment;
//
//public class TimerThread extends Thread{
//    private boolean running;
//    public int Seconds=0;
//    public int Minutes=0;
//
//    public void setRunning(boolean running){
//        this.running = running;
//    }
//
//    @Override
//    public void run() {
//        running = true;
//
//        while (running){
//            try {
//                Thread.sleep(1000);
//                OrderDetailsFragment.textView.setText("" + Minutes + ":"
//                        + String.format("%02d", Seconds));
//
//                if (Seconds == 60){
//                    Minutes++;
//                    Seconds = 0;
//                }
//
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
