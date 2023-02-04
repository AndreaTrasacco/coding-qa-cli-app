package it.unipi.lsmsd.coding_qa.dao;

import it.unipi.lsmsd.coding_qa.model.User;

import java.util.List;

public interface UserNodeDAO {

    void create(String nickname);

    public void delete(String nickname);

    public List<String> getFollowingList(String nickname);

    public void followUser(String myNickname, String userToFollow);

    public void deleteFollowed(String myNickname, String userToUnFollow);
}
