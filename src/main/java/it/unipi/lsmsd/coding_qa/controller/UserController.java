package it.unipi.lsmsd.coding_qa.controller;

import it.unipi.lsmsd.coding_qa.dto.*;
import it.unipi.lsmsd.coding_qa.service.AnswerService;
import it.unipi.lsmsd.coding_qa.service.QuestionService;
import it.unipi.lsmsd.coding_qa.service.ServiceLocator;
import it.unipi.lsmsd.coding_qa.service.UserService;
import it.unipi.lsmsd.coding_qa.utils.Constants;
import it.unipi.lsmsd.coding_qa.view.MainView;
import it.unipi.lsmsd.coding_qa.view.QuestionView;
import it.unipi.lsmsd.coding_qa.view.UserView;

public class UserController {
    private UserService userService;
    private QuestionService questionService;
    private AnswerService answerService;
    private UserView userView = new UserView();
    private QuestionView questionView = new QuestionView();
    private MainView mainView = new MainView();
    private AuthenticationController authenticationController = new AuthenticationController();

    public UserController(){
        userService = ServiceLocator.getUserService();
        questionService = ServiceLocator.getQuestionService();
    }

    public void openSelfProfile(){ // PARAMETRO PUO ESSERE UTILE (OPPURE FUNZIONE SEPARATA)?? Admin può cancellare utente
        // call show profile
        // .. menu on the profile (use cases diagram)
        int choice = userView.selfUserProfileMenu();
        do{
            switch (choice) {
                case 1: // browse your question
                    questionController.browseYourQuestion();
                    break;
                case 2: // browse your answer

                    break;
                case 3: // show your info
                    showYourInfo();
                    break;
                case 4: // update your profile
                    updateYourProfile();
                    break;
                case 5: // browse followed user
                    browseFollowedUser();
                    break;
                case 6: // go back
                    return;
            }
        } while (true);
    }

    private void browseFollowedUser() {
        try{
            int page;
            int pageAns;
        } catch(Exception e){
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    private void updateYourProfile() {
        try{
            UserDTO userDTO = authenticationController.getLoggedUser();
            UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
            userRegistrationDTO.setNickname(userDTO.getNickname());
            userView.updateProfile(userRegistrationDTO);
            userService.updateInfo(userRegistrationDTO);
        } catch (Exception e){
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    private void showYourInfo() {
        try{
            UserDTO userDTO = authenticationController.getLoggedUser();
            userService.getInfo(userDTO.getNickname());
        } catch (Exception e){
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    private void browseYourQuestion() { //TODO DA COPIARE
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

    public void openProfileIfAdmin(UserDTO userDTO) { // TODO
        userView.adminUserProfile(userDTO);
    }

    // update

    public static void main(String args[]){

    }
}
