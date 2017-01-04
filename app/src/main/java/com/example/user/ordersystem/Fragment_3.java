package com.example.user.ordersystem;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class Fragment_3 extends Fragment {
    private String value = "";
    private WebView webView;
    private LocationManager lmgr;
    private MyGPSListener myGPSListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        SuccessPage mainActivity = (SuccessPage) activity;
        value = mainActivity.getFacebookData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_3, container, false);
        webView = (WebView) v.findViewById(R.id.webview);
        init();


//        Button btn = (Button)v.findViewById(R.id.gotoEat);
//        btn.setOnClickListener(new Button.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                gotoEat();
//            }
//        });
        return v;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        init();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView txtResult = (TextView) this.getView().findViewById(R.id.textView3);
        txtResult.setText(value);
    }

    @Override
    public void onDestroy() {
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lmgr.removeUpdates(myGPSListener);
        super.onDestroy();
    }

    //-----------------更新位置-----------------------------------
    private void init() {
        initWebView();

        lmgr = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        myGPSListener = new MyGPSListener();
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lmgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 100, myGPSListener);
    }

    //-----------------位置listener-----------------------------------
    private class MyGPSListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {}

        @Override
        public void onProviderEnabled(String s) {}

        @Override
        public void onProviderDisabled(String s) {}
    }

    private void initWebView(){
        webView.setWebViewClient(new WebViewClient());
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/map.html");
    }
    public void gotoEat(){
        webView.loadUrl("javascript:goto(24.150536, 120.651019)");
    }

}
