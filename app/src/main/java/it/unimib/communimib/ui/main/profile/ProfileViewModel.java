package it.unimib.communimib.ui.main.profile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.communimib.model.Result;
import it.unimib.communimib.repository.IPostRepository;
import it.unimib.communimib.repository.IReportRepository;
import it.unimib.communimib.repository.IUserRepository;

public class ProfileViewModel extends ViewModel {

    private final IUserRepository userRepository;
    private final IPostRepository postRepository;
    private final IReportRepository repository;

    private MutableLiveData<Result> addedPostResult;
    private MutableLiveData<Result> changedPostResult;
    private MutableLiveData<Result> removedPostResult;
    private MutableLiveData<Result> cancelledPostResult;

    private MutableLiveData<Result> addedReportResult;
    private MutableLiveData<Result> changedReportResult;
    private MutableLiveData<Result> removedReportResult;
    private MutableLiveData<Result> cancelledReportResult;


    public ProfileViewModel(IUserRepository userRepository, IPostRepository postRepository, IReportRepository repository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.repository = repository;

        addedPostResult = new MutableLiveData<>();
        changedPostResult = new MutableLiveData<>();
        removedPostResult = new MutableLiveData<>();
        cancelledPostResult = new MutableLiveData<>();

        addedReportResult = new MutableLiveData<>();
        changedReportResult = new MutableLiveData<>();
        removedReportResult = new MutableLiveData<>();
        cancelledReportResult = new MutableLiveData<>();
    }

    public void getCurrentUser(){
        userRepository.getCurrentUser();
    }

    public void readPostsByUser(){

    }

    public MutableLiveData<Result> getAddedPostResult() {
        return addedPostResult;
    }

    public MutableLiveData<Result> getChangedPostResult() {
        return changedPostResult;
    }

    public MutableLiveData<Result> getRemovedPostResult() {
        return removedPostResult;
    }

    public MutableLiveData<Result> getCancelledPostResult() {
        return cancelledPostResult;
    }

    public MutableLiveData<Result> getAddedReportResult() {
        return addedReportResult;
    }

    public MutableLiveData<Result> getChangedReportResult() {
        return changedReportResult;
    }

    public MutableLiveData<Result> getRemovedReportResult() {
        return removedReportResult;
    }

    public MutableLiveData<Result> getCancelledReportResult() {
        return cancelledReportResult;
    }

    public void cleanViewModel(){
        addedPostResult = new MutableLiveData<>();
        changedPostResult = new MutableLiveData<>();
        removedPostResult = new MutableLiveData<>();
        cancelledPostResult = new MutableLiveData<>();

        addedReportResult = new MutableLiveData<>();
        changedReportResult = new MutableLiveData<>();
        removedReportResult = new MutableLiveData<>();
        cancelledReportResult = new MutableLiveData<>();
    }
}
