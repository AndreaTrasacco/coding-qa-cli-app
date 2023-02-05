package it.unipi.lsmsd.coding_qa.service;

import it.unipi.lsmsd.coding_qa.dto.PageDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionDTO;
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
    void createQuestion(Question question) throws BusinessException;
    void addAnswer(String questionId, Answer answer) throws BusinessException;
    void updateQuestion(Question question) throws BusinessException;
    void updateAnswer(Answer answer) throws BusinessException;
    void deleteQuestion(Question question) throws BusinessException;
    void deleteAnswer(String answerId) throws BusinessException;
    void voteAnswer(String answerId, boolean voteType) throws BusinessException; // true: upvote, false: downvote
    void reportQuestion(String questionId) throws BusinessException;
    void reportAnswer(String answerId) throws BusinessException;
    List<QuestionsAndAnswersReportedDTO> getReportedQuestionsAndAnswers() throws BusinessException;
    void acceptAnswer(String answerId) throws BusinessException;
    Question getQuestionInfo(String id) throws BusinessException;
    PageDTO<QuestionDTO> getQuestionPageByTitle(int page, String searchString) throws BusinessException;
    PageDTO<QuestionDTO> getQuestionPageByTopic(int page, String topic) throws BusinessException;
    List<QuestionNodeDTO> getCreatedAndAnsweredQuestions(User user) throws BusinessException;
}
