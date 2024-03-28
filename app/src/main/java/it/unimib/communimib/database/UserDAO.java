package it.unimib.communimib.database;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import it.unimib.communimib.model.User;

public interface UserDAO {

    @Query("SELECT * FROM User")
    User getUser();

    @Delete
    int deleteUser();

    @Update
    int updateUser(User newLoggedUser);

    @Insert
    long insertUser(User loggedUser);
}
