package it.unimib.communimib.datasource.dashboard;

import java.util.HashMap;
import java.util.Map;

import it.unimib.communimib.Callback;
import it.unimib.communimib.datasource.post.IPostRemoteDataSource;
import it.unimib.communimib.model.Post;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.util.ErrorMapper;

public class FakeDashboardRemoteDataSource implements IPostRemoteDataSource {

    public Map<String, Post> posts;

    public FakeDashboardRemoteDataSource() {
        posts = new HashMap<>();
    }

    @Override
    public void readAllPosts(Callback addedCallback, Callback changedCallback, Callback removedCallback, Callback cancelledCallback) {

    }

    @Override
    public void readPostsByCategory(String category, Callback addedCallback, Callback changedCallback, Callback removedCallback, Callback cancelledCallback) {

    }

    @Override
    public void readPostsByTitleOrDescription(String keyword, Callback addedCallback, Callback changedCallback, Callback removedCallback, Callback cancelledCallback) {

    }

    @Override
    public void readPostsByTitleOrDescriptionAndCategory(String keyword, String category, Callback addedCallback, Callback changedCallback, Callback removedCallback, Callback cancelledCallback) {

    }

    @Override
    public void readPostsByUid(String uid, Callback addedCallback, Callback changedCallback, Callback removedCallback, Callback cancelledCallback) {

    }

    @Override
    public void createPost(Post post, Callback callback) {
        post.setPid("12345");
        posts.put(post.getPid(), post);
        callback.onComplete(new Result.Success());
    }



    @Override
    public void deletePost(Post post, Callback callback) {
        if(posts.containsKey(post.getPid())){
            posts.remove(post.getPid());
            callback.onComplete(new Result.Success());
        } else {
            callback.onComplete(new Result.Error(ErrorMapper.POST_DELETING_ERROR));
        }
    }

    @Override
    public void undoDeletePost(Post post, Callback callback) {

    }
}
