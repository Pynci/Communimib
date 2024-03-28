package it.unimib.communimib.datasource.user;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Result;

public class AuthDataSource implements IAuthDataSource {

    private FirebaseAuth auth;
    private FirebaseUser fbUser;

    public AuthDataSource(){
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void signUp(String email, String password, Callback callback){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        fbUser = auth.getCurrentUser();
                        if(fbUser != null){
                            callback.onComplete(new Result.Success());
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
