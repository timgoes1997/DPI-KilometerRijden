package com.github.timgoes1997.jms.request;

import java.io.Serializable;

public class VehicleRequest implements Serializable {
    private String license;
    private String country;
    private long startPeriod;
    private long endPeriod;

    public VehicleRequest(String license, String country){
        this.country = country;
        this.license = license;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
