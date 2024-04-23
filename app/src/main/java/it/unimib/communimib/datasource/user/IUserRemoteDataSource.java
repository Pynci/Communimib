package it.unimib.communimib.datasource.user;

import android.net.Uri;

import java.util.List;

import it.unimib.communimib.Callback;

public interface IUserRemoteDataSource {

    void storeUserParameters(String uid, String email, String name, String surname, boolean isUnimibEmployee, Callback callback);
    void getUserByEmail(String email, Callback callback);
    void updateNameAndSurname(String uid, String name, String surname, Callback callback);

    void uploadPropic(String uid, Uri uri, Callback callback);

    void storeUserInterests(List<String> userInterests, String userId, Callback callback);

    void getUserInterests(String userId, Callback callback);
}
