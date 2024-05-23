package it.unimib.communimib.repository;

import java.util.List;

import it.unimib.communimib.Callback;
import it.unimib.communimib.datasource.post.ICommentRemoteDataSource;
import it.unimib.communimib.datasource.post.IPostRemoteDataSource;
import it.unimib.communimib.model.Comment;
import it.unimib.communimib.model.Post;
import it.unimib.communimib.model.User;

public class PostRepository implements IPostRepository{

    private IPostRemoteDataSource postRemoteDataSource;
    private ICommentRemoteDataSource commentRemoteDataSource;

    public PostRepository(IPostRemoteDataSource postRemoteDataSource, ICommentRemoteDataSource commentRemoteDataSource) {
        this.postRemoteDataSource = postRemoteDataSource;
        this.commentRemoteDataSource = commentRemoteDataSource;
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
    public void readPostsByUid(String uid,
                               Callback addedCallback,
                               Callback changedCallback,
                               Callback removedCallback,
                               Callback cancelledCallback) {
        postRemoteDataSource.readPostsByUid(uid, addedCallback, changedCallback, removedCallback, cancelledCallback);
    }


    @Override
    public void createPost(String title, String description, String category, User author,
                           String email, String link, List<String> pictures, Callback callback) {
        postRemoteDataSource.createPost(new Post(title, description, category, author, email, link, System.currentTimeMillis(), pictures), callback);
    }

    @Override
    public void deletePost(Post post, Callback callback) {
        postRemoteDataSource.deletePost(post, callback);
    }

    @Override
    public void readCommentsByPid(String pid,
                                  Callback addedCallback,
                                  Callback changedCallback,
                                  Callback removedCallback,
                                  Callback cancelledCallback){
        commentRemoteDataSource.readCommentsByPid(pid, addedCallback, changedCallback, removedCallback, cancelledCallback);
    }

    @Override
    public void createComment(String pid, User author, String text, Callback callback){
        commentRemoteDataSource.createComment(pid, new Comment(author, text, System.currentTimeMillis()), callback);
    }
}
