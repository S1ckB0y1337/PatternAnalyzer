//Nikolaos Katsiopis
//icsd13076
package com.buftas.patternanalyzer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

/*Class that defines the methods for creation of the Database and the tables.
  Also defines the onCreate and onUpgrade methods for the database management
*/
public class DatabaseConnectionHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "UsersDB";
    public static final int DATABASE_VERSION = 2;

    public static final String CREATE_TABLE = "create table if not exists " + DatabaseSchema.UsernameEntry.TABLE_NAME +
            " (" + DatabaseSchema.UsernameEntry.USERNAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DatabaseSchema.UsernameEntry.USERNAME +
            " TEXT NOT NULL, " + DatabaseSchema.UsernameEntry.COLOR_PATTERN_SUBMISSION + " INTEGER DEFAULT 0, " + DatabaseSchema.UsernameEntry.PIANO_PATTERN_SUBMISSION + " INTEGER DEFAULT 0 );";

    public static final String DROP_TABLE = "drop table if exists " + DatabaseSchema.UsernameEntry.TABLE_NAME;


    public DatabaseConnectionHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("Database Operations", "Database Created...");

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        Log.d("Database Operations", "TABLE usernames Created...");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        Log.d("Database Operations", "TABLE users Droped...");
        onCreate(db);
        Log.d("Database Operations", "TABLE users Recreated...");
    }

    //This method adds a new user on the database
    public long addUser(String username, SQLiteDatabase db) {
        String sql = "INSERT INTO " + DatabaseSchema.UsernameEntry.TABLE_NAME + " (" + DatabaseSchema.UsernameEntry.USERNAME + ") " +
                "VALUES (?)";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.bindString(1, username);
        long rowID = statement.executeInsert();
        return rowID;
    }
}
