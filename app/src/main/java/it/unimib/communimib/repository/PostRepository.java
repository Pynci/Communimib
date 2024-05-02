package it.unimib.communimib.repository;

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
    public void createPost(String title, String description, User author, Callback callback) {

    }

    @Override
    public void deletePost(Post post, Callback callback) {

    }
}
