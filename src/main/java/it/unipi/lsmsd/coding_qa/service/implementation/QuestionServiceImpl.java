package it.unipi.lsmsd.coding_qa.service.implementation;

import it.unipi.lsmsd.coding_qa.dao.*;
import it.unipi.lsmsd.coding_qa.dao.enums.DAORepositoryEnum;
import it.unipi.lsmsd.coding_qa.dao.exception.DAONodeException;
import it.unipi.lsmsd.coding_qa.dto.*;
import it.unipi.lsmsd.coding_qa.model.Question;
import it.unipi.lsmsd.coding_qa.service.QuestionService;
import it.unipi.lsmsd.coding_qa.service.exception.BusinessException;

import java.util.Date;

public class QuestionServiceImpl implements QuestionService {

    private final QuestionDAO questionDAO;
    private final QuestionNodeDAO questionNodeDAO;

    public QuestionServiceImpl() {
        this.questionDAO = DAOLocator.getQuestionDAO(DAORepositoryEnum.MONGODB);
        this.questionNodeDAO = DAOLocator.getQuestionNodeDAO(DAORepositoryEnum.NEO4J);
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
                throw new BusinessException("Error in creating the question");
            }
        } catch (Exception e) {
            throw new BusinessException("Error in creating the question");
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
        } catch (DAONodeException e) { // Update of question node failed --> Rollback updating the question with the old question
            try {
                if (oldQuestion != null) {
                    questionDAO.updateQuestion(oldQuestion);
                }
            } catch (Exception ex) {
                throw new BusinessException("Error in updating the question");
            }
        } catch (Exception ex) {
            throw new BusinessException("Error in updating the question");
        }
    }

    @Override
    public void deleteQuestion(String questionId) throws BusinessException {
        try {
            questionDAO.deleteQuestion(questionId);
            questionNodeDAO.delete(questionId);
        } catch (DAONodeException ex) { // If there is an error in deletion of node Question -> retry deletion
            try {
                questionNodeDAO.delete(questionId);
            } catch (DAONodeException e) {
                throw new BusinessException("Error in deleting the question");
            }
        } catch (Exception ex) {
            throw new BusinessException("Error in deleting the question");
        }
    }

    @Override
    public void reportQuestion(String questionId, boolean report) throws BusinessException {
        try {
            questionDAO.reportQuestion(questionId, report);
        } catch (Exception e) {
            throw new BusinessException("Error in reporting the question");
        }
    }

    @Override
    public PageDTO<QuestionDTO> getReportedQuestions(int page) throws BusinessException {
        try {
            return questionDAO.getReportedQuestions(page);
        } catch (Exception e) {
            throw new BusinessException("Error in getting the reported questions");
        }
    }

    @Override
    public QuestionPageDTO getQuestionInfo(String id) throws BusinessException {
        try {
            return questionDAO.getQuestionInfo(id);
        } catch (Exception e) {
            throw new BusinessException("Error in getting the question");
        }
    }

    @Override
    public PageDTO<QuestionDTO> searchQuestions(int page, String searchString, String topicFilter) throws BusinessException {
        try {
            return questionDAO.searchQuestions(page, searchString, topicFilter);
        } catch (Exception e) {
            throw new BusinessException("Error in searching questions");
        }
    }

    @Override
    public PageDTO<QuestionDTO> browseQuestions(int page) throws BusinessException {
        try {
            return questionDAO.browseQuestions(page);
        } catch (Exception e) {
            throw new BusinessException("Error in browsing questions");
        }
    }

    @Override
    public PageDTO<QuestionDTO> viewCreatedQuestions(String nickname, int page) throws BusinessException {
        try {
            return questionNodeDAO.viewCreatedQuestions(nickname, page);
        } catch (Exception e) {
            throw new BusinessException("Error viewing created questions");
        }
    }

    @Override
    public PageDTO<QuestionDTO> viewAnsweredQuestions(String nickname, int page) throws BusinessException {
        try {
            return questionNodeDAO.viewAnsweredQuestions(nickname, page);
        } catch (Exception e) {
            throw new BusinessException("Error viewing answered questions");
        }
    }
}
