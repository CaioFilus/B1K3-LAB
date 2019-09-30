package com.npdeas.b1k3labapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.npdeas.b1k3labapp.Database.Enuns.Deficiency;
import com.npdeas.b1k3labapp.Database.Enuns.Gender;
import com.npdeas.b1k3labapp.Database.Enuns.Scholarity;
import com.npdeas.b1k3labapp.Database.Structs.UserForm;

public class FormCRUD extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smartMobility.db";

    private static final String TABLE_NAME = "user_Form";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_SCHOLARITY = "scholarity";
    private static final String COLUMN_INCOME = "income";
    private static final String COLUMN_STATE = "state";
    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_DISTRICTY = "districty";
    private static final String COLUMN_WHAT_DEFCY = "what_defcy";
    private static final String COLUMN_IS_SENDED = "is_sended";


    private static final int DATABASE_VERSION = 1;

    public FormCRUD(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        onCreate(getWritableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_GENDER + " INTEGER, " +
                COLUMN_AGE + " INTEGER, " +
                COLUMN_SCHOLARITY + " INTEGER, " +
                COLUMN_INCOME + " INTEGER, " +
                COLUMN_STATE + " TEXT, " +
                COLUMN_CITY + " TEXT, " +
                COLUMN_DISTRICTY + " TEXT, " +
                COLUMN_WHAT_DEFCY + " INTEGER DEFAULT NULL, " +
                COLUMN_IS_SENDED + " INTEGER" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public UserForm getUserForm() {
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + "'" + 1 + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        UserForm userForm = null;
        if (cursor.moveToFirst()) {
            userForm = new UserForm();
            userForm.setGender(Gender.getValue(cursor.getInt(cursor.getColumnIndex(COLUMN_GENDER))));
            userForm.setAge(cursor.getInt(cursor.getColumnIndex(COLUMN_AGE)));
            userForm.setScholarity(Scholarity.getValue(cursor.getInt(cursor.getColumnIndex(COLUMN_GENDER))));
            userForm.setIncome(cursor.getInt(cursor.getColumnIndex(COLUMN_INCOME)));
            userForm.setState(cursor.getString(cursor.getColumnIndex(COLUMN_STATE)));
            userForm.setCity(cursor.getString(cursor.getColumnIndex(COLUMN_CITY)));
            userForm.setDistrict(cursor.getString(cursor.getColumnIndex(COLUMN_DISTRICTY)));
            //userForm.setDeficiency(Deficiency.getValue(cursor.getInt(cursor.getColumnIndex(COLUMN_DEFICIENCY))));
            userForm.setWhatDefcy(Deficiency.getValue(cursor.getInt(cursor.getColumnIndex(COLUMN_WHAT_DEFCY))));
            boolean isSended = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_SENDED)) != 0;
            userForm.setSended(isSended);
            cursor.close();
        }
        return userForm;
    }

    public void setUserForm(UserForm userForm){
        SQLiteDatabase dbW = this.getWritableDatabase();
        UserForm olduserForm = getUserForm();
        ContentValues contentValues = new ContentValues();
        //contentValues.put(COLUMN_ID, 1);
        contentValues.put(COLUMN_GENDER, userForm.getGender().value);
        contentValues.put(COLUMN_AGE, userForm.getAge());
        contentValues.put(COLUMN_INCOME, userForm.getIncome());
        contentValues.put(COLUMN_SCHOLARITY, userForm.getScholarity().value);
        contentValues.put(COLUMN_STATE, userForm.getState());
        contentValues.put(COLUMN_CITY, userForm.getCity());
        contentValues.put(COLUMN_DISTRICTY, userForm.getDistrict());
        if(userForm.getWhatDefcy() != null){
            contentValues.put(COLUMN_WHAT_DEFCY, userForm.getWhatDefcy().value);
        }
        contentValues.put(COLUMN_IS_SENDED, userForm.isSended()? 1 : 0);
        if(olduserForm == null){
            dbW.insert(TABLE_NAME, null, contentValues);
        }else{
            dbW.update(TABLE_NAME,contentValues,"id=?",new String[]{"1"});
        }

        //contentValues.put(COLUMN_DEFICIENCY, userForm.getDeficiency().value);


    }
}
