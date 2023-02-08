package it.unipi.lsmsd.coding_qa.dao.mongodb;

import com.mongodb.client.*;
import com.mongodb.client.model.*;
import it.unipi.lsmsd.coding_qa.dao.AnswerDAO;
import it.unipi.lsmsd.coding_qa.dao.base.BaseMongoDBDAO;
import it.unipi.lsmsd.coding_qa.dao.exception.DAOException;
import it.unipi.lsmsd.coding_qa.dto.*;
import it.unipi.lsmsd.coding_qa.dto.aggregations.QuestionScoreDTO;
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
    public static void main(String[] args) {
        AnswerMongoDBDAO answerDAO = new AnswerMongoDBDAO();
        try {
            //answerDAO.create("63e14ed126dc67afbbdb0a58", new Answer("1", "a", new Date(), "a", 1, new ArrayList<>(), false, false));
            //AnswerDTO answerDTO = new AnswerDTO("63e14ed126dc67afbbdb0a58_2023-02-08T16:19:28.705+00:00", null, null, "a", 1 , false);
            //System.out.println("prima: "+answerDTO.getBody());
            //answerDAO.getBody(answerDTO);
            //System.out.println("dopo: "+answerDTO.getBody());
            //answerDAO.delete("63e14ed126dc67afbbdb0a58_2023-02-07T18:26:25.307+00:00");
            //answerDAO.report("63e14ed126dc67afbbdb0a58_2023-02-07T18:37:30.975+00:00");
            //answerDAO.vote("63e14ed126dc67afbbdb0a58_2023-02-07T18:37:30.975+00:00", true, "1");
            //answerDAO.vote("63e14ed126dc67afbbdb0a58_2023-02-08T16:19:28.705+00:00", true, "1");
            //answerDAO.accept("63e14ed126dc67afbbdb0a58_2023-02-08T16:19:28.705+00:00");
            //PageDTO<QuestionDTO> page = answerDAO.getReportedQuestions(1);
            //System.out.println("reported questions: "+page.getEntries());
            //PageDTO<AnswerDTO> page = answerDAO.getReportedAnswers(1);
            //System.out.println("reported answers: "+page.getEntries());
            //System.out.println("reported answers: "+page.getEntries().get(0).getBody());
            //System.out.println("reported answers: "+page.getEntries().get(0).getId());
            PageDTO<AnswerDTO> page = answerDAO.getAnswersPage(1, "63d171b409f8b5fdd264791d");
            System.out.println("answers: "+page.getEntries());
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    // OK
    public void create(String questionId, Answer answer) throws DAOException{
        try(MongoClient myClient = getConnection()) {
            MongoDatabase database = myClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestion = database.getCollection("questions");
            Document answerDoc = new Document();
            //Date date = new Date();
            Date date = new Date(System.currentTimeMillis());
            //date.toInstant();
            //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ");
            //String formattedDate = dateFormat.format(date);
            answerDoc.put("body", answer.getBody());
            answerDoc.put("createdDate", date); // messo string, dovevo mettere date?
            answerDoc.put("author", answer.getAuthor());
            answerDoc.put("score", 0);
            answerDoc.put("accepted", false);
            collectionQuestion.updateOne(Filters.eq("_id", new ObjectId(questionId)),
                            Updates.push("answers", answerDoc));

            answer.setId(questionId+date);

        } catch(Exception e){
            throw new DAOException(e);
        }

    }

    // OK
    public Answer update(Answer answer) throws DAOException{
        try(MongoClient myClient = getConnection()) {
            MongoDatabase database = myClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestion = database.getCollection("questions");

            // questionId_answerIndex
            String questionId = answer.getId().substring(0, answer.getId().indexOf('_'));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ");
            Date createdDate = null;
            try {
                createdDate = dateFormat.parse(answer.getId().substring(answer.getId().indexOf('_')+1));
            } catch(ParseException e){
                System.out.println("Errore nel parsing1"+e.getMessage());
            }

            int answerIndex = findAnswerIndex(questionId, createdDate);
            System.out.println("indice: "+answerIndex);

            collectionQuestion.updateOne(Filters.eq("_id", new ObjectId(questionId)),
                    Updates.combine(Updates.set("answers." + answerIndex + ".body", answer.getBody()),
                            Updates.set("answers." + answerIndex + ".author", answer.getAuthor()),
                            Updates.set("answers." + answerIndex + ".score", answer.getScore()),
                            Updates.set("answers." + answerIndex + ".voters", answer.getVoters()),
                            Updates.set("answers." + answerIndex + ".accepted", answer.isAccepted()),
                            Updates.set("answers." + answerIndex + ".reported", answer.isReported())));
            return answer;
        } catch(Exception e){
            throw new DAOException(e);
        }
    }

    // OK
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
            int score = answerDoc.getInteger("score");;
            answer.setScore(score);
            boolean accepted = answerDoc.getBoolean("accepted");
            answer.setAccepted(accepted);
        } catch(Exception e){
            throw new DAOException(e);
        }
    }

    // OK TODO (se cancella l'unica risposta rimane array answers), va bene unset? usare pull?
    public AnswerScoreDTO delete(String id) throws DAOException{
        try (MongoClient myClient = getConnection()){
            MongoDatabase database = myClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestion = database.getCollection("questions");

            // questionId_createdDate
            String questionId = id.substring(0, id.indexOf('_'));
            //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            Date createdDate = null;
            try {
                createdDate = dateFormat.parse(id.substring(id.indexOf('_')+1));
            } catch(ParseException e){
                System.out.println("Errore nel parsing1"+e.getMessage());
            }

            int answerIndex = findAnswerIndex(questionId, createdDate);


            collectionQuestion.updateOne(
                    Filters.eq("_id", new ObjectId(questionId)),
                    Updates.unset("answers." + answerIndex)
            );
        } catch(Exception e){
            throw new DAOException(e);
        }
    }

    // OK
    public void report(String id) throws DAOException{
        try (MongoClient myClient = getConnection()){
            MongoDatabase database = myClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestion = database.getCollection("questions");

            String questionId = id.substring(0, id.indexOf('_'));
            //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            Date createdDate = null;
            try {
                createdDate = dateFormat.parse(id.substring(id.indexOf('_')+1));
            } catch(ParseException e){
                System.out.println("Errore nel parsing");
            }

            int answerIndex = findAnswerIndex(questionId, createdDate);
            System.out.println("index: "+answerIndex);

            collectionQuestion.updateOne(
                    Filters.eq("_id", new ObjectId(questionId)),
                    Updates.set("answers." + answerIndex + ".reported", true)
            );
        } catch(Exception e){
            throw new DAOException(e);
        }
    }


    // OK
    public boolean vote(String id, boolean voteType, String idVoter) throws DAOException{
        try (MongoClient myClient = getConnection()){
            MongoDatabase database = myClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestion = database.getCollection("questions");
            int increment = voteType ? 1 : -1;

            String questionId = id.substring(0, id.indexOf('_'));
            //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            Date createdDate = null;
            try {
                createdDate = dateFormat.parse(id.substring(id.indexOf('_')+1));
            } catch(ParseException e){
                System.out.println("Errore nel parsing");
            }
            int answerDocIndex = findAnswerIndex(questionId, createdDate);

            // TODO fare ottimizzazione
            // ottimizzare, fa due volte la query?
            //ottimizzare facendo --> List<String> voters = collectionQuestion.find("_id", new ObjectId(questionId)). getList(answers.voters);
            Document answerDoc = findAnswer(id);
            List<String> voters = (List<String>) answerDoc.get("voters");

            if(voters != null) {
                for (String idVot : voters) {
                    // user has already voted
                    if (idVoter == idVot) {
                        return false;
                    }
                }
            }

            collectionQuestion.updateOne(
                    Filters.eq("_id", new ObjectId(questionId)),
                    Updates.combine(Updates.inc("answers." + answerDocIndex + ".score", increment),
                    Updates.addToSet("answers."+answerDocIndex+".voters", idVoter))
            );
        } catch(Exception e){
            throw new DAOException(e);
        }

        return true;
    }

    // OK
    public void accept(String id) throws DAOException{
        try(MongoClient myClient = getConnection()) {
            MongoDatabase database = myClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestion = database.getCollection("questions");
            // questionId_createdDate
            String questionId = id.substring(0, id.indexOf('_'));
            //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            Date createdDate = null;
            try {
                createdDate = dateFormat.parse(id.substring(id.indexOf('_')+1));
            } catch(ParseException e){
                System.out.println("Errore nel parsing");
            }

            int answerIndex = findAnswerIndex(questionId, createdDate);

            collectionQuestion.updateOne(
                    Filters.eq("_id", new ObjectId(questionId)),
                    Updates.set("answers." + answerIndex + ".a-ccepted", true)
            );
        } catch(Exception e){
            throw new DAOException(e);
        }
    }

    // OK
    public PageDTO<QuestionDTO> getReportedQuestions(int page) throws DAOException{
        try(MongoClient myClient = getConnection()){
            MongoDatabase database = myClient.getDatabase(DB_NAME);
            PageDTO<QuestionDTO> pageDTO = new PageDTO<>();
            List<QuestionDTO> reportedQuestions = new ArrayList<>();
            MongoCollection<Document> collectionQuestions = database.getCollection("questions");

            int pageOffset = (page - 1) * Constants.PAGE_SIZE;

            Bson project = Projections.include("_id", "title", "createdDate", "topic", "author");
            collectionQuestions.find(Filters.eq("reported", true)).projection(project).skip(pageOffset).limit(Constants.PAGE_SIZE).forEach(doc -> {
                QuestionDTO temp = new QuestionDTO(doc.getObjectId("_id").toString(), doc.getString("title"),
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

    // OK
    public PageDTO<AnswerDTO> getReportedAnswers(int page) throws DAOException{
        try(MongoClient myClient = getConnection()){
            MongoDatabase database = myClient.getDatabase(DB_NAME);
            PageDTO<AnswerDTO> pageDTO = new PageDTO<>();
            List<AnswerDTO> reportedAnswers = new ArrayList<>();
            MongoCollection<Document> collectionQuestions = database.getCollection("questions");

            int pageOffset = (page - 1) * Constants.PAGE_SIZE;

            Bson filter = Filters.eq("answers.reported", true);
            Bson project = Projections.include("_id", "answers.body", "answers.createdDate", "answers.author", "answers.score");
            List<Document> result = collectionQuestions.find(filter).projection(project).skip(pageOffset).limit(Constants.PAGE_SIZE).into(new ArrayList<>());

            System.out.println("result: "+result);

            for (Document document : result) {
                for(Document ans : document.getList("answers", Document.class)){
                    reportedAnswers.add(new AnswerDTO(document.getObjectId("_id").toString()+"_"+ans.getDate("createdDate").toInstant(), ans.getString("body"),
                            ans.getDate("createdDate"), ans.getString("author"),
                            ans.getInteger("score"), false));
                }

            }

            System.out.println("result: "+reportedAnswers);

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
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date createdDate = null;
        try {
            createdDate = dateFormat.parse(answerId.substring(answerId.indexOf('_')+1));
        } catch(ParseException e){
            System.out.println("Errore nel parsing");
        }

        try (MongoClient myClient = getConnection()){
            MongoDatabase database = myClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestion = database.getCollection("questions");
            Document question = collectionQuestion.find(Filters.eq("_id", new ObjectId(questionId))).first();
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
            Document question = collectionQuestion.find(Filters.eq("_id", new ObjectId(questionId))).first();
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


    public PageDTO<AnswerDTO> getAnswersPage(int page, String id) throws DAOException{
        try(MongoClient myClient = getConnection()) {
            MongoDatabase database = myClient.getDatabase(DB_NAME);
            MongoCollection collectionQuestions = database.getCollection("questions");
            Bson match = Aggregates.match(Filters.eq("_id", new ObjectId(id)));
            Bson unwind = Aggregates.unwind("answers");
            Bson sort = Aggregates.sort(Sorts.descending("answers.createdDate"));
            Bson skip = Aggregates.skip((page-1) * Constants.PAGE_SIZE);
            Bson limit = Aggregates.limit(Constants.PAGE_SIZE);
            List<AnswerDTO> answers = new ArrayList<>();
            QuestionPageDTO questionPageDTO = new QuestionPageDTO();
            questionPageDTO.setId(id);


            AggregateIterable<Document> mongoList = collectionQuestions.aggregate(Arrays.asList(match, unwind, sort, limit, skip));
            for(Document doc : mongoList){
                questionPageDTO.setTitle(doc.getString("title"));
                questionPageDTO.setBody(doc.getString("body"));
                questionPageDTO.setAuthor(doc.getString("author"));
                questionPageDTO.setTopic(doc.getString("topic"));
                questionPageDTO.setCreatedDate(doc.getDate("createdDate"));

                AnswerDTO answer = new AnswerDTO(doc.getObjectId("_id").toString() + "_" + doc.getDate("createdDate").toInstant(),
                        doc.getString("answer.body"), doc.getDate("createdDate"), doc.getString("author"), doc.getInteger("score")
                        , doc.getBoolean("accepted"));

                answers.add(answer);
            }

            /*try(MongoCursor<Document> result = mongoList.iterator()) {
                while (result.hasNext()) {
                    Document doc = result.next();

                }
            }*/

            /*collectionQuestions.aggregate(Arrays.asList(match, unwind, sort, limit)).forEach(doc -> {

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
            });*/

            PageDTO<AnswerDTO> answersPage = new PageDTO<>();
            answersPage.setEntries(answers);
            answersPage.setCounter(answers.size());

            return answersPage;
        } catch(Exception e){
            throw new DAOException(e);
        }
    }

}
