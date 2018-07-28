package com.prcalibradores.app.model;

public class Model {
    private String mId;
    private String mName;
    private String mDescription;
    private String mPieces;
    private String mFinishedPieces;

    public Model(String id, String name, String description, String pieces, String finishedPieces) {
        mId = id;
        mName = name;
        mDescription = description;
        mPieces = pieces;
        mFinishedPieces = finishedPieces;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    @Override
    public String toString() {
        return "Model{" +
                "mId='" + mId + '\'' +
                ", mName='" + mName + '\'' +
                ", mDescription='" + mDescription + '\'' +
                '}';
    }

    public String getFinishedPieces() {
        return mFinishedPieces;
    }

    public void setFinishedPieces(String finishedPieces) {
        mFinishedPieces = finishedPieces;
    }

    public String getPieces() {
        return mPieces;
    }

    public void setPieces(String pieces) {
        mPieces = pieces;
    }
}
