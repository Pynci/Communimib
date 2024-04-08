package it.unimib.communimib.database;

import it.unimib.communimib.model.User;

public class FakeUserDAO implements UserDAO {

    User user;

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void clearUser() {
        user = null;
    }

    @Override
    public int updateUser(User newLoggedUser) {
        user = newLoggedUser;
        return 1;
    }

    @Override
    public long insertUser(User loggedUser) {
        user = loggedUser;
        return 12345;
    }
}
