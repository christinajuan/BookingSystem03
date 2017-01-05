package com.example.user.ordersystem;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Types.BoomType;
import com.nightonke.boommenu.Types.ButtonType;
import com.nightonke.boommenu.Types.PlaceType;
import com.nightonke.boommenu.Util;

import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        {

            private LoginButton loginButton;
            private CallbackManager callbackManager;
            LoginManager lmg;
            private ProfileTracker profileTracker;
            private String loginID;
            private int count;

            //-----------------menu-----------------------------------
            BoomMenuButton boomMenuButton;
            private Context mContext;
            private Drawable[] drawables;
            private int[][] colors;
            private String[] STRINGS;



            
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Button button = (Button)findViewById(R.id.login);
        button.setOnClickListener(clickListener);


        //-----------------FB--------------------
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");




        lmg.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {}
            @Override
            public void onCancel() {}
            @Override
            public void onError(FacebookException error) {}
        });

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                Log.v("ppking" , "oldProfile : "+ oldProfile );
                Log.v("ppking" , "currentProfile : "+ currentProfile );

                if(currentProfile !=null) {
                    loginID = currentProfile.getId();
                    //TODO : loginID 是由FB讀取出來判斷帳戶的依據並儲存於資料庫內
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                count++;

                                MultipartUtility mu = new MultipartUtility("https://android-test-db-ppking2897.c9users.io/DataBase/Insert02.php", "UTF-8");
                                if(count<2) {
                                    mu.addFormField("accountId", loginID);
                                    mu.addFormField("seatIdNumber", "0");
                                    mu.addFormField("checkOut", "0");
                                    List<String> ret = mu.finish();
                                    Log.v("ppking","ret" + ret);
                                    Log.v("ppking","profileTracker" + loginID);
                                    Intent it = new Intent(MainActivity.this,MyService.class);
                                    it.putExtra("ppking" ,loginID);
                                    startService(it);
                                }
                            } catch (Exception e) {
                                Log.v("ppking", "DB Error:" + e.toString());
                            }
                        }
                    }.start();
                }
            }

        };
        //-----------------end-----------------------------------

        //-----------------要求授權-----------------------------------
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        }
    //-----------------menuOnCreate-----------------------------------
        boomMenuButton = (BoomMenuButton)findViewById(R.id.boom);
        mContext = this;
        initInfoBoom();




        //-----------------onCreate_end-----------------------------------
    }




    //-------------FB-------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
        //Log.v("ppking" , "onActivityResult");
    }
    //--------------end---------------------------




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_booking) {
            Intent it = new Intent();
            it.setClass(MainActivity.this,SeatPage.class);
            startActivity(it);
            finish();
        } else if (id == R.id.nav_waiter) {

        } else if (id == R.id.nav_menu) {
            Intent it = new Intent();
            it.setClass(MainActivity.this,menuActivity.class);
            startActivity(it);
            finish();
        } else if (id == R.id.nav_news) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    View.OnClickListener clickListener = new View.OnClickListener(){
        public void onClick(View v){
            Intent it = new Intent();
            it.setClass(MainActivity.this,SeatPage.class);
            startActivity(it);
            finish();
        }
    };


//-----------------menu-----------------------------------

private void initInfoBoom() {

    drawables = new Drawable[3];
    int[] drawablesResource = new int[]{
            R.drawable.booking,
            R.drawable.waiter,
            R.drawable.menu
//            ,
//            R.drawable.news

    };
    for (int i = 0; i < 3; i++)
        drawables[i] = ContextCompat.getDrawable(mContext, drawablesResource[i]);

    colors = new int[3][2];
    for (int i = 0; i < 3; i++) {
        colors[i][1] = ContextCompat.getColor(mContext, R.color.colorOrange);
        colors[i][0] = Util.getInstance().getPressedColor(colors[i][1]);
    }
    STRINGS = new String[]{
            "訂位",
            "入座QRCode",
            "菜單"
//            ,
//            "最新消息"

    };
    String[] strings = new String[3];
    for (int i = 0; i < 3; i++)
        strings[i] = STRINGS[i];


}
            @Override
            public void onWindowFocusChanged(boolean hasFocus) {
                super.onWindowFocusChanged(hasFocus);

                boomMenuButton.init(
                        drawables, // 子按钮的图标Drawable数组，不可以为null
                        STRINGS,     // 子按钮的文本String数组，可以为null
                        colors,    // 子按钮的背景颜色color二维数组，包括按下和正常状态的颜色，不可为null
                        ButtonType.HAM,     // 子按钮的类型
                        BoomType.PARABOLA,  // 爆炸类型
                        PlaceType.HAM_3_1,  // 排列类型
                        null,               // 展开时子按钮移动的缓动函数类型
                        null,               // 展开时子按钮放大的缓动函数类型
                        null,               // 展开时子按钮旋转的缓动函数类型
                        null,               // 隐藏时子按钮移动的缓动函数类型
                        null,               // 隐藏时子按钮缩小的缓动函数类型
                        null,               // 隐藏时子按钮旋转的缓动函数类型
                        null                // 旋转角度
                );
            }

        }
