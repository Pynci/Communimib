package it.unimib.communimib.ui.main.profile;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.unimib.communimib.util.ServiceLocator;

public class CurrentUserProfileViewModelFactory implements ViewModelProvider.Factory {

    private Context context;

    public CurrentUserProfileViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CurrentUserProfileViewModel(
                ServiceLocator.getInstance().getUserRepository(context),
                ServiceLocator.getInstance().getPostRepository(),
                ServiceLocator.getInstance().getReportRepository());
    }
}
