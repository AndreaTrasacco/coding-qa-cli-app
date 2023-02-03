package it.unipi.lsmsd.coding_qa.service.implementation;

import it.unipi.lsmsd.coding_qa.dao.DAOLocator;
import it.unipi.lsmsd.coding_qa.dao.UserDAO;
import it.unipi.lsmsd.coding_qa.dao.UserNodeDAO;
import it.unipi.lsmsd.coding_qa.dao.enums.DAORepositoryEnum;
import it.unipi.lsmsd.coding_qa.model.RegisteredUser;
import it.unipi.lsmsd.coding_qa.service.UserService;
import it.unipi.lsmsd.coding_qa.service.exception.BusinessException;

import java.util.List;

public class UserServiceImpl implements UserService {

    private UserDAO userDAO;
    private UserNodeDAO userNodeDAO;

    public UserServiceImpl(){
        this.userDAO = DAOLocator.getUserDAO(DAORepositoryEnum.MONGODB);
        this.userNodeDAO = DAOLocator.getUserNodeDAO(DAORepositoryEnum.NEO4J);
    }
    public RegisteredUser register(RegisteredUser user) throws BusinessException{
        try {
            return userDAO.register(user);
        } catch(Exception e){
            throw new BusinessException(e);
        }
    }

    public boolean login(RegisteredUser user) throws BusinessException{
        try {
            return userDAO.authenticate(user.getNickname(), user.getEncPassword());
        } catch(Exception e){
            throw new BusinessException(e);
        }
    }

    public RegisteredUser getInfo(RegisteredUser user) throws BusinessException{
        try {
            return userDAO.getInfo(user.getId());
        } catch(Exception e){
            throw new BusinessException(e);
        }
    }

    public void updateInfo(RegisteredUser user) throws BusinessException{
        try {
            userDAO.updateInfo(user);
        } catch(Exception e){
            throw new BusinessException(e);
        }
    }

    public void follow(RegisteredUser myself, RegisteredUser userToFollow) throws BusinessException{
        try {
            userNodeDAO.followUser(myself.getNickname(), userToFollow.getNickname());
        } catch(Exception e){
            throw new BusinessException(e);
        }
    }

    public void delete(RegisteredUser user) throws BusinessException{
        try {
            userDAO.delete(user.getId());
            userNodeDAO.delete(user.getNickname());
        } catch(Exception e){
            throw new BusinessException(e);
        }
    }

    public List<String> getFollowerList(RegisteredUser user) throws BusinessException{
        try {
            return userNodeDAO.getFollowerList(user.getNickname());
        } catch(Exception e){
            throw new BusinessException(e);
        }
    }
}
