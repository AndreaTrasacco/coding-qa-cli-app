package it.unipi.lsmsd.coding_qa.dto;

import java.util.Date;

// class used for the registration and for the update of user info
public class UserRegistrationDTO {
    private String fullName;
    private String nickname;
    private Date birthdate;
    private String country;
    private String website;
    private String encPassword;

    public UserRegistrationDTO() {
    }

    public UserRegistrationDTO(String fullName, String nickname, Date birthdate, String country, String website, String encPassword) {
        this.fullName = fullName;
        this.nickname = nickname;
        this.birthdate = birthdate;
        this.country = country;
        this.website = website;
        this.encPassword = encPassword;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEncPassword() {
        return encPassword;
    }

    public void setEncPassword(String encPassword) {
        this.encPassword = encPassword;
    }
}
