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
    private User currentUser;

    public UserRepository(IAuthDataSource authDataSource, IUserRemoteDataSource userRemoteDataSource, IUserLocalDataSource localDataSource){
        this.authDataSource = authDataSource;
        this.userRemoteDataSource = userRemoteDataSource;
        this.userLocalDataSource = localDataSource;
    }

    @Override
    public void signUp(String email, String password, String name, String surname, Callback callback) {

        authDataSource.signUp(email, password, authResult -> {
            if(authResult.isSuccessful()){
                userRemoteDataSource.storeUserParameters(((Result.SignupSuccess) authResult).getUid(), email, name, surname, dbResult -> {
                    if(dbResult.isSuccessful()) {
                        currentUser = new User(email, name, surname);
                        userLocalDataSource.insertUser(currentUser, localdbResult -> {
                            if(localdbResult.isSuccessful()){
                                authDataSource.sendEmailVerification(callback);
                            }
                            else{
                                callback.onComplete(localdbResult);
                            }
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
        authDataSource.signIn(email, password, authResult -> {
            if(authResult.isSuccessful()){
                userRemoteDataSource.getUserByEmail(email, remoteResult -> {
                    if(remoteResult.isSuccessful()){
                        currentUser = ((Result.UserSuccess) remoteResult).getUser();
                        userLocalDataSource.insertUser(currentUser, callback);
                    }
                });
            }
            else{
                callback.onComplete(authResult);
            }
        });
    }

    @Override
    public void signOut(Callback callback) {
        authDataSource.signOut(authResult -> {
            if(authResult.isSuccessful()){
                userLocalDataSource.deleteUser(currentUser, localResult -> {
                    if(localResult.isSuccessful()){
                        currentUser = null;
                    }
                    callback.onComplete(localResult);
                });
            }
            else{
                callback.onComplete(authResult);
            }
        });
    }

    @Override
    public void isSessionStillActive(Callback callback){
        if(authDataSource.isSessionStillActive()){
            if(currentUser == null){
                userLocalDataSource.getUser(localResult -> {
                    if(localResult.isSuccessful()){
                        currentUser = ((Result.UserSuccess) localResult).getUser();
                        callback.onComplete(new Result.BooleanSuccess(true));
                    }
                    else{
                        callback.onComplete(localResult);
                    }
                });
            }
            else{
                callback.onComplete(new Result.BooleanSuccess(true));
            }
        }
        else{
            userLocalDataSource.deleteUser(new User(authDataSource.getCurrentUserEmail()), localResult -> {
                if(localResult.isSuccessful()){
                    callback.onComplete(new Result.BooleanSuccess(false));
                }
                else{
                    callback.onComplete(localResult);
                }
            });
        }
    }

    @Override
    public void sendEmailVerification(String email, Callback callback) {
        // scrivo sto commento altrimenti Sonar mi picchia
    }

    @Override
    public void updateUserNameAndSurname(String name, String surname, Callback callback) {
        // scrivo sto commento altrimenti Sonar mi picchia
    }

    //serve? per ora lo lascio così -Luca
    @Override
    public void getUserByEmail(String email, Callback callback) {
        userRemoteDataSource.getUserByEmail(email, callback);
    }

    @Override
    public void resetPassword(String email, Callback callback) {
        // scrivo sto commento altrimenti Sonar mi picchia
    }
}
