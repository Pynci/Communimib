package it.unimib.communimib.datasource.post;

import it.unimib.communimib.Callback;

public interface ICommentRemoteDataSource {
    void readCommentsByPid(String pid,
                           Callback addedCallback,
                           Callback changedCallback,
                           Callback removedCallback,
                           Callback cancelledCallback);
}
