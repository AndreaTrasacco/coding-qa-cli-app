package it.unipi.lsmsd.coding_qa.service.implementation;

import it.unipi.lsmsd.coding_qa.dao.DAOLocator;
import it.unipi.lsmsd.coding_qa.dao.SuggestionsDAO;
import it.unipi.lsmsd.coding_qa.dao.UserNodeDAO;
import it.unipi.lsmsd.coding_qa.dao.enums.DAORepositoryEnum;
import it.unipi.lsmsd.coding_qa.model.Question;
import it.unipi.lsmsd.coding_qa.model.User;
import it.unipi.lsmsd.coding_qa.service.SuggestionsService;

import java.util.List;

public class SuggestionsServiceImpl implements SuggestionsService {

    private SuggestionsDAO suggestionsDAO;
    public SuggestionsServiceImpl(){
        this.suggestionsDAO = DAOLocator.getSuggestionDAO(DAORepositoryEnum.NEO4J);
    }
    public List<Question> questionToReadSuggestions(User user){
        return suggestionsDAO.questionToReadSuggestions(user.getNickname());
    }
    public List<Question> questionToAnswerSuggestions(User user){
        return suggestionsDAO.questionToAnswerSuggestions(user.getNickname());
    }
}
