package it.unipi.lsmsd.coding_qa.service.implementation;

import it.unipi.lsmsd.coding_qa.dao.*;
import it.unipi.lsmsd.coding_qa.dao.enums.DAORepositoryEnum;
import it.unipi.lsmsd.coding_qa.dao.exception.DAOException;
import it.unipi.lsmsd.coding_qa.dao.exception.DAONodeException;
import it.unipi.lsmsd.coding_qa.dto.*;
import it.unipi.lsmsd.coding_qa.model.Answer;
import it.unipi.lsmsd.coding_qa.model.Question;
import it.unipi.lsmsd.coding_qa.model.User;
import it.unipi.lsmsd.coding_qa.service.QuestionService;
import it.unipi.lsmsd.coding_qa.service.exception.BusinessException;

import java.util.List;

public class QuestionServiceImpl implements QuestionService {

    private QuestionDAO questionDAO;
    private AnswerDAO answerDAO;
    private QuestionNodeDAO questionNodeDAO;
    private UserDAO userDAO;

    public QuestionServiceImpl(){
        this.questionDAO = DAOLocator.getQuestionDAO(DAORepositoryEnum.MONGODB);
        this.answerDAO = DAOLocator.getAnswerDAO(DAORepositoryEnum.MONGODB);
        this.questionNodeDAO = DAOLocator.getQuestionNodeDAO(DAORepositoryEnum.NEO4J);
    }
    @Override
    public void createQuestion(Question question) throws BusinessException { // TODO ROLLBACK, PASSARE QUESTIONDTO (FORSE?)
        try {
            questionDAO.createQuestion(question);
            questionNodeDAO.create(question);
        } catch(Exception e){
            throw new BusinessException(e);
        }
    }

    @Override
    public void addAnswer(String questionId, AnswerDTO answerDTO) throws BusinessException {
        try {
            Answer answer = new Answer(answerDTO.getId(), answerDTO.getBody(), answerDTO.getCreatedDate(),
                    answerDTO.getAuthor(), answerDTO.getScore(), answerDTO.getVoters(), answerDTO.isAccepted(), false);
            answerDAO.create(questionId, answer);
            questionNodeDAO.createAnswer(answer);
        } catch (DAONodeException e){
            try {
                answerDAO.delete(answerDTO.getId());
            } catch (DAOException ex) {
                throw new BusinessException(ex);
            }
        } catch (Exception e){
            throw new BusinessException(e);
        }
    }

    @Override
    public void updateQuestion(Question question) throws BusinessException {
        try {
            questionDAO.updateQuestion(question);
            questionNodeDAO.update(question);
        } catch(Exception e){
            throw new BusinessException(e);
        }
    }

    @Override
    public void updateAnswer(String answerId, String body) throws BusinessException {
        try {
            answerDAO.updateBody(answerId, body);
        } catch(Exception e){
            throw new BusinessException(e);
        }
    }

    @Override
    public void deleteQuestion(Question question) throws BusinessException {
        try {
            List<AnswerScoreDTO> answers = questionDAO.deleteQuestion(question.getId());// TODO VA MODIFICATO LO SCORE DEGLI UTENTI DELLE RISPOSTE
            // for each answer decrease/increase score of author
            //questionNodeDAO.deleteIngoingEdges(question);
            //questionNodeDAO.delete(question);
        } catch(Exception e){
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
        } catch (DAONodeException e){
            String questionId = answerDTO.getId().substring(0, answerDTO.getId().indexOf('_'));
            Answer answer = new Answer(answerDTO.getId(), answerDTO.getBody(), answerDTO.getCreatedDate(),
                    answerDTO.getAuthor(), answerDTO.getScore(), answerDTO.getVoters(), answerDTO.isAccepted(), false);
            try {
                answerDAO.create(questionId, answer);
            } catch (DAOException ex) {
                throw new BusinessException(ex);
            }
        } catch(Exception e){
            throw new BusinessException(e);
        }
    }

    @Override
    public boolean voteAnswer(String answerId, boolean voteType, String userId) throws BusinessException {
        try {
            return answerDAO.vote(answerId, voteType, userId);
        } catch (Exception e){
            throw new BusinessException(e);
        }
    }

    @Override
    public void reportQuestion(String questionId, boolean report) throws BusinessException {
        try {
            questionDAO.reportQuestion(questionId, report);
        } catch (Exception e){
            throw new BusinessException(e);
        }
    }

    @Override
    public void reportAnswer(String answerId, boolean report) throws BusinessException {
        try {
            answerDAO.report(answerId, report);
        } catch (Exception e){
            throw new BusinessException(e);
        }
    }

    @Override
    public PageDTO<QuestionDTO> getReportedQuestions() throws BusinessException {
        try {
            return questionDAO.getReportedQuestions();
        } catch (Exception e){
            throw new BusinessException(e);
        }
    }

    @Override
    public PageDTO<AnswerDTO> getReportedAnswers() throws BusinessException {
        try {
            return null;
        } catch (Exception e){
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
        } catch (DAONodeException e){
            try {
                answerDAO.accept(answerId, false);
            } catch (DAOException ex) {
                throw new BusinessException(ex);
            }
        } catch (Exception e){
            throw new BusinessException(e);
        }
        return success;
    }

    @Override
    public QuestionPageDTO getQuestionInfo(String id) throws BusinessException {
        try {
            return questionDAO.getQuestionInfo(id);
        } catch (Exception e){
            throw new BusinessException(e);
        }
    }

    @Override
    public PageDTO<QuestionDTO> getQuestionPageByTitle(int page, String searchString, String topicFilter) throws BusinessException {
        try {
            return questionDAO.searchQuestions(page, searchString, topicFilter);
        } catch (Exception e){
            throw new BusinessException(e);
        }
    }

    @Override
    public PageDTO<QuestionDTO> getQuestionPageByTopic(int page, String topic) throws BusinessException {
        try {
            //return questionDAO.getQuestionPageByTopic(page, topic);
            return null;
        } catch (Exception e){
            throw new BusinessException(e);
        }
    }

    /*@Override
    public List<QuestionNodeDTO> getCreatedAndAnsweredQuestions(User user) throws BusinessException {
        try {
            return questionNodeDAO.viewCreatedAndAnsweredQuestions(user);
        } catch (Exception e){
            throw new BusinessException(e);
        }
    }*/
}
