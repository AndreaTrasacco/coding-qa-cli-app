package it.unipi.lsmsd.coding_qa.dao;

import it.unipi.lsmsd.coding_qa.dto.PageDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionDTO;
import it.unipi.lsmsd.coding_qa.model.Question;

import java.util.List;

public interface QuestionDAO {
    public void createQuestion(Question question);

    public void deleteQuestion(Question question);

    public void updateQuestion(Question question);


    public void reportQuestion(Question question);

    public List<Question> getReportedQuestions();

    public PageDTO<QuestionDTO> getQuestionPageByTitle(int page, String searchString);

    public PageDTO<QuestionDTO> getQuestionPageByTopic(int page, String topic);
}
