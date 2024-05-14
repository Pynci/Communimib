package it.unimib.communimib.datasource.dashboard;

import java.util.Map;

import it.unimib.communimib.Callback;
import it.unimib.communimib.datasource.post.IPostRemoteDataSource;
import it.unimib.communimib.model.Post;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.util.ErrorMapper;

public class FakeDashboardRemoteDataSource implements IPostRemoteDataSource {

    private Callback addedCallback;
    private Callback changedCallback;
    private Callback removedCallback;
    private Callback cancelledCallback;

    private String category;
    private String keyword;
    private Map<String, Post> posts;

    @Override
    public void readAllPosts(Callback addedCallback, Callback changedCallback, Callback removedCallback, Callback cancelledCallback) {
        this.addedCallback = addedCallback;
        this.changedCallback = changedCallback;
        this.removedCallback = removedCallback;
        this.cancelledCallback = cancelledCallback;
    }

    @Override
    public void readPostsByCategory(String category, Callback addedCallback, Callback changedCallback, Callback removedCallback, Callback cancelledCallback) {
        this.category = category;
        this.addedCallback = addedCallback;
        this.changedCallback = changedCallback;
        this.removedCallback = removedCallback;
        this.cancelledCallback = cancelledCallback;
    }

    @Override
    public void readPostsByTitleOrDescription(String keyword, Callback addedCallback, Callback changedCallback, Callback removedCallback, Callback cancelledCallback) {
        this.keyword = keyword;
        this.addedCallback = addedCallback;
        this.changedCallback = changedCallback;
        this.removedCallback = removedCallback;
        this.cancelledCallback = cancelledCallback;
    }

    @Override
    public void readPostsByTitleOrDescriptionAndCategory(String keyword, String category, Callback addedCallback, Callback changedCallback, Callback removedCallback, Callback cancelledCallback) {
        this.keyword = keyword;
        this.category = category;
        this.addedCallback = addedCallback;
        this.changedCallback = changedCallback;
        this.removedCallback = removedCallback;
        this.cancelledCallback = cancelledCallback;
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
}
