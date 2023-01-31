package it.unipi.lsmsd.coding_qa.dao;

import it.unipi.lsmsd.coding_qa.model.Answer;

public interface AnswerDAO {
    // metodi che hanno bisogno dell'id hanno un solo parametro id (nelle impl. mongodb viene diviso)
    Answer create(Answer answer);
    Answer update(Answer answer);
    void delete(Answer answer);
    void report(Answer answer);
}
