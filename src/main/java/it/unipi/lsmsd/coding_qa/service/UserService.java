package it.unipi.lsmsd.coding_qa.service;

import it.unipi.lsmsd.coding_qa.model.RegisteredUser;

import java.util.List;

public interface UserService {
    // register --> UserDAO
    RegisteredUser register(RegisteredUser user); // concettualmente va bene utilizzare RegisteredUser durante la registrazione?
    // login
    boolean login(RegisteredUser user);
    // getUserInfo
    RegisteredUser getInfo(RegisteredUser user);
    // updateInfo or update for each field
    void updateInfo(RegisteredUser user);
    // follow
    void follow(RegisteredUser myself, RegisteredUser userToFollow);
    // delete
    void delete(RegisteredUser user);
    // getFollowerList
    List<String> getFollowerList(RegisteredUser user);
}
