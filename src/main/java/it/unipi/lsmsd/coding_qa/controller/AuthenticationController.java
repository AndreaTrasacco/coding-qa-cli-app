package it.unipi.lsmsd.coding_qa.controller;

import it.unipi.lsmsd.coding_qa.dto.UserDTO;
import it.unipi.lsmsd.coding_qa.dto.UserLoginDTO;
import it.unipi.lsmsd.coding_qa.dto.UserRegistrationDTO;
import it.unipi.lsmsd.coding_qa.service.ServiceLocator;
import it.unipi.lsmsd.coding_qa.service.UserService;
import it.unipi.lsmsd.coding_qa.service.exception.BusinessException;
import it.unipi.lsmsd.coding_qa.view.AuthenticationView;
import it.unipi.lsmsd.coding_qa.view.MainView;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthenticationController {
    private static AuthenticationView authView = new AuthenticationView();
    private static MainView mainView = new MainView();
    private static UserService userService = ServiceLocator.getUserService();;
    private static UserDTO loggedUser = null;

    public static int start() { // do .. while
        try {
            int choice = authView.initialMenu();
            switch (choice) {
                case 1: // sign up
                    UserRegistrationDTO userRegistrationDTO = authView.signUp();
                    userRegistrationDTO.setEncPassword(encryptPassword(userRegistrationDTO.getEncPassword()));
                    loggedUser = userService.register(userRegistrationDTO);
                    mainView.showMessage("#### REGISTERED ####");
                    break;
                case 2: // login
                    UserLoginDTO userLoginDTO = authView.login();
                    loggedUser = userService.login(userLoginDTO.getNickname(), encryptPassword(userLoginDTO.getPassword()));
                    if(loggedUser != null)
                        mainView.showMessage("#### LOGGED IN ####");
                    else
                        mainView.showMessage("#### LOG IN FAILED ####");
                    break;
            }
            return choice;
        } catch (BusinessException e) {
            System.out.println(e.getMessage());
            System.exit(1);
            return -1;
        }
    }

    public static void logout() {
        authView.showLogoutMessage();
    }

    public static String encryptPassword(String originalString) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("!!!! ERROR DURING PASSWORD ENCRYPTION !!!!");
            System.exit(1);
        }
        byte[] hash = digest.digest(originalString.getBytes(StandardCharsets.UTF_8));
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

    public static UserDTO getLoggedUser() {
        return loggedUser;
    }

    public static String getLoggedUserNickname() {
        return loggedUser.getNickname();
    }

    public static void main(String[] args) {
        System.out.println(encryptPassword("admin"));
    }
}
