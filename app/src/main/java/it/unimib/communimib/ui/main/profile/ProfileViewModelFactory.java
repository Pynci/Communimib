package it.unimib.communimib.ui.main.profile;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.unimib.communimib.util.ServiceLocator;

public class ProfileViewModelFactory implements ViewModelProvider.Factory {

    private Context context;

    public ProfileViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ProfileViewModel(
                ServiceLocator.getInstance().getUserRepository(context),
                ServiceLocator.getInstance().getPostRepository(),
                ServiceLocator.getInstance().getReportRepository());
    }
}
