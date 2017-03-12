package com.aftarobot.admin.departments;

import com.aftarobot.traffic.library.data.DepartmentDTO;

import java.util.List;

/**
 * Created by aubreymalabie on 3/10/17.
 */

public class DeptListContract {

    public interface Presenter {
        void addDepartment(DepartmentDTO department);
        void getDepartments();
    }
    public interface View {
        void onDepartmentAdded(String key);
        void onDepartmentsFound(List<DepartmentDTO> departments);
        void onError(String message);
    }
}
