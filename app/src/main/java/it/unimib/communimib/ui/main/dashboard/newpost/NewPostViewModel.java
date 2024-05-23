package it.unimib.communimib.ui.main.dashboard.newpost;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;
import it.unimib.communimib.repository.IPostRepository;
import it.unimib.communimib.repository.IUserRepository;

public class NewPostViewModel extends ViewModel {

    private final IPostRepository postRepository;
    private final IUserRepository userRepository;
    private MutableLiveData<Result> postCreationResult;

    public NewPostViewModel(IPostRepository postRepository, IUserRepository userRepository){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        postCreationResult = new MutableLiveData<>();
    }

    public User getCurrentUser(){
        return userRepository.getCurrentUser();
    }

    public void createPost(String title, String description, String category, User author,
                           String email, String link, List<String> pictures){
        postRepository.createPost(title, description, category, author, email, link, pictures, result -> postCreationResult.setValue(result));
    }

    public LiveData<Result> getPostCreationResult() {
        return postCreationResult;
    }
}
