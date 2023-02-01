package it.unipi.lsmsd.coding_qa.dao.neo4j;

import it.unipi.lsmsd.coding_qa.dao.QuestionNodeDAO;
import it.unipi.lsmsd.coding_qa.dao.base.BaseNeo4JDAO;
import it.unipi.lsmsd.coding_qa.model.Question;
import org.neo4j.driver.Session;

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
}
