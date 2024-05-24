package it.unimib.communimib.datasource.user;
import android.net.Uri;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;
import it.unimib.communimib.util.Constants;
import it.unimib.communimib.util.ErrorMapper;

public class UserRemoteDataSource implements IUserRemoteDataSource{

    private final DatabaseReference databaseReference;
    private final StorageReference storageReference;
    private static final String AUTHOR_NAME_PATH = "author/name/";
    private static final String AUTHOR_SURNAME_PATH = "author/surname/";
    private static final String AUTHOR_PROPIC_PATH = "author/propic/";

    public UserRemoteDataSource() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance(Constants.DATABASE).getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public void storeUserParameters(String uid, String email, String name, String surname, boolean isUnimibEmployee, Callback callback) {
        databaseReference
                .child(Constants.USERS_PATH)
                .child(uid)
                .setValue(new User(uid, email, name, surname, isUnimibEmployee))
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        callback.onComplete(new Result.Success());
                    }
                    else{
                        callback.onComplete(new Result.Error(ErrorMapper.REMOTEDB_INSERT_ERROR));
                    }
                });
    }

    @Override
    public void getUserByEmail(String email, Callback callback) {
        databaseReference
                .child(Constants.USERS_PATH)
                .orderByChild("email")
                .equalTo(email)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DataSnapshot queryResult = task.getResult();
                        Iterator<DataSnapshot> iterator = queryResult.getChildren().iterator();
                        if(iterator.hasNext()){
                            callback.onComplete(new Result.UserSuccess(iterator.next().getValue(User.class)));
                        }
                        else{
                            callback.onComplete(new Result.Error(ErrorMapper.USER_NOT_FOUND_ERROR));
                        }
                    }
                    else{
                        callback.onComplete(new Result.Error(ErrorMapper.REMOTEDB_GET_ERROR));
                    }
                });
    }

    @Override
    public void updateNameAndSurname(String uid, String name, String surname, Callback callback) {
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put(Constants.USERS_PATH + "/" + uid + "/name", name);
        updateMap.put(Constants.USERS_PATH + "/" + uid + "/surname", surname);

        updateReportsAuthorNameAndSurname(uid, name, surname, updateMap, callback);
    }

    private void updateReportsAuthorNameAndSurname(String uid, String name, String surname, Map<String, Object> updateMap, Callback callback) {
        databaseReference
                .child(Constants.USERSREPORTS_PATH)
                .child(uid)
                .get()
                .addOnCompleteListener(getTask -> {
                    if(getTask.isSuccessful()){
                        DataSnapshot snapshot = getTask.getResult();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String rid = dataSnapshot.getKey();
                            updateMap.put(Constants.REPORTS_PATH + "/" + rid + AUTHOR_NAME_PATH, name);
                            updateMap.put(Constants.REPORTS_PATH + "/" + rid + AUTHOR_SURNAME_PATH, surname);
                        }
                        updatePostsAuthorNameAndSurname(uid, name, surname, updateMap, callback);
                    }
                    else{
                        callback.onComplete(new Result.Error(ErrorMapper.REMOTEDB_UPDATE_ERROR));
                    }
                });
    }

    private void updatePostsAuthorNameAndSurname(String uid, String name, String surname, Map<String, Object> updateMap, Callback callback){
        databaseReference
                .child(Constants.USERSPOSTS_PATH)
                .child(uid)
                .get()
                .addOnCompleteListener(getTask -> {
                    if(getTask.isSuccessful()){
                        DataSnapshot snapshot = getTask.getResult();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String pid = dataSnapshot.getKey();
                            updateMap.put(Constants.POST_PATH + "/" + pid + AUTHOR_NAME_PATH, name);
                            updateMap.put(Constants.POST_PATH + "/" + pid + AUTHOR_SURNAME_PATH, surname);
                        }
                        updateCommentsAuthorNameAndSurname(uid, name, surname, updateMap, callback);
                    }
                    else{
                        callback.onComplete(new Result.Error(ErrorMapper.REMOTEDB_UPDATE_ERROR));
                    }
                });
    }

    private void updateCommentsAuthorNameAndSurname(String uid, String name, String surname, Map<String, Object> updateMap, Callback callback){
        databaseReference
                .child(Constants.USERSCOMMENTS_PATH)
                .child(uid)
                .get()
                .addOnCompleteListener(getTask -> {
                    if(getTask.isSuccessful()){
                        DataSnapshot snapshot = getTask.getResult();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            String pid = postSnapshot.getKey();
                            for (DataSnapshot commentSnapshot : postSnapshot.getChildren()) {
                                String cid = commentSnapshot.getKey();
                                updateMap.put(Constants.COMMENT_PATH + "/" + pid + "/" + cid + AUTHOR_NAME_PATH, name);
                                updateMap.put(Constants.COMMENT_PATH + "/" + pid + "/" + cid + AUTHOR_SURNAME_PATH, surname);
                            }
                        }
                        executeUpdate(updateMap, callback);
                    }
                    else{
                        callback.onComplete(new Result.Error(ErrorMapper.REMOTEDB_UPDATE_ERROR));
                    }
                });
    }

    private void executeUpdate(Map<String, Object> updateMap, Callback callback) {
        databaseReference
                .updateChildren(updateMap)
                .addOnCompleteListener(updateTask -> {
                    if(updateTask.isSuccessful()){
                        callback.onComplete(new Result.Success());
                    }
                    else{
                        callback.onComplete(new Result.Error(ErrorMapper.REMOTEDB_UPDATE_ERROR));
                    }
                });
    }

    @Override
    public void uploadPropic(String uid, Uri uri, Callback callback){
        storageReference
                .child(Constants.STORAGE_USERSPROPICS)
                .child(uid)
                .putFile(uri)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        updatePropicUri(uid, callback);
                    }
                    else{
                        callback.onComplete(new Result.Error(ErrorMapper.REMOTEDB_UPDATE_ERROR));
                    }
                });
    }

    private void updatePropicUri(String uid, Callback callback){

        Map<String, Object> updateMap = new HashMap<>();
        storageReference
                .child(Constants.STORAGE_USERSPROPICS)
                .child(uid)
                .getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        String uri = task.getResult().toString();
                        updateMap.put(Constants.USERS_PATH + "/" + uid + "/propic", uri);
                        updateReportsAuthorPropicUri(uid, uri, updateMap, callback);
                    }
                    else{
                        callback.onComplete(new Result.Error(ErrorMapper.REMOTEDB_UPDATE_ERROR));
                    }
                });
    }

    private void updateReportsAuthorPropicUri(String uid, String uri, Map<String, Object> updateMap, Callback callback){
        databaseReference
                .child(Constants.USERSREPORTS_PATH)
                .child(uid)
                .get()
                .addOnCompleteListener(getTask -> {
                    if(getTask.isSuccessful()){
                        DataSnapshot snapshot = getTask.getResult();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String rid = dataSnapshot.getKey();
                            updateMap.put(Constants.REPORTS_PATH + "/" + rid + AUTHOR_PROPIC_PATH, uri);
                        }
                        updatePostsAuthorPropicUri(uid, uri, updateMap, callback);
                    }
                    else{
                        callback.onComplete(new Result.Error(ErrorMapper.REMOTEDB_UPDATE_ERROR));
                    }
                });
    }

    private void updatePostsAuthorPropicUri(String uid, String uri, Map<String, Object> updateMap, Callback callback){
        databaseReference
                .child(Constants.USERSPOSTS_PATH)
                .child(uid)
                .get()
                .addOnCompleteListener(getTask -> {
                    if(getTask.isSuccessful()){
                        DataSnapshot snapshot = getTask.getResult();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String pid = dataSnapshot.getKey();
                            updateMap.put(Constants.POST_PATH + "/" + pid + AUTHOR_PROPIC_PATH, uri);
                        }
                        updateCommentsAuthorPropicUri(uid, uri, updateMap, callback);
                    }
                    else{
                        callback.onComplete(new Result.Error(ErrorMapper.REMOTEDB_UPDATE_ERROR));
                    }
                });
    }

    private void updateCommentsAuthorPropicUri(String uid, String uri, Map<String, Object> updateMap, Callback callback){
        databaseReference
                .child(Constants.USERSCOMMENTS_PATH)
                .child(uid)
                .get()
                .addOnCompleteListener(getTask -> {
                    if(getTask.isSuccessful()){
                        DataSnapshot snapshot = getTask.getResult();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            String pid = postSnapshot.getKey();
                            for (DataSnapshot commentSnapshot : postSnapshot.getChildren()) {
                                String cid = commentSnapshot.getKey();
                                updateMap.put(Constants.COMMENT_PATH + "/" + pid + "/" + cid + AUTHOR_PROPIC_PATH, uri);
                            }
                        }
                        executePropicUpdate(uri, updateMap, callback);
                    }
                    else{
                        callback.onComplete(new Result.Error(ErrorMapper.REMOTEDB_UPDATE_ERROR));
                    }
                });
    }

    private void executePropicUpdate(String uri, Map<String, Object> updateMap, Callback callback) {
        databaseReference
                .updateChildren(updateMap)
                .addOnCompleteListener(updateTask -> {
                    if(updateTask.isSuccessful()){
                        callback.onComplete(new Result.UriSuccess(uri));
                    }
                    else{
                        callback.onComplete(new Result.Error(ErrorMapper.REMOTEDB_UPDATE_ERROR));
                    }
                });
    }


    @Override
    public void storeUserFavoriteBuildings(List<String> userInterests, String userId, Callback callback) {
        databaseReference
                .child(Constants.USER_FAVORITE_BUILDINGS_PATH)
                .child(userId)
                .setValue(userInterests)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        callback.onComplete(new Result.Success());
                    else
                        callback.onComplete(new Result.Error(ErrorMapper.REMOTE_SAVE_USER_FAVORITE_BUILDINGS_ERROR));
                });
    }

    @Override
    public void getUserFavoriteBuildings(String userId, Callback callback) {
        databaseReference
                .child(Constants.USER_FAVORITE_BUILDINGS_PATH)
                .child(userId)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> userInterests = new ArrayList<>();
                        for (DataSnapshot snapshot : task.getResult().getChildren()) {
                            // Itera attraverso tutti i figli della snapshot
                            String interest = snapshot.getValue(String.class);
                            userInterests.add(interest);
                        }
                        callback.onComplete(new Result.UserFavoriteBuildingsSuccess(userInterests));
                    }
                    else{
                        callback.onComplete(new Result.Error(ErrorMapper.REMOTE_READ_USER_FAVORITE_BUILDINGS_ERROR));
                    }
                });

    }
}
