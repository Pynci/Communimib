package it.unimib.communimib.ui.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SigninViewModelFactory implements ViewModelProvider.Factory {

    public SigninViewModelFactory(){

    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        return (T) new SigninViewModel();
    }
}
