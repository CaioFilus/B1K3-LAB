package com.npdeas.b1k3labapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.npdeas.b1k3labapp.Route.Npdeas.ModalType;
import com.npdeas.b1k3labapp.Database.Structs.User;

/**
 * Created by NPDEAS on 8/23/2018.
 */

public class UserCRUD extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "smartMobility.db";

    private static final String TABLE_NAME = "user";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PASS = "password";
    private static final String COLUMN_SALT = "salt";
    private static final String COLUMN_WEB_USER_ID = "web_user_id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_MODAL = "modal";

    private static final int DATABASE_VERSION = 2;


    private static final String ALTER_TABLE_VER_2 = "ALTER TABLE " + TABLE_NAME + " ADD " + COLUMN_MODAL + " INTEGER DEFAULT 0";

    public UserCRUD(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //onCreate(getWritableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_PASS + " TEXT, " +
                COLUMN_SALT + " TEXT, " +
                COLUMN_WEB_USER_ID + " INTEGER, " +
                COLUMN_EMAIL + " TEXT);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < 2) {
            db.execSQL(ALTER_TABLE_VER_2);
        }

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public User getUser() {
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + "'" + 1 + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            User user = null;
            if (cursor.moveToFirst()) {
                user = new User();
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASS)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
                user.setWebId(cursor.getInt(cursor.getColumnIndex(COLUMN_WEB_USER_ID)));
                user.setModalType(ModalType.getValue(cursor.getInt(cursor.getColumnIndex(COLUMN_MODAL))));
                cursor.close();
            }
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteUser() {
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + "'" + 1 + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                db.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(1)});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUser(User user) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, 1);
        contentValues.put(COLUMN_NAME, user.getUsername());
        contentValues.put(COLUMN_PASS, user.getPassword());
        contentValues.put(COLUMN_EMAIL, user.getEmail());
        contentValues.put(COLUMN_MODAL, user.getModalType().value);
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }
}
