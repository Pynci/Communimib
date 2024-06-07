package it.unimib.communimib.ui.main.dashboard;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.unimib.communimib.util.ServiceLocator;

public class DashboardViewModelFactory implements ViewModelProvider.Factory {

    public DashboardViewModelFactory() {
        // costruttore vuoto
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DashboardViewModel(
                ServiceLocator.getInstance().getPostRepository()
        );
    }
}
