package com.prcalibradores.prapp.model;

import java.util.UUID;

public class User {
    private UUID mUUID;
    private String mIDDB;
    private String username;
    private String password;
    private boolean saveSession;

    public void setSaveSession(boolean saveSession) {
        this.saveSession = saveSession;
    }

    public boolean isSaveSession() {
        return saveSession;

    }

    public User(){}

    public User(UUID UUID, String IDDB, String username, String password) {
        mUUID = UUID;
        mIDDB = IDDB;
        this.username = username;
        this.password = password;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        mUUID = UUID.randomUUID();
    }

    public User(String IDDB, String username, String password) {
        mIDDB = IDDB;
        this.username = username;
        this.password = password;
        mUUID = UUID.randomUUID();
    }

    public User(UUID UUID, String username, String password) {
        mUUID = UUID;
        this.username = username;
        this.password = password;
    }

    public UUID getUUID() {
        return mUUID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getIDDB() {
        return mIDDB;
    }

    public void setIDDB(String IDDB) {
        mIDDB = IDDB;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "mUUID=" + mUUID +
                ", mIDDB='" + mIDDB + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
