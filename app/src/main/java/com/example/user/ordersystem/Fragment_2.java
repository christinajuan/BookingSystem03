package com.example.user.ordersystem;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Fragment_2 extends Fragment {
    private String value = "";
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        SuccessPage mainActivity = (SuccessPage) activity;
        value = mainActivity.getGoogleData();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_2, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView txtResult = (TextView) this.getView().findViewById(R.id.textView2);
        txtResult.setText(value);
    }
}
