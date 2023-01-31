package it.unipi.lsmsd.coding_qa.model;

import java.util.Date;

public abstract class User {
    public String id;
    private String fullName;
    private String nickname;
    private String encPassword;

    public User(String id, String fullName, String nickname, String encPassword){
        this.id = id;
        this.fullName = fullName;
        this.nickname = nickname;
        this.encPassword = encPassword;
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

    public String getEncPassword() {
        return encPassword;
    }

    public void setEncPassword(String encPassword) {
        this.encPassword = encPassword;
    }
}
