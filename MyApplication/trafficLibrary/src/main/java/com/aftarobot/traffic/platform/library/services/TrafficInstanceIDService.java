package com.aftarobot.traffic.platform.library.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by aubreymalabie on 2/23/17.
 */

public class TrafficInstanceIDService extends FirebaseInstanceIdService {
    public static final String TAG = TrafficInstanceIDService.class.getSimpleName();
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {

    }
}
