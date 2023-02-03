package it.unipi.lsmsd.coding_qa.dao;

import it.unipi.lsmsd.coding_qa.model.*;

import java.util.List;

public interface SuggestionsDAO {
    List<Question> questionToReadSuggestions(String nickname);
    List<Question> questionToAnswerSuggestions(String nickname);
}
