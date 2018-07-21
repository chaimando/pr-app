package com.prcalibradores.app.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.prcalibradores.app.database.DatabaseHelper;
import com.prcalibradores.app.database.UserCursorWrapper;

import java.util.UUID;

import static com.prcalibradores.app.database.DbSchema.UserTable;

public class UsersLab {
    private static UsersLab sUsersLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static UsersLab get(Context context) {
        if (sUsersLab == null) {
            sUsersLab = new UsersLab(context);
        }
        return sUsersLab;
    }

    private UsersLab (Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new DatabaseHelper(mContext).getWritableDatabase();
    }

    public void saveUser(User user) {
        delete();
        ContentValues values = getContentValues(user);

        mDatabase.insert(UserTable.NAME, null, values);
    }

    public void delete() {
        mDatabase.delete(UserTable.NAME, null, null);
    }

    public void updateUser(User user) {
        String uuidString = user.getUUID().toString();
        ContentValues values = getContentValues(user);

        mDatabase.update(UserTable.NAME, values,
                UserTable.Cols.ID + " = ?",
                new String[] {uuidString});
    }

    public User getUser(UUID id) {
        UserCursorWrapper cursorWrapper = queryUsers(
                UserTable.Cols.ID + " = ?",
                new String[] {id.toString()}
        );
        try {
            if (cursorWrapper.getCount() == 0) {
                return null;
            }
            cursorWrapper.moveToFirst();
            return cursorWrapper.getUser();
        } finally {
            cursorWrapper.close();
        }
    }

    public User getUser(String id, String username, String password) {
        UserCursorWrapper cursorWrapper = queryUsers(
                UserTable.Cols.USERNAME + " = ? and " +
                 UserTable.Cols.PASSWORD + " = ? and " +
                 UserTable.Cols.ID_DB + " = ?",
                 new String[] {username, password, id}
        );
        try {
            if (cursorWrapper.getCount() == 0) {
                return null;
            }
            cursorWrapper.moveToFirst();
            return cursorWrapper.getUser();
        } finally {
            cursorWrapper.close();
        }
    }

    public UUID isLocallyRegistered(String username, String password) {
        UserCursorWrapper cursorWrapper = queryUsers(
                UserTable.Cols.USERNAME + " = ? and " + UserTable.Cols.PASSWORD + " = ?",
                 new String[] {username, password}
                );

        try {
            switch (cursorWrapper.getCount()) {
                case 1:
                    cursorWrapper.moveToFirst();
                    Log.d("UsersLab", cursorWrapper.getUser().toString());
                    return cursorWrapper.getUser().getUUID();
                case 0: default:
                    return null;
            }
        } finally {
            cursorWrapper.close();
        }
    }

    private static ContentValues getContentValues(User user) {
        ContentValues values = new ContentValues();
        values.put(UserTable.Cols.ID, user.getUUID().toString());
        values.put(UserTable.Cols.ID_DB, user.getIDDB());
        values.put(UserTable.Cols.USERNAME, user.getUsername());
        values.put(UserTable.Cols.PASSWORD, user.getPassword());
        values.put(UserTable.Cols.PROCESS_ID, user.getProcessId());
        values.put(UserTable.Cols.PROCESS_NAME, user.getProcessName());
        return values;
    }

    private UserCursorWrapper queryUsers(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                 UserTable.NAME,
                null,
                 whereClause,
                 whereArgs,
                null,
                null,
                null
        );
        return new UserCursorWrapper(cursor);
    }
}
