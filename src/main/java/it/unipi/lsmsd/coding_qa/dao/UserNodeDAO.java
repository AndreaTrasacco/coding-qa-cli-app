package it.unipi.lsmsd.coding_qa.dao;

import it.unipi.lsmsd.coding_qa.dto.PageDTO;
import it.unipi.lsmsd.coding_qa.model.User;

import java.util.List;

public interface UserNodeDAO { // TODO ATTENZIONE USARE DETACH
    void create(String nickname);
    void delete(String nickname);
    PageDTO<String> getFollowingList(String nickname);
    void followUser(String myNickname, String userToFollow);
    void deleteFollowed(String myNickname, String userToUnFollow);
}
