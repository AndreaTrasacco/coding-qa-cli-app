package it.unipi.lsmsd.coding_qa.service;

import it.unipi.lsmsd.coding_qa.dto.UserDTO;
import it.unipi.lsmsd.coding_qa.model.RegisteredUser;
import it.unipi.lsmsd.coding_qa.model.User;
import it.unipi.lsmsd.coding_qa.service.exception.BusinessException;

import java.util.List;

public interface UserService {
    RegisteredUser register(RegisteredUser user) throws BusinessException; // concettualmente va bene utilizzare RegisteredUser durante la registrazione?
    // login
    User login(String username, String encPassword) throws BusinessException; // TODO distinguere tra admin e reg user? (ritornando un USER)
    // getUserInfo
    UserDTO getInfo(RegisteredUser user) throws BusinessException;
    // updateInfo or update for each field
    void updateInfo(RegisteredUser user) throws BusinessException;
    // follow
    void follow(RegisteredUser myself, RegisteredUser userToFollow) throws BusinessException;
    // delete
    void delete(RegisteredUser user) throws BusinessException; // TODO LEGGERE TODO IN IMPL
    // getFollowerList
    List<String> getFollowerList(RegisteredUser user) throws BusinessException;
}
