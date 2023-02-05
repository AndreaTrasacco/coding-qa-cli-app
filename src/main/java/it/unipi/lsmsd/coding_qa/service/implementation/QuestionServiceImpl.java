package it.unipi.lsmsd.coding_qa.service.implementation;

import it.unipi.lsmsd.coding_qa.dao.*;
import it.unipi.lsmsd.coding_qa.dao.enums.DAORepositoryEnum;
import it.unipi.lsmsd.coding_qa.dto.PageDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionDTO;
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
    public void addAnswer(String questionId, Answer answer) throws BusinessException {
        try {
            answerDAO.create(questionId, answer);
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
    public void updateAnswer(Answer answer) throws BusinessException {
        try {
            answerDAO.update(answer);
        } catch(Exception e){
            throw new BusinessException(e);
        }
    }

    @Override
    public void deleteQuestion(Question question) throws BusinessException {
        try {
            questionDAO.deleteQuestion(question.getId());
            questionNodeDAO.deleteIngoingEdges(question);
            questionNodeDAO.delete(question);
        } catch(Exception e){
            throw new BusinessException(e);
        }
    }

    @Override
    public void deleteAnswer(Answer answer) throws BusinessException {
        try {
            answerDAO.delete(answer.getId()); // TODO METODO POTREBBE RESTITUIRE ID DOMANDA?? PER ESSERE USATO NEL GRAFO. E SCORE??
            // TODO SCALARE ANCHE SCORE DELL'UTENTE
            // TODO CONTROLLARE SE L'UTENTE HA ALTRE RISPOSTE SU QUELLA DOMANDA
            questionNodeDAO.deleteAnsweredEdge(answer.getParentQuestionId(), answer.getAuthor());
        } catch (Exception e){
            throw new BusinessException(e);
        }
    }

    @Override
    public void voteAnswer(String answerId, boolean voteType) throws BusinessException {
        try {
            answerDAO.vote(answerId, voteType);
        } catch (Exception e){
            throw new BusinessException(e);
        }
    }

    @Override
    public void reportQuestion(String questionId) throws BusinessException {
        try {
            questionDAO.reportQuestion(questionId);
        } catch (Exception e){
            throw new BusinessException(e);
        }
    }

    @Override
    public void reportAnswer(String answerId) throws BusinessException {
        try {
            answerDAO.report(answerId);
        } catch (Exception e){
            throw new BusinessException(e);
        }
    }

    @Override
    public List<QuestionsAndAnswersReportedDTO> getReportedQuestionsAndAnswers() throws BusinessException {
        try {
            return answerDAO.getReportedQuestionsAndAnswers();
        } catch (Exception e){
            throw new BusinessException(e);
        }
    }

    @Override
    public void acceptAnswer(String answerId) throws BusinessException {
        try {
            answerDAO.accept(answerId);
            questionNodeDAO.close(new Question(answer.getParentQuestionId()));
        } catch (Exception e){
            throw new BusinessException(e);
        }
    }

    @Override
    public Question getQuestionInfo(String id) throws BusinessException {
        try {
            return questionDAO.getQuestionInfo(id);
        } catch (Exception e){
            throw new BusinessException(e);
        }
    }

    @Override
    public PageDTO<QuestionDTO> getQuestionPageByTitle(int page, String searchString) throws BusinessException {
        try {
            return questionDAO.getQuestionPageByTitle(page, searchString);
        } catch (Exception e){
            throw new BusinessException(e);
        }
    }

    @Override
    public PageDTO<QuestionDTO> getQuestionPageByTopic(int page, String topic) throws BusinessException {
        try {
            return questionDAO.getQuestionPageByTopic(page, topic);
        } catch (Exception e){
            throw new BusinessException(e);
        }
    }

    @Override
    public List<QuestionNodeDTO> getCreatedAndAnsweredQuestions(User user) throws BusinessException {
        try {
            return questionNodeDAO.viewCreatedAndAnsweredQuestions(user);
        } catch (Exception e){
            throw new BusinessException(e);
        }
    }
}
