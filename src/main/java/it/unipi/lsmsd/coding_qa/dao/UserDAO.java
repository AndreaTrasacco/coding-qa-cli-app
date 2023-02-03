package it.unipi.lsmsd.coding_qa.dao;

import it.unipi.lsmsd.coding_qa.model.*;

public interface UserDAO {
    RegisteredUser register(RegisteredUser user);
    boolean authenticate(String username, String password);
    void updateInfo(RegisteredUser user);
    RegisteredUser getInfo(String id);
    void delete(String id);

}
