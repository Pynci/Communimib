package it.unimib.communimib.datasource.user;

import java.util.ArrayList;
import java.util.List;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;
import it.unimib.communimib.util.ErrorMapper;

public class FakeAuthDataSource implements IAuthDataSource {

    private List<User> signedupUsers;
    private User currentUser;
    private boolean emailVerified;

    public FakeAuthDataSource(){
        signedupUsers = new ArrayList<>();
        currentUser = null;
        emailVerified = false;
    }

    @Override
    public boolean isSessionStillActive() {
        return currentUser != null;
    }

    @Override
    public String getCurrentUserEmail() {
        if(isSessionStillActive()){
            return currentUser.getEmail();
        }
        else{
            return "";
        }
    }

    @Override
    public void signUp(String email, String password, Callback callback) {
        boolean emailAlreadyInUse = false;
        for(int i = 0; i < signedupUsers.size(); i++) {
            if(signedupUsers.get(i).getEmail().equals(email)){
                emailAlreadyInUse = true;
            }
        }
        if(emailAlreadyInUse){
            callback.onComplete(new Result.Error(ErrorMapper.SIGNUP_ERROR));
        }
        else{
            currentUser = new User(email);
            signedupUsers.add(new User(email));
            callback.onComplete(new Result.SignupSuccess("12345"));
        }
    }

    @Override
    public void sendEmailVerification(Callback callback) {
        if(isSessionStillActive()){
            emailVerified = true;
            callback.onComplete(new Result.Success());
        }
        else{
            callback.onComplete(new Result.Error(ErrorMapper.USER_NOT_AUTHENTICATED_ERROR));
        }

    }

    @Override
    public void signIn(String email, String password, Callback callback) {
        String correctPassword = "password";
        boolean userExists = false;
        for(int i = 0; i < signedupUsers.size(); i++) {
            if(signedupUsers.get(i).getEmail().equals(email)){
                userExists = true;
                if(correctPassword.equals(password)){
                    currentUser = signedupUsers.get(i);
                    callback.onComplete(new Result.Success());
                }
                else{
                    callback.onComplete(new Result.Error(ErrorMapper.SIGNIN_ERROR));
                }
            }
        }
        if(!userExists){
            callback.onComplete(new Result.Error(ErrorMapper.SIGNIN_ERROR));
        }
    }

    @Override
    public void isEmailVerified(Callback callback) {
        if(isSessionStillActive()){
            callback.onComplete(new Result.BooleanSuccess(emailVerified));
        }
        else{
            callback.onComplete(new Result.Error(ErrorMapper.USER_NOT_AUTHENTICATED_ERROR));
        }

    }

    @Override
    public void signOut(Callback callback) {
        if(isSessionStillActive()){
            currentUser = null;
            callback.onComplete(new Result.Success());
        }
        else{
            callback.onComplete(new Result.Error(ErrorMapper.USER_NOT_AUTHENTICATED_ERROR));
        }
    }

    @Override
    public void resetPassword(String email, Callback callback) {
        callback.onComplete(new Result.Success());
    }
}
