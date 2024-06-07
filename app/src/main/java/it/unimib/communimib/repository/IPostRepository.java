package it.unimib.communimib.repository;

import java.util.List;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Post;
import it.unimib.communimib.model.User;

public interface IPostRepository {

    void readAllPosts(Callback addedCallback,
                             Callback changedCallback,
                             Callback removedCallback,
                             Callback cancelledCallback);

    void readPostsByCategory(String category,
                                    Callback addedCallback,
                                    Callback changedCallback,
                                    Callback removedCallback,
                                    Callback cancelledCallback);

    void readPostsByTitleOrDescription(String keyword,
                                              Callback addedCallback,
                                              Callback changedCallback,
                                              Callback removedCallback,
                                              Callback cancelledCallback);

    void readPostsByTitleOrDescriptionAndCategory(String keyword,
                                              String category,
                                              Callback addedCallback,
                                              Callback changedCallback,
                                              Callback removedCallback,
                                              Callback cancelledCallback);
    void readPostsByUid(String uid,
                               Callback addedCallback,
                               Callback changedCallback,
                               Callback removedCallback,
                               Callback cancelledCallback);

    void createPost(String title, String description, String category, User author, String email, String link, List<String> pictures, Callback callback);

    void deletePost(Post post, Callback callback);

    void undoDeletePost(Post post, Callback callback);

    void readCommentsByPid(String pid,
                           Callback addedCallback,
                           Callback changedCallback,
                           Callback removedCallback,
                           Callback cancelledCallback);

    void createComment(String pid, User author, String text, Callback callback);
}
