package com.example.user.ordersystem;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
            private LocationManager lmgr;
            private MyGPSListener myGPSListener;


            
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
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
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
        }else {
            init();
        }

        }
            //-----------------onCreate_end-----------------------------------

    //-----------------更新位置-----------------------------------
    private void init(){
        lmgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        myGPSListener = new MyGPSListener();
        lmgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,100,myGPSListener);
    }

    //-----------------位置listener-----------------------------------
    private class MyGPSListener implements LocationListener{

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

}
