package it.unipi.lsmsd.coding_qa.model;

import java.util.Date;

public class RegisteredUser extends User{
    private Date birthdate;
    private String country;
    private Date createdDate;
    private String website = "";
    private int score = 0;

    public RegisteredUser(){

    }

    public RegisteredUser(Date birthdate, String country, Date createdDate, String website, int score) {
        this.birthdate = birthdate;
        this.country = country;
        this.createdDate = createdDate;
        this.website = website;
        this.score = score;
    }

    public RegisteredUser(String id, String fullName, String nickname, String encPassword,
                          Date birthdate, String country, Date createdDate, String website, int score) {
        super(id, fullName, nickname, encPassword);
        this.birthdate = birthdate;
        this.country = country;
        this.createdDate = createdDate;
        this.website = website;
        this.score = score;
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
}
