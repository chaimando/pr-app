package com.prcalibradores.app.model;

import java.util.UUID;

public class User {
    private UUID mUUID;
    private String mIDDB;
    private String mUsername;
    private String mPassword;
    private String mProcessId;
    private String mProcessName;

    public User(UUID UUID, String IDDB, String username, String password) {
        mUUID = UUID;
        mIDDB = IDDB;
        this.mUsername = username;
        this.mPassword = password;
    }

    public User(String IDDB, String username, String password, String processId, String processName) {
        mIDDB = IDDB;
        this.mUsername = username;
        this.mPassword = password;
        mProcessId = processId;
        mUUID = UUID.randomUUID();
        mProcessName = processName;
    }

    public User(UUID UUID, String IDDB, String username, String password, String processId) {
        mUUID = UUID;
        mIDDB = IDDB;
        mUsername = username;
        mPassword = password;
        mProcessId = processId;
    }

    public User(UUID UUID, String IDDB, String username, String password, String processId, String processName) {
        mUUID = UUID;
        mIDDB = IDDB;
        mUsername = username;
        mPassword = password;
        mProcessId = processId;
        mProcessName = processName;
    }

    public UUID getUUID() {
        return mUUID;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getIDDB() {
        return mIDDB;
    }

    public void setIDDB(String IDDB) {
        mIDDB = IDDB;
    }

    public void setUsername(String username) {
        this.mUsername = username;
    }

    public void setPassword(String password) {
        this.mPassword = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "mUUID=" + mUUID +
                ", mIDDB='" + mIDDB + '\'' +
                ", mUsername='" + mUsername + '\'' +
                ", mPassword='" + mPassword + '\'' +
                ", mProcessId='" + mProcessId + '\'' +
                '}';
    }

    public void setUUID(String id) {
        mUUID = UUID.fromString(id);
    }

    public String getProcessId() {
        return mProcessId;
    }

    public void setProcessId(String processId) {
        mProcessId = processId;
    }

    public String getProcessName() {
        return mProcessName;
    }

    public void setProcessName(String processName) {
        mProcessName = processName;
    }
}
