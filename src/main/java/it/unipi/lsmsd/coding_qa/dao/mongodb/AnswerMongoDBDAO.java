package it.unipi.lsmsd.coding_qa.dao.mongodb;

import it.unipi.lsmsd.coding_qa.dao.AnswerDAO;
import it.unipi.lsmsd.coding_qa.dao.UserDAO;
import it.unipi.lsmsd.coding_qa.dao.base.BaseMongoDBDAO;
import it.unipi.lsmsd.coding_qa.model.Answer;

public class AnswerMongoDBDAO extends BaseMongoDBDAO implements AnswerDAO {
    // Su mongo db l'id dell'answer deve essere scomposto
    public Answer create(Answer answer){

        return answer;
    }
    public Answer update(Answer answer){

        return answer;
    }
    public void delete(Answer answer){

    }
    public void report(Answer answer){

    }
}
