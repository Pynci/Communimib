package it.unimib.communimib.ui.main.dashboard.newpost;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.unimib.communimib.util.ServiceLocator;

public class NewPostViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public NewPostViewModelFactory(Context context) {
        this.context = context;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        return (T) new NewPostViewModel(
                ServiceLocator.getInstance().getPostRepository(),
                ServiceLocator.getInstance().getUserRepository(context)
        );
    }
}
