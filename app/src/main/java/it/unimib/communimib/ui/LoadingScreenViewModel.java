package it.unimib.communimib.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoadingScreenViewModel extends ViewModel {

    private MutableLiveData<Boolean> isReady;

    public LoadingScreenViewModel() {
        isReady = new MutableLiveData<>(false);
    }

    public LiveData<Boolean> areDataAvaible() {
        return isReady;
    }

    public void setDataAvaible() throws InterruptedException {
        Thread.sleep(3000);
        isReady.setValue(true);
    }
}
