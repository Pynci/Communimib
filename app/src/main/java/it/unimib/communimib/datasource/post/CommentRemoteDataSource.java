package it.unimib.communimib.datasource.post;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Comment;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.util.Constants;
import it.unimib.communimib.util.ErrorMapper;

public class CommentRemoteDataSource implements ICommentRemoteDataSource {

    private final DatabaseReference databaseReference;

    public CommentRemoteDataSource(){
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        this.databaseReference = FirebaseDatabase.getInstance(Constants.DATABASE).getReference();
    }

    @Override
    public void readCommentsByPid(String pid,
                                  Callback addedCallback,
                                  Callback changedCallback,
                                  Callback removedCallback,
                                  Callback cancelledCallback){
        databaseReference
                .child(Constants.COMMENT_PATH)
                .child(pid)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Comment comment = snapshot.getValue(Comment.class);
                        if (comment != null)
                            comment.setCid(snapshot.getKey());

                        addedCallback.onComplete(new Result.CommentSuccess(comment));
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Comment comment = snapshot.getValue(Comment.class);
                        if (comment != null)
                            comment.setCid(snapshot.getKey());

                        changedCallback.onComplete(new Result.CommentSuccess(comment));
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        Comment comment = snapshot.getValue(Comment.class);
                        if (comment != null)
                            comment.setCid(snapshot.getKey());

                        addedCallback.onComplete(new Result.CommentSuccess(comment));
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        // per ora niente
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        cancelledCallback.onComplete(new Result.Error(ErrorMapper.COMMENT_READ_ERROR));
                    }
                });
    }

    @Override
    public void createComment(String pid, Comment comment, Callback callback){
        String key = databaseReference.child(Constants.COMMENT_PATH).child(pid).push().getKey();
        comment.setCid(key);

        databaseReference
                .child(Constants.COMMENT_PATH)
                .child(pid)
                .child(comment.getCid())
                .setValue(comment)
                .addOnCompleteListener(commentTask -> {
                   if(commentTask.isSuccessful()){
                       databaseReference
                               .child(Constants.USERSCOMMENTS_PATH)
                               .child(comment.getAuthor().getUid())
                               .child(pid)
                               .child(comment.getCid())
                               .setValue(true)
                               .addOnCompleteListener(userCommentTask -> {
                                   if(userCommentTask.isSuccessful()){
                                       increaseCommentsCounter(pid, callback);
                                   }
                                   else{
                                       callback.onComplete(new Result.Error(ErrorMapper.COMMENT_CREATION_ERROR));
                                   }
                               });
                   }
                   else{
                       callback.onComplete(new Result.Error(ErrorMapper.COMMENT_CREATION_ERROR));
                   }
                });
    }

    private void increaseCommentsCounter(String pid, Callback callback){
        databaseReference
                .child(Constants.POST_PATH)
                .child(pid)
                .child("comments")
                .get()
                .addOnCompleteListener(getTask -> {
                    if(getTask.isSuccessful()){
                        Integer commentsCounter = getTask.getResult().getValue(Integer.class);
                        if(commentsCounter != null)
                            commentsCounter++;
                        databaseReference
                                .child(Constants.POST_PATH)
                                .child(pid)
                                .child("comments")
                                .setValue(commentsCounter)
                                .addOnCompleteListener(setTask -> {
                                    if(setTask.isSuccessful()){
                                        callback.onComplete(new Result.Success());
                                    }
                                    else{
                                        callback.onComplete(new Result.Error(ErrorMapper.COMMENT_CREATION_ERROR));
                                    }
                                });
                    }
                    else{
                        callback.onComplete(new Result.Error(ErrorMapper.COMMENT_CREATION_ERROR));
                    }
                });
    }

}
