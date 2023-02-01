package it.unipi.lsmsd.coding_qa.dao;

import it.unipi.lsmsd.coding_qa.model.Question;

public interface QuestionNodeDAO {
    void create(Question question);
    void update(Question question);
    void delete(Question question);
    // fare create, update, delete di answer ???

}
