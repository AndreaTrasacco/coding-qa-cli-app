package it.unipi.lsmsd.coding_qa.dao;

import it.unipi.lsmsd.coding_qa.dao.exception.DAONodeException;
import it.unipi.lsmsd.coding_qa.dto.PageDTO;
import it.unipi.lsmsd.coding_qa.model.User;

import java.util.List;

public interface UserNodeDAO {
    void create(String nickname) throws DAONodeException;

    void delete(String nickname) throws DAONodeException;

    PageDTO<String> getFollowingList(String nickname, int page) throws DAONodeException;

    void followUser(String myNickname, String userToFollow) throws DAONodeException;

    void deleteFollowed(String myNickname, String userToUnFollow) throws DAONodeException;
}
