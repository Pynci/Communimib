package it.unimib.communimib.datasource.token;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Token;

public interface ITokenRemoteDataSource {

    void getAllToken(Callback addedCallback,
                            Callback changedCallback,
                            Callback removedCallback,
                            Callback cancelledCallback);

    void sendRegistrationToServer(Token token, Callback callback);
}
