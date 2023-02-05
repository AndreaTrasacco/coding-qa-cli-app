package it.unipi.lsmsd.coding_qa.dao;

import it.unipi.lsmsd.coding_qa.dto.QuestionsAndAnswersReportedDTO;
import it.unipi.lsmsd.coding_qa.model.Answer;

import java.util.List;

public interface AnswerDAO { // TODO ECCEZIONI
    void create(String questionId, Answer answer); // TODO RISCRIVERE - INDICE NON C'E PRIMA DI AGGIUNGERLO, POI VA SETTATO IN ANSWER ID
    Answer update(Answer answer);
    void delete(String id);
    void report(String id);
    void vote(String id, boolean voteType); // true: upvote, false: downvote
    void accept(String id); // accepting an answer means closing the question
    List<QuestionsAndAnswersReportedDTO> getReportedQuestionsAndAnswers(); // TODO CORREZIONE (SPLIT METODO) --> SERVE OPPORTUNA ANSWER DTO
}
