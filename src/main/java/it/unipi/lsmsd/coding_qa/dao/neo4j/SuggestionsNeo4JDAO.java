package it.unipi.lsmsd.coding_qa.dao.neo4j;

import it.unipi.lsmsd.coding_qa.dao.SuggestionsDAO;
import it.unipi.lsmsd.coding_qa.dao.base.BaseMongoDBDAO;
import it.unipi.lsmsd.coding_qa.dao.base.BaseNeo4JDAO;
import it.unipi.lsmsd.coding_qa.dao.exception.DAONodeException;
import it.unipi.lsmsd.coding_qa.dto.PageDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionDTO;
import it.unipi.lsmsd.coding_qa.model.Answer;
import it.unipi.lsmsd.coding_qa.model.Question;
import it.unipi.lsmsd.coding_qa.model.User;
import it.unipi.lsmsd.coding_qa.utils.Constants;
import org.neo4j.driver.*;
import org.neo4j.driver.summary.ResultSummary;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

public class SuggestionsNeo4JDAO extends BaseNeo4JDAO implements SuggestionsDAO {
    // method for suggesting questions the user might be interested in
    public PageDTO<QuestionDTO> questionsToRead(int page, String nickname) throws DAONodeException {
        String suggestionQuery = "MATCH (startUserQuestion:Question) <- [:CREATED]-(startUser:User{ nickname : $nickname})" +
                "WHERE startUserQuestion.closed = false" +
                "WITH DISTINCT(startUserQuestion.topic) AS topics, startUser" +
                "MATCH p = (startUser)-[*1..2]->(followed:User)-[:CREATED]->(q2:Question)" +
                "WHERE q2.closed = true AND q2.topic IN topics" +
                "RETURN DISTINCT q2, followed, length(p) as depth" +
                "ORDER BY depth DESC" +
                "SKIP $toSkip" +
                "LIMIT $toLimit ";

        return retrieveQuestions(suggestionQuery, nickname, page);
    }

    // method for suggesting questions that the user might be able to answer
    public PageDTO<QuestionDTO> questionsToAnswer(int page, String nickname) throws DAONodeException {

        PageDTO<Question> pageDTO = new PageDTO<>();
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        String suggestionQuery = "MATCH (u1:User{nickname : $nickname})-[:ANSWERED]->(q1)<-[:CREATED]-(followed:User)-[:CREATED]->(q2:Question{closed: false})<-[a:ANSWERED]-()" +
                "RETURN q2, followed, COUNT(a) AS ans_count" +
                "ORDER BY ans_count" +
                "SKIP $toSkip" +
                "LIMIT $toLimit ";

        return retrieveQuestions(suggestionQuery, nickname, page);
    }

    private PageDTO<QuestionDTO> retrieveQuestions(String suggestionQuery, String nickname, int page) throws DAONodeException {
        PageDTO<QuestionDTO> pageDTO = new PageDTO<>();
        List<QuestionDTO> questionDTOList;
        int totalCount = 0;
        int pageOffset = (page - 1) * Constants.PAGE_SIZE;

        try(Session session = getSession()){
            questionDTOList = session.readTransaction( (TransactionWork<List<QuestionDTO>>) tx -> {
                Result result = tx.run(suggestionQuery, parameters("nickname", nickname, "toSkip", pageOffset, "toLimit", Constants.PAGE_SIZE));
                ArrayList<QuestionDTO> questions = new ArrayList<>();
                while(result.hasNext()) {
                    Record question = result.next();
                    String stringCreatedDate = question.get("createdDate").asString();
                    Date createdDate;
                    try {
                        createdDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(stringCreatedDate);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    QuestionDTO temp = new QuestionDTO(question.get("q2.id").asString(), question.get("q2.title").asString(),
                            createdDate, question.get("q2.topic").asString(),
                            question.get("q2.closed").asBoolean(), question.get("followed.nickname").asString());
                    questions.add(temp);
                }
                return questions;
            });
        } catch (Exception e){
            throw new DAONodeException(e);
        }

        pageDTO.setCounter(questionDTOList.size());
        pageDTO.setEntries(questionDTOList);
        return pageDTO;
    }
}
