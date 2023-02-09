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

    void reportQuestion(String questionId, boolean report) throws BusinessException;

    PageDTO<QuestionDTO> getReportedQuestions(int page) throws BusinessException;

    QuestionPageDTO getQuestionInfo(String id) throws BusinessException;

    PageDTO<QuestionDTO> searchQuestions(int page, String searchString, String topicFilter) throws BusinessException;

    PageDTO<QuestionDTO> viewCreatedQuestions(String nickname, int page) throws BusinessException;

    PageDTO<QuestionDTO> viewAnsweredQuestions(String nickname, int page) throws BusinessException;
}
