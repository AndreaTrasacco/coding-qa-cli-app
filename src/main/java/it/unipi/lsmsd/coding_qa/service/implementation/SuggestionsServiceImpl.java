package it.unipi.lsmsd.coding_qa.service.implementation;

import it.unipi.lsmsd.coding_qa.dao.DAOLocator;
import it.unipi.lsmsd.coding_qa.dao.SuggestionsDAO;
import it.unipi.lsmsd.coding_qa.dao.UserNodeDAO;
import it.unipi.lsmsd.coding_qa.dao.enums.DAORepositoryEnum;
import it.unipi.lsmsd.coding_qa.dto.PageDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionDTO;
import it.unipi.lsmsd.coding_qa.model.Question;
import it.unipi.lsmsd.coding_qa.model.User;
import it.unipi.lsmsd.coding_qa.service.SuggestionsService;
import it.unipi.lsmsd.coding_qa.service.exception.BusinessException;

import java.util.List;

public class SuggestionsServiceImpl implements SuggestionsService {

    private SuggestionsDAO suggestionsDAO;
    public SuggestionsServiceImpl(){
        this.suggestionsDAO = DAOLocator.getSuggestionDAO(DAORepositoryEnum.NEO4J);
    }
    public PageDTO<QuestionDTO> questionToReadSuggestions(int page, String nickname) throws BusinessException {
        try {
            return suggestionsDAO.questionsToRead(page, nickname);
        } catch(Exception e){
            throw new BusinessException(e);
        }
    }
    public PageDTO<QuestionDTO> questionToAnswerSuggestions(int page, String nickname) throws BusinessException{
        try {
            return suggestionsDAO.questionsToAnswer(page, nickname);
        } catch(Exception e){
            throw new BusinessException(e);
        }
    }
}
