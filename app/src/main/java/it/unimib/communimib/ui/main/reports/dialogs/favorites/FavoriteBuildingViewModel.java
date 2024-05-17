package it.unimib.communimib.ui.main.reports.dialogs.favorites;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import it.unimib.communimib.DialogCallback;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.repository.IUserRepository;

public class FavoriteBuildingViewModel extends ViewModel {

    private final MutableLiveData<Result> getUserFavoriteBuildingsResult;
    private final MutableLiveData<Result> setUserFavoriteBuildingsResult;

    private final IUserRepository iUserRepository;
    public FavoriteBuildingViewModel (IUserRepository iUserRepository) {
        getUserFavoriteBuildingsResult = new MutableLiveData<>();
        setUserFavoriteBuildingsResult = new MutableLiveData<>();
        this.iUserRepository = iUserRepository;
    }

    public void setUserFavoriteBuildings(List<String> userInterests, DialogCallback dialogCallback) {
        iUserRepository.storeUserFavoriteBuildings(userInterests, setUserFavoriteBuildingsResult::postValue);
        dialogCallback.onComplete();
    }

    public void getUserFavoriteBuildings() {
        iUserRepository.readUserFavoriteBuildings(getUserFavoriteBuildingsResult::postValue);
    }

    public LiveData<Result> getGetUserFavoriteBuildingsResult() {
        return getUserFavoriteBuildingsResult;
    }

    public LiveData<Result> getSetUserFavoriteBuildingsResult() {
        return setUserFavoriteBuildingsResult;
    }
}
