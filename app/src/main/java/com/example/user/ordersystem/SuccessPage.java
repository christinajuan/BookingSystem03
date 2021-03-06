package com.example.user.ordersystem;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.widget.TextView;

public class SuccessPage extends FragmentActivity  {

    private TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_bottom);
        FragmentTabHost tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);


        //1
        tabHost.addTab(tabHost.newTabSpec("倒數計時")
                        .setIndicator("倒數計時"),
                com.example.user.ordersystem.Fragment_1.class,
                null);
        //2
        tabHost.addTab(tabHost.newTabSpec("座位")
                        .setIndicator("座位"),
                com.example.user.ordersystem.Fragment_2.class,
                null);
        //3
        tabHost.addTab(tabHost.newTabSpec("怎麼去")
                        .setIndicator("怎麼去"),
                com.example.user.ordersystem.Fragment_3.class,
                null);
        //4
        tabHost.addTab(tabHost.newTabSpec("菜單")
                        .setIndicator("菜單"),
                com.example.user.ordersystem.Fragment_4.class,
                null);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent it = new Intent();
//                it.setClass(SuccessPage.this,SeatPage.class);
//                startActivity(it);
//                finish();
//
//            }
//        });
    }


    /**************************
     *
     * 給子頁籤呼叫用
     *
     **************************/
    public String getAppleData(){
        return "Apple 123";
    }
    public String getGoogleData(){
        return "Google 456";
    }
    public String getFacebookData(){
        return "Facebook 789";
    }
}
