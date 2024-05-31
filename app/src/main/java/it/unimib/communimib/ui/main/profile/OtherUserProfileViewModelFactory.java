package it.unimib.communimib.ui.main.profile;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.unimib.communimib.util.ServiceLocator;

public class OtherUserProfileViewModelFactory implements ViewModelProvider.Factory {

    public OtherUserProfileViewModelFactory() {
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new OtherUserProfileViewModel(
                ServiceLocator.getInstance().getPostRepository(),
                ServiceLocator.getInstance().getReportRepository());
    }
}
