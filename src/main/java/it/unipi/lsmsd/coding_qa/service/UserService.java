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

    UserDTO getInfo(String nickname) throws BusinessException;

    void updateInfo(UserRegistrationDTO userRegistrationDTO) throws BusinessException;

    void follow(String myself, String userToFollow) throws BusinessException;

    void unfollow(String myself, String userToUnFollow) throws BusinessException;

    void delete(String id, String nickname) throws BusinessException;

    PageDTO<String> getFollowerList(String nickname) throws BusinessException;

    int getScore(String userId) throws BusinessException;
}
