package it.unipi.lsmsd.coding_qa.dao.neo4j;

import it.unipi.lsmsd.coding_qa.dao.QuestionNodeDAO;
import it.unipi.lsmsd.coding_qa.dao.base.BaseNeo4JDAO;
import it.unipi.lsmsd.coding_qa.dto.QuestionNodeDTO;
import it.unipi.lsmsd.coding_qa.model.Question;
import it.unipi.lsmsd.coding_qa.model.User;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

public class QuestionNeo4JDAO extends BaseNeo4JDAO implements QuestionNodeDAO {
    public void create(Question question){
        String insertQuestion = "CREATE (q: Question{id: $id, title : $title, topic : $topic, closed : $closed, createdDate : $createdDate}";
        try(Session session = getSession()){
            session.writeTransaction(tx -> {
                tx.run(insertQuestion, parameters("id", question.getId(), "title", question.getTitle(),
                        "topic", question.getTopic(), "closed", question.getClosed(), "createdDate", question.getCreatedDate())).consume();
                return 1;
            });
        }
    }
    public void update(Question question){
        String updateQuestion = "MATCH (q:Question)" +
                "WHERE q.id = $id" +
                "SET q.title = $title, q.topic = $topic, q.closed = $closed, q.createdDate = $createdDate}";
        try(Session session = getSession()){
            session.writeTransaction(tx -> {
                tx.run(updateQuestion, parameters("id", question.getId(), "title", question.getTitle(),
                        "topic", question.getTopic(), "closed", question.getClosed(), "createdDate", question.getCreatedDate())).consume();
                return 1;
            });
        }
    }
    public void delete(Question question){
        String deleteQuestion = "MATCH (q:Question) WHERE q.id = id DELETE q)";
        try(Session session = getSession()){
            session.writeTransaction(tx -> {
                tx.run(deleteQuestion, parameters("id", question.getId())).consume();
                return 1;
            });
        }
    }

    public void close(Question question){
        String closeQuestion = "MATCH (q:Question)" +
                "WHERE q.id = $id" +
                "SET q.closed = true";
        try(Session session = getSession()){
            session.writeTransaction(tx -> {
                tx.run(closeQuestion, parameters("id", question.getId())).consume();
                return 1;
            });
        }
    }

    public List<QuestionNodeDTO> viewCreatedAndAnsweredQuestions(User user){
        String createdQuestionsQuery = "MATCH (u:User {nickname: $nickname})" +
                "MATCH (u)-[:CREATED]->(createdQuestion:Question)" +
                "RETURN createdQuestion";

        String answeredQuestionsQuery = "MATCH (u:User {nickname: $nickname})" +
                "OPTIONAL MATCH (u)-[:ANSWERED]->(answeredQuestion:Question)" +
                "RETURN answeredQuestion";

        try(Session session = getSession()){
            List<QuestionNodeDTO> targetQuestions = session.readTransaction(tx -> {
                Result resultCreated = tx.run(createdQuestionsQuery, parameters("nickname", user.getNickname()));
                List<QuestionNodeDTO> questions = new ArrayList<>();
                while(resultCreated.hasNext()){
                    Record r = resultCreated.next();
                    QuestionNodeDTO q = new QuestionNodeDTO(r.get("title").asString(), true);
                    questions.add(q);
                }

                Result resultAnswered = tx.run(answeredQuestionsQuery, parameters("nickname", user.getNickname()));
                while(resultAnswered.hasNext()){
                    Record r = resultAnswered.next();
                    QuestionNodeDTO q = new QuestionNodeDTO(r.get("title").asString(), false);
                    questions.add(q);
                }

                return questions;
            });
            return targetQuestions;
        }

    }
}
