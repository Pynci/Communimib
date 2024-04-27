package it.unimib.communimib.repository;

import android.net.Uri;
import android.util.Log;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import it.unimib.communimib.Callback;
import it.unimib.communimib.datasource.user.IAuthDataSource;
import it.unimib.communimib.datasource.user.IUserLocalDataSource;
import it.unimib.communimib.datasource.user.IUserRemoteDataSource;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;
import it.unimib.communimib.util.Constants;
import it.unimib.communimib.util.ErrorMapper;
import it.unimib.communimib.util.ServiceLocator;

public class UserRepository implements IUserRepository{

    private static volatile IUserRepository INSTANCE;
    private ScheduledExecutorService pollingExecutor =
            Executors.newSingleThreadScheduledExecutor();
    private final IAuthDataSource authDataSource;
    private final IUserRemoteDataSource userRemoteDataSource;
    private final IUserLocalDataSource userLocalDataSource;
    private User currentUser;
    private long lastFavoriteBuildingsUpdate;

    private UserRepository(IAuthDataSource authDataSource, IUserRemoteDataSource userRemoteDataSource, IUserLocalDataSource localDataSource){
        this.authDataSource = authDataSource;
        this.userRemoteDataSource = userRemoteDataSource;
        this.userLocalDataSource = localDataSource;
        this.lastFavoriteBuildingsUpdate = -1;
    }

    public static IUserRepository getInstance(IAuthDataSource authDataSource, IUserRemoteDataSource userRemoteDataSource, IUserLocalDataSource localDataSource) {
        if (INSTANCE == null) {
            synchronized(ServiceLocator.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UserRepository(authDataSource, userRemoteDataSource, localDataSource);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public User getCurrentUser(){
        return currentUser;
    }

    @Override
    public void signUp(String email, String password, String name, String surname, Callback callback) {

        authDataSource.signUp(email, password, authResult -> {
            if(authResult.isSuccessful()){
                userRemoteDataSource.storeUserParameters(((Result.SignupSuccess) authResult).getUid(), email, name, surname, isUnimibEmployee(email), dbResult -> {
                    if(dbResult.isSuccessful()) {
                        currentUser = new User(((Result.SignupSuccess) authResult).getUid(), email, name, surname, isUnimibEmployee(email));
                        userLocalDataSource.insertUser(currentUser, callback);
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
                getUserByEmail(email, remoteResult -> {
                    if(remoteResult.isSuccessful()){
                        currentUser = ((Result.UserSuccess) remoteResult).getUser();
                        userLocalDataSource.insertUser(currentUser, callback);
                    }
                    else{
                        callback.onComplete(remoteResult);
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
                userLocalDataSource.deleteUser(localResult -> {
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
            userLocalDataSource.deleteUser(localResult -> {
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
    public void sendEmailVerification(Callback callback) {
        authDataSource.sendEmailVerification(callback);
    }

    @Override
    public void isEmailVerified(Callback callback){
        authDataSource.isEmailVerified(callback);
    }

    @Override
    public void startEmailPolling(Callback callback){
        if (pollingExecutor == null || pollingExecutor.isShutdown()) {
            pollingExecutor = Executors.newSingleThreadScheduledExecutor();
        }

        pollingExecutor.scheduleAtFixedRate(() -> {
            isEmailVerified(result -> {
                Log.d(this.getClass().getSimpleName(), "MAIL: sto controllando...");
                if(result.isSuccessful()){
                    if(((Result.BooleanSuccess)result).getBoolean()){
                        stopEmailPolling();
                        callback.onComplete(new Result.Success());
                    }
                }
                else{
                    stopEmailPolling();
                    callback.onComplete(result);
                }
            });
        }, 0, 4, TimeUnit.SECONDS);
    }

    @Override
    public void stopEmailPolling(){
        if (pollingExecutor != null && !pollingExecutor.isShutdown()){
            pollingExecutor.shutdown();
        }
    }

    @Override
    public void updateUserNameAndSurname(String name, String surname, Callback callback) {
        if(currentUser != null){
            userRemoteDataSource.updateNameAndSurname(currentUser.getUid(), name, surname, remoteResult -> {
                if(remoteResult.isSuccessful()){
                    currentUser.setName(name);
                    currentUser.setSurname(surname);
                    userLocalDataSource.updateUser(currentUser, callback);
                }
                else{
                    callback.onComplete(remoteResult);
                }
            });
        }
        else{
            callback.onComplete(new Result.Error(ErrorMapper.USER_NOT_AUTHENTICATED_ERROR));
        }
    }

    @Override
    public void uploadPropic(Uri localUri, Callback callback){
        if(currentUser != null){
            userRemoteDataSource.uploadPropic(currentUser.getUid(), localUri, remoteResult -> {
                if(remoteResult.isSuccessful()){
                    String remoteDownloadUri = ((Result.UriSuccess) remoteResult).getUri();
                    currentUser.setPropic(remoteDownloadUri);
                    userLocalDataSource.updateUser(currentUser, callback);
                }
                else{
                    callback.onComplete(remoteResult);
                }
            });
        }
        else{
            callback.onComplete(new Result.Error(ErrorMapper.USER_NOT_AUTHENTICATED_ERROR));
        }
    }

    @Override
    public void getUserByEmail(String email, Callback callback) {
        userRemoteDataSource.getUserByEmail(email, callback);
    }

    @Override
    public void resetPassword(String email, Callback callback) {
        authDataSource.resetPassword(email, callback);
    }

    @Override
    public void storeUserFavoriteBuildings(List<String> userInterests, Callback callback) {
        if(currentUser != null){
            userRemoteDataSource.storeUserFavoriteBuildings(userInterests, getCurrentUser().getUid(), resultRemote -> {
                if(resultRemote.isSuccessful()){
                    userLocalDataSource.saveUserFavoriteBuildings(userInterests, callback);
                }
                else{
                    callback.onComplete(resultRemote);
                }
            });
        }
    }

    public void readUserFavoriteBuildings(Callback callback) {

        long currentTime = System.currentTimeMillis();

        if(currentTime - lastFavoriteBuildingsUpdate > Constants.FAVORITE_BUILDINGS_TIMEOUT){
            userRemoteDataSource.getUserFavoriteBuildings(currentUser.getUid(), remoteResult -> {
                if(remoteResult.isSuccessful()){
                    userLocalDataSource.saveUserFavoriteBuildings(((Result.UserFavoriteBuildings) remoteResult).getFavoriteBuildings(), localResult -> {
                        if(localResult.isSuccessful()){
                            lastFavoriteBuildingsUpdate = currentTime;
                            callback.onComplete(remoteResult);
                        }
                        else{
                            callback.onComplete(localResult);
                        }
                    });
                }
                else{
                    callback.onComplete(remoteResult);
                }
            });
        }
        else{
            userLocalDataSource.getUserFavoriteBuildings(localResult -> {
                if(localResult.isSuccessful()){
                    callback.onComplete(localResult);
                }
                else{
                    userRemoteDataSource.getUserFavoriteBuildings(currentUser.getUid(), remoteResult -> {
                        if(remoteResult.isSuccessful()){
                            userLocalDataSource.saveUserFavoriteBuildings(((Result.UserFavoriteBuildings) remoteResult).getFavoriteBuildings(), callback);
                        }
                        else{
                            callback.onComplete(remoteResult);
                        }
                    });
                }
            });
        }
    }

    private boolean isUnimibEmployee(String email){
        return email.substring(email.indexOf("@")).equals("@unimib.it");
    }
}
