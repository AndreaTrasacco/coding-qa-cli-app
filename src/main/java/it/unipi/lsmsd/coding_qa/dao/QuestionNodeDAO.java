package it.unipi.lsmsd.coding_qa.dao;

import it.unipi.lsmsd.coding_qa.dto.QuestionNodeDTO;
import it.unipi.lsmsd.coding_qa.model.Question;
import it.unipi.lsmsd.coding_qa.model.User;

import java.util.List;

public interface QuestionNodeDAO {
    void create(Question question);
    void update(Question question);
    void delete(Question question);
    void deleteIngoingEdges(Question question);
    void deleteAnsweredEdge(String questionId, String nickname);
    void close(Question question);
    List<QuestionNodeDTO> viewCreatedAndAnsweredQuestions(User user);
    // fare create, update, delete di answer ???

}
