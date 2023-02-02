package it.unipi.lsmsd.coding_qa.dao;

import it.unipi.lsmsd.coding_qa.dto.QuestionsAndAnswersReportedDTO;
import it.unipi.lsmsd.coding_qa.model.Answer;

import java.util.List;

public interface AnswerDAO {
    // metodi che hanno bisogno dell'id hanno un solo parametro id (nelle impl. mongodb viene diviso)
    Answer create(Answer answer);
    Answer update(Answer answer);
    void delete(Answer answer);
    void report(Answer answer);
    void vote(Answer answer, boolean voteType); // true: upvote, false: downvote
    void accept(Answer answer); // accepting an answer means closing the question
    List<QuestionsAndAnswersReportedDTO> getReportedQuestionsAndAnswers();
}
