package it.unimib.communimib.ui.main.profile;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.communimib.model.Post;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;
import it.unimib.communimib.repository.IPostRepository;
import it.unimib.communimib.repository.IReportRepository;
import it.unimib.communimib.repository.IUserRepository;

public class ProfileViewModel extends ViewModel {

    private final IUserRepository userRepository;
    private final IPostRepository postRepository;
    private final IReportRepository reportRepository;

    private MutableLiveData<Result> addedPostResult;
    private MutableLiveData<Result> changedPostResult;
    private MutableLiveData<Result> removedPostResult;
    private MutableLiveData<Result> undoDeletePostResult;
    private MutableLiveData<Result> cancelledPostResult;

    private MutableLiveData<Result> addedReportResult;
    private MutableLiveData<Result> changedReportResult;
    private MutableLiveData<Result> removedReportResult;
    private MutableLiveData<Result> cancelledReportResult;

    private MutableLiveData<Result> updateUserPropicResult;
    private MutableLiveData<Result> updateUserNameAndSurnameResult;


    public ProfileViewModel(IUserRepository userRepository, IPostRepository postRepository, IReportRepository repository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.reportRepository = repository;

        addedPostResult = new MutableLiveData<>();
        changedPostResult = new MutableLiveData<>();
        removedPostResult = new MutableLiveData<>();
        undoDeletePostResult = new MutableLiveData<>();
        cancelledPostResult = new MutableLiveData<>();

        addedReportResult = new MutableLiveData<>();
        changedReportResult = new MutableLiveData<>();
        removedReportResult = new MutableLiveData<>();
        cancelledReportResult = new MutableLiveData<>();

        updateUserPropicResult = new MutableLiveData<>();
        updateUserNameAndSurnameResult = new MutableLiveData<>();
    }

    public User getCurrentUser(){
        return userRepository.getCurrentUser();
    }

    public void readPostsByUser(){
        String uid = getCurrentUser().getUid();
        postRepository.readPostsByUid(uid,
                postAdded -> addedPostResult.setValue(postAdded),
                postChanged -> changedPostResult.setValue(postChanged),
                postRemoved -> removedPostResult.setValue(postRemoved),
                postCancelled -> cancelledPostResult.setValue(postCancelled));
    }

    public void readReportsByUser(){
        String uid = getCurrentUser().getUid();
        reportRepository.readReportsByUid(uid,
                reportAdded -> addedReportResult.setValue(reportAdded),
                reportChanged -> changedReportResult.setValue(reportChanged),
                reportRemoved -> removedReportResult.setValue(reportRemoved),
                reportCancelled -> cancelledReportResult.setValue(reportCancelled));
    }

    public void updateUserParameters(Uri uri, String name, String surname) {
        //Prendo l'utente corrente
        User currentUser = getCurrentUser();

        //Se l'immagine profilo è diversa la devo aggiornare
        if(uri != null)
            userRepository.uploadPropic(uri, result -> updateUserPropicResult.postValue(result));

        //Se il nome ed il cognome sono diversi li devo aggiornare
        if(!currentUser.getName().equals(name) || !currentUser.getSurname().equals(surname))
            userRepository.updateUserNameAndSurname(name, surname, result -> updateUserNameAndSurnameResult.postValue(result));
    }

    public void deletePost(Post post){
        postRepository.deletePost(post, postDeleted -> removedPostResult.setValue(postDeleted));
    }

    public void undoDeletePost(Post post){
        postRepository.undoDeletePost(post, undoDeletePost -> undoDeletePostResult.setValue(undoDeletePost));
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

    public LiveData<Result> getUndoDeletePostResult(){
        return undoDeletePostResult;
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

    public LiveData<Result> getUpdateUserPropicResult() {
        return updateUserPropicResult;
    }

    public LiveData<Result> getUpdateUserNameAndSurnameResult() {
        return updateUserNameAndSurnameResult;
    }

    public void cleanViewModel(){
        addedPostResult = new MutableLiveData<>();
        changedPostResult = new MutableLiveData<>();
        removedPostResult = new MutableLiveData<>();
        undoDeletePostResult = new MutableLiveData<>();
        cancelledPostResult = new MutableLiveData<>();

        addedReportResult = new MutableLiveData<>();
        changedReportResult = new MutableLiveData<>();
        removedReportResult = new MutableLiveData<>();
        cancelledReportResult = new MutableLiveData<>();

        updateUserPropicResult = new MutableLiveData<>();
        updateUserNameAndSurnameResult = new MutableLiveData<>();
    }
}
