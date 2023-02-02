package it.unipi.lsmsd.coding_qa.dao.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import it.unipi.lsmsd.coding_qa.dao.AnswerDAO;
import it.unipi.lsmsd.coding_qa.dao.base.BaseMongoDBDAO;
import it.unipi.lsmsd.coding_qa.dto.QuestionsAndAnswersReportedDTO;
import it.unipi.lsmsd.coding_qa.model.Answer;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class AnswerMongoDBDAO extends BaseMongoDBDAO implements AnswerDAO {
    // Su mongo db l'id dell'answer deve essere scomposto
    public Answer create(Answer answer){
        MongoDatabase database = getDB();
        MongoCollection<Document> collectionQuestion = database.getCollection("questions");

        // questionId_answerIndex
        String questionId = answer.getId().substring(0, answer.getId().indexOf('-'));
        String answerIndex = answer.getId().substring(answer.getId().indexOf('-'));

        Document docUser = new Document("_id", questionId)
                .append("answers"+answerIndex+"body", answer.getBody())
                .append("answers"+answerIndex+"createdDate", answer.getCreatedDate())
                .append("answers"+answerIndex+"author", answer.getAuthor())
                .append("answers"+answerIndex+"score", answer.getScore())
                .append("answers"+answerIndex+"voters", answer.getVoters())
                .append("answers"+answerIndex+"accepted", answer.isAccepted())
                .append("answers"+answerIndex+"reported", answer.isReported());
        collectionQuestion.insertOne(docUser);

        return answer;
    }
    public Answer update(Answer answer){
        MongoDatabase database = getDB();
        MongoCollection<Document> collectionQuestion = database.getCollection("questions");

        // questionId_answerIndex
        String questionId = answer.getId().substring(0, answer.getId().indexOf('-'));
        String answerIndex = answer.getId().substring(answer.getId().indexOf('-'));

        collectionQuestion.updateOne(Filters.eq("_id", questionId),
                Updates.combine(Updates.set("answers"+answerIndex+"body", answer.getBody()),
                        Updates.set("answers"+answerIndex+"createdDate", answer.getCreatedDate()),
                        Updates.set("answers"+answerIndex+"author", answer.getAuthor()),
                        Updates.set("answers"+answerIndex+"score", answer.getScore()),
                        Updates.set("answers"+answerIndex+"voters", answer.getVoters()),
                        Updates.set("answers"+answerIndex+"accepted", answer.isAccepted()),
                        Updates.set("answers"+answerIndex+"reported", answer.isReported())));
        return answer;
    }
    public void delete(Answer answer){
        MongoDatabase database = getDB();
        MongoCollection<Document> collectionQuestion = database.getCollection("questions");

        // questionId_answerIndex
        String questionId = answer.getId().substring(0, answer.getId().indexOf('-'));
        String answerIndex = answer.getId().substring(answer.getId().indexOf('-'));

        collectionQuestion.updateOne(
                Filters.eq("_id", questionId),
                Updates.unset("answers.<i>")
        );
    }
    public void report(Answer answer){
        MongoDatabase database = getDB();
        MongoCollection<Document> collectionQuestion = database.getCollection("questions");
        // questionId_answerIndex
        String questionId = answer.getId().substring(0, answer.getId().indexOf('-'));
        String answerIndex = answer.getId().substring(answer.getId().indexOf('-'));
        collectionQuestion.updateOne(
                Filters.eq("_id", questionId),
                Updates.set("answers." + answerIndex + ".reported", true)
        );
    }

    public void vote(Answer answer, boolean voteType){
        MongoDatabase database = getDB();
        MongoCollection<Document> collectionQuestion = database.getCollection("questions");
        int increment = voteType? 1 : -1;

        // questionId_answerIndex
        String questionId = answer.getId().substring(0, answer.getId().indexOf('-'));
        String answerIndex = answer.getId().substring(answer.getId().indexOf('-'));
        collectionQuestion.updateOne(
                Filters.eq("_id", questionId),
                Updates.inc("answers." + answerIndex + ".score", increment)
        );

    }

    public void accept(Answer answer){
        MongoDatabase database = getDB();
        MongoCollection<Document> collectionQuestion = database.getCollection("questions");
        // questionId_answerIndex
        String questionId = answer.getId().substring(0, answer.getId().indexOf('-'));
        String answerIndex = answer.getId().substring(answer.getId().indexOf('-'));
        collectionQuestion.updateOne(
                Filters.eq("_id", questionId),
                Updates.set("answers." + answerIndex + ".accepted", true)
        );
    }

    // DECIDERE IN CHE CLASSE METTERE QUESTO METODO (o question o answer)
    public List<QuestionsAndAnswersReportedDTO> getReportedQuestionsAndAnswers(){
        MongoDatabase database = getDB();
        MongoCollection<Document> collectionQuestion = database.getCollection("questions");
        Bson filter = Filters.eq("reported", true);

        List<QuestionsAndAnswersReportedDTO> questionsAndAnswersReported = new ArrayList<>();

        MongoCursor<Document> cursor = collectionQuestion.find(filter).iterator();

        while (cursor.hasNext()) {
            Document question = cursor.next();
            QuestionsAndAnswersReportedDTO q = new QuestionsAndAnswersReportedDTO(question.getString("_id"),
                    question.getString("title"), question.getString("body"), question.getString("author"),
                    question.getDate("createdDate"), 0);
            questionsAndAnswersReported.add(q);

            List<Document> answers = (List<Document>) question.get("answers");
            for (Document answer : answers) {
                if (answer.getBoolean("reported")) {
                    QuestionsAndAnswersReportedDTO a = new QuestionsAndAnswersReportedDTO(question.getString("_id")+answers.indexOf(answer),
                            question.getString("title"), answer.getString("body"), answer.getString("author"),
                            answer.getDate("createdDate"), 0);
                    questionsAndAnswersReported.add(q);
                }
            }
        }

        return questionsAndAnswersReported;
    }

}
