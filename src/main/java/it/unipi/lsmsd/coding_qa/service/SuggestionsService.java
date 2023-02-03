package it.unipi.lsmsd.coding_qa.service;

import it.unipi.lsmsd.coding_qa.model.Question;
import it.unipi.lsmsd.coding_qa.model.User;

import java.util.List;

public interface SuggestionsService {
    List<Question> questionToReadSuggestions(User user);
    List<Question> questionToAnswerSuggestions(User user);

}
