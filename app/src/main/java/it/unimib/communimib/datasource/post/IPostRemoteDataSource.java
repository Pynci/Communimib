package it.unimib.communimib.datasource.post;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Post;

public interface IPostRemoteDataSource {

    public void readAllPosts(Callback addedCallback,
                             Callback changedCallback,
                             Callback removedCallback,
                             Callback cancelledCallback);

    public void readPostsByCategory(String category,
                                    Callback addedCallback,
                                    Callback changedCallback,
                                    Callback removedCallback,
                                    Callback cancelledCallback);
    public void createPost(Post post, Callback callback);

    public void deletePost(Post post, Callback callback);
}
