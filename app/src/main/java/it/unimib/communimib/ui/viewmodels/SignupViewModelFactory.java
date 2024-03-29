package it.unimib.communimib.ui.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


public class SignupViewModelFactory implements ViewModelProvider.Factory {

    public SignupViewModelFactory(){

    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        return (T) new SignupViewModel();
    }
}
