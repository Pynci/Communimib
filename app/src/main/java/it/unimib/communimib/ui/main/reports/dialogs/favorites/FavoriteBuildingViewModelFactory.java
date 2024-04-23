package it.unimib.communimib.ui.main.reports.dialogs.favorites;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.unimib.communimib.util.ServiceLocator;

public class FavoriteBuildingViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public FavoriteBuildingViewModelFactory (Context context) {
        this.context = context;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        return (T) new FavoriteBuildingViewModel(
                ServiceLocator.getInstance().getUserRepository(context));
    }
}