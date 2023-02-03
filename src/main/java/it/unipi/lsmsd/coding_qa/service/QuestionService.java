package it.unipi.lsmsd.coding_qa.service;

import it.unipi.lsmsd.coding_qa.dto.PageDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionDTO;
import it.unipi.lsmsd.coding_qa.model.Answer;
import it.unipi.lsmsd.coding_qa.model.Question;

public interface QuestionService {
    // create
    // addAnswer
    // update ... question
    // update ... answer
    // removeAnswer
    // voteAnswer (anche unvote)
    // reportQuestion
    // reportAnswer
    // removeQuestion
    // getQuestionInfo
    // searchQuestion type
    // searchQuestionByTopic type
    // getReportedQuestions

    public void createQuestion(Question question);

    public void addAnswer(Answer answer);

    public void updateQuestion(Question question);

    public void updateAnswer(Answer answer);

    public void deleteQuestion(Question question);

    public void deleteAnswer(Answer answer);

    public void voteAnswer(Answer answer, boolean voteType); // true: upvote, false: downvote

    public void reportQuestion(Question question);

    public void reportAnswer(Answer answer);

    public Question getQuestionInfo(String id);

    public PageDTO<QuestionDTO> getQuestionPageByTitle(int page, String searchString);

    public PageDTO<QuestionDTO> getQuestionPageByTopic(int page, String topic);
}
