package it.unipi.lsmsd.coding_qa.dao;

import it.unipi.lsmsd.coding_qa.dto.UserDTO;
import it.unipi.lsmsd.coding_qa.model.*;

public interface UserDAO { // TODO AGGIUNGERE PAGINAZIONE DOVE SERVE
    void register(RegisteredUser user);
    User authenticate(String username, String encPassword);
    void updateInfo(RegisteredUser user);
    UserDTO getInfo(String id);
    int getScore(String id); // TODO
    void updateScore(String nickname, int quantity); // TODO
    void delete(String id);
}
