package it.unipi.lsmsd.coding_qa.service;

import it.unipi.lsmsd.coding_qa.dto.PageDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionNodeDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionsAndAnswersReportedDTO;
import it.unipi.lsmsd.coding_qa.model.Answer;
import it.unipi.lsmsd.coding_qa.model.Question;
import it.unipi.lsmsd.coding_qa.model.User;
import it.unipi.lsmsd.coding_qa.service.exception.BusinessException;

import java.util.List;

public interface QuestionService {
    // create
    // addAnswer
    // update ... question
    // update ... answer
    // removeAnswer
    // voteAnswer (anche unvote)
    // reportQuestion
    // reportAnswer
    // removeQuestion
    // getQuestionInfo
    // searchQuestion type
    // searchQuestionByTopic type
    // getReportedQuestions

    public void createQuestion(Question question) throws BusinessException;

    public void addAnswer(Answer answer) throws BusinessException;

    public void updateQuestion(Question question) throws BusinessException;

    public void updateAnswer(Answer answer) throws BusinessException;

    public void deleteQuestion(Question question) throws BusinessException;

    public void deleteAnswer(Answer answer) throws BusinessException;

    public void voteAnswer(Answer answer, boolean voteType) throws BusinessException; // true: upvote, false: downvote

    public void reportQuestion(Question question) throws BusinessException;

    public void reportAnswer(Answer answer) throws BusinessException;

    public List<QuestionsAndAnswersReportedDTO> getReportedQuestionsAndAnswers() throws BusinessException;

    public void acceptAnswer(Answer answer) throws BusinessException;

    public Question getQuestionInfo(String id) throws BusinessException;

    public PageDTO<QuestionDTO> getQuestionPageByTitle(int page, String searchString) throws BusinessException;

    public PageDTO<QuestionDTO> getQuestionPageByTopic(int page, String topic) throws BusinessException;

    public List<QuestionNodeDTO> getCreatedAndAnsweredQuestions(User user) throws BusinessException;
}
