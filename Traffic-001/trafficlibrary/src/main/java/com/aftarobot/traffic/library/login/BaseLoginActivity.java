package com.aftarobot.traffic.library.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.aftarobot.traffic.backend.trafficApi.model.Data;
import com.aftarobot.traffic.backend.trafficApi.model.FCMResponseDTO;
import com.aftarobot.traffic.backend.trafficApi.model.FCMUserDTO;
import com.aftarobot.traffic.backend.trafficApi.model.FCMessageDTO;
import com.aftarobot.traffic.library.data.UserDTO;
import com.aftarobot.traffic.library.util.SharedUtil;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.BuildConfig;
import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


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
                        Log.d(TAG, "onConnected: googleApiClient is connected");
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

        if (auth.getCurrentUser() != null) {
            Log.i(TAG, "onCreate: ++++++ user logged in: "
                    .concat(auth.getCurrentUser().getEmail()));
            user = SharedUtil.getUser(this);
            if (user == null) {
                Log.w(TAG, "$$$$$$$$$$$$$$ onCreate: getting user from Firebase");
                loginPresenter.getUserByEmail(auth.getCurrentUser().getEmail());
            } else {
                Log.i(TAG, "onCreate: ###### cached user found: "
                        .concat(user.getFullName()
                                .concat(" - ")
                                .concat(user.getEmail())));
                userLoggedIn(false);
            }

        }
    }

    private void listenToAuthChanges() {
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Log.w(TAG, "############ onAuthStateChanged: getting user dto");
                    loginPresenter.getUserByEmail(firebaseAuth.getCurrentUser().getEmail());
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
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE},
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
                        userLoggedIn(true);
                    }
                } else {
                    Log.e(TAG, "onRequestPermissionsResult: DENIED");
                }
                break;
        }

    }

    public void startLogin() {
        listenToAuthChanges();
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()))
                .build(), REQUEST_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@onActivityResult: resultCode: " + resultCode);
        if (requestCode == REQUEST_LOGIN) {
            if (resultCode == RESULT_OK) {
                Log.w(TAG, "onActivityResult: Firebase login OK. current user: ".concat(auth.getCurrentUser().getEmail()));
                return;
            }
            if (resultCode == RESULT_CANCELED) {
                loginFailed(USER_CANCELLED);
                return;
            }
            if (resultCode == RESULT_FIRST_USER) {
                loginFailed(USER_CANCELLED);
                return;
            }
        }

    }

    @Override
    public void onUserFound(UserDTO user) {
        Log.i(TAG, "onUserFound... adding to FCM backend: ".concat(user.getFullName()));
        this.user = user;
        SharedUtil.saveUser(user, this);
        FCMUserDTO u = new FCMUserDTO();
        u.setDate(user.getDateRegistered());
        u.setUserID(user.getUserID());
        u.setFirstName(user.getFirstName());
        u.setLastName(user.getLastName());
        u.setToken(SharedUtil.getCloudMsgToken(this));
        u.setAndroidVersion(Build.VERSION.RELEASE);
        u.setManufacturer(Build.MANUFACTURER);
        u.setDeviceModel(Build.MODEL);
        u.setDepartmentID(user.getDepartmentID());
        u.setDepartmentName(user.getDepartmentName());


        loginPresenter.addUserToFCM(u);
        userLoggedIn(true);

    }

    @Override
    public void onUserAddedToFCM(FCMResponseDTO response) {
        if (response.getStatusCode() > 0) {
            FirebaseCrash.report(new Exception(response.getMessage()));
            SharedUtil.saveFCMStatus(false, getApplicationContext());
            userLoggedIn(true);
            return;
        }
        Log.i(TAG, "onUserAddedToFCM: FCM user added OK to GAE: msg:\n".concat(response.getMessage()));
        FCMessageDTO msg = new FCMessageDTO();
        msg.setDate(new Date().getTime());
        Data d = new Data();
        d.setDate(new Date().getTime());
        d.setUserID(user.getUserID());
        d.setMessage("Welcome to the best Traffic Officer app in the world!");
        d.setTitle("Welcome to TrafficOfficer");
        msg.setUserIDs(new ArrayList<String>());
        msg.getUserIDs().add(user.getUserID());
        msg.setData(d);
        Log.d(TAG, "onUserAddedToFCM: sending welcome message via FCM");
        manageTopicSubscriptions(response, user);
        loginPresenter.sendMessage(msg);
    }

    @Override
    public void onMessageSent(FCMResponseDTO response) {
        Log.i(TAG, "onMessageSent: TrafficOfficer FCM message sent OK. Yeaabo!!");
    }

    public static final int FIREBASE_AUTH_FAILED = 7, TRAFFIC_USER_NOT_FOUND = 9, USER_CANCELLED = 11;

    @Override
    public void onError(String message) {
        FirebaseCrash.report(new Exception(message));
        Log.e(TAG, "onError: ".concat(message));
        loginFailed(FIREBASE_AUTH_FAILED);
    }

    private void manageTopicSubscriptions(FCMResponseDTO response, UserDTO user) {
        Log.d(TAG, "manageTopicSubscriptions: CHECK response status and subscribe to topics");
        if (response.getStatusCode() == 0) {
            Log.i(TAG, "onResponse: successfully sent welcome message via FCM: "
                    + response.getMessage());
            FirebaseMessaging.getInstance().subscribeToTopic("general");
            Log.w(TAG, "################## ==> Subscribed to general topic ");
            subscribe(user);

        } else {
            FirebaseCrash.report(new Exception("Failed to send FCM message: " + response.getMessage()));
            Log.e(TAG, "failed to send FCM message: " +
                    response.getMessage());
        }
    }

    private void subscribe(UserDTO user) {
        FirebaseMessaging.getInstance().subscribeToTopic("department" + user.getDepartmentID());
        Log.w(TAG, "################# ==> Subscribed to topic department: " + user.getDepartmentName());

        switch (user.getUserType()) {
            case UserDTO.TRAFFIC_OFFICER:
                FirebaseMessaging.getInstance().subscribeToTopic("officers" + user.getDepartmentID());
                Log.w(TAG, "################## ==> Subscribed to topic: officers: " + user.getDepartmentName());
                break;
            case UserDTO.ADMINISTRATOR:
                FirebaseMessaging.getInstance().subscribeToTopic("administrators" + user.getDepartmentID());
                Log.w(TAG, "################## ==> Subscribed to topic: administrators: " + user.getDepartmentName());
                break;
            case UserDTO.POLITICAL_OFFICIAL:
                FirebaseMessaging.getInstance().subscribeToTopic("officials" + user.getDepartmentID());
                Log.w(TAG, "################## ==> Subscribed to topic: officials: " + user.getDepartmentName());
                break;
        }
    }

    public interface BaseLocationListener {
        void onLocation(Location location);

        void onError(String message);
    }

    /**
     * Abstract methods called after authentication
     */
    public abstract void userLoggedIn(boolean isFirstTime);

    public abstract void loginFailed(int type);

}
