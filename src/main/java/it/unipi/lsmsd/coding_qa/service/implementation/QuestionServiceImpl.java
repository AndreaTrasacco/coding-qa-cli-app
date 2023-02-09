package it.unipi.lsmsd.coding_qa.service.implementation;

import it.unipi.lsmsd.coding_qa.dao.*;
import it.unipi.lsmsd.coding_qa.dao.enums.DAORepositoryEnum;
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
            //answerDAO.updateBody();
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
    public void deleteAnswer(String answerId) throws BusinessException {
        try {
            answerDAO.delete(answerId); // TODO METODO POTREBBE RESTITUIRE ID DOMANDA?? PER ESSERE USATO NEL GRAFO. E SCORE??
            // TODO SCALARE ANCHE SCORE DELL'UTENTE
            // TODO CONTROLLARE SE L'UTENTE HA ALTRE RISPOSTE SU QUELLA DOMANDA
            //questionNodeDAO.deleteAnsweredEdge(answer.getParentQuestionId(), answer.getAuthor());
        } catch (Exception e){
            throw new BusinessException(e);
        }
    }

    @Override
    public void voteAnswer(String answerId, boolean voteType) throws BusinessException {
        try {
            //answerDAO.vote(answerId, voteType);
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
    public void reportAnswer(String answerId) throws BusinessException { // TODO "UNREPORT" (ANCHE QUI AGGIUNGENDO PARAM)
        try {
            answerDAO.report(answerId, true);
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
    public void acceptAnswer(String questionId, String answerId) throws BusinessException {
        try {/*
            answerDAO.accept(answerId);
            questionNodeDAO.close(new Question(answer.getParentQuestionId()));*/
        } catch (Exception e){
            throw new BusinessException(e);
        }
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
