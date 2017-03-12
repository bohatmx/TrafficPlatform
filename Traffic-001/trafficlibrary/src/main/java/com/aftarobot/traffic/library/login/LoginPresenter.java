package com.aftarobot.traffic.library.login;

import android.util.Log;

import com.aftarobot.traffic.backend.trafficApi.model.FCMResponseDTO;
import com.aftarobot.traffic.backend.trafficApi.model.FCMUserDTO;
import com.aftarobot.traffic.backend.trafficApi.model.FCMessageDTO;
import com.aftarobot.traffic.library.api.DataAPI;
import com.aftarobot.traffic.library.api.TrafficEndpointAPI;
import com.aftarobot.traffic.library.data.UserDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by aubreymalabie on 3/11/17.
 */

public class LoginPresenter implements LoginContract.Presenter {
    LoginContract.View view;
    TrafficEndpointAPI trafficApi;
    DataAPI api;
    public static final String TAG = LoginPresenter.class.getSimpleName();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
        api = new DataAPI();
        trafficApi = new TrafficEndpointAPI();
    }

    @Override
    public void getUserByEmail(String email) {
        api.getUserByEmail(email, new DataAPI.UserListener() {
            @Override
            public void onResponse(UserDTO user) {
                view.onUserFound(user);
            }

            @Override
            public void onError(String message) {
               view.onError(message);
            }
        });
    }

    @Override
    public void addUserToFCM(FCMUserDTO user) {
        Log.e(TAG, "addUserToFCM: ............ " +  GSON.toJson(user));
         trafficApi.saveUser(user, new TrafficEndpointAPI.TrafficEndpointListener() {
             @Override
             public void onResponse(FCMResponseDTO response) {
                 view.onUserAddedToFCM(response);
             }

             @Override
             public void onError(String message) {
                  view.onError(message);
             }
         });
    }

    @Override
    public void sendMessage(FCMessageDTO message) {
        trafficApi.sendMessage(message, new TrafficEndpointAPI.TrafficEndpointListener() {
            @Override
            public void onResponse(FCMResponseDTO response) {
                view.onMessageSent(response);
            }

            @Override
            public void onError(String message) {
                  view.onError(message);
            }
        });
    }
}
