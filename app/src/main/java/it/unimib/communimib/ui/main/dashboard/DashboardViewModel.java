package it.unimib.communimib.ui.main.dashboard;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.communimib.model.Result;
import it.unimib.communimib.repository.IPostRepository;
import it.unimib.communimib.repository.IUserRepository;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<Result> postAddedReadResult;
    private MutableLiveData<Result> postEditedReadResult;
    private MutableLiveData<Result> postRemovedReadResult;
    private MutableLiveData<Result> postCancelledReadResult;
    private IPostRepository postRepository;
    private IUserRepository userRepository;

    public DashboardViewModel(IPostRepository postRepository, IUserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;

        this.postAddedReadResult = new MutableLiveData<>();
        this.postEditedReadResult = new MutableLiveData<>();
        this.postRemovedReadResult = new MutableLiveData<>();
        this.postCancelledReadResult = new MutableLiveData<>();
    }

    public void readAllPosts(){
        postRepository.readAllPosts(
                postAdded -> postAddedReadResult.setValue(postAdded),
                postEdited -> postEditedReadResult.setValue(postEdited),
                postRemoved -> postRemovedReadResult.setValue(postRemoved),
                postCancelled -> postCancelledReadResult.setValue(postCancelled)
        );
    }

    public void readPostsByCategory(String category){
        postRepository.readPostsByCategory(category,
                postAdded -> postAddedReadResult.setValue(postAdded),
                postEdited -> postEditedReadResult.setValue(postEdited),
                postRemoved -> postRemovedReadResult.setValue(postRemoved),
                postCancelled -> postCancelledReadResult.setValue(postCancelled)
        );
    }
    public MutableLiveData<Result> getPostAddedReadResult() {
        return postAddedReadResult;
    }

    public MutableLiveData<Result> getPostEditedReadResult() {
        return postEditedReadResult;
    }

    public MutableLiveData<Result> getPostRemovedReadResult() {
        return postRemovedReadResult;
    }

    public MutableLiveData<Result> getPostCancelledReadResult() {
        return postCancelledReadResult;
    }

    public void cleanViewModel(){
        this.postAddedReadResult = new MutableLiveData<>();
        this.postEditedReadResult = new MutableLiveData<>();
        this.postRemovedReadResult = new MutableLiveData<>();
        this.postCancelledReadResult = new MutableLiveData<>();
    }
}
