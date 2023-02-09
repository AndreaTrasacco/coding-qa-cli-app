package it.unipi.lsmsd.coding_qa.dao;

import it.unipi.lsmsd.coding_qa.dao.exception.DAOException;
import it.unipi.lsmsd.coding_qa.dto.AnswerScoreDTO;
import it.unipi.lsmsd.coding_qa.dto.PageDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionPageDTO;
import it.unipi.lsmsd.coding_qa.model.Question;

import java.util.List;

public interface QuestionDAO {
    void createQuestion(Question question) throws DAOException;

    List<AnswerScoreDTO> deleteQuestion(String id) throws DAOException;

    Question updateQuestion(Question question) throws DAOException;

    void reportQuestion(String id, boolean report) throws DAOException;

    QuestionPageDTO getQuestionInfo(String id) throws DAOException;

    PageDTO<QuestionDTO> getReportedQuestions(int page) throws DAOException;

    PageDTO<QuestionDTO> searchQuestions(int page, String searchString, String topicFilter) throws DAOException;
}
