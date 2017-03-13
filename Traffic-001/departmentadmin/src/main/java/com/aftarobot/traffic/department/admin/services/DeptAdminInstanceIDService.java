package com.aftarobot.traffic.department.admin.services;

import android.app.IntentService;
import android.util.Log;

import com.aftarobot.traffic.backend.trafficApi.model.FCMResponseDTO;
import com.aftarobot.traffic.backend.trafficApi.model.FCMUserDTO;
import com.aftarobot.traffic.library.data.UserDTO;
import com.aftarobot.traffic.library.login.LoginContract;
import com.aftarobot.traffic.library.login.LoginPresenter;
import com.aftarobot.traffic.library.util.SharedUtil;
import com.aftarobot.traffic.library.util.Util;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class DeptAdminInstanceIDService extends FirebaseInstanceIdService  {
    public static final String TAG = DeptAdminInstanceIDService.class.getSimpleName();
    LoginPresenter presenter;

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "###### Refreshed FCM token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
        presenter = new LoginPresenter(new LoginContract.View() {
            @Override
            public void onUserFound(UserDTO user) {

            }

            @Override
            public void onUserAddedToFCM(FCMResponseDTO response) {
                Log.w(TAG, "onUserAddedToFCM: user has been added to GAE for FCM" );
            }

            @Override
            public void onMessageSent(FCMResponseDTO response) {

            }

            @Override
            public void onError(String message) {
                FirebaseCrash.report(new Exception("Unable to refresh FCM token on GAE"));
                Log.e(TAG, "onError: ".concat(message) );
            }
        });
    }

    private void sendRegistrationToServer(String token) {
        //todo check if sharedUtil has old token
        String oldToken = SharedUtil.getCloudMsgToken(getApplicationContext());
        SharedUtil.saveCloudMsgToken(token,getApplicationContext());

        if (oldToken != null) {

            UserDTO user = SharedUtil.getUser(getApplicationContext());

            FCMUserDTO u = Util.createFCMUser(user,token);
            presenter.addUserToFCM(u);

        }


    }


}
