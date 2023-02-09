package it.unipi.lsmsd.coding_qa.service;

import it.unipi.lsmsd.coding_qa.dto.AnswerDTO;
import it.unipi.lsmsd.coding_qa.dto.PageDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionPageDTO;
import it.unipi.lsmsd.coding_qa.service.exception.BusinessException;

public interface AnswerService { // TODO fare test
    void addAnswer(String questionId, AnswerDTO answerDTO) throws BusinessException;

    void updateAnswer(String answerId, String body) throws BusinessException;

    void deleteAnswer(AnswerDTO answerDTO) throws BusinessException; // TODO aggiornamento score

    boolean voteAnswer(String answerId, boolean voteType, String userId) throws BusinessException; // true: upvote, false: downvote

    void reportAnswer(String answerId, boolean report) throws BusinessException;

    PageDTO<AnswerDTO> getReportedAnswers() throws BusinessException;

    boolean acceptAnswer(String answerId) throws BusinessException;
}
