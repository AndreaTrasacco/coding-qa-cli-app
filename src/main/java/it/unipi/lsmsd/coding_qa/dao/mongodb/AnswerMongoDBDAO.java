package it.unipi.lsmsd.coding_qa.dao.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import it.unipi.lsmsd.coding_qa.dao.AnswerDAO;
import it.unipi.lsmsd.coding_qa.dao.base.BaseMongoDBDAO;
import it.unipi.lsmsd.coding_qa.model.Answer;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class AnswerMongoDBDAO extends BaseMongoDBDAO implements AnswerDAO {
    public void create(String questionId, Answer answer){
        MongoDatabase database = getDB();
        MongoCollection<Document> collectionQuestion = database.getCollection("questions");

        // questionId_answerIndex
        String answerIndex = answer.getId().substring(answer.getId().indexOf('_'));

        Document docUser = new Document("_id", new ObjectId(questionId))
                .append("answers"+answerIndex+"body", answer.getBody())
                .append("answers"+answerIndex+"createdDate", answer.getCreatedDate())
                .append("answers"+answerIndex+"author", answer.getAuthor())
                .append("answers"+answerIndex+"score", answer.getScore())
                .append("answers"+answerIndex+"voters", answer.getVoters())
                .append("answers"+answerIndex+"accepted", answer.isAccepted())
                .append("answers"+answerIndex+"reported", answer.isReported());
        collectionQuestion.insertOne(docUser);
        // SERVE FARE UNA UPDATE DELLA QUESTION, NON INSERT

        //AGGIUNGERE ID ALLA RISPOSTA CON QUESTION ID _ POSIZIONE _ CREATED_DATE
    }
    public Answer update(Answer answer){
        MongoDatabase database = getDB();
        MongoCollection<Document> collectionQuestion = database.getCollection("questions");

        // questionId_answerIndex
        String questionId = answer.getId().substring(0, answer.getId().indexOf('-'));
        String answerIndex = answer.getId().substring(answer.getId().indexOf('-'));

        collectionQuestion.updateOne(Filters.eq("_id", new ObjectId(questionId)),
                Updates.combine(Updates.set("answers"+answerIndex+"body", answer.getBody()),
                        Updates.set("answers"+answerIndex+"createdDate", answer.getCreatedDate()),
                        Updates.set("answers"+answerIndex+"author", answer.getAuthor()),
                        Updates.set("answers"+answerIndex+"score", answer.getScore()),
                        Updates.set("answers"+answerIndex+"voters", answer.getVoters()),
                        Updates.set("answers"+answerIndex+"accepted", answer.isAccepted()),
                        Updates.set("answers"+answerIndex+"reported", answer.isReported())));
        return answer;
    }
    public void delete(String id){
        MongoDatabase database = getDB();
        MongoCollection<Document> collectionQuestion = database.getCollection("questions");

        // questionId_answerIndex
        String questionId = id.substring(0, id.indexOf('_'));
        String answerIndex = id.substring(id.indexOf('_'));

        collectionQuestion.updateOne(
                Filters.eq("_id", new ObjectId(questionId)),
                Updates.unset("answers." + answerIndex)
        );
    }
    public void report(String id){
        MongoDatabase database = getDB();
        MongoCollection<Document> collectionQuestion = database.getCollection("questions");
        // The answer id has the following format: questionId_answerIndex
        String questionId = id.substring(0, id.indexOf('-'));
        String answerIndex = id.substring(id.indexOf('-'));
        collectionQuestion.updateOne(
                Filters.eq("_id", new ObjectId(questionId)),
                Updates.set("answers." + answerIndex + ".reported", true)
        );
    }

    public void vote(String id, boolean voteType){
        MongoDatabase database = getDB();
        MongoCollection<Document> collectionQuestion = database.getCollection("questions");
        int increment = voteType? 1 : -1;

        // questionId_answerIndex
        String questionId = id.substring(0, id.indexOf('_'));
        String answerIndex = id.substring(id.indexOf('_'));
        collectionQuestion.updateOne(
                Filters.eq("_id", new ObjectId(questionId)),
                Updates.inc("answers." + answerIndex + ".score", increment)
        );

    }

    public void accept(String id){
        MongoDatabase database = getDB();
        MongoCollection<Document> collectionQuestion = database.getCollection("questions");
        // questionId_answerIndex
        String questionId = id.substring(0, id.indexOf('_'));
        String answerIndex = id.substring(id.indexOf('_'));
        collectionQuestion.updateOne(
                Filters.eq("_id", new ObjectId(questionId)),
                Updates.set("answers." + answerIndex + ".accepted", true)
        );
    }

    public List<QuestionsAndAnswersReportedDTO> getReportedQuestionsAndAnswers(){
        MongoDatabase database = getDB();
        MongoCollection<Document> collectionQuestion = database.getCollection("questions");
        Bson filter = Filters.eq("reported", true);

        List<QuestionsAndAnswersReportedDTO> questionsAndAnswersReported = new ArrayList<>();

        try (MongoCursor<Document> cursor = collectionQuestion.find(filter).iterator()) {

            while (cursor.hasNext()) {
                Document question = cursor.next();
                QuestionsAndAnswersReportedDTO q = new QuestionsAndAnswersReportedDTO(question.getString("_id"),
                        question.getString("title"), question.getString("body"), question.getString("author"),
                        question.getDate("createdDate"), 0);
                questionsAndAnswersReported.add(q);

                List<Document> answers = (ArrayList<Document>) question.get("answers");
                for (Document answer : answers) {
                    if (answer.getBoolean("reported")) {
                        QuestionsAndAnswersReportedDTO a = new QuestionsAndAnswersReportedDTO(question.getString("_id") + answers.indexOf(answer),
                                question.getString("title"), answer.getString("body"), answer.getString("author"),
                                answer.getDate("createdDate"), 0);
                        questionsAndAnswersReported.add(a);
                    }
                }
            }
        }

        return questionsAndAnswersReported;
    }

}
