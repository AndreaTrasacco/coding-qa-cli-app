package it.unipi.lsmsd.coding_qa.dao;

import it.unipi.lsmsd.coding_qa.dao.exception.DAOException;
import it.unipi.lsmsd.coding_qa.dto.PageDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionDTO;
import it.unipi.lsmsd.coding_qa.model.Question;

public interface QuestionDAO { // TODO AGGIUNGERE PAGINAZIONE DOVE SERVE, ECCEZIONI
    void createQuestion(Question question) throws DAOException;
    void deleteQuestion(String id); // TODO VA MODIFICATO LO SCORE DEGLI UTENTI DELLE RISPOSTE
    void updateQuestion(Question question);
    void reportQuestion(String id);
    Question getQuestionInfo(String id);
    PageDTO<QuestionDTO> getReportedQuestions();
    PageDTO<QuestionDTO> getQuestionPageByTitle(int page, String searchString); // TODO "Metodo" per body
    PageDTO<QuestionDTO> getQuestionPageByTopic(int page, String topic);  // TODO FARE CON AGGREGATE E RECUPERARE ANCHE CLOSED
}
