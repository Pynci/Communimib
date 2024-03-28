package it.unimib.communimib.datasource.user;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Result;

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
                            callback.onComplete(new Result.Error(task.getException().getLocalizedMessage()));
                        }
                    }
                    else{
                        callback.onComplete(new Result.Error(task.getException().getLocalizedMessage()));
                    }
                });
    }

}
