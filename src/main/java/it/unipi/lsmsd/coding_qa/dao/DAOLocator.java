package it.unipi.lsmsd.coding_qa.dao;

import it.unipi.lsmsd.coding_qa.dao.enums.DAORepositoryEnum;
import it.unipi.lsmsd.coding_qa.dao.mongodb.AggregationsMongoDBDAO;
import it.unipi.lsmsd.coding_qa.dao.mongodb.AnswerMongoDBDAO;
import it.unipi.lsmsd.coding_qa.dao.mongodb.QuestionMongoDBDAO;
import it.unipi.lsmsd.coding_qa.dao.mongodb.UserMongoDBDAO;
import it.unipi.lsmsd.coding_qa.dao.neo4j.QuestionNeo4JDAO;
import it.unipi.lsmsd.coding_qa.dao.neo4j.SuggestionsNeo4JDAO;
import it.unipi.lsmsd.coding_qa.dao.neo4j.UserNeo4JDAO;

public class DAOLocator {
    public static AggregationsDAO getAggregationDAO(DAORepositoryEnum daoRepositoryEnum){
        if(DAORepositoryEnum.MONGODB.equals(daoRepositoryEnum)){
            return new AggregationsMongoDBDAO();
        }
        throw new UnsupportedOperationException("Unsupported DAO: " + daoRepositoryEnum);
    }

    public static UserDAO getUserDAO(DAORepositoryEnum daoRepositoryEnum){
        if(DAORepositoryEnum.MONGODB.equals(daoRepositoryEnum)){
            return new UserMongoDBDAO();
        }
        throw new UnsupportedOperationException("Unsupported DAO: " + daoRepositoryEnum);
    }

    public static AnswerDAO getAnswerDAO(DAORepositoryEnum daoRepositoryEnum){
        if(DAORepositoryEnum.MONGODB.equals(daoRepositoryEnum)){
            return new AnswerMongoDBDAO();
        }
        throw new UnsupportedOperationException("Unsupported DAO: " + daoRepositoryEnum);
    }

    public static QuestionDAO getQuestionDAO(DAORepositoryEnum daoRepositoryEnum){
        if(DAORepositoryEnum.MONGODB.equals(daoRepositoryEnum)){
            return new QuestionMongoDBDAO();
        }
        throw new UnsupportedOperationException("Unsupported DAO: " + daoRepositoryEnum);
    }

    public static QuestionNodeDAO getQuestionNodeDAO(DAORepositoryEnum daoRepositoryEnum){
        if(DAORepositoryEnum.NEO4J.equals(daoRepositoryEnum)){
            return new QuestionNeo4JDAO();
        }
        throw new UnsupportedOperationException("Unsupported DAO: " + daoRepositoryEnum);
    }

    public static SuggestionsDAO getSuggestionDAO(DAORepositoryEnum daoRepositoryEnum){
        if(DAORepositoryEnum.NEO4J.equals(daoRepositoryEnum)){
            return new SuggestionsNeo4JDAO();
        }
        throw new UnsupportedOperationException("Unsupported DAO: " + daoRepositoryEnum);
    }

    public static UserNodeDAO getUserNodeDAO(DAORepositoryEnum daoRepositoryEnum){
        if(DAORepositoryEnum.NEO4J.equals(daoRepositoryEnum)){
            return new UserNeo4JDAO();
        }
        throw new UnsupportedOperationException("Unsupported DAO: " + daoRepositoryEnum);
    }
}
