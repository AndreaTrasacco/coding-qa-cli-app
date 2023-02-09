package it.unipi.lsmsd.coding_qa.service.implementation;

import it.unipi.lsmsd.coding_qa.dao.*;
import it.unipi.lsmsd.coding_qa.dao.enums.DAORepositoryEnum;
import it.unipi.lsmsd.coding_qa.dao.exception.DAOException;
import it.unipi.lsmsd.coding_qa.dao.exception.DAONodeException;
import it.unipi.lsmsd.coding_qa.dto.AnswerDTO;
import it.unipi.lsmsd.coding_qa.dto.AnswerScoreDTO;
import it.unipi.lsmsd.coding_qa.dto.PageDTO;
import it.unipi.lsmsd.coding_qa.model.Answer;
import it.unipi.lsmsd.coding_qa.service.AnswerService;
import it.unipi.lsmsd.coding_qa.service.exception.BusinessException;

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
            Answer answer = new Answer(answerDTO.getId(), answerDTO.getBody(), answerDTO.getCreatedDate(),
                    answerDTO.getAuthor(), answerDTO.getScore(), answerDTO.getVoters(), answerDTO.isAccepted(), false);
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
            //questionNodeDAO.deleteAnsweredEdge(answer.getParentQuestionId(), answer.getAuthor());
        } catch (DAONodeException e) {
            String questionId = answerDTO.getId().substring(0, answerDTO.getId().indexOf('_'));
            Answer answer = new Answer(answerDTO.getId(), answerDTO.getBody(), answerDTO.getCreatedDate(),
                    answerDTO.getAuthor(), answerDTO.getScore(), answerDTO.getVoters(), answerDTO.isAccepted(), false);
            try {
                answerDAO.create(questionId, answer);
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
    public PageDTO<AnswerDTO> getReportedAnswers() throws BusinessException {
        try {
            return null;
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    @Override
    public boolean acceptAnswer(String answerId) throws BusinessException {
        boolean success = false;
        try {
            String questionId = answerId.substring(0, answerId.indexOf('_'));
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
}
