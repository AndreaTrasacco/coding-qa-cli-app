package it.unipi.lsmsd.coding_qa.dao;

import it.unipi.lsmsd.coding_qa.model.*;

import java.util.List;

public interface SuggestionsDAO {
    List<Question> questionToReadSuggestions(User user);
    List<Question> questionToAnswerSuggestions(User user);
}
