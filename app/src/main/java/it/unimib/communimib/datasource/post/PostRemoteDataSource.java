package it.unimib.communimib.datasource.post;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Post;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.util.Constants;
import it.unimib.communimib.util.ErrorMapper;

public class PostRemoteDataSource implements IPostRemoteDataSource{

    private final DatabaseReference databaseReference;
    private final List<ChildEventListener> currentListeners;
    private final List<DatabaseReference> currentReferences;
    private int picturesCounter;
    private int numberOfPictures;

    public PostRemoteDataSource() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        this.databaseReference = FirebaseDatabase.getInstance(Constants.DATABASE).getReference();
        currentListeners = new ArrayList<>();
        currentReferences = new ArrayList<>();
    }

    @Override
    public void readAllPosts(Callback addedCallback, Callback changedCallback, Callback removedCallback, Callback cancelledCallback) {
        removeAllQueryListeners();
        addQueryListener(null, null, addedCallback, changedCallback, removedCallback, cancelledCallback);
    }

    @Override
    public void readPostsByCategory(String category, Callback addedCallback, Callback changedCallback, Callback removedCallback, Callback cancelledCallback) {
        removeAllQueryListeners();
        addQueryListener("category", category, addedCallback, changedCallback, removedCallback, cancelledCallback);
    }

    @Override
    public void readPostsByUid(String uid, Callback addedCallback, Callback changedCallback, Callback removedCallback, Callback cancelledCallback) {
        removeAllQueryListeners();
        addQueryListener("author/uid", uid, addedCallback, changedCallback, removedCallback, cancelledCallback);
    }

    @Override
    public void readPostsByTitleOrDescription(String keyword, Callback addedCallback, Callback changedCallback, Callback removedCallback, Callback cancelledCallback) {
        removeAllQueryListeners();
        Query query = databaseReference.child(Constants.POST_PATH);
        currentReferences.add(query.getRef());

        currentListeners.add(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Post post = snapshot.getValue(Post.class);
                if(post != null){
                    post.setPid(snapshot.getKey());
                    String title = post.getTitle().toLowerCase();
                    String description = post.getDescription().toLowerCase();
                    if(title.contains(keyword) || description.contains(keyword)){
                        addedCallback.onComplete(new Result.PostSuccess(post));
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Post post = snapshot.getValue(Post.class);
                if(post != null){
                    post.setPid(snapshot.getKey());
                    String title = post.getTitle().toLowerCase();
                    String description = post.getDescription().toLowerCase();
                    if(title.contains(keyword) || description.contains(keyword)){
                        changedCallback.onComplete(new Result.PostSuccess(post));
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);
                if (post != null){
                    post.setPid(snapshot.getKey());
                    String title = post.getTitle().toLowerCase();
                    String description = post.getDescription().toLowerCase();
                    if(title.contains(keyword) || description.contains(keyword)){
                        removedCallback.onComplete(new Result.PostSuccess(post));
                    }
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

        query.addChildEventListener(currentListeners.get(0));
    }

    public void readPostsByTitleOrDescriptionAndCategory(String keyword, String category,
                                                         Callback addedCallback, Callback changedCallback,
                                                         Callback removedCallback, Callback cancelledCallback){
        removeAllQueryListeners();
        Query query = databaseReference.child(Constants.POST_PATH).orderByChild("category").equalTo(category);
        currentReferences.add(query.getRef());

        currentListeners.add(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Post post = snapshot.getValue(Post.class);
                if(post != null){
                    post.setPid(snapshot.getKey());
                    String title = post.getTitle().toLowerCase();
                    String description = post.getDescription().toLowerCase();
                    if(title.contains(keyword) || description.contains(keyword)){
                        addedCallback.onComplete(new Result.PostSuccess(post));
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Post post = snapshot.getValue(Post.class);
                if(post != null){
                    post.setPid(snapshot.getKey());
                    String title = post.getTitle().toLowerCase();
                    String description = post.getDescription().toLowerCase();
                    if(title.contains(keyword) || description.contains(keyword)){
                        changedCallback.onComplete(new Result.PostSuccess(post));
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);
                if(post != null){
                    post.setPid(snapshot.getKey());
                    String title = post.getTitle().toLowerCase();
                    String description = post.getDescription().toLowerCase();
                    if(title.contains(keyword) || description.contains(keyword)){
                        removedCallback.onComplete(new Result.PostSuccess(post));
                    }
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

        query.addChildEventListener(currentListeners.get(0));
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

    @Override
    public void undoDeletePost(Post post, Callback callback){
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
                                        callback.onComplete(new Result.Error(ErrorMapper.POST_UNDO_DELETING_ERROR));
                                    }
                                });
                    }
                    else{
                        callback.onComplete(new Result.Error(ErrorMapper.POST_UNDO_DELETING_ERROR));
                    }
                });
    }

    private void removeAllQueryListeners(){
        if(!currentListeners.isEmpty() && !currentReferences.isEmpty()){
            for (int i = 0; i < currentListeners.size(); i++) {
                currentReferences.get(i).removeEventListener(currentListeners.get(i));
            }
            currentListeners.clear();
            currentReferences.clear();
        }
    }

    private void addQueryListener(String path, String queryParameter,
                                  Callback addedCallback,
                                  Callback changedCallback,
                                  Callback removedCallback,
                                  Callback cancelledCallback) {
        Query query;

        if((queryParameter == null || queryParameter.isEmpty()) && (path == null || path.isEmpty())){
             query = databaseReference
                    .child(Constants.POST_PATH)
                    .limitToFirst(30);
        }
        else{
            query = databaseReference
                    .child(Constants.POST_PATH)
                    .orderByChild(path)
                    .equalTo(queryParameter);
        }

        currentReferences.add(query.getRef());
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Post post = snapshot.getValue(Post.class);
                if(post != null){
                    post.setPid(snapshot.getKey());
                }
                addedCallback.onComplete(new Result.PostSuccess(post));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Post post = snapshot.getValue(Post.class);
                if(post != null){
                    post.setPid(snapshot.getKey());
                }
                changedCallback.onComplete(new Result.PostSuccess(post));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);
                if(post != null){
                    post.setPid(snapshot.getKey());
                }
                removedCallback.onComplete(new Result.PostSuccess(post));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // per ora niente, nel caso aggiungere una callback
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                cancelledCallback.onComplete(new Result.Error(ErrorMapper.REMOTEDB_GET_ERROR));
            }
        };

        currentListeners.add(listener);
        query.addChildEventListener(listener);
    }
}
