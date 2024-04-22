package it.unimib.communimib.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class User {

    private String uid;
    @PrimaryKey @NonNull private String email;
    @ColumnInfo(name = "name") private String name;
    @ColumnInfo(name = "surname") private String surname;
    @ColumnInfo(name = "is_verified") private boolean isUnimibEmployee;
    @ColumnInfo(name = "propic_uri") private String propicUri;

    @Ignore
    public User(){

    }

    @Ignore
    public User(@NonNull String email){
        this.email = email;
    }

    public User(String uid, @NonNull String email, String name, String surname, boolean isUnimibEmployee) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.isUnimibEmployee = isUnimibEmployee;
        propicUri = null;
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

    public boolean isUnimibEmployee() {
        return isUnimibEmployee;
    }

    public void setUnimibEmployee(boolean unimibEmployee) {
        isUnimibEmployee = unimibEmployee;
    }

    public String getPropicUri() {
        return propicUri;
    }

    public void setPropicUri(String propicUri) {
        this.propicUri = propicUri;
    }
}