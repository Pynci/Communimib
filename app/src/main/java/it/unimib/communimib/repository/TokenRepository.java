package it.unimib.communimib.repository;

import it.unimib.communimib.Callback;
import it.unimib.communimib.datasource.token.ITokenRemoteDataSource;
import it.unimib.communimib.model.Token;
import it.unimib.communimib.model.User;

public class TokenRepository implements ITokenRepository{

    private final ITokenRemoteDataSource tokenRemoteDataSource;

    public TokenRepository(ITokenRemoteDataSource tokenRemoteDataSource) {
        this.tokenRemoteDataSource = tokenRemoteDataSource;
    }

    @Override
    public void getAllToken(Callback addedCallback,
                            Callback changedCallback,
                            Callback removedCallback,
                            Callback cancelledCallback) {
        tokenRemoteDataSource.getAllToken(addedCallback, changedCallback, removedCallback, cancelledCallback);
    }

    @Override
    public void sendRegistrationToServer(Token token, Callback callback) {
        tokenRemoteDataSource.sendRegistrationToServer(token, callback);
    }

    @Override
    public void checkTokenExistence(String token, User user, Callback callback) {

    }
}
