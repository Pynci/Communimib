package it.unimib.communimib.repository;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Token;
import it.unimib.communimib.model.User;

public class TokenRepository implements ITokenRepository{


    @Override
    public void getAllToken(Callback addedCallback, Callback changedCallback, Callback removedCallback, Callback cancelledCallback) {

    }

    @Override
    public void sendRegistrationToServer(Token token, Callback callback) {

    }

    @Override
    public void checkTokenExistence(String token, User user, Callback callback) {

    }
}
