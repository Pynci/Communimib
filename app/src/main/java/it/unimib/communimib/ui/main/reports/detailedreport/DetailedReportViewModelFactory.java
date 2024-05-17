package it.unimib.communimib.ui.main.reports.detailedreport;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.unimib.communimib.util.ServiceLocator;

public class DetailedReportViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;

    public DetailedReportViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailedReportViewModel(
                ServiceLocator.getInstance().getUserRepository(context),
                ServiceLocator.getInstance().getReportRepository());
    }
}
