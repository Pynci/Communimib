package it.unimib.communimib.ui.loading;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.unimib.communimib.util.ServiceLocator;

public class LoadingScreenViewModelFactory implements ViewModelProvider.Factory {

    private Context context;

    public LoadingScreenViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        return (T) new LoadingScreenViewModel(ServiceLocator.getInstance().getUserRepository(context));
    }
}
