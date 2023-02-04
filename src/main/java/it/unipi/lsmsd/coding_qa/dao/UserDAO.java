package it.unipi.lsmsd.coding_qa.dao;

import it.unipi.lsmsd.coding_qa.model.*;

public interface UserDAO {
    void register(RegisteredUser user);
    User authenticate(String username, String encPassword);
    void updateInfo(RegisteredUser user);
    RegisteredUser getInfo(String id); // TODO VALUTARE USO DTO
    int getScore(String id); // TODO
    void updateScore(String nickname, int type); // TODO
    void delete(String id);
}
