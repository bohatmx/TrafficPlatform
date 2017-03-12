package com.aftarobot.traffic.officer;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aftarobot.traffic.library.data.FCMData;
import com.aftarobot.traffic.library.login.BaseLoginActivity;
import com.aftarobot.traffic.library.util.Constants;
import com.aftarobot.traffic.officer.capture.CaptureDriverActivity;
import com.aftarobot.traffic.officer.services.TicketUploadService;
import com.google.firebase.crash.FirebaseCrash;

import es.dmoral.toasty.Toasty;

public class MainActivity extends BaseLoginActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Snackbar snackbar;
    FloatingActionButton fab;
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    FCMData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Traffic Officer");

        data = (FCMData)getIntent().getSerializableExtra("data");
        if (data != null) {
            processFCMessage();
        }

        setup();
    }
    private void processFCMessage() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Message from Firebase")
                .setMessage("This message received from Firebase Cloud Messaging\n\n".concat(data.getMessage()))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }
    private void setup() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCapture();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
//        drawer.openDrawer(GravityCompat.START, true);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (user != null) {
            View hdr = navigationView.getHeaderView(0);
            TextView txt = (TextView) hdr.findViewById(R.id.txtName);
            txt.setText(user.getFullName());
            getSupportActionBar().setSubtitle(user.getDepartmentName());
        }
    }

    public static final int REQUEST_CAPTURE = 1243;
    private void startCapture() {
        Intent m = new Intent(getApplicationContext(), CaptureDriverActivity.class);
        startActivityForResult(m, REQUEST_CAPTURE);
    }

    @Override
    public void userLoggedIn(boolean isFirstTime) {
        Log.i(TAG, "userLoggedIn: #################............");
        getLocationPermission();
        Intent m = new Intent(this, TicketUploadService.class);
        startService(m);
        if (isFirstTime) {
            showSnackBar("Welcome to TrafficOfficer! ", "OK", Constants.GREEN);
        }

    }

    @Override
    public void loginFailed() {
        Log.e(TAG, "loginFailed: -------------------");
        Toasty.error(this, "Login failed. Please check with your administrator", Toast.LENGTH_SHORT, true).show();
        FirebaseCrash.report(new Exception("User login failed"));
        finish();
    }

    @Override
    public void loginCancelled() {
        Log.w(TAG, "loginCancelled: *************");
        Toasty.warning(this, "Login cancelled", Toast.LENGTH_SHORT, true).show();
        finish();
    }

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
    public void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

        switch (id) {

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void showSnackBar(String title, String action, String color) {
        snackbar = Snackbar.make(toolbar, title, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.parseColor(color));
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public void showSnackBar(String title) {
        snackbar = Snackbar.make(toolbar, title, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}
