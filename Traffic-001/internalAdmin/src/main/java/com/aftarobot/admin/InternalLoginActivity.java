package com.aftarobot.admin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.aftarobot.traffic.library.login.BaseLoginActivity;
import com.aftarobot.traffic.library.login.LoginFragment;
import com.aftarobot.traffic.library.util.Constants;
import com.google.firebase.crash.FirebaseCrash;

import es.dmoral.toasty.Toasty;

public class InternalLoginActivity extends BaseLoginActivity implements LoginFragment.LoginListener {
    public static final String TAG = InternalLoginActivity.class.getSimpleName();
    LoginFragment loginFragment;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: $$$$$$$$$$ ** $$$$$$$$$$$$$$$$");
        isStaff = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_m2);

        setFragment();
    }

    private void setFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        loginFragment = new LoginFragment();
        loginFragment.setListener(this);
        ft.replace(R.id.frameLayout, loginFragment);
        ft.commit();
    }

    @Override
    public void userLoggedIn(boolean isFirstTime) {
        Log.i(TAG, "userLoggedIn: #################............");
        startMain();
    }

    @Override
    public void loginFailed(int type) {
        Log.e(TAG, "loginFailed: -----------------error type: " + type);

        if (firebaseOK) {
            startMain();
            return;
        }

        switch (type) {
            case FIREBASE_AUTH_FAILED:
                snackbar = Snackbar.make(loginFragment.btn, "Login failed", Snackbar.LENGTH_INDEFINITE);
                snackbar.setActionTextColor(Color.parseColor(Constants.ORANGE));
                snackbar.setAction("Try Again", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startLogin();
                    }
                }).show();
                FirebaseCrash.report(new Exception("User login failed"));
                break;
            case TRAFFIC_USER_NOT_FOUND:
                Log.e(TAG, "loginFailed: Firebase user not found by email ... " );
                 startMain();
                break;
            case USER_CANCELLED:
                Toasty.warning(this, "You cancelled the login process. See ya later, alligator!",
                        Toast.LENGTH_LONG, true).show();
                FirebaseCrash.report(new Exception("User login cancelled"));
                finish();
                break;
        }

    }

    private void startMain() {
        Intent m = new Intent(this,MainActivity.class);
        startActivity(m);
        finish();
    }
    public void showSnackBar(String title, String action, String color) {
        snackbar = Snackbar.make(loginFragment.btn, title, Snackbar.LENGTH_INDEFINITE);
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
        snackbar = Snackbar.make(loginFragment.btn, title, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    public void onLoginRequired() {
        startLogin();
    }
}
