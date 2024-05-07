package com.example.processingapplications.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CheckPhoneResponse {

    private String type;

    @JsonProperty("country_code")
    private String countryCode;

    @JsonProperty("city_code")
    private String cityCode;
    private String number;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}
