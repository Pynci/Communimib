package it.unimib.communimib.ui.main.reports;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.unimib.communimib.util.ServiceLocator;

public class ReportsViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;

    public ReportsViewModelFactory (Context context) {
        this.context = context;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        return (T) new ReportsViewModel(
                ServiceLocator.getInstance().getReportRepository(),
                ServiceLocator.getInstance().getUserRepository(context));
    }
}
