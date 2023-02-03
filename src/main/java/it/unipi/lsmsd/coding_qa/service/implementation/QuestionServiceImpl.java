package it.unipi.lsmsd.coding_qa.service.implementation;

import it.unipi.lsmsd.coding_qa.dao.AnswerDAO;
import it.unipi.lsmsd.coding_qa.dao.QuestionDAO;
import it.unipi.lsmsd.coding_qa.dao.QuestionNodeDAO;
import it.unipi.lsmsd.coding_qa.dto.PageDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionDTO;
import it.unipi.lsmsd.coding_qa.model.Answer;
import it.unipi.lsmsd.coding_qa.model.Question;
import it.unipi.lsmsd.coding_qa.service.QuestionService;

public class QuestionServiceImpl implements QuestionService {

    private QuestionDAO questionDAO;
    private AnswerDAO answerDAO;
    private QuestionNodeDAO questionNodeDAO;
    @Override
    public void createQuestion(Question question) {

    }

    @Override
    public void addAnswer(Answer answer) {

    }

    @Override
    public void updateQuestion(Question question) {

    }

    @Override
    public void updateAnswer(Answer answer) {

    }

    @Override
    public void deleteQuestion(Question question) {

    }

    @Override
    public void deleteAnswer(Answer answer) {

    }

    @Override
    public void voteAnswer(Answer answer, boolean voteType) {

    }

    @Override
    public void reportQuestion(Question question) {

    }

    @Override
    public void reportAnswer(Answer answer) {

    }

    @Override
    public Question getQuestionInfo(String id) {
        return null;
    }

    @Override
    public PageDTO<QuestionDTO> getQuestionPageByTitle(int page, String searchString) {
        return null;
    }

    @Override
    public PageDTO<QuestionDTO> getQuestionPageByTopic(int page, String topic) {
        return null;
    }
}
