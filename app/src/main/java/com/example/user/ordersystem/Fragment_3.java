package com.example.user.ordersystem;
import android.app.Activity;
import android.os.Bundle;
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        SuccessPage mainActivity = (SuccessPage) activity;
        value = mainActivity.getFacebookData();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_3, container, false);
        webView = (WebView)v.findViewById(R.id.webview);
        initWebView();

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView txtResult = (TextView) this.getView().findViewById(R.id.textView3);
        txtResult.setText(value);
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
