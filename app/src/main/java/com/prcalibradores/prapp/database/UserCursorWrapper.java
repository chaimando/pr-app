package com.prcalibradores.prapp.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.prcalibradores.prapp.model.User;

import java.util.UUID;

import static com.prcalibradores.prapp.database.DbSchema.*;

public class UserCursorWrapper extends CursorWrapper {
    public UserCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public User getUser() {
        UUID id = UUID.fromString(getString(getColumnIndex(UserTable.Cols.ID)));
        String id_db = getString(getColumnIndex(UserTable.Cols.ID_DB));
        String user = getString(getColumnIndex(UserTable.Cols.USERNAME));
        String pass = getString(getColumnIndex(UserTable.Cols.PASSWORD));
        return new User(id, id_db, user, pass);
    }
}
