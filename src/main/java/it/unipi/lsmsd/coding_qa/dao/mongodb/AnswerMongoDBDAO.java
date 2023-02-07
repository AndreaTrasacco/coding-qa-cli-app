package it.unipi.lsmsd.coding_qa.dao.mongodb;

import com.mongodb.client.*;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import it.unipi.lsmsd.coding_qa.dao.AnswerDAO;
import it.unipi.lsmsd.coding_qa.dao.base.BaseMongoDBDAO;
import it.unipi.lsmsd.coding_qa.dao.exception.DAOException;
import it.unipi.lsmsd.coding_qa.dto.AnswerDTO;
import it.unipi.lsmsd.coding_qa.dto.PageDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionPageDTO;
import it.unipi.lsmsd.coding_qa.model.Answer;
import it.unipi.lsmsd.coding_qa.model.Question;
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
import java.util.concurrent.atomic.AtomicBoolean;

public class AnswerMongoDBDAO extends BaseMongoDBDAO implements AnswerDAO {
    public void create(String questionId, Answer answer) throws DAOException{
        try(MongoClient myClient = getConnection()) {
            MongoDatabase database = myClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestion = database.getCollection("questions");
            Document answerDoc = new Document();
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            String formattedDate = dateFormat.format(date);
            answerDoc.put("body", answer.getBody());
            answerDoc.put("createdDate", formattedDate); // messo string, dovevo mettere date?
            answerDoc.put("author", answer.getAuthor());
            collectionQuestion.updateOne(Filters.eq("_id", new ObjectId(questionId)),
                            Updates.push("answers", answerDoc));

            answer.setId(questionId+formattedDate);

        } catch(Exception e){
            throw new DAOException(e);
        }


        //AGGIUNGERE ID ALLA RISPOSTA CON QUESTION ID _ CREATED_DATE
    }
    public Answer update(Answer answer) throws DAOException{
        try(MongoClient myClient = getConnection()) {
            MongoDatabase database = myClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestion = database.getCollection("questions");

            // questionId_answerIndex
            String questionId = answer.getId().substring(0, answer.getId().indexOf('-'));
            String answerIndex = answer.getId().substring(answer.getId().indexOf('-'));

            collectionQuestion.updateOne(Filters.eq("_id", new ObjectId(questionId)),
                    Updates.combine(Updates.set("answers" + answerIndex + "body", answer.getBody()),
                            Updates.set("answers" + answerIndex + "createdDate", answer.getCreatedDate()),
                            Updates.set("answers" + answerIndex + "author", answer.getAuthor()),
                            Updates.set("answers" + answerIndex + "score", answer.getScore()),
                            Updates.set("answers" + answerIndex + "voters", answer.getVoters()),
                            Updates.set("answers" + answerIndex + "accepted", answer.isAccepted()),
                            Updates.set("answers" + answerIndex + "reported", answer.isReported())));
            return answer;
        } catch(Exception e){
            throw new DAOException(e);
        }
    }

    public void getBody(AnswerDTO answer) throws DAOException{
        try (MongoClient myClient = getConnection()){
            MongoDatabase database = myClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestion = database.getCollection("questions");

            Document answerDoc = findAnswer(answer.getId());

            // Answer has been deleted in the meanwhile
            if (answerDoc == null) {
                throw new DAOException("this answer no longer exists");
            }

            String body = answerDoc.getString("body");
            answer.setBody(body);
            int score = answerDoc.getInteger("score");
            answer.setScore(score);
            boolean accepted = answerDoc.getBoolean("accepted");
            answer.setAccepted(accepted);
        } catch(Exception e){
            throw new DAOException(e);
        }
    }
    public void delete(String id) throws DAOException{
        try (MongoClient myClient = getConnection()){
            MongoDatabase database = myClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestion = database.getCollection("questions");

            // questionId_answerIndex
            String questionId = id.substring(0, id.indexOf('_'));
            String answerIndex = id.substring(id.indexOf('_'));

            collectionQuestion.updateOne(
                    Filters.eq("_id", new ObjectId(questionId)),
                    Updates.unset("answers." + answerIndex)
            );
        } catch(Exception e){
            throw new DAOException(e);
        }
    }
    public void report(String id) throws DAOException{
        try (MongoClient myClient = getConnection()){
            MongoDatabase database = myClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestion = database.getCollection("questions");
            // The answer id has the following format: questionId_answerIndex
            String questionId = id.substring(0, id.indexOf('-'));
            String answerIndex = id.substring(id.indexOf('-'));
            collectionQuestion.updateOne(
                    Filters.eq("_id", new ObjectId(questionId)),
                    Updates.set("answers." + answerIndex + ".reported", true)
            );
        } catch(Exception e){
            throw new DAOException(e);
        }
    }

    // FARE CONTROLLO VOTERS
    public boolean vote(String id, boolean voteType, String idVoter) throws DAOException{
        try (MongoClient myClient = getConnection()){
            MongoDatabase database = myClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestion = database.getCollection("questions");
            int increment = voteType ? 1 : -1;

            String questionId = id.substring(0, id.indexOf('_'));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            Date createdDate = null;
            try {
                createdDate = dateFormat.parse(id.substring(id.indexOf('_')));
            } catch(ParseException e){
                System.out.println("Errore nel parsing");
            }
            int answerDocIndex = findAnswerIndex(questionId, createdDate);

            // ottimizzare, fa due volte la query?
            Document answerDoc = findAnswer(id);
            List<String> voters = (List<String>) answerDoc.get("voters");
            for(String idVot : voters){
                // user has already voted
                if(idVoter == idVot){
                    return false;
                }
            }

            collectionQuestion.updateOne(
                    Filters.eq("_id", new ObjectId(questionId)),
                    Updates.inc("answers." + answerDocIndex + ".score", increment)
            );
        } catch(Exception e){
            throw new DAOException(e);
        }

        return true;
    }

    public void accept(String id) throws DAOException{
        try(MongoClient myClient = getConnection()) {
            MongoDatabase database = myClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestion = database.getCollection("questions");
            // questionId_createdDate
            String questionId = id.substring(0, id.indexOf('_'));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            Date createdDate = null;
            try {
                createdDate = dateFormat.parse(id.substring(id.indexOf('_')));
            } catch(ParseException e){
                System.out.println("Errore nel parsing");
            }

            int answerIndex = findAnswerIndex(questionId, createdDate);

            collectionQuestion.updateOne(
                    Filters.eq("_id", new ObjectId(questionId)),
                    Updates.set("answers." + answerIndex + ".accepted", true)
            );
        } catch(Exception e){
            throw new DAOException(e);
        }
    }

    public PageDTO<QuestionDTO> getReportedQuestions(int page) throws DAOException{
        try(MongoClient myClient = getConnection()){
            MongoDatabase database = myClient.getDatabase(DB_NAME);
            PageDTO<QuestionDTO> pageDTO = new PageDTO<>();
            List<QuestionDTO> reportedQuestions = new ArrayList<>();
            MongoCollection<Document> collectionQuestions = database.getCollection("questions");

            int pageOffset = (page - 1) * Constants.PAGE_SIZE;

            collectionQuestions.find(Filters.eq("reported", true)).skip(pageOffset).limit(Constants.PAGE_SIZE).forEach(doc -> {
                QuestionDTO temp = new QuestionDTO(doc.getObjectId("_id").toHexString(), doc.getString("title"),
                        doc.getDate("createdDate"), doc.getString("topic"), doc.getString("author"));
                reportedQuestions.add(temp);
            });

            pageDTO.setCounter(reportedQuestions.size());
            pageDTO.setEntries(reportedQuestions);
            return pageDTO;


        } catch(Exception e){
            throw new DAOException(e);
        }
    }
    public PageDTO<AnswerDTO> getReportedAnswers(int page) throws DAOException{
        try(MongoClient myClient = getConnection()){
            MongoDatabase database = myClient.getDatabase(DB_NAME);
            PageDTO<AnswerDTO> pageDTO = new PageDTO<>();
            List<AnswerDTO> reportedAnswers = new ArrayList<>();
            MongoCollection<Document> collectionQuestions = database.getCollection("questions");

            int pageOffset = (page - 1) * Constants.PAGE_SIZE;

            Bson filter = Filters.eq("ansewers.reported", true);
            List<Document> result = collectionQuestions.find(filter).skip(pageOffset).limit(Constants.PAGE_SIZE).into(new ArrayList<>());

            for (Document document : result) {
                reportedAnswers.add(new AnswerDTO(document.getString("_id"), document.getString("body"),
                        document.getDate("createdDate"), document.getString("author"),
                        document.getInteger("score"), document.getBoolean("accepted")));
            }

            pageDTO.setCounter(reportedAnswers.size());
            pageDTO.setEntries(reportedAnswers);
            return pageDTO;


        } catch(Exception e){
            throw new DAOException(e);
        }
    }


    /*public List<QuestionsAndAnswersReportedDTO> getReportedQuestionsAndAnswers(){
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
    }*/

    private Document findAnswer(String answerId) throws DAOException{
        String questionId = answerId.substring(0, answerId.indexOf('_'));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Date createdDate = null;
        try {
            createdDate = dateFormat.parse(answerId.substring(answerId.indexOf('_')));
        } catch(ParseException e){
            System.out.println("Errore nel parsing");
        }

        try (MongoClient myClient = getConnection()){
            MongoDatabase database = myClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestion = database.getCollection("questions");
            Document question = collectionQuestion.find(Filters.eq("_id", questionId)).first();
            List<Document> answers = (List<Document>) question.get("answers");
            for(Document answer : answers){
                Date date = answer.getDate("createdDate");
                if(date.compareTo(createdDate) == 0){
                    return answer;
                }
            }
        } catch(Exception e){
            throw new DAOException(e);
        }

        return null;
    }

    private int findAnswerIndex(String questionId, Date createdDate) throws DAOException{

        try (MongoClient myClient = getConnection()){
            MongoDatabase database = myClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestion = database.getCollection("questions");
            Document question = collectionQuestion.find(Filters.eq("_id", questionId)).first();
            List<Document> answers = (List<Document>) question.get("answers");
            for(Document answer : answers){
                Date date = answer.getDate("createdDate");
                if(date.compareTo(createdDate) == 0){
                    return answers.indexOf(answer);
                }
            }
        } catch(Exception e){
            throw new DAOException(e);
        }

        return -1;
    }


    /*public PageDTO<AnswerDTO> getAnswersPage(int page, String id) throws DAOException{
        try(MongoClient myClient = getConnection()) {
            MongoDatabase database = myClient.getDatabase(DB_NAME);
            MongoCollection collectionQuestions = database.getCollection("questions");
            Bson match = Aggregates.match(Filters.eq("_id", new ObjectId(id)));
            Bson unwind = Aggregates.unwind("answers");
            Bson sort = Aggregates.sort(Sorts.descending("answers.createdDate"));
            Bson skip = Aggregates.skip((page-1) * Constants.PAGE_SIZE);
            Bson limit = Aggregates.limit(Constants.PAGE_SIZE);
            List<Answer> answers = new ArrayList<>();
            QuestionPageDTO questionPageDTO = new QuestionPageDTO();
            questionPageDTO.setId(id);
            collectionQuestions.aggregate(Arrays.asList(match, unwind, sort, limit)).forEach(doc -> {

                questionPageDTO.setTitle(doc.getString("title"));
                questionPageDTO.setBody(doc.getString("body"));
                questionPageDTO.setAuthor(doc.getString("author"));
                questionPageDTO.setTopic(doc.getString("topic"));
                questionPageDTO.setCreatedDate(doc.getDate("createdDate"));


                // TODO TESTARE BENE
                Answer answer = new Answer(doc.getObjectId("_id").toString() + doc.getDate("createdDate"),
                        doc.getString("answer.body"), doc.getDate("createdDate"), doc.getString("author"), doc.getInteger("score"),
                        doc.getList("answers.voters", String.class), doc.getBoolean("accepted"), doc.getBoolean("reported"));

                answers.add(answer);
            });

            PageDTO<Answer> answersPage = new PageDTO<>();
            answersPage.setEntries(answers);
            answersPage.setCounter(answers.size());
        } catch(Exception e){
            throw new DAOException(e);
        }
    }*/

}
