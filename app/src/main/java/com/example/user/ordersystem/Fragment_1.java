package com.example.user.ordersystem;
import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Fragment_1 extends Fragment {
    private String value = "";
    private TextView mTextView;


    //訂位時間倒數參數
    int tt = 10000;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        SuccessPage mainActivity = (SuccessPage) activity;
        //value = mainActivity.getAppleData();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_1, container, false);
        mTextView = (TextView)v.findViewById(R.id.textView2);
        new CountDownTimer(tt,1000){

            @Override
            public void onFinish() {
                mTextView.setText("Done!");
            }

            @Override
            public void onTick(long millisUntilFinished) {
                mTextView.setText("入座倒數:"+millisUntilFinished/1000);
            }

        }.start();
        return v;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView txtResult = (TextView) this.getView().findViewById(R.id.textView1);
        txtResult.setText(value);
    }

}
