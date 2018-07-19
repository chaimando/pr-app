package com.prcalibradores.app.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.prcalibradores.app.model.User;

import java.util.UUID;

import static com.prcalibradores.app.database.DbSchema.UserTable;

public class UserCursorWrapper extends CursorWrapper {
    public UserCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public User getUser() {
        UUID id = UUID.fromString(getString(getColumnIndex(UserTable.Cols.ID)));
        String id_db = getString(getColumnIndex(UserTable.Cols.ID_DB));
        String user = getString(getColumnIndex(UserTable.Cols.USERNAME));
        String pass = getString(getColumnIndex(UserTable.Cols.PASSWORD));
        String process_id = getString(getColumnIndex(UserTable.Cols.PROCESS_ID));
        return new User(id, id_db, user, pass, process_id);
    }
}
