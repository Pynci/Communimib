package it.unimib.communimib.datasource.user;

import com.google.firebase.auth.FirebaseAuth;

import it.unimib.communimib.Callback;

public class AuthDataSource implements IAuthDataSource {

    private FirebaseAuth auth;

    public AuthDataSource(){
        auth = FirebaseAuth.getInstance();
    }



}
