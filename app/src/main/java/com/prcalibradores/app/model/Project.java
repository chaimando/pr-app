package com.prcalibradores.app.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Project {
    private String DATE_PATTERN = "dd/MM/yyyy";

    private String mId;
    private String mName;
    private Date mStartdate;
    private Date mDeadline;
    private String mStatus;

    public Project(String id, String name, String status) {
        mId = id;
        mName = name;
        mStatus = status;
    }

    public Project(String id, String name, Date startdate, Date deadline, String mStatus) {
        mId = id;
        mName = name;
        mStartdate = startdate;
        mDeadline = deadline;
        this.mStatus = mStatus;
    }

    public Project(String id, String name, String startdate, String deadline, String mStatus) {
        mId = id;
        mName = name;
        try {
            mStartdate = new SimpleDateFormat(DATE_PATTERN, Locale.US).parse(startdate);
        } catch (ParseException e) {
            e.printStackTrace();
            mStartdate = new Date();
        }
        try {
            mDeadline = new SimpleDateFormat(DATE_PATTERN, Locale.US).parse(deadline);
        } catch (ParseException e) {
            e.printStackTrace();
            mDeadline = new Date();
        }
        this.mStatus = mStatus;
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

    public Date getStartdate() {
        return mStartdate;
    }

    public void setStartdate(Date startdate) {
        mStartdate = startdate;
    }

    public void setStartdate(String startdate) {
        try {
            mStartdate = new SimpleDateFormat(DATE_PATTERN, Locale.US).parse(startdate);
        } catch (ParseException e) {
            e.printStackTrace();
            mStartdate = new Date();
        }
    }

    public Date getDeadline() {
        return mDeadline;
    }

    public void setDeadline(Date deadline) {
        mDeadline = deadline;
    }

    public void setDeadline(String deadline) {
        try {
            mDeadline = new SimpleDateFormat(DATE_PATTERN, Locale.US).parse(deadline);
        } catch (ParseException e) {
            e.printStackTrace();
            mDeadline = new Date();
        }
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        this.mStatus = status;
    }
}
