package it.unimib.communimib.ui.main.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.communimib.model.Report;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.repository.IPostRepository;
import it.unimib.communimib.repository.IReportRepository;

public class OtherUserProfileViewModel extends ViewModel {

    private final IPostRepository postRepository;
    private final IReportRepository reportRepository;

    private MutableLiveData<Result> addedPostResult;
    private MutableLiveData<Result> changedPostResult;
    private MutableLiveData<Result> removedPostResult;
    private MutableLiveData<Result> cancelledPostResult;

    private MutableLiveData<Result> addedReportResult;
    private MutableLiveData<Result> changedReportResult;
    private MutableLiveData<Result> removedReportResult;
    private MutableLiveData<Result> cancelledReportResult;
    private MutableLiveData<Result> closedReportResult;


    public OtherUserProfileViewModel(IPostRepository postRepository, IReportRepository repository) {
        this.postRepository = postRepository;
        this.reportRepository = repository;

        addedPostResult = new MutableLiveData<>();
        changedPostResult = new MutableLiveData<>();
        removedPostResult = new MutableLiveData<>();
        cancelledPostResult = new MutableLiveData<>();

        addedReportResult = new MutableLiveData<>();
        changedReportResult = new MutableLiveData<>();
        removedReportResult = new MutableLiveData<>();
        cancelledReportResult = new MutableLiveData<>();
        closedReportResult = new MutableLiveData<>();
    }

    public void readPostsByUser(String uid){
        postRepository.readPostsByUid(uid,
                postAdded -> addedPostResult.setValue(postAdded),
                postChanged -> changedPostResult.setValue(postChanged),
                postRemoved -> removedPostResult.setValue(postRemoved),
                postCancelled -> cancelledPostResult.setValue(postCancelled));
    }

    public void readReportsByUser(String uid){
        reportRepository.readReportsByUid(uid,
                reportAdded -> addedReportResult.setValue(reportAdded),
                reportChanged -> changedReportResult.setValue(reportChanged),
                reportRemoved -> removedReportResult.setValue(reportRemoved),
                reportCancelled -> cancelledReportResult.setValue(reportCancelled));
    }

    public void closeReport(Report report){
        reportRepository.deleteReport(report, reportDeleted -> closedReportResult.setValue(reportDeleted));
    }

    public LiveData<Result> getAddedPostResult() {
        return addedPostResult;
    }

    public LiveData<Result> getChangedPostResult() {
        return changedPostResult;
    }

    public LiveData<Result> getRemovedPostResult() {
        return removedPostResult;
    }

    public LiveData<Result> getCancelledPostResult() {
        return cancelledPostResult;
    }

    public LiveData<Result> getAddedReportResult() {
        return addedReportResult;
    }

    public LiveData<Result> getChangedReportResult() {
        return changedReportResult;
    }

    public LiveData<Result> getRemovedReportResult() {
        return removedReportResult;
    }

    public LiveData<Result> getCancelledReportResult() {
        return cancelledReportResult;
    }

    public LiveData<Result> getClosedReportResult() {
        return closedReportResult;
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
        closedReportResult = new MutableLiveData<>();
    }
}
