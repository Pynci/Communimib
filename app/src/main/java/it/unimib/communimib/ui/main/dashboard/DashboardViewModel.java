package it.unimib.communimib.ui.main.dashboard;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.communimib.model.Result;
import it.unimib.communimib.repository.IPostRepository;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<Result> postAddedReadResult;
    private MutableLiveData<Result> postChangedReadResult;
    private MutableLiveData<Result> postRemovedReadResult;
    private MutableLiveData<Result> readCancelledResult;
    private final IPostRepository postRepository;
    private String visualizedCategory;

    public DashboardViewModel(IPostRepository postRepository) {
        this.postRepository = postRepository;
        this.visualizedCategory = "Tutti";

        this.postAddedReadResult = new MutableLiveData<>();
        this.postChangedReadResult = new MutableLiveData<>();
        this.postRemovedReadResult = new MutableLiveData<>();
        this.readCancelledResult = new MutableLiveData<>();
    }

    public void readAllPosts(){
        postRepository.readAllPosts(
                postAdded -> postAddedReadResult.setValue(postAdded),
                postChanged -> postChangedReadResult.setValue(postChanged),
                postRemoved -> postRemovedReadResult.setValue(postRemoved),
                readCancelled -> readCancelledResult.setValue(readCancelled)
        );
    }
    public void readPostsByCategory(String category){
        postRepository.readPostsByCategory(category,
                postAdded -> postAddedReadResult.setValue(postAdded),
                postEdited -> postChangedReadResult.setValue(postEdited),
                postRemoved -> postRemovedReadResult.setValue(postRemoved),
                readCancelled -> readCancelledResult.setValue(readCancelled)
        );
    }

    public void readPostsByTitleOrDescription(String keyword){
        if(getVisualizedCategory().equals("Tutti")){
            postRepository.readPostsByTitleOrDescription(keyword,
                    postAdded -> postAddedReadResult.setValue(postAdded),
                    postEdited -> postChangedReadResult.setValue(postEdited),
                    postRemoved -> postRemovedReadResult.setValue(postRemoved),
                    readCancelled -> readCancelledResult.setValue(readCancelled));
        } else {
            postRepository.readPostsByTitleOrDescriptionAndCategory(keyword,
                    getVisualizedCategory(),
                    postAdded -> postAddedReadResult.setValue(postAdded),
                    postEdited -> postChangedReadResult.setValue(postEdited),
                    postRemoved -> postRemovedReadResult.setValue(postRemoved),
                    readCancelled -> readCancelledResult.setValue(readCancelled));
        }
    }


    public MutableLiveData<Result> getPostAddedReadResult() {
        return postAddedReadResult;
    }

    public MutableLiveData<Result> getPostChangedReadResult() {
        return postChangedReadResult;
    }

    public MutableLiveData<Result> getPostRemovedReadResult() {
        return postRemovedReadResult;
    }

    public MutableLiveData<Result> getReadCancelledResult() {
        return readCancelledResult;
    }

    public String getVisualizedCategory() {
        return visualizedCategory;
    }

    public void setVisualizedCategory(String visualizedCategory) {
        this.visualizedCategory = visualizedCategory;
    }

    public void cleanViewModel(){
        this.postAddedReadResult = new MutableLiveData<>();
        this.postChangedReadResult = new MutableLiveData<>();
        this.postRemovedReadResult = new MutableLiveData<>();
        this.readCancelledResult = new MutableLiveData<>();
    }
}
