package it.unipi.lsmsd.coding_qa.service.implementation;

import it.unipi.lsmsd.coding_qa.dao.DAOLocator;
import it.unipi.lsmsd.coding_qa.dao.UserDAO;
import it.unipi.lsmsd.coding_qa.dao.UserNodeDAO;
import it.unipi.lsmsd.coding_qa.dao.enums.DAORepositoryEnum;
import it.unipi.lsmsd.coding_qa.model.RegisteredUser;
import it.unipi.lsmsd.coding_qa.service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {

    private UserDAO userDAO;
    private UserNodeDAO userNodeDAO;

    public UserServiceImpl(){
        this.userDAO = DAOLocator.getUserDAO(DAORepositoryEnum.MONGODB);
        this.userNodeDAO = DAOLocator.getUserNodeDAO(DAORepositoryEnum.NEO4J);
    }
    public RegisteredUser register(RegisteredUser user){
        return userDAO.register(user);
    }

    public boolean login(RegisteredUser user){
        return userDAO.authenticate(user.getNickname(), user.getEncPassword());
    }

    public RegisteredUser getInfo(RegisteredUser user){
        return userDAO.getInfo(user.getId());
    }

    public void updateInfo(RegisteredUser user){
        userDAO.updateInfo(user);
    }

    public void follow(RegisteredUser myself, RegisteredUser userToFollow){
        userNodeDAO.followUser(myself.getNickname(), userToFollow.getNickname());
    }

    public void delete(RegisteredUser user){
        userDAO.delete(user.getId());
        userNodeDAO.delete(user.getNickname());
    }

    public List<String> getFollowerList(RegisteredUser user){
        return userNodeDAO.getFollowerList(user.getNickname());
    }
}
