package it.unimib.communimib.ui.main.reports.dialogs.filters;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import it.unimib.communimib.DialogCallback;

public class FiltersViewModel extends ViewModel {

    private MutableLiveData<List<String>> chosenFilter;

    public FiltersViewModel() {
        chosenFilter = new MutableLiveData<>();
    }

    public LiveData<List<String>> getChosenFilter() {
        return chosenFilter;
    }

    protected void setFilters(List<String> list, DialogCallback callback) {
        chosenFilter.setValue(list);
        callback.onComplete();
    }

    public void cleanViewModel(){
        chosenFilter = new MutableLiveData<>();
    }
}
