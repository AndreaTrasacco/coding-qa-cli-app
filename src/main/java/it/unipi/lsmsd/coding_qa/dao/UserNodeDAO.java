package it.unipi.lsmsd.coding_qa.dao;

import it.unipi.lsmsd.coding_qa.model.User;

import java.util.List;

public interface UserNodeDAO {

    public void create(String nickname);

    public void delete(String nickname);

    public void update(String nickname);

    public List<String> getFollowingList(String nickname);

    public List<String> getFollowers(String nickname);

    public void followUser(String myNickname, String userToFollow);

    public void deleteCreated(String nickname, String id);

    public void deleteAnswered(String nickname, String id);

    public void deleteFollowed(String myNickname, String userToFollow);
}
