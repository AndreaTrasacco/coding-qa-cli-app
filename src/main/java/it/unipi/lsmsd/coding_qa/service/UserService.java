package it.unipi.lsmsd.coding_qa.service;

import it.unipi.lsmsd.coding_qa.model.RegisteredUser;
import it.unipi.lsmsd.coding_qa.service.exception.BusinessException;

import java.util.List;

public interface UserService {
    // register --> UserDAO
    RegisteredUser register(RegisteredUser user) throws BusinessException; // concettualmente va bene utilizzare RegisteredUser durante la registrazione?
    // login
    boolean login(RegisteredUser user) throws BusinessException;
    // getUserInfo
    RegisteredUser getInfo(RegisteredUser user) throws BusinessException;
    // updateInfo or update for each field
    void updateInfo(RegisteredUser user) throws BusinessException;
    // follow
    void follow(RegisteredUser myself, RegisteredUser userToFollow) throws BusinessException;
    // delete
    void delete(RegisteredUser user) throws BusinessException;
    // getFollowerList
    List<String> getFollowerList(RegisteredUser user) throws BusinessException;
}
