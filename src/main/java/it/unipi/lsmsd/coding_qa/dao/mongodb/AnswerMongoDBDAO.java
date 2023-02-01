package it.unipi.lsmsd.coding_qa.dao.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import it.unipi.lsmsd.coding_qa.dao.AnswerDAO;
import it.unipi.lsmsd.coding_qa.dao.UserDAO;
import it.unipi.lsmsd.coding_qa.dao.base.BaseMongoDBDAO;
import it.unipi.lsmsd.coding_qa.model.Answer;
import org.bson.Document;

public class AnswerMongoDBDAO extends BaseMongoDBDAO implements AnswerDAO {
    // Su mongo db l'id dell'answer deve essere scomposto
    public Answer create(Answer answer){
        MongoDatabase database = getDB();
        MongoCollection<Document> collectionQuestion = database.getCollection("Question");
        Document docUser = new Document("_id", answer.getId()) // gestire ID !!!!!!!
                .append("body", answer.getBody())
                .append("createdDate", answer.getCreatedDate())
                .append("author", answer.getAuthor())
                .append("score", answer.getScore())
                .append("voters", answer.getVoters())
                .append("accepted", answer.isAccepted())
                .append("reported", answer.isReported());
        collectionQuestion.insertOne(docUser);

        return answer;
    }
    public Answer update(Answer answer){
        MongoDatabase database = getDB();
        MongoCollection<Document> collectionQuestion = database.getCollection("Question");

        collectionQuestion.updateOne(Filters.eq("_id", answer.getId()), // GESTIRE ID !!!!!!
                Updates.combine(Updates.set("body", answer.getBody()),
                        Updates.set("createdDate", answer.getCreatedDate()),
                        Updates.set("author", answer.getAuthor()),
                        Updates.set("score", answer.getScore()),
                        Updates.set("voters", answer.getVoters()),
                        Updates.set("accepted", answer.isAccepted()),
                        Updates.set("reported", answer.isReported())));
        return answer;
    }
    public void delete(Answer answer){
        MongoDatabase database = getDB();
        MongoCollection<Document> collectionQuestion = database.getCollection("User");
        collectionQuestion.deleteOne(Filters.eq("_id", answer.getId()));
    }
    public void report(Answer answer){

    }
}
