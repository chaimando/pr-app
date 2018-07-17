package com.prcalibradores.prapp.model;

public class Model {
    private String mId;
    private String mName;
    private String mDescription;

    public Model(String id, String name, String description) {
        mId = id;
        mName = name;
        mDescription = description;
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
}
