package it.unipi.lsmsd.coding_qa.service.implementation;

import it.unipi.lsmsd.coding_qa.dao.*;
import it.unipi.lsmsd.coding_qa.dao.enums.DAORepositoryEnum;
import it.unipi.lsmsd.coding_qa.dao.exception.DAOException;
import it.unipi.lsmsd.coding_qa.dao.exception.DAONodeException;
import it.unipi.lsmsd.coding_qa.dto.*;
import it.unipi.lsmsd.coding_qa.model.Answer;
import it.unipi.lsmsd.coding_qa.model.Question;
import it.unipi.lsmsd.coding_qa.service.QuestionService;
import it.unipi.lsmsd.coding_qa.service.exception.BusinessException;

import java.util.Date;
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
        userDAO = DAOLocator.getUserDAO(DAORepositoryEnum.MONGODB);
    }

    @Override
    public void createQuestion(QuestionPageDTO questionPageDTO) throws BusinessException {
        String questionId = null;
        try {
            Question question = new Question();
            question.setTitle(questionPageDTO.getTitle());
            question.setBody(questionPageDTO.getBody());
            question.setAuthor(questionPageDTO.getAuthor());
            question.setCreatedDate(new Date(System.currentTimeMillis()));
            question.setTopic(questionPageDTO.getTopic());
            questionDAO.createQuestion(question);
            questionId = question.getId();
            questionNodeDAO.create(question);
        } catch (DAONodeException e) { // Insertion of question node failed --> Rollback deleting the question
            try {
                if (questionId != null) {
                    questionDAO.deleteQuestion(questionId);
                }
            } catch (Exception ex) {
                throw new BusinessException(ex);
            }
        } catch (Exception e) {
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
    public void updateQuestion(QuestionPageDTO questionPageDTO) throws BusinessException {
        Question oldQuestion = null;
        try {
            Question question = new Question();
            question.setTitle(questionPageDTO.getTitle());
            question.setBody(questionPageDTO.getBody());
            question.setTopic(questionPageDTO.getTopic());
            oldQuestion = questionDAO.updateQuestion(question);
            questionNodeDAO.update(question);
        } catch (
                DAONodeException e) { // Update of question node failed --> Rollback updating the question with the old question
            try {
                if (oldQuestion != null) {
                    questionDAO.updateQuestion(oldQuestion);
                }
            } catch (Exception ex) {
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
        } catch(Exception e){
            throw new BusinessException(e);
        }
    }

    @Override
    public void deleteQuestion(String questionId) throws BusinessException { // TODO ROLLBACK
        try {
            List<AnswerScoreDTO> answersScores = questionDAO.deleteQuestion(questionId);// TODO VA MODIFICATO LO SCORE DEGLI UTENTI DELLE RISPOSTE
            for (AnswerScoreDTO answerScoreDTO : answersScores){
                // userDAO.updateScore
            }
            // for each answer decrease/increase score of author
            //questionNodeDAO.deleteIngoingEdges(question);
            //questionNodeDAO.delete(question);
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
        } catch (Exception e) {
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
    public PageDTO<QuestionDTO> getReportedQuestions(int page) throws BusinessException {
        try {
            return questionDAO.getReportedQuestions(page);
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
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    @Override
    public PageDTO<QuestionDTO> searchQuestions(int page, String searchString, String topicFilter) throws BusinessException {
        try {
            return questionDAO.searchQuestions(page, searchString, topicFilter);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    @Override
    public PageDTO<QuestionDTO> viewCreatedQuestions(String nickname, int page) throws BusinessException {
        try {
            return questionNodeDAO.viewCreatedQuestions(nickname, page);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    @Override
    public PageDTO<QuestionDTO> viewAnsweredQuestions(String nickname, int page) throws BusinessException {
        try {
            return questionNodeDAO.viewAnsweredQuestions(nickname, page);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }
}
