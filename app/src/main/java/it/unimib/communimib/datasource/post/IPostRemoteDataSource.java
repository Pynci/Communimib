package it.unimib.communimib.datasource.post;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Post;

public interface IPostRemoteDataSource {

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
    void createPost(Post post, Callback callback);

    void deletePost(Post post, Callback callback);

    void undoDeletePost(Post post, Callback callback);
}
