package com.aftarobot.admin.users;

import com.aftarobot.traffic.library.api.DataAPI;
import com.aftarobot.traffic.library.api.ListAPI;
import com.aftarobot.traffic.library.data.ResponseBag;
import com.aftarobot.traffic.library.data.UserDTO;

/**
 * Created by aubreymalabie on 3/10/17.
 */

public class UserPresenter implements UserContract.Presenter {
    UserContract.View view;
    DataAPI dataAPI;
    ListAPI listAPI;

    public UserPresenter(UserContract.View view) {
        this.view = view;
        dataAPI = new DataAPI();
        listAPI = new ListAPI();
    }

    @Override
    public void addUser(UserDTO user) {
        dataAPI.addUser(user, new DataAPI.DataListener() {
            @Override
            public void onResponse(String key) {
                view.onUserAdded(key);
            }

            @Override
            public void onError(String message) {
               view.onError(message);
            }
        });
    }

    @Override
    public void getUsers(String departmentID) {
         listAPI.getDepartmentUsers(departmentID, new ListAPI.ListListener() {
             @Override
             public void onResponse(ResponseBag bag) {
                 view.onUsersFound(bag.getUsers());
             }

             @Override
             public void onError(String message) {
                  view.onError(message);
             }
         });
    }
}
