package it.unipi.lsmsd.coding_qa.dto;

import java.util.Date;

public class UserDTO {
    public String id;
    private String fullName;
    private String nickname;
    private Date birthdate;
    private String country;
    private Date createdDate;
    private String website;
    private int score;

    public UserDTO() {
    }

    public UserDTO(String id, String fullName, String nickname, Date birthdate, String country, Date createdDate, String website, int score) {
        this.id = id;
        this.fullName = fullName;
        this.nickname = nickname;
        this.birthdate = birthdate;
        this.country = country;
        this.createdDate = createdDate;
        this.website = website;
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "******************************************* USER PROFILE *******************************************\n" +
                "\t* Full Name: " + fullName +
                "\n\t* Nickname: " + nickname +
                "\n\t* Birth Date: " + birthdate +
                "\n\t* Country: " + country +
                "\n\t* Created Date: " + createdDate +
                ((website.equals("")) ? "" : "\n\t* Website='" + website) +
                "\n\t* Total score=" + score +
                "\n****************************************************************************************************\n";
    }
}
