package it.unimib.communimib.repository;

import android.util.Log;

import it.unimib.communimib.Callback;
import it.unimib.communimib.datasource.user.IAuthDataSource;
import it.unimib.communimib.datasource.user.IUserRemoteDataSource;
import it.unimib.communimib.model.Result;

public class UserRepository implements IUserRepository{

    private final IAuthDataSource authDataSource;
    private final IUserRemoteDataSource userRemoteDataSource;

    public UserRepository(IAuthDataSource authDataSource, IUserRemoteDataSource userRemoteDataSource){
        this.authDataSource = authDataSource;
        this.userRemoteDataSource = userRemoteDataSource;
    }

    @Override
    public void signUp(String email, String password, String name, String surname, Callback callback) {

        authDataSource.signUp(email, password, authResult -> {

            if(authResult.isSuccessful()){

                userRemoteDataSource.storeUserParameters(
                        ((Result.AuthSuccess) authResult).getUid(),
                        email,
                        name,
                        surname,
                        dbResult -> {

                    if(dbResult.isSuccessful()){
                        Log.d(this.getClass().getSimpleName(), "funziona");
                    }
                    else{
                        Log.d(this.getClass().getSimpleName(), "NON funziona il salvataggio su Realtime Database");
                        Log.d(this.getClass().getSimpleName(), ((Result.Error) dbResult).getMessage());
                    }

                });
            }
            else{
                Log.d(this.getClass().getSimpleName(), "NON funziona la registrazione su Firebase Authentication");
                Log.d(this.getClass().getSimpleName(), ((Result.Error) authResult).getMessage());
            }
        });
    }

    @Override
    public void signIn(String email, String password, Callback callback) {

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
