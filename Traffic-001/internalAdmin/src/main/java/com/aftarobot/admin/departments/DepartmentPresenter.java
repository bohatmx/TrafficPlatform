package com.aftarobot.admin.departments;

import com.aftarobot.traffic.library.api.DataAPI;
import com.aftarobot.traffic.library.api.ListAPI;
import com.aftarobot.traffic.library.data.ResponseBag;
import com.aftarobot.traffic.library.data.DepartmentDTO;

/**
 * Created by aubreymalabie on 3/10/17.
 */

public class DepartmentPresenter implements DeptListContract.Presenter {
    DeptListContract.View view;
    DataAPI dataAPI;
    ListAPI listAPI;

    public DepartmentPresenter(DeptListContract.View view) {
        this.view = view;
        dataAPI = new DataAPI();
        listAPI = new ListAPI();
    }

    @Override
    public void addDepartment(DepartmentDTO department) {
          dataAPI.addDepartment(department, new DataAPI.DataListener() {
              @Override
              public void onResponse(String key) {
                  view.onDepartmentAdded(key);
              }

              @Override
              public void onError(String message) {
                  view.onError(message);
              }
          });
    }

    @Override
    public void getDepartments() {
       listAPI.getDepartments(new ListAPI.ListListener() {
           @Override
           public void onResponse(ResponseBag bag) {
               view.onDepartmentsFound(bag.getDepartments());
           }

           @Override
           public void onError(String message) {
               view.onError(message);
           }
       });
    }
}
