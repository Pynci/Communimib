package it.unimib.communimib.ui.main.dashboard;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.unimib.communimib.ui.main.dashboard.DashboardViewModel;
import it.unimib.communimib.util.ServiceLocator;

public class DashboardViewModelFactory implements ViewModelProvider.Factory {

    private Context context;

    public DashboardViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DashboardViewModel(
                ServiceLocator.getInstance().getPostRepository(),
                ServiceLocator.getInstance().getUserRepository(context)
        );
    }
}
