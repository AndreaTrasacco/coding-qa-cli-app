package it.unipi.lsmsd.coding_qa.dao.mongodb;

import com.mongodb.client.*;
import com.mongodb.client.model.*;
import com.mongodb.client.result.InsertOneResult;
import it.unipi.lsmsd.coding_qa.dao.QuestionDAO;
import it.unipi.lsmsd.coding_qa.dao.base.BaseMongoDBDAO;
import it.unipi.lsmsd.coding_qa.dao.exception.DAOException;
import it.unipi.lsmsd.coding_qa.dto.*;
import it.unipi.lsmsd.coding_qa.model.*;
import it.unipi.lsmsd.coding_qa.utils.Constants;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.*;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.ascending;

public class QuestionMongoDBDAO extends BaseMongoDBDAO implements QuestionDAO {
    @Override
    public void createQuestion(Question question) throws DAOException {
        Document docQuestion = new Document("title", question.getTitle())
                .append("body", question.getBody())
                .append("topic", question.getTopic())
                .append("author", question.getAuthor())
                .append("createdDate", question.getCreatedDate());
        try (MongoClient mongoClient = getConnection()) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestions = mongoDatabase.getCollection("questions");
            InsertOneResult result = collectionQuestions.insertOne(docQuestion);
            // Set the id of the question
            question.setId(result.getInsertedId().asObjectId().getValue().toHexString());
        } catch (Exception ex) {
            throw new DAOException(ex);
        }
    }

    @Override
    public List<AnswerScoreDTO> deleteQuestion(String id) throws DAOException {
        try (MongoClient mongoClient = getConnection()) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestions = mongoDatabase.getCollection("questions");
            Bson fields = fields(excludeId(), include("answers"));
            Document deletedQAnswers = collectionQuestions.findOneAndDelete(eq("_id", new ObjectId(id)), new FindOneAndDeleteOptions().projection(fields));
            // For each answer in the deleted question
            List<AnswerScoreDTO> answerScores = new ArrayList<>();
            if (deletedQAnswers != null && deletedQAnswers.containsKey("answers")) {
                List<Document> answers = (ArrayList<Document>) deletedQAnswers.get("answers");
                for (Document answer : answers) {
                    // Get author and score of the answer
                    if (answer.getInteger("score") != 0)
                        answerScores.add(new AnswerScoreDTO(answer.getString("author"), answer.getInteger("score")));
                }
            }
            return answerScores;
        } catch (Exception ex) {
            throw new DAOException(ex);
        }
    }

    @Override
    public Question updateQuestion(Question question) throws DAOException {
        //Only title, body and topic can be updated with this method
        try (MongoClient mongoClient = getConnection()) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestions = mongoDatabase.getCollection("questions");
            Document doc = collectionQuestions.findOneAndUpdate(
                    eq("_id", new ObjectId(question.getId())),
                    Updates.combine(Updates.set("title", question.getTitle()), Updates.set("body", question.getBody()), Updates.set("topic", question.getTopic())),
                    new FindOneAndUpdateOptions().projection(fields(excludeId(), include("title", "body", "topic"))));
            Question oldQuestion = null;
            if (doc != null) {
                oldQuestion = new Question();
                oldQuestion.setId(question.getId());
                oldQuestion.setTitle(doc.getString("title"));
                oldQuestion.setBody(doc.getString("body"));
                oldQuestion.setTopic(doc.getString("topic"));
            }
            return oldQuestion;
        } catch (Exception ex) {
            throw new DAOException(ex);
        }
    }

    @Override
    public void reportQuestion(String id, boolean report) throws DAOException {
        try (MongoClient mongoClient = getConnection()) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestions = mongoDatabase.getCollection("questions");
            collectionQuestions.updateOne(eq("_id", new ObjectId(id)), Updates.set("reported", report));
        } catch (Exception ex) {
            throw new DAOException(ex);
        }
    }

    @Override
    public QuestionPageDTO getQuestionInfo(String id) throws DAOException {
        try (MongoClient mongoClient = getConnection()) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestions = mongoDatabase.getCollection("questions");
            QuestionPageDTO questionPageDTO = new QuestionPageDTO();
            Document doc = collectionQuestions.find(eq("_id", new ObjectId(id))).projection(fields(excludeId(), include("title", "body", "topic", "author", "createdDate"))).first();
            if (doc == null) return null;
            questionPageDTO.setId(id);
            questionPageDTO.setTitle(doc.getString("title"));
            questionPageDTO.setTopic(doc.getString("topic"));
            questionPageDTO.setBody(doc.getString("body"));
            questionPageDTO.setAuthor(doc.getString("author"));
            questionPageDTO.setCreatedDate(doc.getDate("createdDate"));
            return questionPageDTO;
        } catch (Exception ex) {
            throw new DAOException(ex);
        }
    }

    @Override
    public PageDTO<QuestionDTO> getReportedQuestions(int page) throws DAOException {
        PageDTO<QuestionDTO> reportedQuestions = new PageDTO<>();
        List<QuestionDTO> reportedQ = new ArrayList<>();
        try (MongoClient mongoClient = getConnection()) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestions = mongoDatabase.getCollection("questions");

            collectionQuestions.find(eq("reported", true)).projection(fields(include("title", "author", "createdDate", "topic"))).sort(ascending("createdDate")).skip((page - 1) * Constants.PAGE_SIZE).limit(Constants.PAGE_SIZE).forEach(doc -> {
                QuestionDTO temp = new QuestionDTO(doc.getObjectId("_id").toString(),
                        doc.getString("title"),
                        doc.getDate("createdDate"),
                        doc.getString("topic"),
                        doc.getString("author"));
                reportedQ.add(temp);
            });
            reportedQuestions.setCounter(reportedQ.size());
            reportedQuestions.setEntries(reportedQ);
            return reportedQuestions;
        } catch (Exception ex) {
            throw new DAOException(ex);
        }
    }

    @Override
    public PageDTO<QuestionDTO> searchQuestions(int page, String searchString, String topicFilter) throws DAOException {
        PageDTO<QuestionDTO> pageDTO = new PageDTO<>();
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        try (MongoClient mongoClient = getConnection()) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestions = mongoDatabase.getCollection("questions");
            int pageOffset = (page - 1) * Constants.PAGE_SIZE;
            TextSearchOptions options = new TextSearchOptions().caseSensitive(false);
            Bson eq = eq("topic", topicFilter);
            Bson text = text(searchString, options);
            Bson project = fields(include("title", "createdDate", "topic", "author"));
            collectionQuestions.find(and(eq, text)).projection(project).skip(pageOffset).limit(Constants.PAGE_SIZE).forEach(doc -> {
                QuestionDTO temp = new QuestionDTO(doc.getObjectId("_id").toString(), doc.getString("title"),
                        doc.getDate("createdDate"), doc.getString("topic"), doc.getString("author"));
                questionDTOList.add(temp);
            });
            pageDTO.setCounter(questionDTOList.size());
            pageDTO.setEntries(questionDTOList);
            return pageDTO;
        } catch (Exception ex) {
            throw new DAOException(ex);
        }
    }

    @Override
    public void setDeletedUserQuestion(String nickname) throws DAOException {
        try (MongoClient mongoClient = getConnection()){
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestions = mongoDatabase.getCollection("questions");

            collectionQuestions.updateMany(
                    Filters.eq("author", nickname),
                    Updates.set("author", "deletedUser")
            );
        } catch (Exception ex) {
            throw new DAOException(ex);
        }
    }

    public static void main(String[] args) {
        Question q = new Question();
        q.setTitle("TITLE");
        q.setBody("BODY");
        q.setAuthor("AUTHOR");
        q.setTopic("TOPIC");
        Date cDate = new Date(System.currentTimeMillis());
        q.setCreatedDate(cDate);
        QuestionMongoDBDAO qDAO = new QuestionMongoDBDAO();
        try {
            // test create and get q info
            /*qDAO.createQuestion(q);
            QuestionPageDTO qPage = qDAO.getQuestionInfo(q.getId());
            System.out.println(qPage.getBody().equals(q.getBody()));
            // test update
            q.setTitle("ELTIT");
            qDAO.updateQuestion(q);
            qPage = qDAO.getQuestionInfo(q.getId());
            System.out.println(qPage.getTitle().equals("ELTIT"));
            // test report and get reported questions
            qDAO.reportQuestion(q.getId(), true);
            PageDTO<QuestionDTO> page = qDAO.getReportedQuestions();
            System.out.println(page.getEntries().get(0).getId().equals(q.getId()));
            // test search
            page = qDAO.searchQuestions(1, "BODY", "TOPIC");
            System.out.println(page.getEntries().get(0).getId().equals(q.getId()));*/
            // test delete
            List<AnswerScoreDTO> answerScoreDTOS = qDAO.deleteQuestion("63d171b409f8b5fdd2647989");
            System.out.println(answerScoreDTOS.size());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
