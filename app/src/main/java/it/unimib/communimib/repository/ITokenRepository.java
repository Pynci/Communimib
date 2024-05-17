package it.unimib.communimib.repository;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Token;
import it.unimib.communimib.model.User;

public interface ITokenRepository {

    void getAllToken(Callback addedCallback,
                     Callback changedCallback,
                     Callback removedCallback,
                     Callback cancelledCallback);

    void sendRegistrationToServer(Token token, Callback callback);

    void checkTokenExistence(String token, User user, Callback callback);
}
