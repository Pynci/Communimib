package it.unimib.communimib.ui.auth.usermanagement;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.unimib.communimib.repository.IUserRepository;
import it.unimib.communimib.ui.auth.usermanagement.EmailManagementViewModel;

public class EmailManagementViewModelFactory implements ViewModelProvider.Factory {

    private final IUserRepository userRepository;

    public EmailManagementViewModelFactory(IUserRepository userRepository){
        this.userRepository = userRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        return (T) new EmailManagementViewModel(userRepository);
    }
}