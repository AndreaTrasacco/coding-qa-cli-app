package it.unipi.lsmsd.coding_qa.dao;

import it.unipi.lsmsd.coding_qa.dao.exception.DAONodeException;
import it.unipi.lsmsd.coding_qa.dto.PageDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionDTO;
import it.unipi.lsmsd.coding_qa.model.*;

import java.util.List;

public interface SuggestionsDAO {
    PageDTO<QuestionDTO> questionsToRead(int page, String nickname) throws DAONodeException;

    PageDTO<QuestionDTO> questionsToAnswer(int page, String nickname) throws DAONodeException;
}
