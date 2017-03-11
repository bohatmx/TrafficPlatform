package com.aftarobot.traffic.library.login;

import com.aftarobot.traffic.library.api.DataAPI;
import com.aftarobot.traffic.library.data.UserDTO;

/**
 * Created by aubreymalabie on 3/11/17.
 */

public class LoginPresenter implements LoginContract.Presenter {
    LoginContract.View view;
    DataAPI api;

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
        api = new DataAPI();
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
}
