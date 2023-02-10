package it.unipi.lsmsd.coding_qa.dao;

import it.unipi.lsmsd.coding_qa.dao.exception.DAOException;
import it.unipi.lsmsd.coding_qa.dto.AnswerDTO;
import it.unipi.lsmsd.coding_qa.dto.AnswerScoreDTO;
import it.unipi.lsmsd.coding_qa.dto.PageDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionDTO;
import it.unipi.lsmsd.coding_qa.model.Answer;

public interface AnswerDAO {
    void create(String questionId, Answer answer) throws DAOException;
    void updateBody(String answerId, String body) throws DAOException;
    void getCompleteAnswer(Answer answer) throws DAOException;
    AnswerScoreDTO delete(String id) throws DAOException;
    PageDTO<AnswerDTO> getAnswersPage(int page, String questionId) throws DAOException;
    void report(String id, boolean report) throws DAOException;
    boolean vote(String id, boolean voteType, String idVoter) throws DAOException; // true: upvote, false: downvote
    boolean accept(String id, boolean accepted) throws DAOException; // Accepting an answer means closing the question
    PageDTO<AnswerDTO> getReportedAnswers(int page) throws DAOException;
}
