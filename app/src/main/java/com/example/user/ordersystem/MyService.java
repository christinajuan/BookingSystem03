package com.example.user.ordersystem;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    private String str;
    private Timer timer;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
        timer.schedule(new timerTask(),1000,5000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        str = intent.getStringExtra("ppking");
        Log.v("ppking","Service" + str);
        return super.onStartCommand(intent, flags, startId);
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if(timer!=null){
//            timer.purge();
//            timer.cancel();
//            timer=null;
//        }
//    }

    public class timerTask extends TimerTask{
        @Override
        public void run() {
            Intent it =new Intent("test");
            it.putExtra("ppking", str);
            sendBroadcast(it);
        }
    }
}
