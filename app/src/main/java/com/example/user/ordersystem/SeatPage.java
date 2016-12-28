package com.example.user.ordersystem;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

public class SeatPage extends AppCompatActivity {
    Button[] btns = new Button[10];
    private Button doSetDate;
    private Button doSetTime;
    private TextView textDate;
    private TextView textTime;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    private TextView tableNmb;
    private String tableNumber;

    //----------------------------------
    private int count;
    private UIHandler uiHandler;
    private String SeatState1 , SeatState2 , SeatState3 , SeatState4 , SeatState5 , SeatState6,
                   SeatState7 , SeatState8 , SeatState9 , SeatState0;
    private String loginId;
    private MyReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_page);

        doFindView();
        GregorianCalendar calendar = new GregorianCalendar();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear+1;
                textDate.setText((year+"/"+month+"/"+dayOfMonth));
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                textTime.setText((hourOfDay>12?hourOfDay-12:hourOfDay)
                +":"+minute+" "+(hourOfDay>12?"PM":"AM"));

            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false);

        Button button = (Button)findViewById(R.id.goSuccessPage);
        button.setOnClickListener(clickListenerGo);

        for (int i = 0; i < 10; i++) {
            btns[i].setOnClickListener(clickListener);}

        //---------DATA BASE-----------
        pullData();
        uiHandler = new UIHandler();
        //---------從Service提取資料---------
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("test");
        registerReceiver(myReceiver,intentFilter);



    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent it = new Intent(SeatPage.this,MyService.class);
        startService(it);
    }

    //找View By ID
    public void doFindView(){


        btns[0] = (Button)findViewById(R.id.b0);
        btns[1] = (Button)findViewById(R.id.b1);
        btns[2] = (Button)findViewById(R.id.b2);
        btns[3] = (Button)findViewById(R.id.b3);
        btns[4] = (Button)findViewById(R.id.b4);
        btns[5] = (Button)findViewById(R.id.b5);
        btns[6] = (Button)findViewById(R.id.b6);
        btns[7] = (Button)findViewById(R.id.b7);
        btns[8] = (Button)findViewById(R.id.b8);
        btns[9] = (Button)findViewById(R.id.b9);


        doSetDate = (Button)findViewById(R.id.buttonDate);
        doSetTime = (Button)findViewById(R.id.buttonTime);
        textDate = (TextView)findViewById(R.id.datetext);
        textTime = (TextView)findViewById(R.id.timetext);
        tableNmb = (TextView)findViewById(R.id.tableNmb);
    }

    //日期視窗
    public void setDate(View v){
        datePickerDialog.show();
    }
    //日期視窗
    public void setTime(View v){
        timePickerDialog.show();
    }


    //位置按鈕
    View.OnClickListener clickListener = new View.OnClickListener() {

        public void onClick(View v) {
            for(int i = 0;i<10;i++) {
                btns[i].setBackgroundColor(getResources().getColor(R.color.colorWhite));
            }
            v.setBackgroundColor(getResources().getColor(R.color.colorOrange));
            //顯示桌號 tableNmb
            Button b = (Button) v;
            tableNumber = b.getText().toString();
            tableNmb.setText("你選擇了 " + tableNumber + "號桌");

        }
    };


    //移動按鈕&上傳資料
    View.OnClickListener clickListenerGo = new View.OnClickListener(){
        public void onClick(View v){
            Intent it = new Intent();
            it.setClass(SeatPage.this,SuccessPage.class);
            pushData();
            startActivity(it);
            finish();

            //上傳資料

        }
    };

    //
    //桌號資料變數> tableNumber








//---------------------DATA BASE-----------------------

    //提取資料
    public void pullData(){

        new Thread(){
            @Override
            public void run() {
                try {

                    MultipartUtility mu = new MultipartUtility("https://android-test-db-ppking2897.c9users.io/DataBase/SeatQuery02.php", "UTF-8");
                    List<String> ret = mu.finish();

                    parseJSON(ret.toString());


                } catch (Exception e) {
                    Log.v("ppking", "DB Error:" + e.toString());
                }
            }
        }.start();
    }
    public void pushData(){

        new Thread() {
            @Override
            public void run() {
                try {
                    count++;

                    MultipartUtility mu = new MultipartUtility("https://android-test-db-ppking2897.c9users.io/DataBase/AccountUpload02.php", "UTF-8");
                    if(count<2) {
                        mu.addFormField("accountid", loginId);
                        mu.addFormField("resdate", textDate.getText().toString());
                        mu.addFormField("restime", textTime.getText().toString());
                        mu.addFormField("seatidnumber",tableNumber);
                        List<String> ret = mu.finish();
                        Log.v("ppking", "ret update:: " + ret);
                    }
                } catch (Exception e) {
                    Log.v("ppking", "DB Error:" + e.toString());
                }
            }
        }.start();
    }



    //接收JSON格式並且找特定字串
    private void parseJSON(String json){
        LinkedList accountInfo = new LinkedList<>();
        try{

//            JSONObject jsonObject = new JSONArray(json).getJSONObject(0);
            JSONArray jsonArray = new JSONArray(json).getJSONArray(0);
            for(int i = 0 ; i <=9 ; i++ ) {
                String stringNo = jsonArray.getString(i);
                accountInfo.add(stringNo);
//                String stringNo1 = jsonArray.getString(1);
//                accountInfo.add(stringNo1);
            }

            Message mesg = new Message();
            Bundle data = new Bundle();
            for (int i = 0 ; i<=9 ; i++) {
                data.putCharSequence("data"+i, accountInfo.get(i).toString());
//            data.putCharSequence("data1", accountInfo.get(1).toString());
//            data.putCharSequence("data2",accountInfo.get(2).toString());
            }
            mesg.setData(data);
            mesg.what=0;
            uiHandler.sendMessage(mesg);


        }catch (Exception e){
            Log.v("ppking", "Error : " + e.toString());
        }
    }
    //由handler這邊將資料丟到前景UI
    private class UIHandler extends android.os.Handler{

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0 :
//                    textView.setText("Accound : "+msg.getData().getCharSequence("data0")+"\n");
//                    textView.append("SeatIdNumber : "+msg.getData().getCharSequence("data1")+"\n");
//                    textView.append("Checkout : $"+msg.getData().getCharSequence("data2")+"\n");
                    //TODO:各座位的狀態，去判斷座位
                    SeatState0 = msg.getData().getCharSequence("data0").toString();
                    SeatState1 = msg.getData().getCharSequence("data1").toString();
                    SeatState2 = msg.getData().getCharSequence("data2").toString();
                    SeatState3 = msg.getData().getCharSequence("data3").toString();
                    SeatState4 = msg.getData().getCharSequence("data4").toString();
                    SeatState5 = msg.getData().getCharSequence("data5").toString();
                    SeatState6 = msg.getData().getCharSequence("data6").toString();
                    SeatState7 = msg.getData().getCharSequence("data7").toString();
                    SeatState8 = msg.getData().getCharSequence("data8").toString();
                    SeatState9 = msg.getData().getCharSequence("data9").toString();
                    break;
            }
        }
    }
    private class MyReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            //-----------從Service提取資料
            loginId = intent.getStringExtra("ppking");
            Log.v("ppking" , "loginId" + loginId);
        }
    }

    //------------------------------END----------------------------------------------------------
}
