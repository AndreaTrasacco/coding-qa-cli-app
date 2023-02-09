package it.unipi.lsmsd.coding_qa.dto;

import java.util.Date;

public class UserRegistrationDTO {
    private String fullName;
    private String nickname;
    private Date birthdate;
    private String country;
    private Date createdDate;
    private String website;
    private int score;
    private String encPassword;

    public UserRegistrationDTO() {
    }

    public UserRegistrationDTO(String fullName, String nickname, Date birthdate, String country, Date createdDate, String website, int score, String encPassword) {
        this.fullName = fullName;
        this.nickname = nickname;
        this.birthdate = birthdate;
        this.country = country;
        this.createdDate = createdDate;
        this.website = website;
        this.score = score;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getEncPassword() {
        return encPassword;
    }

    public void setEncPassword(String encPassword) {
        this.encPassword = encPassword;
    }
}
