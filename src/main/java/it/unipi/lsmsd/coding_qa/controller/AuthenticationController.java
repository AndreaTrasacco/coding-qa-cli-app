package it.unipi.lsmsd.coding_qa.controller;

import it.unipi.lsmsd.coding_qa.dto.UserDTO;
import it.unipi.lsmsd.coding_qa.dto.UserLoginDTO;
import it.unipi.lsmsd.coding_qa.dto.UserRegistrationDTO;
import it.unipi.lsmsd.coding_qa.service.ServiceLocator;
import it.unipi.lsmsd.coding_qa.service.UserService;
import it.unipi.lsmsd.coding_qa.service.exception.BusinessException;
import it.unipi.lsmsd.coding_qa.view.AuthenticationView;

public class AuthenticationController {
    private AuthenticationView authView = new AuthenticationView();
    private UserService userService;
    private UserDTO loggedUser = null; // TODO RIFLETTERE

    public AuthenticationController() {
        userService = ServiceLocator.getUserService();
    }

    public int start() { // do .. while
        int choice = authView.initialMenu();
        try {
            switch (choice) {
                case 1: // sign up
                    UserRegistrationDTO userRegistrationDTO = authView.signUp();
                    userRegistrationDTO.setEncPassword(encryptPassword(userRegistrationDTO.getEncPassword().getBytes()));
                    loggedUser = userService.register(authView.signUp());
                    break;
                case 2: // login
                    UserLoginDTO userLoginDTO = authView.login();
                    loggedUser = userService.login(userLoginDTO.getNickname(), userLoginDTO.getPassword());
                    break;
            }
            return choice;
        } catch (BusinessException e) {
            System.out.println(e.getMessage()); // TODO CAMBIARE E METTERE NEI CASE ??
            return -1;
        }
    }

    public void logout() {
        authView.showLogoutMessage();
    }

    public static String encryptPassword(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public UserDTO getLoggedUser() {
        return loggedUser;
    }

    public String getLoggedUserNickname() {
        return loggedUser.getNickname();
    }
}
