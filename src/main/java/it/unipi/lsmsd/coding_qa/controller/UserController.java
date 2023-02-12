package it.unipi.lsmsd.coding_qa.controller;

import it.unipi.lsmsd.coding_qa.dto.*;
import it.unipi.lsmsd.coding_qa.service.ServiceLocator;
import it.unipi.lsmsd.coding_qa.service.UserService;
import it.unipi.lsmsd.coding_qa.service.exception.BusinessException;
import it.unipi.lsmsd.coding_qa.utils.Constants;
import it.unipi.lsmsd.coding_qa.view.MainView;
import it.unipi.lsmsd.coding_qa.view.QuestionView;
import it.unipi.lsmsd.coding_qa.view.UserView;

public class UserController {
    private UserService userService;
    private QuestionController questionController = new QuestionController();
    private AuthenticationController authenticationController = new AuthenticationController();
    private UserView userView = new UserView();
    private QuestionView questionView = new QuestionView();
    private MainView mainView = new MainView();

    public UserController() {
        userService = ServiceLocator.getUserService();
    }

    public void openSelfProfile() { // TODO TESTARE
        try {
            UserDTO loggedUser = AuthenticationController.getLoggedUser();
            mainView.showMessage("########################################### YOUR PROFILE ###########################################");
            mainView.view(loggedUser);
            do {
                switch (userView.selfUserProfileMenu()) {
                    case 1: // browse your questions
                        questionController.browseYourQuestions();
                        break;
                    case 2: // browse your answers
                        questionController.browseCreatedOrAnsweredQuestions(loggedUser.getNickname(), false);
                        break;
                    case 3: // update your profile
                        updateYourProfile();
                        break;
                    case 4: // browse followed user
                        browseFollowedUser();
                        break;
                    case 5: // go back
                        return;
                }
            } while (true);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }

    private void browseFollowedUser() { // TODO TESTARE
        try {
            int page = 1;
            do {
                UserDTO loggedUser = AuthenticationController.getLoggedUser();
                PageDTO<String> pageDTO = userService.getFollowerList(loggedUser.getNickname());
                if (pageDTO.getCounter() == 0) return;
                switch (userView.browseFollowedUsers(pageDTO)) {
                    case 1: // choose a user to view
                        int number = mainView.inputMessageWithPaging("Specify the user number", pageDTO.getCounter());
                        openProfile(pageDTO.getEntries().get(number - 1));
                        break;
                    case 2: // go to the next page
                        if (pageDTO.getCounter() == Constants.PAGE_SIZE)
                            page++;
                        else
                            mainView.showMessage("!!!! THIS IS THE LAST PAGE !!!!");
                        break;
                    case 3: // go to the previous page
                        if (page > 1)
                            page--;
                        else
                            mainView.showMessage("!!!! THIS IS THE FIRST PAGE !!!!");
                        break;
                    case 4:
                        return;
                }
            } while (true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    private void updateYourProfile() { // TODO TESTARE
        try {
            UserDTO userDTO = AuthenticationController.getLoggedUser();
            UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();

            userRegistrationDTO.setNickname(userDTO.getNickname());
            userRegistrationDTO.setFullName(userDTO.getFullName());
            userRegistrationDTO.setCountry(userDTO.getCountry());
            userRegistrationDTO.setEncPassword("");
            userRegistrationDTO.setWebsite(userDTO.getWebsite());

            userView.updateProfile(userRegistrationDTO);
            userService.updateInfo(userRegistrationDTO);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public void openProfileIfAdmin(UserDTO userDTO) {  // TODO TESTARE
        try {
            switch (userView.adminUserProfile(userDTO)) {
                case 1: // delete user
                    userService.delete(userDTO.getId(), userDTO.getNickname());
                    break;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    private void openUserProfile(UserDTO userDTO) { // TODO TESTARE
        try {
            UserDTO loggedUser = AuthenticationController.getLoggedUser();
            switch (userView.otherUserProfileMenu()) {
                case 1: // follow user
                    if (loggedUser != null)
                        userService.follow(loggedUser.getNickname(), userDTO.getNickname());
                    else
                        mainView.showMessage("!!!! ACTION NOT POSSIBLE !!!!");
                    break;
                case 2: // unfollow user
                    if (loggedUser != null)
                        userService.unfollow(loggedUser.getNickname(), userDTO.getNickname());
                    else
                        mainView.showMessage("!!!! ACTION NOT POSSIBLE !!!!");
                    break;
                case 3: // browse created q
                    questionController.browseCreatedOrAnsweredQuestions(userDTO.getNickname(), true);
                    break;
                case 4: // browse answered q
                    questionController.browseCreatedOrAnsweredQuestions(userDTO.getNickname(), false);
                    break;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public void openProfile(String nickname) throws BusinessException {  // TODO TESTARE
        UserDTO userDTO = userService.getInfo(nickname);
        if (AuthenticationController.getLoggedUserNickname().equals("admin")) // admin
            openProfileIfAdmin(userDTO);
        else if (nickname.equals(AuthenticationController.getLoggedUserNickname())) // self profile
            openSelfProfile();
        else // other user profile
            openUserProfile(userDTO);
    }
}
