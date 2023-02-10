package it.unipi.lsmsd.coding_qa.service.implementation;

import it.unipi.lsmsd.coding_qa.dao.*;
import it.unipi.lsmsd.coding_qa.dao.enums.DAORepositoryEnum;
import it.unipi.lsmsd.coding_qa.dao.exception.DAOException;
import it.unipi.lsmsd.coding_qa.dao.exception.DAONodeException;
import it.unipi.lsmsd.coding_qa.dto.AnswerDTO;
import it.unipi.lsmsd.coding_qa.dto.AnswerScoreDTO;
import it.unipi.lsmsd.coding_qa.dto.PageDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionPageDTO;
import it.unipi.lsmsd.coding_qa.model.Answer;
import it.unipi.lsmsd.coding_qa.service.AnswerService;
import it.unipi.lsmsd.coding_qa.service.exception.BusinessException;

import java.util.Date;

public class AnswerServiceImpl implements AnswerService {
    private AnswerDAO answerDAO;
    private QuestionNodeDAO questionNodeDAO;
    private UserDAO userDAO;

    public AnswerServiceImpl() {
        this.answerDAO = DAOLocator.getAnswerDAO(DAORepositoryEnum.MONGODB);
        this.questionNodeDAO = DAOLocator.getQuestionNodeDAO(DAORepositoryEnum.NEO4J);
        userDAO = DAOLocator.getUserDAO(DAORepositoryEnum.MONGODB);
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
                throw new BusinessException(ex);
            }
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    @Override
    public void updateAnswer(String answerId, String body) throws BusinessException {
        try {
            answerDAO.updateBody(answerId, body);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    @Override
    public void deleteAnswer(AnswerDTO answerDTO) throws BusinessException {
        try {
            AnswerScoreDTO score = answerDAO.delete(answerDTO.getId());
            userDAO.updateScore(score.getAuthor(), score.getScore());
            questionNodeDAO.deleteAnswer(answerDTO.getId(), answerDTO.getAuthor());
            // TODO METODO POTREBBE RESTITUIRE ID DOMANDA?? PER ESSERE USATO NEL GRAFO. E SCORE??
            // TODO SCALARE ANCHE SCORE DELL'UTENTE
            // TODO CONTROLLARE SE L'UTENTE HA ALTRE RISPOSTE SU QUELLA DOMANDA
        } catch (DAONodeException e) {
            String questionId = answerDTO.getId().substring(0, answerDTO.getId().indexOf('_'));
            Answer answer = new Answer(answerDTO.getId(), answerDTO.getBody(), answerDTO.getCreatedDate(),
                    answerDTO.getAuthor(), answerDTO.getScore(), answerDTO.getVoters(), answerDTO.isAccepted(), false);
            try {
                answerDAO.create(questionId, answer); // TODO NON RICREA VOTERS ECC... PROBLEMA
            } catch (DAOException ex) {
                throw new BusinessException(ex);
            }
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    @Override
    public boolean voteAnswer(String answerId, boolean voteType, String userId) throws BusinessException {
        try {
            return answerDAO.vote(answerId, voteType, userId);
            // TODO VA MODIFICATO LO SCORE DELL'UTENTE!! -> FARE IN UNA TRANSACTION DENTRO VOTE (PRIMA TESTARE TRANSACTION IN QUESTION)
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    @Override
    public void reportAnswer(String answerId, boolean report) throws BusinessException {
        try {
            answerDAO.report(answerId, report);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    @Override
    public PageDTO<AnswerDTO> getReportedAnswers(int page) throws BusinessException {
        try {
            return answerDAO.getReportedAnswers(page);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    @Override
    public boolean acceptAnswer(String answerId) throws BusinessException {
        boolean success = false;
        try {
            String questionId = answerId.substring(0, answerId.indexOf('_')); // TODO QUESTA COSA DIPENDE DA MONGO - MODIFICARE (FARE RITORNARE DA ACCEPT LA QUESTION ID E SE NULL E' COME SE FOSSE SUCCESS = FALSE)
            success = answerDAO.accept(answerId, true);
            questionNodeDAO.updateClose(questionId, true);
        } catch (DAONodeException e) {
            try {
                answerDAO.accept(answerId, false);
            } catch (DAOException ex) {
                throw new BusinessException(ex);
            }
        } catch (Exception e) {
            throw new BusinessException(e);
        }
        return success;
    }

    @Override
    public PageDTO<AnswerDTO> getAnswersPage(int page, String questionId) throws BusinessException {
        try {
            return answerDAO.getAnswersPage(page, questionId);
        } catch (Exception e) {
            throw new BusinessException(e);
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
            answerDTO.setBody(answerDTO.getBody());
            answerDTO.setScore(answer.getScore());
            answerDTO.setAccepted(answer.isAccepted());
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }
}
