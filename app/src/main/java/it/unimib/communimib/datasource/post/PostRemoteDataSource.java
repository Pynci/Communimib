package it.unimib.communimib.datasource.post;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Post;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.util.Constants;
import it.unimib.communimib.util.ErrorMapper;

public class PostRemoteDataSource implements IPostRemoteDataSource{

    private DatabaseReference databaseReference;

    public PostRemoteDataSource() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        this.databaseReference = FirebaseDatabase.getInstance(Constants.DATABASE).getReference();
    }

    @Override
    public void readAllPosts(Callback addedCallback, Callback changedCallback, Callback removedCallback, Callback cancelledCallback) {

        databaseReference
                .child(Constants.POST_PATH)
                .limitToFirst(10)
                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Post post = snapshot.getValue(Post.class);
                post.setPid(snapshot.getKey());

                addedCallback.onComplete(new Result.PostSuccess(post));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Post post = snapshot.getValue(Post.class);
                post.setPid(snapshot.getKey());

                changedCallback.onComplete(new Result.PostSuccess(post));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);
                post.setPid(snapshot.getKey());

                removedCallback.onComplete(new Result.PostSuccess(post));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                cancelledCallback.onComplete(new Result.Error(ErrorMapper.REMOTEDB_GET_ERROR));
            }
        });
    }

    @Override
    public void readPostsByCategory(String queryParameter, Callback addedCallback, Callback changedCallback, Callback removedCallback, Callback cancelledCallback) {

        databaseReference
                .child(Constants.POST_PATH)
                .orderByChild("category")
                .equalTo(queryParameter)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Post post = snapshot.getValue(Post.class);
                        post.setPid(snapshot.getKey());

                        addedCallback.onComplete(new Result.PostSuccess(post));
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Post post = snapshot.getValue(Post.class);
                        post.setPid(snapshot.getKey());

                        changedCallback.onComplete(new Result.PostSuccess(post));
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        Post post = snapshot.getValue(Post.class);
                        post.setPid(snapshot.getKey());

                        removedCallback.onComplete(new Result.PostSuccess(post));
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        cancelledCallback.onComplete(new Result.Error(ErrorMapper.REMOTEDB_GET_ERROR));
                    }
                });
    }

    @Override
    public void createPost(Post post, Callback callback) {
        String key = databaseReference.child(Constants.POST_PATH).push().getKey();

        databaseReference
                .child(Constants.POST_PATH)
                .child(key).setValue(post)
                .addOnCompleteListener(postTask -> {
                if(postTask.isSuccessful()){
                    databaseReference
                            .child(Constants.USERSPOSTS_PATH)
                            .child(post.getAuthor().getUid())
                            .child(key)
                            .setValue(true)
                            .addOnCompleteListener(userPostTask -> {
                                if(userPostTask.isSuccessful()){
                                    callback.onComplete(new Result.Success());
                                } else {
                                    callback.onComplete(new Result.Error(ErrorMapper.REPORT_CREATION_ERROR));
                                }
                            });
                } else {
                    callback.onComplete(new Result.Error(ErrorMapper.REPORT_CREATION_ERROR));
                }
        });
    }

    @Override
    public void deletePost(Post post, Callback callback) {
        databaseReference
                .child(Constants.POST_PATH)
                .child(post.getPid())
                .removeValue()
                .addOnCompleteListener(postTask -> {
                    if(postTask.isSuccessful()){
                        databaseReference
                                .child(Constants.USERSPOSTS_PATH)
                                .child(post.getAuthor().getUid())
                                .child(post.getPid())
                                .removeValue()
                                .addOnCompleteListener(userPostTask -> {
                                    if(userPostTask.isSuccessful()){
                                        callback.onComplete(new Result.Success());
                                    } else {
                                        callback.onComplete(new Result.Error(ErrorMapper.REPORT_DELETING_ERROR));
                                    }
                                });
                    } else {
                        callback.onComplete(new Result.Error(ErrorMapper.REPORT_DELETING_ERROR));
                    }
                });
    }
}
