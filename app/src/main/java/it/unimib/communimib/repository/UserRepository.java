package it.unimib.communimib.repository;

import it.unimib.communimib.Callback;
import it.unimib.communimib.datasource.user.IAuthDataSource;
import it.unimib.communimib.datasource.user.IUserLocalDataSource;
import it.unimib.communimib.datasource.user.IUserRemoteDataSource;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;

public class UserRepository implements IUserRepository{

    private final IAuthDataSource authDataSource;
    private final IUserRemoteDataSource userRemoteDataSource;
    private final IUserLocalDataSource userLocalDataSource;

    public UserRepository(IAuthDataSource authDataSource, IUserRemoteDataSource userRemoteDataSource, IUserLocalDataSource localDataSource){
        this.authDataSource = authDataSource;
        this.userRemoteDataSource = userRemoteDataSource;
        this.userLocalDataSource = localDataSource;
    }

    @Override
    public void signUp(String email, String password, String name, String surname, Callback callback) {
        authDataSource.signUp(email, password, authResult -> {
            if(authResult.isSuccessful()){
                userRemoteDataSource.storeUserParameters(((Result.AuthSuccess) authResult).getUid(), email, name, surname, dbResult -> {
                    if(dbResult.isSuccessful()) {
                        userLocalDataSource.insertUser(new User(email, name, surname), localdbResult -> {
                            callback.onComplete(localdbResult);
                        });
                    }
                    else{
                        callback.onComplete(dbResult);
                    }
                });
            }
            else{
                callback.onComplete(authResult);
            }
        });
    }

    @Override
    public void signIn(String email, String password, Callback callback) {
        authDataSource.signIn(email, password, result -> {

        });
    }

    @Override
    public void signOut(Callback callback) {

    }

    @Override
    public void sendEmailVerification(String email, Callback callback) {

    }

    @Override
    public void updateUserNameAndSurname(String name, String surname, Callback callback) {

    }

    @Override
    public void getUserByEmail(String email, Callback callback) {

    }

    @Override
    public void resetPassword(String email, Callback callback) {

    }
}
