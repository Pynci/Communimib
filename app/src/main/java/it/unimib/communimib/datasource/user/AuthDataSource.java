package it.unimib.communimib.datasource.user;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.util.ErrorMapper;

public class AuthDataSource implements IAuthDataSource {

    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;

    public AuthDataSource(){
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void signUp(String email, String password, Callback callback){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        firebaseUser = auth.getCurrentUser();
                        if(firebaseUser != null){
                            callback.onComplete(new Result.AuthSuccess(firebaseUser.getUid()));
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

}
