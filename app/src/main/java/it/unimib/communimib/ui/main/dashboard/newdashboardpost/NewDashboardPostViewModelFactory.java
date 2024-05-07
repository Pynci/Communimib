package it.unimib.communimib.ui.main.dashboard.newdashboardpost;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.unimib.communimib.util.ServiceLocator;

public class NewDashboardPostViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public NewDashboardPostViewModelFactory (Context context) {
        this.context = context;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        return (T) new NewDashboardPostViewModel(
                ServiceLocator.getInstance().getPostRepository(),
                ServiceLocator.getInstance().getUserRepository(context)
        );
    }
}
