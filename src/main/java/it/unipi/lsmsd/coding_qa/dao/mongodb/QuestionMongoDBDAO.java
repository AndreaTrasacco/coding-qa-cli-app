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
import java.util.concurrent.atomic.AtomicBoolean;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.*;

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
            question.setId(result.getInsertedId().asString().toString());
        } catch (Exception ex) {
            throw new DAOException(ex);
        }
    }

    @Override
    public List<AnswerScoreDTO> deleteQuestion(String id) throws DAOException {
        try (MongoClient mongoClient = getConnection()) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DB_NAME);
            MongoCollection collectionQuestions = mongoDatabase.getCollection("questions");
            Bson project = project(fields(excludeId(), include("answers")));

            Document deletedQAnswers = (Document) collectionQuestions.findOneAndDelete(eq("_id", new ObjectId(id)), new FindOneAndDeleteOptions().projection(project));
            // For each answer in the deleted question
            List<AnswerScoreDTO> answerScores = new ArrayList<>();
            if (deletedQAnswers.containsKey("answers")) {
                List<Document> answers = (ArrayList<Document>) deletedQAnswers.get("answers");
                for (int i = 0; i < answers.size(); i++) {
                    // Get author and score of the answer
                    if (answers.get(i).getInteger("score") != 0)
                        answerScores.add(new AnswerScoreDTO(answers.get(i).getString("author"), answers.get(i).getInteger("score")));
                }
            }
            return answerScores;
        } catch (Exception ex) {
            throw new DAOException(ex);
        }
    }

    @Override
    public void updateQuestion(Question question) throws DAOException {
        //Only title, body and topic can be updated
        try (MongoClient mongoClient = getConnection()) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestions = mongoDatabase.getCollection("questions");
            collectionQuestions.updateOne(eq("_id", question.getId()), Updates.combine(Updates.set("title", question.getTitle()), Updates.set("body", question.getBody()), Updates.set("topic", question.getBody())));
        } catch (Exception ex) {
            throw new DAOException(ex);
        }
    }

    @Override
    public void reportQuestion(String id) throws DAOException {
        try (MongoClient mongoClient = getConnection()) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestions = mongoDatabase.getCollection("questions");
            collectionQuestions.updateOne(eq("_id", new ObjectId(id)), Updates.set("reported", true));
        } catch (Exception ex) {
            throw new DAOException(ex);
        }
    }

    @Override
    public QuestionPageDTO getQuestionInfo(String id) throws DAOException {
        try (MongoClient mongoClient = getConnection()) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestions = mongoDatabase.getCollection("questions");
            // Get the question info and firt PAGE_SIZE answers
            // To do this is necessary to select the proper document and use unwind operator to separate the array in different documents
            Bson match = match(eq("_id", new ObjectId(id)));
            Bson unwind = unwind("answers");
            Bson sort = sort(descending("answers.createdDate"));
            Bson limit = limit(Constants.PAGE_SIZE);
            List<Answer> answers = new ArrayList<>();
            QuestionPageDTO questionPageDTO = new QuestionPageDTO();
            AtomicBoolean first = new AtomicBoolean(true);
            collectionQuestions.aggregate(Arrays.asList(match, unwind, sort, limit)).forEach(doc -> {
                if (first.get()) {
                    first.set(false);
                    questionPageDTO.setTitle(doc.getString("title"));
                    questionPageDTO.setBody(doc.getString("body"));
                    questionPageDTO.setAuthor(doc.getString("author"));
                    questionPageDTO.setTopic(doc.getString("topic"));
                    questionPageDTO.setCreatedDate(doc.getDate("createdDate"));
                }
                Answer answer = new Answer(doc.getObjectId("_id").toString() + doc.getDate("createdDate"),
                        doc.getString("answer.body"),
                        doc.getDate("createdDate"),
                        doc.getString("author"),
                        doc.getInteger("score"),
                        doc.getList("answers.voters", String.class), // TODO TESTARE BENE
                        doc.getBoolean("accepted"),
                        doc.getBoolean("reported"));
                answers.add(answer);
            });
            if (first.get()) return null;
            questionPageDTO.setId(id);
            PageDTO<Answer> answersPage = new PageDTO<>();
            answersPage.setEntries(answers);
            answersPage.setCounter(answers.size());
            questionPageDTO.setAnswers(answersPage);
            return questionPageDTO;
        } catch (Exception ex) {
            throw new DAOException(ex);
        }
    }

    @Override
    public PageDTO<QuestionDTO> getReportedQuestions() throws DAOException {
        PageDTO<QuestionDTO> reportedQuestions = new PageDTO<>();
        List<QuestionDTO> reportedQ = new ArrayList<>();
        try (MongoClient mongoClient = getConnection()) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestions = mongoDatabase.getCollection("questions");

            collectionQuestions.find(eq("reported", true)).projection(fields(include("title", "author", "createdDate", "topic"))).forEach(doc -> {
                QuestionDTO temp = new QuestionDTO(doc.getObjectId("_id").toString(),
                        doc.getString("title"),
                        doc.getDate("createdDate"),
                        doc.getString("topic"),
                        doc.getString("author"));
                reportedQ.add(temp);
            });
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
            collectionQuestions.find(and(eq, text)).projection(project).sort(descending("createdDate")).skip(pageOffset).limit(Constants.PAGE_SIZE).forEach(doc -> {
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
}
