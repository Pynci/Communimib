package it.unimib.communimib.ui.main.reports.dialogs.reportcreation;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.unimib.communimib.ui.main.reports.dialogs.reportcreation.ReportsCreationViewModel;
import it.unimib.communimib.util.ServiceLocator;

public class ReportsCreationViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public ReportsCreationViewModelFactory (Context context) {
        this.context = context;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        return (T) new ReportsCreationViewModel(
                ServiceLocator.getInstance().getReportRepository(),
                ServiceLocator.getInstance().getUserRepository(context),
                ServiceLocator.getInstance().getTokenRepository());
    }
}
