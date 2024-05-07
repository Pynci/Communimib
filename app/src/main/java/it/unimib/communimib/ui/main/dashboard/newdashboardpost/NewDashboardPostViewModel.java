package it.unimib.communimib.ui.main.dashboard.newdashboardpost;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;
import it.unimib.communimib.repository.IPostRepository;
import it.unimib.communimib.repository.IUserRepository;
import it.unimib.communimib.repository.PostRepository;

public class NewDashboardPostViewModel extends ViewModel {

    private final IPostRepository postRepository;
    private final IUserRepository userRepository;
    private MutableLiveData<Result> postCreationResult;

    public NewDashboardPostViewModel(IPostRepository postRepository, IUserRepository userRepository){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        postCreationResult = new MutableLiveData<>();
    }

    public User getCurrentUser(){
        return userRepository.getCurrentUser();
    }

    public void createPost(String title, String description, String category, User author,
                           String email, String link, long timestamp, List<String> pictures){
        postRepository.createPost(title, description, category, author, email, link, timestamp, pictures, result -> postCreationResult.setValue(result));
    }

    public LiveData<Result> getPostCreationResult() {
        return postCreationResult;
    }
}
