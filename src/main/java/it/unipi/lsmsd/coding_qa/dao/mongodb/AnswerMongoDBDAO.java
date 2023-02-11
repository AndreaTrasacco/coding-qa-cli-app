package it.unipi.lsmsd.coding_qa.dao.mongodb;

import com.mongodb.client.*;
import com.mongodb.client.model.*;
import com.mongodb.client.result.UpdateResult;
import it.unipi.lsmsd.coding_qa.dao.AnswerDAO;
import it.unipi.lsmsd.coding_qa.dao.base.BaseMongoDBDAO;
import it.unipi.lsmsd.coding_qa.dao.exception.DAOException;
import it.unipi.lsmsd.coding_qa.dto.*;
import it.unipi.lsmsd.coding_qa.model.Answer;
import it.unipi.lsmsd.coding_qa.utils.Constants;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

public class AnswerMongoDBDAO extends BaseMongoDBDAO implements AnswerDAO {
    public static void main(String[] args) {
        AnswerMongoDBDAO answerDAO = new AnswerMongoDBDAO();
        try {
            // Answer answer = new Answer("BODY", new Date(System.currentTimeMillis()), "AUTHOR");
            // answerDAO.create("63d171b409f8b5fdd264791f", answer);
            // System.out.println(answer.getId());
            // answerDAO.report(answer.getId(), true);
            // System.out.println(answerDAO.accept(answer.getId()));
            // PageDTO<AnswerDTO> page = answerDAO.getReportedAnswers(1);
            // System.out.println(answerDAO.vote(answer.getId(), true, "1"));
            // System.out.println(answerDAO.vote(answer.getId(), false, "2"));
            // System.out.println(answerDAO.vote(answer.getId(), false, "2"));
            // AnswerScoreDTO answerScoreDTO = answerDAO.delete(answer.getId());
            // page = answerDAO.getAnswersPage(1, "63d171b409f8b5fdd2647926");
            // System.out.println("END MAIN");
            System.out.println(answerDAO.accept("63d171b409f8b5fdd264792c_2022-12-01T00:22:05.490Z", true));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void create(String questionId, Answer answer) throws DAOException {
        try (MongoClient myClient = getConnection()) {
            MongoDatabase database = myClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestion = database.getCollection("questions");
            Document answerDoc = new Document();
            answerDoc.put("body", answer.getBody());
            answerDoc.put("createdDate", answer.getCreatedDate());
            answerDoc.put("author", answer.getAuthor());
            answerDoc.put("score", answer.getScore());
            collectionQuestion.updateOne(Filters.eq("_id", new ObjectId(questionId)),
                    Updates.push("answers", answerDoc));
            answer.setId(questionId + '_' + answer.getCreatedDate().toInstant());
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    public void updateBody(String id, String body) throws DAOException {
        try (MongoClient myClient = getConnection()) {
            MongoDatabase database = myClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestion = database.getCollection("questions");
            // Structure of answer id : questionId_createdDate
            String questionId = id.substring(0, id.indexOf('_'));
            Date createdDate = getDateFromString(id.substring(id.indexOf('_') + 1));
            collectionQuestion.updateOne(
                    eq("_id", new ObjectId(questionId)),
                    Updates.combine(Updates.set("answers.$[xxx].body", body)),
                    new UpdateOptions().arrayFilters(Arrays.asList(eq("xxx.createdDate", createdDate)))
            );
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    public void getCompleteAnswer(Answer answer) throws DAOException {
        try (MongoClient myClient = getConnection()) {
            MongoDatabase database = myClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestion = database.getCollection("questions");
            String answerId = answer.getId();
            String questionId = answerId.substring(0, answerId.indexOf('_'));
            Bson match = and(eq("_id", new ObjectId(questionId)), eq("answers.createdDate", answer.getCreatedDate()));
            Document result = collectionQuestion.find(match).projection(fields(excludeId(), Projections.elemMatch("answers", eq("createdDate", answer.getCreatedDate())))).first();
            if (result != null && result.containsKey("answers")) {
                for (Document ansDoc : result.getList("answers", Document.class)) {
                    answer.setBody(ansDoc.getString("body"));
                    answer.setScore(ansDoc.getInteger("score"));
                    if (ansDoc.containsKey("accepted"))
                        answer.setAccepted(ansDoc.getBoolean("accepted"));
                    break; // Only one answer in the array so it is immaterial
                }
            }
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    public String delete(String id) throws DAOException { // The method returns the question id of the deleted answer
        try (MongoClient mongoClient = getConnection(); ClientSession session = mongoClient.startSession()) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DB_NAME);

            TransactionBody txnBody = (TransactionBody<String>) () -> {
                MongoCollection<Document> collectionQuestions = mongoDatabase.getCollection("questions");
                MongoCollection<Document> collectionUsers = mongoDatabase.getCollection("users");
                // Structure of answer id : questionId_createdDate
                String questionId = id.substring(0, id.indexOf('_'));
                Date createdDate;
                try {
                    createdDate = getDateFromString(id.substring(id.indexOf('_') + 1));
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
                // After deletion of answer --> get score and update the score of the author of the answer
                Document docBefore = collectionQuestions.findOneAndUpdate(
                        eq("_id", new ObjectId(questionId)),
                        Updates.combine(Updates.pull("answers", new Document("createdDate", createdDate))),
                        new FindOneAndUpdateOptions().projection(fields(excludeId(), Projections.elemMatch("answers", and(eq("createdDate", createdDate), ne("score", 0)))))
                );
                if (docBefore != null && docBefore.containsKey("answers")) {
                    Document ansDoc = docBefore.getList("answers", Document.class).get(0);
                    collectionUsers.updateOne(eq("nickname", ansDoc.getString("author")),
                    Updates.inc("score", ansDoc.getInteger("score") * (-1)));
                }
                return questionId;
            };
            try {
                return (String) session.withTransaction(txnBody, txnOptions);
            } catch (Exception ex) {
                throw new DAOException(ex);
            }
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    public void report(String id, boolean report) throws DAOException {
        try (MongoClient myClient = getConnection()) {
            MongoDatabase database = myClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestion = database.getCollection("questions");
            String questionId = id.substring(0, id.indexOf('_'));
            Date createdDate = getDateFromString(id.substring(id.indexOf('_') + 1));
            collectionQuestion.updateOne(
                    eq("_id", new ObjectId(questionId)),
                    Updates.combine(Updates.set("answers.$[xxx].reported", report)),
                    new UpdateOptions().arrayFilters(Arrays.asList(eq("xxx.createdDate", createdDate)))
            );
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    public boolean vote(String id, boolean voteType, String idVoter, String owner) throws DAOException {
        // The method will vote the answer (and return true) if and only if idVoter is not already in the voters of the answer
        try (MongoClient mongoClient = getConnection(); ClientSession session = mongoClient.startSession()) {
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);
            int increment = voteType ? 1 : -1;
            String questionId = id.substring(0, id.indexOf('_'));
            Date createdDate = getDateFromString(id.substring(id.indexOf('_') + 1));

            TransactionBody txnBody = (TransactionBody<Boolean>) () -> {
                MongoCollection<Document> collectionQuestions = database.getCollection("questions");
                MongoCollection<Document> collectionUsers = database.getCollection("users");
                UpdateResult updateResult = collectionQuestions.updateOne(
                        session,
                        eq("_id", new ObjectId(questionId)),
                        Updates.combine(
                                Updates.inc("answers.$[xxx].score", increment),
                                Updates.push("answers.$[xxx].voters", idVoter)
                        ),
                        new UpdateOptions().arrayFilters(Arrays.asList(and(eq("xxx.createdDate", createdDate),
                                ne("xxx.voters", idVoter))))
                );
                if(updateResult.getModifiedCount() == 1){
                    collectionUsers.updateOne(
                            session,
                            eq("nickname", owner),
                            Updates.inc("score", increment)
                    );
                }
                return (updateResult.getModifiedCount() == 1);
            };
            return (Boolean) session.withTransaction(txnBody, txnOptions);
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    public String accept(String id, boolean accepted) throws DAOException {
        // The method will accept the answer (and return the questionId) if and only if there isn't already an accepted answer
        // If there is already an accepted answer the method will return null
        try (MongoClient myClient = getConnection()) {
            MongoDatabase database = myClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestion = database.getCollection("questions");
            String questionId = id.substring(0, id.indexOf('_'));
            Date createdDate = getDateFromString(id.substring(id.indexOf('_') + 1));
            UpdateResult updateResult = collectionQuestion.updateOne(
                    and(eq("_id", new ObjectId(questionId)), ne("answers.accepted", accepted)),
                    Updates.combine(Updates.set("answers.$[xxx].accepted", accepted)),
                    new UpdateOptions().arrayFilters(Arrays.asList(eq("xxx.createdDate", createdDate)))
            );
            return (updateResult.getModifiedCount() == 1) ? questionId : null;
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    public PageDTO<AnswerDTO> getReportedAnswers(int page) throws DAOException {
        try (MongoClient myClient = getConnection()) {
            MongoDatabase database = myClient.getDatabase(DB_NAME);
            PageDTO<AnswerDTO> pageDTO = new PageDTO<>();
            List<AnswerDTO> reportedAnswers = new ArrayList<>();
            MongoCollection<Document> collectionQuestions = database.getCollection("questions");
            int pageOffset = (page - 1) * Constants.PAGE_SIZE;
            Bson filter = eq("answers.reported", true);
            Bson project = fields(include("answers"), Projections.elemMatch("answers", eq("reported", true)));
            collectionQuestions.find(filter).projection(project).skip(pageOffset).limit(Constants.PAGE_SIZE).forEach(doc -> {
                for (Document ans : doc.getList("answers", Document.class)) {
                    reportedAnswers.add(new AnswerDTO(doc.getObjectId("_id").toString() + "_" + ans.getDate("createdDate").toInstant(),
                            "",
                            ans.getDate("createdDate"),
                            ans.getString("author"),
                            ans.getInteger("score")));
                }
            });
            pageDTO.setCounter(reportedAnswers.size());
            pageDTO.setEntries(reportedAnswers);
            return pageDTO;
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    public PageDTO<AnswerDTO> getAnswersPage(int page, String questionId) throws DAOException {
        PageDTO<AnswerDTO> answersPage = new PageDTO<>();
        List<AnswerDTO> answers = new ArrayList<>();
        try (MongoClient myClient = getConnection()) {
            MongoDatabase database = myClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestions = database.getCollection("questions");
            Bson match = match(Filters.eq("_id", new ObjectId(questionId)));
            Bson unwind = unwind("$answers");
            Bson project = project(fields(exclude("title","body","createdDate","author","topic"))); // exclude "question fields" and leave only _id and "answer fields"
            Bson skip = Aggregates.skip((page - 1) * Constants.PAGE_SIZE);
            Bson limit = Aggregates.limit(Constants.PAGE_SIZE);
            collectionQuestions.aggregate(Arrays.asList(match, unwind, project, skip, limit)).forEach(doc -> {
                if(doc.containsKey("answers")){
                    Document ansDoc = doc.get("answers", Document.class);
                    boolean accepted = false;
                    if(ansDoc.containsKey("accepted"))
                        accepted = ansDoc.getBoolean("accepted");
                    List<String> voters = new ArrayList<>();
                    if(ansDoc.containsKey("voters")){
                        voters = new ArrayList<>(ansDoc.getList("voters", String.class));
                    }
                    AnswerDTO answer = new AnswerDTO(doc.getObjectId("_id").toString() + "_" + ansDoc.getDate("createdDate").toInstant(),
                            ansDoc.getString("body"),
                            ansDoc.getDate("createdDate"),
                            ansDoc.getString("author"),
                            ansDoc.getInteger("score"),
                            voters,
                            accepted);
                    answers.add(answer);
                }
            });
            answersPage.setEntries(answers);
            answersPage.setCounter(answers.size());
            return answersPage;
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    private Date getDateFromString(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        return dateFormat.parse(date);
    }
}
