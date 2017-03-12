package com.aftarobot.traffic.library.login;

import com.aftarobot.traffic.backend.trafficApi.model.FCMResponseDTO;
import com.aftarobot.traffic.backend.trafficApi.model.FCMUserDTO;
import com.aftarobot.traffic.backend.trafficApi.model.FCMessageDTO;
import com.aftarobot.traffic.library.data.UserDTO;

/**
 * Created by aubreymalabie on 3/11/17.
 */

public class LoginContract {
    public interface Presenter {
        void getUserByEmail(String email);
        void addUserToFCM(FCMUserDTO user);
        void sendMessage(FCMessageDTO message);
    }
    public interface View {
        void onUserFound(UserDTO user);
        void onUserAddedToFCM(FCMResponseDTO response);
        void onMessageSent(FCMResponseDTO response);
        void onError(String message);
    }
}
