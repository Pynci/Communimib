package it.unimib.communimib.repository;

import android.net.Uri;

import java.util.List;

import it.unimib.communimib.Callback;
import it.unimib.communimib.datasource.post.IPostRemoteDataSource;
import it.unimib.communimib.model.Post;
import it.unimib.communimib.model.User;

public class PostRepository implements IPostRepository{

    private IPostRemoteDataSource postRemoteDataSource;

    public PostRepository(IPostRemoteDataSource postRemoteDataSource) {
        this.postRemoteDataSource = postRemoteDataSource;
    }

    @Override
    public void readAllPosts(Callback addedCallback,
                             Callback changedCallback,
                             Callback removedCallback,
                             Callback cancelledCallback) {
        postRemoteDataSource.readAllPosts(addedCallback,changedCallback, removedCallback, cancelledCallback);
    }

    @Override
    public void readPostsByCategory(String category,
                                    Callback addedCallback,
                                    Callback changedCallback,
                                    Callback removedCallback,
                                    Callback cancelledCallback) {
        postRemoteDataSource.readPostsByCategory(category, addedCallback, changedCallback, removedCallback, cancelledCallback);
    }

    @Override
    public void readPostsByTitleOrDescription(String keyword,
                                              Callback addedCallback,
                                              Callback changedCallback,
                                              Callback removedCallback,
                                              Callback cancelledCallback) {
        postRemoteDataSource.readPostsByTitleOrDescription(keyword, addedCallback, changedCallback, removedCallback, cancelledCallback);
    }

    public void readPostsByTitleOrDescriptionAndCategory(String keyword,
                                                         String category,
                                                         Callback addedCallback,
                                                         Callback changedCallback,
                                                         Callback removedCallback,
                                                         Callback cancelledCallback){
        postRemoteDataSource.readPostsByTitleOrDescriptionAndCategory(keyword.toLowerCase(), category, addedCallback, changedCallback, removedCallback, cancelledCallback);
    }

    @Override
    public void createPost(String title, String description, String category, User author,
                           String email, String link, long timestamp, List<String> pictures,
                           Callback callback) {
        postRemoteDataSource.createPost(new Post(title, description, category, author, email, link, timestamp, pictures), callback);
    }

    @Override
    public void deletePost(Post post, Callback callback) {
        postRemoteDataSource.deletePost(post, callback);
    }
}
