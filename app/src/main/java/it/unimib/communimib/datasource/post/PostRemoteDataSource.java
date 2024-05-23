package it.unimib.communimib.datasource.post;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Post;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;
import it.unimib.communimib.util.Constants;
import it.unimib.communimib.util.ErrorMapper;

public class PostRemoteDataSource implements IPostRemoteDataSource{

    private final DatabaseReference databaseReference;
    private int picturesCounter;
    private int numberOfPictures;

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
                //per ora niente
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
                        //per ora niente
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        cancelledCallback.onComplete(new Result.Error(ErrorMapper.REMOTEDB_GET_ERROR));
                    }
                });
    }

    @Override
    public void readPostsByTitleOrDescription(String keyword, Callback addedCallback, Callback changedCallback, Callback removedCallback, Callback cancelledCallback) {
        databaseReference
                .child(Constants.POST_PATH)
                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Post post = snapshot.getValue(Post.class);
                post.setPid(snapshot.getKey());
                String title = post.getTitle().toLowerCase();
                String description = post.getDescription().toLowerCase();
                if(title.contains(keyword) || description.contains(keyword)){
                    addedCallback.onComplete(new Result.PostSuccess(post));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Post post = snapshot.getValue(Post.class);
                post.setPid(snapshot.getKey());
                String title = post.getTitle().toLowerCase();
                String description = post.getDescription().toLowerCase();
                if(title.contains(keyword) || description.contains(keyword)){
                    changedCallback.onComplete(new Result.PostSuccess(post));
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);
                post.setPid(snapshot.getKey());
                String title = post.getTitle().toLowerCase();
                String description = post.getDescription().toLowerCase();
                if(title.contains(keyword) || description.contains(keyword)){
                    removedCallback.onComplete(new Result.PostSuccess(post));
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // per ora niente, nel caso aggiungere una callback
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                cancelledCallback.onComplete(new Result.Error(ErrorMapper.REMOTEDB_GET_ERROR));
            }
        });
    }

    public void readPostsByTitleOrDescriptionAndCategory(String keyword,
                                                         String category,
                                                         Callback addedCallback,
                                                         Callback changedCallback,
                                                         Callback removedCallback,
                                                         Callback cancelledCallback){

        databaseReference
                .child(Constants.POST_PATH)
                .orderByChild("category")
                .equalTo(category)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Post post = snapshot.getValue(Post.class);
                        post.setPid(snapshot.getKey());
                        String title = post.getTitle().toLowerCase();
                        String description = post.getDescription().toLowerCase();
                        if(title.contains(keyword) || description.contains(keyword)){
                            addedCallback.onComplete(new Result.PostSuccess(post));
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Post post = snapshot.getValue(Post.class);
                        post.setPid(snapshot.getKey());
                        String title = post.getTitle().toLowerCase();
                        String description = post.getDescription().toLowerCase();
                        if(title.contains(keyword) || description.contains(keyword)){
                            changedCallback.onComplete(new Result.PostSuccess(post));
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        Post post = snapshot.getValue(Post.class);
                        post.setPid(snapshot.getKey());
                        String title = post.getTitle().toLowerCase();
                        String description = post.getDescription().toLowerCase();
                        if(title.contains(keyword) || description.contains(keyword)){
                            removedCallback.onComplete(new Result.PostSuccess(post));
                        }
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        //per ora niente
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        cancelledCallback.onComplete(new Result.Error(ErrorMapper.REMOTEDB_GET_ERROR));
                    }
                });
    }

    @Override
    public void readPostsByUid(String uid,
                               Callback addedCallback,
                               Callback changedCallback,
                               Callback removedCallback,
                               Callback cancelledCallback) {
        databaseReference
                .child(Constants.POST_PATH)
                .orderByChild("author")
                .equalTo(uid)
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
        post.setPid(key);

        if(!post.getPictures().isEmpty()){
            picturesCounter = 0;
            numberOfPictures = post.getPictures().size();
            List<String> downloadUris = new ArrayList<>();
            for (int i = 0; i < numberOfPictures; i++) {
                createPostWithPictures(post, i, downloadUris, callback);
            }
        }
        else{
            createSimplePost(post, callback);
        }
    }

    //richiamare questo metodo tante volte quante sono le immagini nel post
    private void createPostWithPictures(Post post, int i, List<String> downloadUris, Callback callback) {
        StorageReference postStorageReference = FirebaseStorage.getInstance().getReference()
                .child(Constants.STORAGE_POSTPICS).child(post.getPid()).child(String.valueOf(i));


        //carico l'immagine corrente sul Firebase Storage
        postStorageReference
                .putFile(Uri.parse(post.getPictures().get(i)))
                .addOnCompleteListener(imageUploadTask -> {
                    if(imageUploadTask.isSuccessful()){
                        //recupero l'uri per il successivo download dell'immagine
                        postStorageReference
                                .getDownloadUrl()
                                .addOnCompleteListener(imageUriDownloadTask -> {
                                    if(imageUriDownloadTask.isSuccessful()){
                                        downloadUris.add(imageUriDownloadTask.getResult().toString());

                                        //se è l'ultima immagine creo il post
                                        picturesCounter++;
                                        if(picturesCounter == numberOfPictures){
                                            post.setPictures(downloadUris);
                                            createSimplePost(post, callback);
                                        }
                                    }
                                    else{
                                        callback.onComplete(new Result.Error(ErrorMapper.POST_CREATION_ERROR));
                                    }
                                });
                    }
                    else{
                        callback.onComplete(new Result.Error(ErrorMapper.POST_CREATION_ERROR));
                    }
                });
    }

    private void createSimplePost(Post post, Callback callback){
        databaseReference
                .child(Constants.POST_PATH)
                .child(post.getPid())
                .setValue(post)
                .addOnCompleteListener(postTask -> {
                    if(postTask.isSuccessful()){
                        databaseReference
                                .child(Constants.USERSPOSTS_PATH)
                                .child(post.getAuthor().getUid())
                                .child(post.getPid())
                                .setValue(true)
                                .addOnCompleteListener(userPostTask -> {
                                    if(userPostTask.isSuccessful()){
                                        callback.onComplete(new Result.Success());
                                    }
                                    else{
                                        callback.onComplete(new Result.Error(ErrorMapper.POST_CREATION_ERROR));
                                    }
                                });
                    }
                    else{
                        callback.onComplete(new Result.Error(ErrorMapper.POST_CREATION_ERROR));
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
                                        callback.onComplete(new Result.Error(ErrorMapper.POST_DELETING_ERROR));
                                    }
                                });
                    } else {
                        callback.onComplete(new Result.Error(ErrorMapper.POST_DELETING_ERROR));
                    }
                });
    }
}
