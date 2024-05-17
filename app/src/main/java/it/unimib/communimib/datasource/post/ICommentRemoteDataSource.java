package it.unimib.communimib.datasource.post;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Comment;

public interface ICommentRemoteDataSource {
    void readCommentsByPid(String pid,
                           Callback addedCallback,
                           Callback changedCallback,
                           Callback removedCallback,
                           Callback cancelledCallback);

    void createComment(String pid, Comment comment, Callback callback);
}
