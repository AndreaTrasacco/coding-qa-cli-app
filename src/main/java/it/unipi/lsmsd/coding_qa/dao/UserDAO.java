package it.unipi.lsmsd.coding_qa.dao;

import it.unipi.lsmsd.coding_qa.dao.exception.DAOException;
import it.unipi.lsmsd.coding_qa.dto.UserDTO;
import it.unipi.lsmsd.coding_qa.model.*;

public interface UserDAO {
    void register(RegisteredUser user) throws DAOException;

    User authenticate(String username, String encPassword) throws DAOException;

    void updateInfo(RegisteredUser user) throws DAOException;

    UserDTO getInfo(String nickname) throws DAOException;

    int getScore(String id) throws DAOException;

    void delete(String nickname) throws DAOException;
}
