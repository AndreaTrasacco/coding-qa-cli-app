package it.unipi.lsmsd.coding_qa.dao;

import it.unipi.lsmsd.coding_qa.dao.exception.DAOException;
import it.unipi.lsmsd.coding_qa.dto.AnswerDTO;
import it.unipi.lsmsd.coding_qa.dto.PageDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionDTO;
import it.unipi.lsmsd.coding_qa.model.Answer;

import java.util.List;

public interface AnswerDAO {
    void create(String questionId, Answer answer) throws DAOException;
    Answer update(Answer answer) throws DAOException;
    void getBody(AnswerDTO answer) throws DAOException;
    void delete(String id) throws DAOException;
    // dato il range della pagina trovare le risposte
    PageDTO<AnswerDTO> getAnswersPage(int page, String id) throws DAOException;
    void report(String id) throws DAOException;
    boolean vote(String id, boolean voteType, String idVoter) throws DAOException; // true: upvote, false: downvote
    void accept(String id) throws DAOException; // accepting an answer means closing the question
    PageDTO<QuestionDTO> getReportedQuestions(int page) throws DAOException;
    PageDTO<AnswerDTO> getReportedAnswers(int page) throws DAOException;
    //List<QuestionsAndAnswersReportedDTO> getReportedQuestionsAndAnswers() throws DAOException;
}
