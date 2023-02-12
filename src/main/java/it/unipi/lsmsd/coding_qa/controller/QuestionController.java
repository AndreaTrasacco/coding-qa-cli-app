package it.unipi.lsmsd.coding_qa.controller;

import it.unipi.lsmsd.coding_qa.dto.*;
import it.unipi.lsmsd.coding_qa.service.*;
import it.unipi.lsmsd.coding_qa.service.exception.BusinessException;
import it.unipi.lsmsd.coding_qa.utils.Constants;
import it.unipi.lsmsd.coding_qa.view.*;

public class QuestionController {
    private static QuestionService questionService = ServiceLocator.getQuestionService();
    private static AnswerService answerService = ServiceLocator.getAnswerService();
    private static QuestionView questionView = new QuestionView();
    private static MainView mainView = new MainView();

    public static void browseQuestions() { // userType --> 0: Admin, 1: Logged, 2: NotLogged TODO TESTARE
        try {
            int page = 1;
            do {
                PageDTO<QuestionDTO> pageDTO = questionService.browseQuestions(page);
                mainView.viewPage(pageDTO);
                switch (questionView.browseQuestionsMenu()) {
                    case 1: // Open a question
                        int index = mainView.inputMessageWithPaging("Specify the question number", pageDTO.getCounter()) - 1;
                        QuestionDTO questionDTO = pageDTO.getEntries().get(index);
                        openQuestion(questionDTO.getId());
                        break;
                    case 2: // Next page
                        if (pageDTO.getCounter() == Constants.PAGE_SIZE)
                            page++;
                        else
                            mainView.showMessage("!!!! THIS IS THE LAST PAGE !!!!");
                        break;
                    case 3: // previous page
                        if (page > 1)
                            page--;
                        else
                            mainView.showMessage("!!!! THIS IS THE FIRST PAGE !!!!");
                        break;
                    case 4: // Search question
                        searchQuestion();
                        break;
                    case 5: // Exit
                        return;
                }
            } while (true);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }

    public static void searchQuestion() { // TODO TESTARE
        try {
            int page = 1;
            QuestionSearchDTO questionSearchDTO = new QuestionSearchDTO();
            questionView.search(questionSearchDTO); // take topic and text to be search in input
            do {
                PageDTO<QuestionDTO> pageDTO = questionService.searchQuestions(page, questionSearchDTO.getText(), questionSearchDTO.getTopic());
                mainView.viewPage(pageDTO);
                switch (questionView.searchQuestionsMenu()) {
                    case 1: // Open a question
                        int index = mainView.inputMessageWithPaging("Specify the question number", pageDTO.getCounter()) - 1;
                        QuestionDTO questionDTO = pageDTO.getEntries().get(index);
                        openQuestion(questionDTO.getId());
                        break;
                    case 2: // Next page
                        if (pageDTO.getCounter() == Constants.PAGE_SIZE)
                            page++;
                        else
                            mainView.showMessage("!!!! THIS IS THE LAST PAGE !!!!");
                        break;
                    case 3: // previous page
                        if (page > 1)
                            page--;
                        else
                            mainView.showMessage("!!!! THIS IS THE FIRST PAGE !!!!");
                        break;
                    case 4: // Exit
                        return;
                }
            } while (true);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }

    public static void browseAnswers(String questionId, String questionOwner) { // TODO TESTARE
        try {
            int page = 1;
            do {
                PageDTO<AnswerDTO> pageDTO = answerService.getAnswersPage(page, questionId);
                mainView.viewPage(pageDTO);
                switch (questionView.menuInAnswersPage()) {
                    case 1: // Select an answer
                        if (pageDTO.getCounter() == 0)
                            mainView.showMessage("!!!! ACTION NOT POSSIBLE !!!!");
                        else {
                            int index = mainView.inputMessageWithPaging("Specify the answer number", pageDTO.getCounter()) - 1;
                            AnswerDTO answerDTO = pageDTO.getEntries().get(index);
                            answerService.getCompleteAnswer(answerDTO);
                            openAnswer(answerDTO, questionOwner);
                        }
                        break;
                    case 2: // Next page
                        if (pageDTO.getCounter() == Constants.PAGE_SIZE)
                            page++;
                        else
                            mainView.showMessage("!!!! THIS IS THE LAST PAGE !!!!");
                        break;
                    case 3: // previous page
                        if (page > 1)
                            page--;
                        else
                            mainView.showMessage("!!!! THIS IS THE FIRST PAGE !!!!");
                        break;
                    case 4: // Exit
                        return;
                }
            } while (true);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }

    public static void browseCreatedOrAnsweredQuestions(String nickname, boolean type) throws Exception { //type : true for created q | false for answered q TODO TESTARE
        int page = 1;
        do {
            PageDTO<QuestionDTO> pageDTO;
            if (type)
                pageDTO = questionService.viewCreatedQuestions(nickname, page);
            else
                pageDTO = questionService.viewAnsweredQuestions(nickname, page);
            mainView.viewPage(pageDTO);
            if(page == 1 && pageDTO.getCounter() == 0)
                return;
            switch (questionView.searchQuestionsMenu()) {
                case 1: // Open a question
                    int index = mainView.inputMessageWithPaging("Specify the question number", pageDTO.getCounter()) - 1;
                    QuestionDTO questionDTO = pageDTO.getEntries().get(index);
                    openQuestion(questionDTO.getId());
                    break;
                case 2: // go to the next page
                    if (pageDTO.getCounter() == Constants.PAGE_SIZE)
                        page++;
                    else
                        mainView.showMessage("!!!! THIS IS THE LAST PAGE !!!!");
                    break;
                case 3: // go ot the previous page
                    if (page > 1)
                        page--;
                    else
                        mainView.showMessage("!!!! THIS IS THE FIRST PAGE !!!!");
                    break;
                case 4: // go back
                    return;
            }
        } while (true);
    }

    public static void updateQuestion(QuestionPageDTO questionPageDTO) throws BusinessException { // TODO TESTARE
        QuestionModifyDTO questionModifyDTO = new QuestionModifyDTO();
        questionModifyDTO.setId(questionPageDTO.getId());
        questionModifyDTO.setBody(questionPageDTO.getBody());
        questionModifyDTO.setTopic(questionPageDTO.getTopic());
        questionModifyDTO.setTitle(questionPageDTO.getTitle());
        questionView.modifyQuestion(questionModifyDTO);
        questionService.updateQuestion(questionModifyDTO);
    }

    public static void openQuestion(String questionId) throws BusinessException { // TODO TESTARE
        QuestionPageDTO questionPageDTO = questionService.getQuestionInfo(questionId);
        int userType = 2; // Not Logged In
        if (AuthenticationController.getLoggedUser() != null) {
            userType = 1; //  Logged user
            if (AuthenticationController.getLoggedUserNickname().equals("admin"))
                userType = 0; // Admin
            else if (questionPageDTO.getAuthor().equals(AuthenticationController.getLoggedUserNickname())) {
                userType = 3; // 3: Logged and Owner of the question
            }
        }
        switch (userType) {
            case 0: // Admin
            case 2: // NotLogged
                questionPageNotLoggedOrAdmin(questionPageDTO);
                break;
            case 1: // Logged (not owner of the question) or Owner of the question
            case 3: // Owner of question
                questionPageLoggedOrLoggedOwner(questionPageDTO);
                break;
        }
    }

    public static void questionPageLoggedOrLoggedOwner(QuestionPageDTO questionPageDTO) throws BusinessException { // TODO TESTARE
        mainView.view(questionPageDTO);
        switch (questionView.menuInQuestionPageLoggedOrOwner()) {
            case 1: // add an answer
                AnswerDTO answerDTO = new AnswerDTO();
                answerDTO.setAuthor(AuthenticationController.getLoggedUserNickname());
                questionView.createAnswer(answerDTO);
                answerService.addAnswer(questionPageDTO.getId(), answerDTO);
                mainView.showMessage("########################################## ANSWER CREATED ##########################################");
                break;
            case 2: // browse answers
                browseAnswers(questionPageDTO.getId(), questionPageDTO.getAuthor());
                break;
            case 3: // report question
                questionService.reportQuestion(questionPageDTO.getId(), true);
                break;
            case 4: // delete question --> only owner of question
                if (AuthenticationController.getLoggedUserNickname().equals(questionPageDTO.getAuthor()))
                    questionService.deleteQuestion(questionPageDTO.getId());
                else
                    mainView.showMessage("!!!! ACTION NOT POSSIBLE !!!!");
                break;
            case 5: // update question --> only owner of question
                if (AuthenticationController.getLoggedUserNickname().equals(questionPageDTO.getAuthor()))
                    updateQuestion(questionPageDTO);
                else
                    mainView.showMessage("!!!! ACTION NOT POSSIBLE !!!!");
                break;
        }
    }

    public static void questionPageNotLoggedOrAdmin(QuestionPageDTO questionPageDTO) { // TODO TESTARE
        mainView.view(questionPageDTO);
        switch (questionView.menuInQuestionPageNotLoggedOrAdmin()) {
            case 1: // browse answers
                browseAnswers(questionPageDTO.getId(), questionPageDTO.getAuthor());
                break;
        }
    }

    public static void openAnswer(AnswerDTO answerDTO, String questionOwner) throws BusinessException { // TODO TESTARE
        mainView.view(answerDTO);
        UserDTO loggedUser = AuthenticationController.getLoggedUser();
        switch (questionView.menuInCompleteAnswer()) {
            case 1:  // Upvote --> possible only for a logged user and not for admin
                if (loggedUser != null && !loggedUser.getNickname().equals("admin")) {
                    VoteDTO voteDTO = new VoteDTO();
                    voteDTO.setAnswerId(answerDTO.getId());
                    voteDTO.setVoteType(true);
                    voteDTO.setAnswerOwner(answerDTO.getAuthor());
                    voteDTO.setVoterId(loggedUser.getId());
                    answerService.voteAnswer(voteDTO);
                } else
                    mainView.showMessage("!!!! ACTION NOT POSSIBLE !!!!");
                break;
            case 2:  // Downvote --> possible only for a logged user and not for admin
                if (loggedUser != null && !loggedUser.getNickname().equals("admin")) {
                    VoteDTO voteDTO = new VoteDTO();
                    voteDTO.setAnswerId(answerDTO.getId());
                    voteDTO.setVoteType(false);
                    voteDTO.setAnswerOwner(answerDTO.getAuthor());
                    voteDTO.setVoterId(loggedUser.getId());
                    answerService.voteAnswer(voteDTO);
                } else
                    mainView.showMessage("!!!! ACTION NOT POSSIBLE !!!!");
                break;
            case 3:  // Report
                answerService.reportAnswer(answerDTO.getId(), true);
                break;
            case 4: // Modify answer --> possible only if the logged user is the author of the answer
                if (loggedUser.getNickname().equals(answerDTO.getAuthor()))
                    updateAnswer(answerDTO);
                else
                    mainView.showMessage("!!!! ACTION NOT POSSIBLE !!!!");
                break;
            case 5: // Accept --> possible only if the logged user is the author of question of the answer
                if (loggedUser.getNickname().equals(questionOwner))
                    answerService.acceptAnswer(answerDTO.getId()); // The answer will be marked as accepted if and only if there isn't any other anser of the question that has been already accepted
                else
                    mainView.showMessage("!!!! ACTION NOT POSSIBLE !!!!");
                break;
            case 6: // Delete Answer --> possible only if the logged user is the author of the answer
                if (loggedUser.getNickname().equals(answerDTO.getAuthor()))
                    answerService.deleteAnswer(answerDTO);
                else
                    mainView.showMessage("!!!! ACTION NOT POSSIBLE !!!!");
                break;
            case 7: // View User Profile
                UserController.openProfile(answerDTO.getAuthor());
                break;
        }
    }

    public static void createQuestion() { // TODO TESTARE
        try {
            QuestionPageDTO questionPageDTO = new QuestionPageDTO();
            questionPageDTO.setAuthor(AuthenticationController.getLoggedUserNickname());
            questionView.createQuestion(questionPageDTO);
            questionService.createQuestion(questionPageDTO);
            mainView.showMessage("######################################### QUESTION CREATED #########################################");
        } catch (BusinessException ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }

    public static void updateAnswer(AnswerDTO answerDTO) throws BusinessException { // TODO TESTARE
        AnswerModifyDTO answerModifyDTO = new AnswerModifyDTO(answerDTO.getBody());
        questionView.modifyAnswer(answerModifyDTO);
        answerService.updateAnswer(answerDTO.getId(), answerModifyDTO.getBody());
        mainView.showMessage("########################################## ANSWER UPDATED ##########################################");
    }
}
