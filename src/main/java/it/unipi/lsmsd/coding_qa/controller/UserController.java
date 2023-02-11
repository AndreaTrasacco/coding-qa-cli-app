package it.unipi.lsmsd.coding_qa.controller;

import it.unipi.lsmsd.coding_qa.dto.UserDTO;
import it.unipi.lsmsd.coding_qa.service.ServiceLocator;
import it.unipi.lsmsd.coding_qa.service.UserService;
import it.unipi.lsmsd.coding_qa.view.UserView;

public class UserController {
    private UserService userService;
    private UserView userView = new UserView();
    public UserController(){
        userService = ServiceLocator.getUserService();
    }

    public void openProfile(boolean admin){ // PARAMETRO PUO ESSERE UTILE (OPPURE FUNZIONE SEPARATA)?? Admin può cancellare utente
        // call show profile
        // .. menu on the profile (use cases diagram)
    }

    public void openProfileIfAdmin(UserDTO userDTO) { // TODO
        userView.adminUserProfile(userDTO);
    }

    // update
}
