package it.unipi.lsmsd.coding_qa.service;

import it.unipi.lsmsd.coding_qa.dto.AnswerDTO;
import it.unipi.lsmsd.coding_qa.dto.PageDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionPageDTO;
import it.unipi.lsmsd.coding_qa.model.Answer;
import it.unipi.lsmsd.coding_qa.model.Question;
import it.unipi.lsmsd.coding_qa.model.User;
import it.unipi.lsmsd.coding_qa.service.exception.BusinessException;

import java.util.List;

public interface QuestionService {
    void createQuestion(QuestionPageDTO questionPageDTO) throws BusinessException;

    void updateQuestion(QuestionPageDTO questionPageDTO) throws BusinessException;

    void deleteQuestion(String questionId) throws BusinessException;

    void addAnswer(String questionId, AnswerDTO answerDTO) throws BusinessException;

    void updateAnswer(String answerId, String body) throws BusinessException;

    void deleteAnswer(AnswerDTO answerDTO) throws BusinessException; // TODO aggiornamento score

    boolean voteAnswer(String answerId, boolean voteType, String userId) throws BusinessException; // true: upvote, false: downvote

    void reportQuestion(String questionId, boolean report) throws BusinessException;

    PageDTO<QuestionDTO> getReportedQuestions(int page) throws BusinessException;

    void reportAnswer(String answerId, boolean report) throws BusinessException;

    PageDTO<AnswerDTO> getReportedAnswers() throws BusinessException;

    boolean acceptAnswer(String answerId) throws BusinessException;

    QuestionPageDTO getQuestionInfo(String id) throws BusinessException;

    PageDTO<QuestionDTO> searchQuestions(int page, String searchString, String topicFilter) throws BusinessException;

    PageDTO<QuestionDTO> viewCreatedQuestions(String nickname, int page) throws BusinessException;

    PageDTO<QuestionDTO> viewAnsweredQuestions(String nickname, int page) throws BusinessException;
}
