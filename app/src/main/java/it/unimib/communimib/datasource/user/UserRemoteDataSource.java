package it.unimib.communimib.datasource.user;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;
import it.unimib.communimib.util.Constants;
import it.unimib.communimib.util.ErrorMapper;

public class UserRemoteDataSource implements IUserRemoteDataSource{

    private final DatabaseReference databaseReference;

    public UserRemoteDataSource() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance(Constants.DATABASE).getReference();
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
        databaseReference
                .child(Constants.USERSREPORTS_PATH)
                .child(uid)
                .get()
                .addOnCompleteListener(getTask -> {
                    if(getTask.isSuccessful()){
                        DataSnapshot snapshot = getTask.getResult();
                        final Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();
                        while(iterator.hasNext()){
                            String rid = iterator.next().getKey();
                            updateMap.put(Constants.REPORTS_PATH + "/" + rid + "/author/name", name);
                            updateMap.put(Constants.REPORTS_PATH + "/" + rid + "/author/surname", surname);
                        }
                        updateMap.put(Constants.USERS_PATH + "/" + uid + "/name", name);
                        updateMap.put(Constants.USERS_PATH + "/" + uid + "/surname", surname);
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
                });
    }

    @Override
    public void uploadPropic(String uid, Uri uri, Callback callback){
        StorageReference userStorageReference = FirebaseStorage.getInstance().getReference()
                .child(Constants.STORAGE_USERSPROPICS).child(uid);

        userStorageReference
                .putFile(uri)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        updatePropicUri(uid, userStorageReference, callback);
                    }
                    else{

                    }
                });
    }

    private void updatePropicUri(String uid, StorageReference reference, Callback callback){
        Map<String, Object> updateMap = new HashMap<>();
        reference
                .getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        String uri = task.getResult().toString();
                        databaseReference
                                .child(Constants.USERSREPORTS_PATH)
                                .child(uid)
                                .get()
                                .addOnCompleteListener(getTask -> {
                                    if(getTask.isSuccessful()){
                                        DataSnapshot snapshot = getTask.getResult();
                                        final Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();
                                        while(iterator.hasNext()){
                                            String rid = iterator.next().getKey();
                                            updateMap.put(Constants.REPORTS_PATH + "/" + rid + "/author/propic", uri);
                                        }
                                        updateMap.put(Constants.USERS_PATH + "/" + uid + "/propic", uri);
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
                                });
                    }
                });
    }




}
