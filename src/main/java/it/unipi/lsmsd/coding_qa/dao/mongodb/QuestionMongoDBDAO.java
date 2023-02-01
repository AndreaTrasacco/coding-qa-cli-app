package it.unipi.lsmsd.coding_qa.dao.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import it.unipi.lsmsd.coding_qa.dao.QuestionDAO;
import it.unipi.lsmsd.coding_qa.dao.base.BaseMongoDBDAO;
import it.unipi.lsmsd.coding_qa.model.Answer;
import it.unipi.lsmsd.coding_qa.model.Question;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class QuestionMongoDBDAO extends BaseMongoDBDAO implements QuestionDAO {
    @Override
    public void createQuestion(Question question) {
        MongoDatabase mongoDatabase = getDB();
        MongoCollection collectionQuestions = mongoDatabase.getCollection("questions");

        Document docQuestion = new Document("title", question.getTitle())
                .append("body", question.getBody())
                .append("topic", question.getTopic())
                .append("author", question.getAuthor())
                .append("createdDate", question.getCreatedDate());

        collectionQuestions.insertOne(docQuestion);
    }

    @Override
    public void deleteQuestion(Question question) {
        MongoDatabase mongoDatabase = getDB();
        MongoCollection collectionQuestions = mongoDatabase.getCollection("questions");

        collectionQuestions.deleteOne(Filters.eq("_id", question.getId()));
    }

    @Override
    public void updateQuestion(Question question) {
        //Only title, body and topic can be updated
        MongoDatabase mongoDatabase = getDB();
        MongoCollection collectionQuestions = mongoDatabase.getCollection("questions");

        collectionQuestions.updateOne(Filters.eq("_id", question.getId()), Updates.combine(Updates.set("title", question.getTitle()), Updates.set("body", question.getBody()), Updates.set("topic", question.getBody())));
    }

    @Override
    public void reportQuestion(Question question) {
        MongoDatabase mongoDatabase = getDB();
        MongoCollection collectionQuestions = mongoDatabase.getCollection("questions");

        collectionQuestions.updateOne(Filters.eq("_id", question.getId()), Updates.set("reported", true));
    }

    @Override
    public List<Question> getReportedQuestions() {
        List<Question> reportedQuestions = new ArrayList<>();
        MongoDatabase mongoDatabase = getDB();
        MongoCollection<Document> collectionQuestions = mongoDatabase.getCollection("questions");

        // TODO se non ci fosse closed levarlo anche dal costruttore di temp
        collectionQuestions.find(Filters.eq("reported", true)).forEach(doc -> {
            Question temp = new Question(doc.getObjectId("_id").toString(), doc.getString("title"),
                    doc.getString("body"), doc.getString("topic"), doc.getString("author"),
                    doc.getList("answers", Answer.class), doc.getBoolean("closed"),
                    doc.getDate("createdDate"), doc.getBoolean("reported"));
            reportedQuestions.add(temp);
        });

        return  reportedQuestions;
    }
}
