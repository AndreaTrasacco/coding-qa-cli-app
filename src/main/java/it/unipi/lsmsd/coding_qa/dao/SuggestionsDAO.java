package it.unipi.lsmsd.coding_qa.dao;

import it.unipi.lsmsd.coding_qa.dto.PageDTO;
import it.unipi.lsmsd.coding_qa.model.*;

import java.util.List;

public interface SuggestionsDAO { // TODO ECCEZIONI, USARE QUESTIONDTO
    PageDTO<Question> questionsToRead(String nickname); // TODO CORREGGERE USANDO PAGINAZIONE
    PageDTO<Question> questionsToAnswer(String nickname); // TODO CORREGGERE USANDO PAGINAZIONE
}
