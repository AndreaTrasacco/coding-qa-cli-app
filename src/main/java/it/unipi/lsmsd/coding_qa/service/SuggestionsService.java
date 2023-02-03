package it.unipi.lsmsd.coding_qa.service;

import it.unipi.lsmsd.coding_qa.model.Question;
import it.unipi.lsmsd.coding_qa.model.User;
import it.unipi.lsmsd.coding_qa.service.exception.BusinessException;

import java.util.List;

public interface SuggestionsService {
    List<Question> questionToReadSuggestions(User user) throws BusinessException;
    List<Question> questionToAnswerSuggestions(User user) throws BusinessException;

}
