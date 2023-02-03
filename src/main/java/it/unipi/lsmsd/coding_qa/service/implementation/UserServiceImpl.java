package it.unipi.lsmsd.coding_qa.service.implementation;

import it.unipi.lsmsd.coding_qa.dao.DAOLocator;
import it.unipi.lsmsd.coding_qa.dao.QuestionNodeDAO;
import it.unipi.lsmsd.coding_qa.dao.UserDAO;
import it.unipi.lsmsd.coding_qa.dao.UserNodeDAO;
import it.unipi.lsmsd.coding_qa.dao.enums.DAORepositoryEnum;
import it.unipi.lsmsd.coding_qa.dto.QuestionNodeDTO;
import it.unipi.lsmsd.coding_qa.model.Question;
import it.unipi.lsmsd.coding_qa.model.RegisteredUser;
import it.unipi.lsmsd.coding_qa.service.UserService;
import it.unipi.lsmsd.coding_qa.service.exception.BusinessException;

import java.util.List;

public class UserServiceImpl implements UserService {

    private UserDAO userDAO;
    private UserNodeDAO userNodeDAO;
    private QuestionNodeDAO questionNodeDAO;

    public UserServiceImpl(){
        this.userDAO = DAOLocator.getUserDAO(DAORepositoryEnum.MONGODB);
        this.userNodeDAO = DAOLocator.getUserNodeDAO(DAORepositoryEnum.NEO4J);
        this.questionNodeDAO = DAOLocator.getQuestionNodeDAO(DAORepositoryEnum.NEO4J);
    }
    public RegisteredUser register(RegisteredUser user) throws BusinessException{
        try {
            userNodeDAO.create(user.getNickname());
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
            userNodeDAO.update(user.getNickname());
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
            // delete all the Answered and Created edges
            List<QuestionNodeDTO> questionsAndAnswersList = questionNodeDAO.viewCreatedAndAnsweredQuestions(user);
            for(int i = 0; i < questionsAndAnswersList.size(); i++){
                if(questionsAndAnswersList.get(i).isType()){
                    userNodeDAO.deleteCreated(user.getNickname(), questionsAndAnswersList.get(i).getId());
                } else {
                    userNodeDAO.deleteAnswered(user.getNickname(), questionsAndAnswersList.get(i).getId());
                }
            }
            // delete all the follow edges
            List<String> followingList = userNodeDAO.getFollowingList(user.getNickname());
            List<String> followerList = userNodeDAO.getFollowers(user.getNickname());
            for(int i = 0; i < followingList.size(); i++){
                // delete who I'm following
                userNodeDAO.deleteFollowed(user.getNickname(), followingList.get(i));
            }
            for(int i = 0; i < followerList.size(); i++){
                //delete who follows me
                userNodeDAO.deleteFollowed(followerList.get(i), user.getNickname());
            }
            userNodeDAO.delete(user.getNickname());
        } catch(Exception e){
            throw new BusinessException(e);
        }
    }

    public List<String> getFollowerList(RegisteredUser user) throws BusinessException{
        try {
            return userNodeDAO.getFollowingList(user.getNickname());
        } catch(Exception e){
            throw new BusinessException(e);
        }
    }
}
