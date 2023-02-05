package it.unipi.lsmsd.coding_qa.dao;

import it.unipi.lsmsd.coding_qa.dto.PageDTO;
import it.unipi.lsmsd.coding_qa.model.Question;

public interface QuestionNodeDAO { // TODO ATTENZIONE USARE DETACH, ECCEZIONI, NON USARE "EDGE" riguarda un graph db
    void create(Question question);
    void update(Question question); // TODO CAPIRE SE SERVE DTO
    void delete(String id);
    //void deleteIngoingEdges(String id);
    void deleteAnsweredEdge(String questionId, String nickname); // CAMBIARE NOME (dipende da graph db)
    void updateClose(String questionId, boolean type);
    PageDTO<QuestionNodeDTO> viewCreatedAndAnsweredQuestions(String nickname); // TODO SEPARARE
    // fare create, update, delete di answer ???

}
