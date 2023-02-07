package it.unipi.lsmsd.coding_qa.dao.neo4j;

import it.unipi.lsmsd.coding_qa.dao.QuestionNodeDAO;
import it.unipi.lsmsd.coding_qa.dao.base.BaseNeo4JDAO;
import it.unipi.lsmsd.coding_qa.dao.exception.DAONodeException;
import it.unipi.lsmsd.coding_qa.dto.PageDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionDTO;
import it.unipi.lsmsd.coding_qa.model.Question;
import it.unipi.lsmsd.coding_qa.utils.Constants;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

public class QuestionNeo4JDAO extends BaseNeo4JDAO implements QuestionNodeDAO {
    public void create(Question question) throws DAONodeException {
        try (Session session = getSession()) {
            session.writeTransaction(tx -> {
                String insertQuestion = "CREATE (q: Question{id: $id, title : $title, topic : $topic, closed : false, createdDate : $createdDate})";
                tx.run(insertQuestion, parameters("id", question.getId(), "title", question.getTitle(),
                        "topic", question.getTopic(), "createdDate", question.getCreatedDate())).consume();
                return null;
            });
        } catch (Exception ex) {
            throw new DAONodeException(ex);
        }
    }

    public void update(Question question) throws DAONodeException {
        try (Session session = getSession()) {
            session.writeTransaction(tx -> {
                String updateQuestion = "MATCH (q:Question)" +
                        "WHERE q.id = $id" +
                        "SET q.title = $title, q.topic = $topic";
                tx.run(updateQuestion, parameters("id", question.getId(), "title", question.getTitle(),
                        "topic", question.getTopic())).consume();
                return null;
            });
        } catch (Exception ex) {
            throw new DAONodeException(ex);
        }
    }

    public void delete(String id) throws DAONodeException {
        try (Session session = getSession()) {
            session.writeTransaction(tx -> {
                String deleteQuestion = "MATCH (q:Question) WHERE q.id = $id DETACH DELETE q";
                tx.run(deleteQuestion, parameters("id", id)).consume();
                return null;
            });
        } catch (Exception ex) {
            throw new DAONodeException(ex);
        }
    }

    public void updateClose(String questionId, boolean type) throws DAONodeException {
        String closeQuestion = "MATCH (q:Question)" +
                "WHERE q.id = $id" +
                "SET q.closed = $type";
        try (Session session = getSession()) {
            session.writeTransaction(tx -> {
                tx.run(closeQuestion, parameters("id", questionId, "type", type)).consume();
                return 1;
            });
        } catch (Exception ex) {
            throw new DAONodeException(ex);
        }
    }

    private PageDTO<QuestionDTO> getQuestions(String nickname, int page, String edge) throws DAONodeException {
        int offset = (page - 1) * Constants.PAGE_SIZE;
        try (Session session = getSession()) {
            PageDTO<QuestionDTO> targetQuestions = session.readTransaction(tx -> {
                String query = "MATCH (u:User {nickname: $nickname})-[:" + edge + "]->(q:Question)" +
                        "RETURN q.id AS id, q.title AS title, q.createdDate AS cDate, q.topic AS topic, q.author AS author" +
                        "ORDER BY cDate DESC" +
                        "SKIP $offset" +
                        "LIMIT $limit";
                Result result = tx.run(query, parameters("nickname", nickname, "offset", offset, "limit", Constants.PAGE_SIZE));
                List<QuestionDTO> entries = new ArrayList<>();
                while (result.hasNext()) {
                    Record r = result.next();
                    entries.add(new QuestionDTO(r.get("id").asString(), r.get("title").asString(), Date.from(r.get("cDate").asLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()), r.get("topic").asString(), r.get("author").asString()));
                }
                PageDTO<QuestionDTO> questions = new PageDTO<>();
                questions.setEntries(entries);
                questions.setCounter(entries.size());
                return questions;
            });
            return targetQuestions;
        } catch (Exception ex) {
            throw new DAONodeException(ex);
        }
    }

    public PageDTO<QuestionDTO> viewCreatedQuestions(String nickname, int page) throws DAONodeException {
        return getQuestions(nickname, page, "CREATED");
    }

    public PageDTO<QuestionDTO> viewAnsweredQuestions(String nickname, int page) throws DAONodeException {
        return getQuestions(nickname, page, "ANSWERED");
    }
}
