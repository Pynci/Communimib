package it.unimib.communimib.ui.main.dashboard.detailedpostwithcomments;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.unimib.communimib.util.ServiceLocator;

public class DetailedPostViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public DetailedPostViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailedPostViewModel(
                ServiceLocator.getInstance().getPostRepository(),
                ServiceLocator.getInstance().getUserRepository(context)
        );
    }
}
