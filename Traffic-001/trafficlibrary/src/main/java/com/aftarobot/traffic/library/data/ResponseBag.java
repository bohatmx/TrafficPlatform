package com.aftarobot.traffic.library.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by aubreymalabie on 3/10/17.
 */

public class ResponseBag implements Serializable{
    private List<UserDTO> users;
    private List<DepartmentDTO> departments;
    private List<CityDTO> cities;

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }

    public List<DepartmentDTO> getDepartments() {
        return departments;
    }

    public void setDepartments(List<DepartmentDTO> departments) {
        this.departments = departments;
    }

    public List<CityDTO> getCities() {
        return cities;
    }

    public void setCities(List<CityDTO> cities) {
        this.cities = cities;
    }
}
