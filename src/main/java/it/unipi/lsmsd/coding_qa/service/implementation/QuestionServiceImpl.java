package it.unipi.lsmsd.coding_qa.service.implementation;

import it.unipi.lsmsd.coding_qa.dao.AnswerDAO;
import it.unipi.lsmsd.coding_qa.dao.DAOLocator;
import it.unipi.lsmsd.coding_qa.dao.QuestionDAO;
import it.unipi.lsmsd.coding_qa.dao.QuestionNodeDAO;
import it.unipi.lsmsd.coding_qa.dao.enums.DAORepositoryEnum;
import it.unipi.lsmsd.coding_qa.dto.PageDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionNodeDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionsAndAnswersReportedDTO;
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
    public void createQuestion(Question question) throws BusinessException { // TODO ROLLBACK
        try {
            questionDAO.createQuestion(question);
            questionNodeDAO.create(question);
        } catch(Exception e){
            throw new BusinessException(e);
        }
    }

    @Override
    public void addAnswer(Answer answer) throws BusinessException {
        try {
            answerDAO.create(answer);
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
            answerDAO.delete(answer);
            // TODO CONTROLLARE SE L'UTENTE HA ALTRE RISPOSTE SUL GRAFO
            questionNodeDAO.deleteAnsweredEdge(answer.getParentQuestionId(), answer.getAuthor());
        } catch (Exception e){
            throw new BusinessException(e);
        }
    }

    @Override
    public void voteAnswer(Answer answer, boolean voteType) throws BusinessException {
        try {
            answerDAO.vote(answer, voteType);
        } catch (Exception e){
            throw new BusinessException(e);
        }
    }

    @Override
    public void reportQuestion(Question question) throws BusinessException {
        try {
            questionDAO.reportQuestion(question);
        } catch (Exception e){
            throw new BusinessException(e);
        }
    }

    @Override
    public void reportAnswer(Answer answer) throws BusinessException {
        try {
            answerDAO.report(answer);
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
    public void acceptAnswer(Answer answer) throws BusinessException {
        try {
            answerDAO.accept(answer);
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
