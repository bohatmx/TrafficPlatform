package com.aftarobot.admin.users;

import com.aftarobot.traffic.library.data.UserDTO;

import java.util.List;

/**
 * Created by aubreymalabie on 3/10/17.
 */

public class UserContract {
    public interface Presenter {
        void addUser(UserDTO user);
        void getUsers(String departmentID);
    }
    public interface View {
        void onUserAdded(String key);
        void onUsersFound(List<UserDTO> users);
        void onError(String message);
    }
}
