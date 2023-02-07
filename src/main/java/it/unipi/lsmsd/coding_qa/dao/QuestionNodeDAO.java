package it.unipi.lsmsd.coding_qa.dao;

import it.unipi.lsmsd.coding_qa.dao.exception.DAONodeException;
import it.unipi.lsmsd.coding_qa.dto.PageDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionDTO;
import it.unipi.lsmsd.coding_qa.model.Question;

public interface QuestionNodeDAO {
    void create(Question question) throws DAONodeException;
    void update(Question question) throws DAONodeException;
    void delete(String id) throws DAONodeException;
    void updateClose(String questionId, boolean type) throws DAONodeException;
    PageDTO<QuestionDTO> viewCreatedQuestions(String nickname, int page) throws DAONodeException;
    PageDTO<QuestionDTO> viewAnsweredQuestions(String nickname, int page) throws DAONodeException;
}
