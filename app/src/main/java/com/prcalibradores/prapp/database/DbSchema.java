package com.prcalibradores.prapp.database;

public class DbSchema {
    public static final class UserTable {
        public static final String NAME = "users";

        public static final class Cols {
            public static final String ID = "id";
            public static final String ID_DB = "id_db";
            public static final String USERNAME = "username";
            public static final String PASSWORD = "password";
            public static final String PROCESS_ID = "process_id";
        }
    }
}
