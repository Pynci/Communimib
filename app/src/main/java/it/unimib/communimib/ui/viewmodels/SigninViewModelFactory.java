package it.unimib.communimib.ui.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.unimib.communimib.repository.IUserRepository;

public class SigninViewModelFactory implements ViewModelProvider.Factory {

    private final IUserRepository userRepository;

    public SigninViewModelFactory(IUserRepository userRepository){
        this.userRepository = userRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        return (T) new SigninViewModel(userRepository);
    }
}
