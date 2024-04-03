package it.unimib.communimib.ui.auth.signup;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.unimib.communimib.ui.auth.signup.SignupViewModel;
import it.unimib.communimib.util.ServiceLocator;


public class SignupViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;

    public SignupViewModelFactory(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        return (T) new SignupViewModel(ServiceLocator.getInstance().getUserRepository(context));
    }
}
