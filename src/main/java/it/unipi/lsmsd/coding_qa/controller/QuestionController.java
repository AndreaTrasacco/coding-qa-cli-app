package it.unipi.lsmsd.coding_qa.controller;

import it.unipi.lsmsd.coding_qa.dto.*;
import it.unipi.lsmsd.coding_qa.service.*;
import it.unipi.lsmsd.coding_qa.service.exception.BusinessException;
import it.unipi.lsmsd.coding_qa.utils.Constants;
import it.unipi.lsmsd.coding_qa.view.*;

public class QuestionController {
    private QuestionService questionService;
    private AnswerService answerService;
    private QuestionView questionView = new QuestionView();
    private MainView mainView = new MainView();
    private UserController userController = new UserController();

    public QuestionController() {
        questionService = ServiceLocator.getQuestionService();
        answerService = ServiceLocator.getAnswerService();
    }

    public void browseQuestions(int userType) { // userType --> 0: Admin, 1: Logged, 2: NotLogged TODO TESTARE
        try {
            int page = 1;
            do {
                PageDTO<QuestionDTO> pageDTO = questionService.browseQuestions(page);
                mainView.viewPage(pageDTO);
                switch (questionView.browseQuestionsMenu()) {
                    case 1: // Open a question
                        int index = mainView.inputMessageWithPaging("Specify the question number", pageDTO.getCounter()) - 1;
                        QuestionDTO questionDTO = pageDTO.getEntries().get(index);
                        if (questionDTO.getAuthor().equals(AuthenticationController.getLoggedUser().getNickname())) {
                            userType = 3; // 3: Logged and Owner of the question
                        }
                        openQuestion(questionDTO.getId(), userType);
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
                        searchQuestion(userType);
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

    public void searchQuestion(int userType) { // 0: Owner, 1: Logged, 2: NotLogged // TODO TESTARE
        try {
            int page = 1;
            do {
                QuestionSearchDTO questionSearchDTO = new QuestionSearchDTO();
                questionView.search(questionSearchDTO); // take topic and text to be search in input
                PageDTO<QuestionDTO> pageDTO = questionService.searchQuestions(1, questionSearchDTO.getText(), questionSearchDTO.getTopic());
                mainView.viewPage(pageDTO);
                switch (questionView.searchQuestionsMenu()) {
                    case 1: // Open a question
                        int index = mainView.inputMessageWithPaging("Specify the question number", pageDTO.getCounter()) - 1;
                        QuestionDTO questionDTO = pageDTO.getEntries().get(index);
                        if (questionDTO.getAuthor().equals(AuthenticationController.getLoggedUser().getNickname())) {
                            userType = 3; // 3: Logged and Owner of the question
                        }
                        openQuestion(questionDTO.getId(), userType);
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

    public void browseAnswers(int userType, String questionId, String questionOwner) { // 1: Logged, 2: NotLogged and Admin, 3: Owner // TODO TESTARE
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

    public void browseYourQuestions() { // TODO MANCANO ALCUNE CHIAMATE AL SERVICE
        /*try {
            int page = 1;
            int pageAns = 1;
            do {
                UserDTO userDTO = AuthenticationController.getLoggedUser();
                PageDTO<QuestionDTO> pageDTO = questionService.viewCreatedQuestions(userDTO.getNickname(), page);
                if (pageDTO.getCounter() == 0) return;
                switch (questionView.browseQuestionsMenu()) {
                    case 1: // open a question
                        int number = mainView.inputMessageWithPaging("Specify the question number", pageDTO.getCounter());
                        QuestionPageDTO questionPageDTO = questionService.getQuestionInfo(pageDTO.getEntries().get(number - 1).getId());
                        mainView.view(questionPageDTO);
                        switch (questionView.menuInQuestionPageOwner()) {
                            case 1: // add an answer
                                AnswerDTO answerDTO = new AnswerDTO();
                                answerDTO.setAuthor(userDTO.getNickname());
                                questionView.createAnswer(answerDTO);
                                // TODO SERVICE
                                break;
                            case 2: // browse answers
                                PageDTO<AnswerDTO> pageDTOAns = answerService.getAnswersPage(pageAns, questionPageDTO.getId());
                                if (pageDTOAns.getCounter() == 0) return;
                                switch (questionView.menuInAnswerPageLogged()) {
                                    case 1:  // select an answer
                                        int ansNumber = mainView.inputMessageWithPaging("Specify the answer number", pageDTO.getCounter());
                                        // TODO METTERE OPEN ANSWER
                                        switch (questionView.menuInAnswer()) {
                                            case 1:  // Upvote
                                                VoteDTO voteDTO = new VoteDTO();
                                                voteDTO.setAnswerId(pageDTOAns.getEntries().get(ansNumber - 1).getId());
                                                voteDTO.setVoteType(true);
                                                voteDTO.setAnswerOwner(pageDTOAns.getEntries().get(ansNumber - 1).getAuthor());
                                                voteDTO.setVoterId(userDTO.getId());
                                                answerService.voteAnswer(voteDTO);
                                                break;
                                            case 2:  // Downvote
                                                VoteDTO voteDTO1 = new VoteDTO();
                                                voteDTO1.setAnswerId(pageDTOAns.getEntries().get(ansNumber - 1).getId());
                                                voteDTO1.setVoteType(false);
                                                voteDTO1.setAnswerOwner(pageDTOAns.getEntries().get(ansNumber - 1).getAuthor());
                                                voteDTO1.setVoterId(userDTO.getId());
                                                answerService.voteAnswer(voteDTO1);
                                                break;
                                            case 3:  // Report
                                                answerService.reportAnswer(pageDTOAns.getEntries().get(ansNumber - 1).getId(), true);
                                                break;
                                            case 4:  // go back
                                                return;
                                        }
                                        break;
                                    case 2:  // go to the next page
                                        if (pageDTOAns.getCounter() == Constants.PAGE_SIZE)
                                            pageAns++;
                                        else
                                            mainView.showMessage("!!!! THIS IS THE LAST PAGE !!!!");
                                        break;
                                    case 3:  // go to the previous page
                                        if (pageAns > 1)
                                            pageAns--;
                                        else
                                            mainView.showMessage("!!!! THIS IS THE FIRST PAGE !!!!");
                                        break;
                                    case 4:  // go back
                                        return;
                                }
                                break;
                            case 3: // delete question
                                questionService.deleteQuestion(questionPageDTO.getId());
                                break;
                            case 4: // update question
                                updateQuestion(questionPageDTO);
                                break;
                            case 5: // go back
                                return;
                        }
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }*/
    }

    public void browseCreatedOrAnsweredQuestions(String nickname, boolean type) throws Exception { //type : true for created q | false for answered q TODO TESTARE
        int page = 1;
        do {
            PageDTO<QuestionDTO> pageDTO;
            if (type)
                pageDTO = questionService.viewCreatedQuestions(nickname, page);
            else
                pageDTO = questionService.viewAnsweredQuestions(nickname, page);
            mainView.viewPage(pageDTO);
            switch (questionView.searchQuestionsMenu()) {
                case 1:
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

    public void updateQuestion(QuestionPageDTO questionPageDTO) throws BusinessException { // TODO TESTARE
        QuestionModifyDTO questionModifyDTO = new QuestionModifyDTO();
        questionModifyDTO.setId(questionPageDTO.getId());
        questionModifyDTO.setBody(questionPageDTO.getBody());
        questionModifyDTO.setTopic(questionPageDTO.getTopic());
        questionModifyDTO.setTitle(questionPageDTO.getTitle());
        questionView.modifyQuestion(questionModifyDTO);
        questionService.updateQuestion(questionModifyDTO);
    }

    public void openQuestion(String questionId, int userType) throws BusinessException { // TODO TESTARE
        QuestionPageDTO questionPageDTO = questionService.getQuestionInfo(questionId);
        switch (userType) {
            case 0: // Admin
            case 2: // NotLogged
                questionPageNotLoggedOrAdmin(questionPageDTO, userType);
                break;
            case 1: // Logged (not owner of the question
                questionPageLogged(questionPageDTO);
                break;
            case 3: // Owner of question
                questionPageOwner(questionPageDTO);
                break;
        }
    }


    public void openAnswer(AnswerDTO answerDTO, String questionOwner) throws BusinessException { // TODO TESTARE
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
                    updateAnswer();
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
                userController.openProfile(answerDTO.getAuthor());
                break;
        }
    }

    public void createQuestion() { // TODO TESTARE
        try {
            QuestionPageDTO questionPageDTO = new QuestionPageDTO();
            questionView.createQuestion(questionPageDTO);
            questionService.createQuestion(questionPageDTO);
            mainView.showMessage("######################################### QUESTION CREATED #########################################");
        } catch (BusinessException ex){
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }

    public void updateAnswer() { // TODO

    }

    public void questionPageLogged(QuestionPageDTO questionPageDTO) { // TODO
        mainView.view(questionPageDTO);

    }

    public void questionPageNotLoggedOrAdmin(QuestionPageDTO questionPageDTO, int userType) { // userType: 0 for admin, 2 for not logged user
        // TODO
    }

    public void questionPageOwner(QuestionPageDTO questionPageDTO) { // TODO

    }
}
