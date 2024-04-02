package it.unimib.communimib.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import it.unimib.communimib.model.User;

@Dao
public interface UserDAO {

    @Query("SELECT * FROM User")
    User getUser();

    @Query("DELETE FROM User")
    void clearUser();

    @Update
    int updateUser(User newLoggedUser);

    @Insert
    long insertUser(User loggedUser);
}
