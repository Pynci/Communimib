package it.unimib.communimib.repository;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Post;
import it.unimib.communimib.model.User;

public interface IPostRepository {

    public void readAllPosts(Callback addedCallback,
                             Callback changedCallback,
                             Callback removedCallback,
                             Callback cancelledCallback);

    public void readPostsByCategory(String category,
                                    Callback addedCallback,
                                    Callback changedCallback,
                                    Callback removedCallback,
                                    Callback cancelledCallback);

    public void createPost(String title, String description, String category, User author, String email, String link, long timestamp, Callback callback);

    public void deletePost(Post post, Callback callback);
}
