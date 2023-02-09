package it.unipi.lsmsd.coding_qa.service;

import it.unipi.lsmsd.coding_qa.dto.PageDTO;
import it.unipi.lsmsd.coding_qa.dto.UserDTO;
import it.unipi.lsmsd.coding_qa.dto.UserRegistrationDTO;
import it.unipi.lsmsd.coding_qa.model.RegisteredUser;
import it.unipi.lsmsd.coding_qa.model.User;
import it.unipi.lsmsd.coding_qa.service.exception.BusinessException;

import java.util.List;

public interface UserService {
    UserDTO register(UserRegistrationDTO user) throws BusinessException;
    UserDTO login(String username, String encPassword) throws BusinessException;
    // getUserInfo
    UserDTO getInfo(String id) throws BusinessException;
    // updateInfo or update for each field
    void updateInfo(UserRegistrationDTO userRegistrationDTO) throws BusinessException;
    // follow
    void follow(String myself, String userToFollow) throws BusinessException;
    // delete
    void delete(RegisteredUser user) throws BusinessException;
    // getFollowerList
    PageDTO<String> getFollowerList(RegisteredUser user) throws BusinessException;
}
