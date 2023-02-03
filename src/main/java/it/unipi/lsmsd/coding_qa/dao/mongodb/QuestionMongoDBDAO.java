package it.unipi.lsmsd.coding_qa.dao.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.TextSearchOptions;
import com.mongodb.client.model.Updates;
import it.unipi.lsmsd.coding_qa.dao.QuestionDAO;
import it.unipi.lsmsd.coding_qa.dao.base.BaseMongoDBDAO;
import it.unipi.lsmsd.coding_qa.dto.PageDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionDTO;
import it.unipi.lsmsd.coding_qa.model.Answer;
import it.unipi.lsmsd.coding_qa.model.Question;
import it.unipi.lsmsd.coding_qa.utils.Constants;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class QuestionMongoDBDAO extends BaseMongoDBDAO implements QuestionDAO {
    @Override
    public void createQuestion(Question question) {
        MongoDatabase mongoDatabase = getDB();
        MongoCollection<Document> collectionQuestions = mongoDatabase.getCollection("questions");

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
        MongoCollection<Document> collectionQuestions = mongoDatabase.getCollection("questions");

        collectionQuestions.updateOne(Filters.eq("_id", question.getId()), Updates.combine(Updates.set("title", question.getTitle()), Updates.set("body", question.getBody()), Updates.set("topic", question.getBody())));
    }

    @Override
    public void reportQuestion(Question question) {
        MongoDatabase mongoDatabase = getDB();
        MongoCollection<Document> collectionQuestions = mongoDatabase.getCollection("questions");

        collectionQuestions.updateOne(Filters.eq("_id", question.getId()), Updates.set("reported", true));
    }

    @Override
    public Question getQuestionInfo(String id) {
        MongoDatabase mongoDatabase = getDB();
        MongoCollection<Document> collectionQuestions = mongoDatabase.getCollection("questions");

        Document doc = collectionQuestions.find(Filters.eq("_id", id)).first();

        Question temp = new Question(doc.getObjectId("_id").toString(), doc.getString("title"),
                doc.getString("body"), doc.getString("topic"), doc.getString("author"),
                doc.getList("answers", Answer.class), doc.getBoolean("closed"),
                doc.getDate("createdDate"), doc.getBoolean("reported"));

        return temp;
    }

    @Override
    public List<Question> getReportedQuestions() {
        List<Question> reportedQuestions = new ArrayList<>();
        MongoDatabase mongoDatabase = getDB();
        MongoCollection<Document> collectionQuestions = mongoDatabase.getCollection("questions");

        collectionQuestions.find(Filters.eq("reported", true)).forEach(doc -> {
            Question temp = new Question(doc.getObjectId("_id").toString(), doc.getString("title"),
                    doc.getString("body"), doc.getString("topic"), doc.getString("author"),
                    doc.getList("answers", Answer.class), doc.getBoolean("closed"),
                    doc.getDate("createdDate"), doc.getBoolean("reported"));
            reportedQuestions.add(temp);
        });

        return  reportedQuestions;
    }

    @Override
    public PageDTO<QuestionDTO> getQuestionPageByTitle(int page, String searchString) {

        PageDTO<QuestionDTO> pageDTO = new PageDTO<>();
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        MongoDatabase mongoDatabase = getDB();
        MongoCollection<Document> collectionQuestions = mongoDatabase.getCollection("questions");

        AtomicInteger counter = new AtomicInteger();
        int pageOffset = (page - 1) * Constants.PAGE_SIZE;

        TextSearchOptions options = new TextSearchOptions().caseSensitive(false);
        Bson filter = Filters.text(searchString, options);
        collectionQuestions.find(filter).skip(pageOffset).limit(Constants.PAGE_SIZE).forEach(doc -> {  // TODO capire se vanno bene la skip e la limit cosi
            QuestionDTO temp = new QuestionDTO(doc.getObjectId("_id").toString(), doc.getString("title"),
                    doc.getDate("createdDate"), doc.getString("topic"), doc.getString("author"));
            questionDTOList.add(temp);
            counter.set(counter.get() + 1); // TODO capire perchè
        });

        pageDTO.setCounter(counter.get());
        pageDTO.setEntries(questionDTOList);
        return pageDTO;
    }

    @Override
    public PageDTO<QuestionDTO> getQuestionPageByTopic(int page, String topic) {
        PageDTO<QuestionDTO> pageDTO = new PageDTO<>();
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        MongoDatabase mongoDatabase = getDB();
        MongoCollection<Document> collectionQuestions = mongoDatabase.getCollection("questions");

        AtomicInteger counter = new AtomicInteger();
        int pageOffset = (page - 1) * Constants.PAGE_SIZE;

        Bson filter = Filters.eq("topic", topic);
        collectionQuestions.find(filter).skip(pageOffset).limit(Constants.PAGE_SIZE).forEach(doc -> {
            QuestionDTO temp = new QuestionDTO(doc.getObjectId("_id").toString(), doc.getString("title"),
                    doc.getDate("createdDate"), doc.getString("topic"), doc.getString("author"));
            questionDTOList.add(temp);
            counter.set(counter.get() + 1);
        });

        pageDTO.setCounter(counter.get());
        pageDTO.setEntries(questionDTOList);
        return pageDTO;
    }
}
