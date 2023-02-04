package it.unipi.lsmsd.coding_qa.dao;

import it.unipi.lsmsd.coding_qa.dto.PageDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionDTO;
import it.unipi.lsmsd.coding_qa.model.Question;

import java.util.List;

public interface QuestionDAO {
    public void createQuestion(Question question);

    public void deleteQuestion(String id);

    public void updateQuestion(Question question);

    public void reportQuestion(Question question); // TODO METTERE ARGOMENTO ID

    public Question getQuestionInfo(String id);

    public List<Question> getReportedQuestions(); // Non utilizzata perchè la funzione getReportedQuestionsAndAnswers raccoglie sia domande che risposte

    public PageDTO<QuestionDTO> getQuestionPageByTitle(int page, String searchString); // TODO "Metodo" per body

    public PageDTO<QuestionDTO> getQuestionPageByTopic(int page, String topic);
}
