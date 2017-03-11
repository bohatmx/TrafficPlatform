package com.aftarobot.traffic.library.data;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by aubreymalabie on 3/10/17.
 */

public class FineDTO implements Serializable, Comparable<FineDTO> {
    private String code, regulation, charge, fineID,
            section, departmentID, departmentName;
    private double fine;
    private long cacheDate;

    public long getCacheDate() {
        return cacheDate;
    }

    public void setCacheDate(long cacheDate) {
        this.cacheDate = cacheDate;
    }

    public String getFineID() {
        return fineID;
    }

    public void setFineID(String fineID) {
        this.fineID = fineID;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(String departmentID) {
        this.departmentID = departmentID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRegulation() {
        return regulation;
    }

    public void setRegulation(String regulation) {
        this.regulation = regulation;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public double getFine() {
        return fine;
    }

    public void setFine(double fine) {
        this.fine = fine;
    }

    @Override
    public int compareTo(@NonNull FineDTO fineDTO) {
        if (this.cacheDate > fineDTO.cacheDate) {
            return -1;
        }
        if (this.cacheDate < fineDTO.cacheDate) {
            return 1;
        }
        return 0;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CODE ").append(this.code).append(" -  ");
        if (this.regulation.length() > 1) {
            sb.append(this.getRegulation());
        } else {
            sb.append(this.getSection());
        }
        sb.append("  - R").append(this.fine);

        return sb.toString();
    }
}
