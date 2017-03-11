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
    private List<String> files;
    private List<PhotoDTO> photos;
    private List<FineDTO> fines;

    public List<FineDTO> getFines() {
        return fines;
    }

    public void setFines(List<FineDTO> fines) {
        this.fines = fines;
    }

    public List<PhotoDTO> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoDTO> photos) {
        this.photos = photos;
    }

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

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }
}
