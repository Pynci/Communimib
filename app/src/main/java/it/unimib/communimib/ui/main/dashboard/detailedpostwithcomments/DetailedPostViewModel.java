package it.unimib.communimib.ui.main.dashboard.detailedpostwithcomments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.communimib.model.Result;
import it.unimib.communimib.repository.IPostRepository;
import it.unimib.communimib.repository.IUserRepository;

public class DetailedPostViewModel extends ViewModel {

    private IPostRepository postRepository;
    private IUserRepository userRepository;
    private MutableLiveData<Result> commentCreationResult;
    private MutableLiveData<Result> commentAddedReadResult;
    private MutableLiveData<Result> commentChangedReadResult;
    private MutableLiveData<Result> commentRemovedReadResult;
    private MutableLiveData<Result> readCancelledResult;

    public DetailedPostViewModel(IPostRepository postRepository, IUserRepository userRepository){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public void readCommentsByPid(String pid){
        postRepository.readCommentsByPid(pid,
                commentAdded -> commentAddedReadResult.setValue(commentAdded),
                commentChanged -> commentChangedReadResult.setValue(commentChanged),
                commentRemoved -> commentRemovedReadResult.setValue(commentRemoved),
                readCancelled -> readCancelledResult.setValue(readCancelled));
    }

    public void createComment(String pid, String text){
        postRepository.createComment(pid, userRepository.getCurrentUser(), text, result -> commentCreationResult.setValue(result));
    }

    public LiveData<Result> getCommentAddedReadResult() {
        return commentAddedReadResult;
    }

    public LiveData<Result> getCommentChangedReadResult() {
        return commentChangedReadResult;
    }

    public LiveData<Result> getCommentRemovedReadResult() {
        return commentRemovedReadResult;
    }

    public LiveData<Result> getReadCancelledResult() {
        return readCancelledResult;
    }

    public LiveData<Result> getCommentCreationResult() {
        return commentCreationResult;
    }

    public void cleanViewModel(){
        this.commentAddedReadResult = new MutableLiveData<>();
        this.commentChangedReadResult = new MutableLiveData<>();
        this.commentRemovedReadResult = new MutableLiveData<>();
        this.readCancelledResult = new MutableLiveData<>();
    }
}
