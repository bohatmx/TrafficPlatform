package com.aftarobot.traffic.library.login;

import com.aftarobot.traffic.library.data.UserDTO;

/**
 * Created by aubreymalabie on 3/11/17.
 */

public class LoginContract {
    public interface Presenter {
        void getUserByEmail(String email);
    }
    public interface View {
        void onUserFound(UserDTO user);
        void onError(String message);
    }
}
