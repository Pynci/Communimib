package it.unimib.communimib.datasource.user;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.util.ErrorMapper;

public class AuthDataSource implements IAuthDataSource {

    private final FirebaseAuth auth;
    private FirebaseUser firebaseUser;

    public AuthDataSource(){
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean isSessionStillActive(){
        firebaseUser = auth.getCurrentUser();
        return firebaseUser != null;
    }

    @Override
    public String getCurrentUserEmail(){
        if(isSessionStillActive()){
            return firebaseUser.getEmail();
        }
        else{
            return "";
        }
    }

    @Override
    public void signUp(String email, String password, Callback callback){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        firebaseUser = auth.getCurrentUser();
                        if(firebaseUser != null){
                            callback.onComplete(new Result.SignupSuccess(firebaseUser.getUid()));
                        }
                        else{
                            callback.onComplete(new Result.Error(ErrorMapper.SIGNUP_FIREBASE_USER_ERROR));
                        }
                    }
                    else{
                        callback.onComplete(new Result.Error(ErrorMapper.SIGNUP_ERROR));
                    }
                });
    }

    @Override
    public void signIn(String email, String password, Callback callback){
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        callback.onComplete(new Result.Success());
                    }
                    else{
                        callback.onComplete(new Result.Error(ErrorMapper.SIGNIN_ERROR));
                    }
                });
    }

    @Override
    public void isEmailVerified(Callback callback) {
        if(isSessionStillActive()){
            firebaseUser.reload();
            callback.onComplete(new Result.BooleanSuccess(firebaseUser.isEmailVerified()));
        }
        else{
            callback.onComplete(new Result.Error(ErrorMapper.USER_NOT_AUTHENTICATED_ERROR));
        }
    }

    @Override
    public void sendEmailVerification(Callback callback) {
        if(isSessionStillActive()) {
            firebaseUser.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            callback.onComplete(new Result.Success());
                        }
                        else{
                            callback.onComplete(new Result.Error(ErrorMapper.EMAIL_SENDING_ERROR));
                        }
                    });
        }
        else{
            callback.onComplete(new Result.Error(ErrorMapper.USER_NOT_AUTHENTICATED_ERROR));
        }
    }

    @Override
    public void signOut(Callback callback){
        if(isSessionStillActive()){
            auth.signOut();
            callback.onComplete(new Result.Success());
        }
        else{
            callback.onComplete(new Result.Error(ErrorMapper.USER_NOT_AUTHENTICATED_ERROR));
        }
    }

    @Override
    public void resetPassword(Callback callback){
        if(isSessionStillActive()){
            auth.sendPasswordResetEmail(firebaseUser.getEmail())
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            callback.onComplete(new Result.Success());
                        }
                        else{
                            callback.onComplete(new Result.Error(ErrorMapper.EMAIL_SENDING_ERROR));
                        }
                    });
        }
        else{
            callback.onComplete(new Result.Error(ErrorMapper.USER_NOT_AUTHENTICATED_ERROR));
        }
    }

}
