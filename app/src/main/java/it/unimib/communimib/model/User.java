package it.unimib.communimib.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.database.Exclude;

@Entity
public class User {

    @Exclude private String uid;
    @PrimaryKey @NonNull private String email;
    @ColumnInfo(name = "name") private String name;
    @ColumnInfo(name = "surname") private String surname;

    @Ignore
    public User(){

    }

    @Ignore
    public User(@NonNull String email){
        this.email = email;
    }

    public User(@NonNull String email, String name, String surname) {
        this.email = email;
        this.name = name;
        this.surname = surname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(@NonNull String uid) {
        this.uid = uid;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}