package it.unimib.communimib.ui.main.reports.dialogs.favorites;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import it.unimib.communimib.DialogCallback;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.repository.IUserRepository;

public class FavoriteBuildingViewModel extends ViewModel {

    private final MutableLiveData<Result> getUserInterestsResult;
    private final MutableLiveData<Result> setUserInterestsResult;

    private final IUserRepository iUserRepository;
    public FavoriteBuildingViewModel (IUserRepository iUserRepository) {
        getUserInterestsResult = new MutableLiveData<>();
        setUserInterestsResult = new MutableLiveData<>();
        this.iUserRepository = iUserRepository;
    }

    public void setUserInterests(List<String> userInterests, DialogCallback dialogCallback) {
        Log.d("Pizza", userInterests.toString());
        dialogCallback.onComplete();
    }

    public LiveData<Result> getUserInterestsResult () {
        return getUserInterestsResult;
    }

    public LiveData<Result> getSetUserInterestsResult() {
        return setUserInterestsResult;
    }
}
