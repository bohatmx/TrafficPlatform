package com.aftarobot.traffic.library.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.aftarobot.traffic.library.data.UserDTO;
import com.aftarobot.traffic.library.util.SharedUtil;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.BuildConfig;
import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;

import java.util.Arrays;

import es.dmoral.toasty.Toasty;


/**
 * Created by aubreymalabie on 3/10/17.
 */

public abstract class BaseLoginActivity extends AppCompatActivity implements LoginContract.View {

    FirebaseAuth auth;
    GoogleApiClient googleApiClient;
    LoginPresenter loginPresenter;
    public UserDTO user;

    public static final int REQUEST_LOGIN = 7657, REQUEST_LOCATION_AND_STORAGE = 7253;
    public static final String TAG = BaseLoginActivity.class.getSimpleName();

    @Override
    public void onUserFound(UserDTO user) {
        Log.i(TAG, "onUserFound: ".concat(user.getFullName()));
        SharedUtil.saveUser(user,this);
        userLoggedIn();
    }

    @Override
    public void onError(String message) {
        FirebaseCrash.report(new Exception(message));
        Log.e(TAG, "onError: ".concat(message) );
        userLoggedIn();
    }

    public interface BaseLocationListener {
        void onLocation(Location location);

        void onError(String message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ....########..........");
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        loginPresenter = new LoginPresenter(this);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Awareness.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        getLocationPermission();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Log.w(TAG, "onConnectionSuspended: ");
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.e(TAG, "onConnectionFailed: Google API client failed");
                    }
                })
                .build();

        if (auth.getCurrentUser() == null) {
            listenToAuthChanges();
            startLogin();
        } else {
            Log.i(TAG, "onCreate: user logged in: ".concat(auth.getCurrentUser().getEmail()));
            user = SharedUtil.getUser(this);
            if (user == null) {
                Log.w(TAG, "onCreate: getting user dto" );
                loginPresenter.getUserByEmail(auth.getCurrentUser().getEmail());
            } else {
                Log.i(TAG, "onCreate: local user found: "
                        .concat(user.getFullName()
                                .concat(" - ")
                                .concat(user.getEmail())));
                Toasty.info(this,"User is in the local cache", Toast.LENGTH_SHORT).show();
                userLoggedIn();
            }

        }
    }

    private void listenToAuthChanges() {
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    if (SharedUtil.getUser(getApplicationContext()) == null) {
                        Log.w(TAG, "onAuthStateChanged: getting user dto" );
                        loginPresenter.getUserByEmail(firebaseAuth.getCurrentUser().getEmail());
                    }
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    public static final int REQ_LOCATION_PERMISSION = 524;

    public void getLocationPermission() {

        Log.e(TAG, "..........getLocation: requesting location if not granted already............");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getLocation: Requesting location permission ..................");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQ_LOCATION_PERMISSION);

            return;
        }
        Log.i(TAG, "getLocationPermission: GRANTED ALREADY");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_LOCATION_PERMISSION:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Log.i(TAG, "onRequestPermissionsResult: PERMISSION_GRANTED");
                        userLoggedIn();
                    }
                } else {
                    Log.e(TAG, "onRequestPermissionsResult: DENIED");
                }
                break;
        }

    }

    private void startLogin() {
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build() ))
                .build(), REQUEST_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: resultCode: " + resultCode);
        if (requestCode == REQUEST_LOGIN) {
            if (resultCode == RESULT_OK) {
                if (auth.getCurrentUser() != null) {
                    Log.w(TAG, "onActivityResult: current user: ".concat(auth.getCurrentUser().getEmail()) );
                    if (SharedUtil.getUser(getApplicationContext()) == null) {
                        Log.e(TAG, "onActivityResult: getting user dto" );
                        loginPresenter.getUserByEmail(auth.getCurrentUser().getEmail());
                    }
                }
                getLocationPermission();
                return;
            }
            if (resultCode == RESULT_CANCELED) {
                loginFailed();
                return;
            }
            loginCancelled();
        }

    }

    public abstract void userLoggedIn();

    public abstract void loginFailed();

    public abstract void loginCancelled();
}
