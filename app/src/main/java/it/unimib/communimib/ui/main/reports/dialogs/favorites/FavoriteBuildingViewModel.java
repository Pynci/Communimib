package it.unimib.communimib.ui.main.reports.dialogs.favorites;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.communimib.model.Result;

public class FavoriteBuildingViewModel extends ViewModel {

    private final MutableLiveData<Result> getUserInterestsResult;
    private final MutableLiveData<Result> setUserInterestsResult;
    public FavoriteBuildingViewModel () {
        getUserInterestsResult = new MutableLiveData<>();
        setUserInterestsResult = new MutableLiveData<>();
    }
}
