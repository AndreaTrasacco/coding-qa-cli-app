package it.unipi.lsmsd.coding_qa.service.implementation;

import it.unipi.lsmsd.coding_qa.dao.*;
import it.unipi.lsmsd.coding_qa.dao.enums.DAORepositoryEnum;
import it.unipi.lsmsd.coding_qa.dao.exception.DAOException;
import it.unipi.lsmsd.coding_qa.dao.exception.DAONodeException;
import it.unipi.lsmsd.coding_qa.dto.AnswerDTO;
import it.unipi.lsmsd.coding_qa.dto.PageDTO;
import it.unipi.lsmsd.coding_qa.dto.VoteDTO;
import it.unipi.lsmsd.coding_qa.model.Answer;
import it.unipi.lsmsd.coding_qa.service.AnswerService;
import it.unipi.lsmsd.coding_qa.service.exception.BusinessException;

import java.util.Date;

public class AnswerServiceImpl implements AnswerService {
    private AnswerDAO answerDAO;
    private QuestionNodeDAO questionNodeDAO;

    public AnswerServiceImpl() {
        this.answerDAO = DAOLocator.getAnswerDAO(DAORepositoryEnum.MONGODB);
        this.questionNodeDAO = DAOLocator.getQuestionNodeDAO(DAORepositoryEnum.NEO4J);
    }

    @Override
    public void addAnswer(String questionId, AnswerDTO answerDTO) throws BusinessException {
        try {
            Answer answer = new Answer();
            answer.setBody(answerDTO.getBody());
            answer.setAuthor(answerDTO.getAuthor());
            answer.setCreatedDate(new Date(System.currentTimeMillis()));
            answer.setScore(0);
            answerDAO.create(questionId, answer);
            questionNodeDAO.createAnswer(answer);
        } catch (DAONodeException e) {
            try {
                answerDAO.delete(answerDTO.getId());
            } catch (DAOException ex) {
                throw new BusinessException("Error in adding the answer");
            }
        } catch (Exception e) {
            throw new BusinessException("Error in adding the answer");
        }
    }

    @Override
    public void updateAnswer(String answerId, String body) throws BusinessException {
        try {
            answerDAO.updateBody(answerId, body);
        } catch (Exception e) {
            throw new BusinessException("Error in updating the answer");
        }
    }

    @Override
    public void deleteAnswer(AnswerDTO answerDTO) throws BusinessException {
        String questionId = null;
        try {
            questionId = answerDAO.delete(answerDTO.getId());
            if(questionId != null)
                questionNodeDAO.deleteAnswer(questionId, answerDTO.getAuthor());
        } catch (
                DAONodeException ex) { // If there is an error in deletion of Answer with "question node" -> retry deletion
            try {
                if (questionId != null)
                    questionNodeDAO.deleteAnswer(questionId, answerDTO.getAuthor());
            } catch (Exception e) {
                throw new BusinessException("Error in deleting the answer");
            }
        } catch (Exception ex) {
            throw new BusinessException("Error in deleting the answer");
        }
    }

    @Override
    public boolean voteAnswer(VoteDTO voteDTO) throws BusinessException {
        try {
            return answerDAO.vote(voteDTO.getAnswerId(), voteDTO.getVoteType(), voteDTO.getVoterId(), voteDTO.getAnswerOwner());
        } catch (Exception e) {
            throw new BusinessException("Error in voting the answer");
        }
    }

    @Override
    public void reportAnswer(String answerId, boolean report) throws BusinessException {
        try {
            answerDAO.report(answerId, report);
        } catch (Exception e) {
            throw new BusinessException("Error in reporting the answer");
        }
    }

    @Override
    public PageDTO<AnswerDTO> getReportedAnswers(int page) throws BusinessException {
        try {
            return answerDAO.getReportedAnswers(page);
        } catch (Exception e) {
            throw new BusinessException("Error in getting the reported answers");
        }
    }

    @Override
    public boolean acceptAnswer(String answerId) throws BusinessException {
        try {
            String questionId = answerDAO.accept(answerId, true);
            if (questionId != null)
                questionNodeDAO.updateClose(questionId, true);
            return (questionId != null);
        } catch (DAONodeException e) {
            try {
                answerDAO.accept(answerId, false);
                return false;
            } catch (DAOException ex) {
                throw new BusinessException("Error in accepting the answer");
            }
        } catch (Exception e) {
            throw new BusinessException("Error in accepting the answer");
        }
    }

    @Override
    public PageDTO<AnswerDTO> getAnswersPage(int page, String questionId) throws BusinessException {
        try {
            return answerDAO.getAnswersPage(page, questionId);
        } catch (Exception e) {
            throw new BusinessException("Error in getting the answers page");
        }
    }

    @Override
    public void getCompleteAnswer(AnswerDTO answerDTO) throws BusinessException {
        try {
            Answer answer = new Answer();
            answer.setId(answerDTO.getId());
            answer.setAuthor(answerDTO.getAuthor());
            answer.setCreatedDate(answerDTO.getCreatedDate());
            answer.setScore(0);
            answer.setAccepted(false);
            answerDAO.getCompleteAnswer(answer);
            answerDTO.setBody(answer.getBody());
            answerDTO.setScore(answer.getScore());
            answerDTO.setAccepted(answer.isAccepted());
        } catch (Exception e) {
            throw new BusinessException("Error in getting the answer");
        }
    }
}
