package it.unipi.lsmsd.coding_qa.controller;

import it.unipi.lsmsd.coding_qa.dto.*;
import it.unipi.lsmsd.coding_qa.service.AnswerService;
import it.unipi.lsmsd.coding_qa.service.QuestionService;
import it.unipi.lsmsd.coding_qa.service.ServiceLocator;
import it.unipi.lsmsd.coding_qa.utils.Constants;
import it.unipi.lsmsd.coding_qa.view.AdminView;
import it.unipi.lsmsd.coding_qa.view.MainView;
import it.unipi.lsmsd.coding_qa.view.QuestionView;

public class QuestionController {
    private QuestionService questionService;
    private AnswerService answerService;
    private QuestionView questionView;
    private MainView mainView;
    private AdminView adminView;
    private AuthenticationController authenticationController;

    public QuestionController(){
        questionService = ServiceLocator.getQuestionService();
        answerService = ServiceLocator.getAnswerService();
        questionView =  new QuestionView();
        mainView = new MainView();
        adminView = new AdminView();
        authenticationController = new AuthenticationController();
    }

    // Browse e search for admin, logged, notlogged (VALUTARE SE TROPPO DUPICATO)

    public void browse(int userType) { // 0: Admin, 1: Logged, 2: NonLogged, 3: Owner
        try{
            int page = 1;
            do{
                PageDTO<QuestionDTO> pageDTO = questionService.browseQuestions(page);
                mainView.viewPage(pageDTO);
                switch(questionView.browseQuestionsMenu()){
                    case 1:
                        //int index = questionView.specifyQuestionIndex(pageDTO); // TODO eliminare metodo specifyQuestionIndex?
                        int index = mainView.inputMessageWithPaging("Select a question", pageDTO.getCounter())-1;
                        QuestionDTO questionDTO = pageDTO.getEntries().get(index);
                        questionService.getQuestionInfo(questionDTO.getId());
                        if(questionDTO.getAuthor() == AuthenticationController.getLoggedUser().getNickname()){
                            userType = 3;
                        }
                        switch(userType){
                            case 0: adminView.adminQuestionMenu(); break;
                            case 1: questionView.menuInQuestionPageLogged(); break;
                            case 2: questionView.menuInQuestionPageNonLogged(); break;
                            case 3: questionView.menuInQuestionPageOwner();
                        }

                        break;
                    case 2:
                        if (pageDTO.getCounter() == Constants.PAGE_SIZE)
                            page++;
                        else
                            mainView.showMessage("!!!! THIS IS THE LAST PAGE !!!!");
                        break;
                    case 3:
                        if (page > 1)
                            page--;
                        else
                            mainView.showMessage("!!!! THIS IS THE FIRST PAGE !!!!");
                        break;
                    case 4:
                        search(userType);
                        break;
                    case 5:
                        return;
                }
            } while(true);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }

    public void search(int userType) { // 0: Owner, 1: Logged, 2: NonLogged, 3: Owner
        try{
            int page = 1;
            do {
                QuestionSearchDTO questionSearchDTO = new QuestionSearchDTO();
                questionView.search(questionSearchDTO);
                PageDTO<QuestionDTO> pageDTO = questionService.searchQuestions(1, questionSearchDTO.getText(), questionSearchDTO.getTopic());
                mainView.viewPage(pageDTO);
                switch(questionView.searchQuestionsMenu()){
                    case 1:
                        int index = questionView.specifyQuestionIndex(pageDTO)-1;
                        QuestionDTO questionDTO = pageDTO.getEntries().get(index);
                        questionService.getQuestionInfo(questionDTO.getId());
                        if(questionDTO.getAuthor() == AuthenticationController.getLoggedUser().getNickname()){
                            userType = 3;
                        }
                        questionService.getQuestionInfo(pageDTO.getEntries().get(index).getId());
                        switch(userType){
                            case 0: adminView.adminQuestionMenu(); break;
                            case 1: questionView.menuInQuestionPageLogged(); break;
                            case 2: questionView.menuInQuestionPageNonLogged(); break;
                            case 3: questionView.menuInQuestionPageOwner();
                        }

                        break;
                    case 2:
                        if (pageDTO.getCounter() == Constants.PAGE_SIZE)
                            page++;
                        else
                            mainView.showMessage("!!!! THIS IS THE LAST PAGE !!!!");
                        break;
                    case 3:
                        if (page > 1)
                            page--;
                        else
                            mainView.showMessage("!!!! THIS IS THE FIRST PAGE !!!!");
                        break;
                    case 4:
                        return;
                }
            } while(true);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }

    public void menuAnswersLogged(int userType, String questionId){ // 0: Admin, 1: Logged, 2: NonLogged, 3: Owner
        try{
            do{
                int page = 1;
                PageDTO<AnswerDTO> pageDTO = answerService.getAnswersPage(page, questionId);
                mainView.viewPage(pageDTO);
                switch(questionView.menuInAnswerPageLogged()){
                    case 1:
                        int index = mainView.inputMessageWithPaging("Select a question", pageDTO.getCounter())-1;
                        answerService.getCompleteAnswer(pageDTO.getEntries().get(index));
                        switch(userType){
                            case 0: adminView.adminAnswerMenu(); break;
                            case 1: questionView.menuInAnswerPageLogged(); break;
                            case 2: questionView.menuInAnswerPageNotLogged(); break;
                            case 3: questionView.menuInAnswerPageOwner();
                        }
                        break;
                    case 2:
                        if (pageDTO.getCounter() == Constants.PAGE_SIZE)
                            page++;
                        else
                            mainView.showMessage("!!!! THIS IS THE LAST PAGE !!!!");
                        break;
                    case 3:
                        if (page > 1)
                            page--;
                        else
                            mainView.showMessage("!!!! THIS IS THE FIRST PAGE !!!!");
                        break;
                    case 4:
                        return;
                }
            } while(true);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }


    private void browseYourQuestion() {
        try {
            int page = 1;
            int pageAns = 1;
            do{
                UserDTO userDTO = authenticationController.getLoggedUser();
                PageDTO<QuestionDTO> pageDTO = questionService.viewCreatedQuestions(userDTO.getNickname(), page);
                if (pageDTO.getCounter() == 0) return;
                switch(questionView.browseQuestionsMenu()){
                    case 1: // open a question
                        int number = mainView.inputMessageWithPaging("Specify the question number", pageDTO.getCounter());
                        QuestionPageDTO questionPageDTO = questionService.getQuestionInfo(pageDTO.getEntries().get(number - 1).getId());
                        mainView.view(questionPageDTO);
                        switch (questionView.menuInQuestionPageOwner()){
                            case 1: // add an answer
                                AnswerDTO answerDTO = new AnswerDTO();
                                answerDTO.setAuthor(userDTO.getNickname());
                                questionView.addAnswer(answerDTO);
                                break;
                            case 2: // browse answers
                                PageDTO<AnswerDTO> pageDTOAns = answerService.getAnswersPage(pageAns, questionPageDTO.getId());
                                if(pageDTOAns.getCounter() == 0) return;
                                switch (questionView.menuInAnswerPageLogged()){
                                    case 1:  // select an answer
                                        int ansNumber = mainView.inputMessageWithPaging("Specify the answer number", pageDTO.getCounter());

                                        switch (questionView.menuInAnswer()){
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
                                QuestionModifyDTO questionModifyDTO = new QuestionModifyDTO();
                                questionModifyDTO.setId(questionPageDTO.getId());
                                questionView.modifyQuestion(questionModifyDTO);
                                questionService.updateQuestion(questionModifyDTO);
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
        } catch (Exception e){
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public void browseCreatedQuestions(String nickname){

    }

    public void browseAnsweredQuestions(String nickname){

    }

    // menu se logged oppure no
    // operazioni varie anche tutto in un solo metodo se basta
}
