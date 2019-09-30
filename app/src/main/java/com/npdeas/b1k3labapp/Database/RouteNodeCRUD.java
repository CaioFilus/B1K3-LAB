package com.npdeas.b1k3labapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.npdeas.b1k3labapp.Route.Npdeas.RouteNode;

import java.util.ArrayList;

public class RouteNodeCRUD extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "smartMobility.db";

    public static final String TABLE_NAME = "route_node";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_LAT = "lat";
    private static final String COLUMN_LNG = "lng";
    private static final String COLUMN_SPEED = "speed";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_OVERTAKING = "overtaking";
    private static final String COLUMN_DECIBEL = "decibel";
    private static final String COLUMN_ROUTE_ID = "route_id";


    private static final int DATABASE_VERSION = 1;

    public RouteNodeCRUD(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        onCreate(getWritableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_LAT + " REAL, " +
                COLUMN_LNG + " REAL, " +
                COLUMN_SPEED + " REAL, " +
                COLUMN_TIME + " INTEGER, " +
                COLUMN_OVERTAKING + " INTEGER, " +
                COLUMN_DECIBEL + " INTEGER, " +
                COLUMN_ROUTE_ID + " INTEGER, " +
                " FOREIGN KEY (" + COLUMN_ROUTE_ID + ") REFERENCES " + RouteCRUD.TABLE_NAME + "(" + RouteCRUD.COLUMN_ID + ")" +
                ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public RouteNode getRoute(int id) {
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + "'" + id + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        RouteNode route = null;/*
        if (cursor.moveToFirst()) {
            routeInfo = new Route();
            user.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASS)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
            user.setUserId(cursor.getInt(cursor.getColumnIndex(COLUMN_WEB_USER_ID)));
            cursor.close();
        }*/
        return route;
    }

    public long addRouteNode(RouteNode routeNode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_LAT, routeNode.getLatitude());
        contentValues.put(COLUMN_LNG, routeNode.getLongetude());
        contentValues.put(COLUMN_SPEED, routeNode.getSpeed());
        contentValues.put(COLUMN_TIME, routeNode.getTime());
        contentValues.put(COLUMN_DECIBEL, routeNode.getDb());
        contentValues.put(COLUMN_OVERTAKING, routeNode.getOvertaking());
        contentValues.put(COLUMN_ROUTE_ID, routeNode.getRouteId());

        return db.insert(TABLE_NAME, null, contentValues);
    }

    public void delete(long route_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(TABLE_NAME, COLUMN_ROUTE_ID + "=?", new String[]{String.valueOf(route_id)});
        }catch (Exception e){
            Log.i("Route_Node_CRUUD", e.getMessage());
        }
    }

    public ArrayList<RouteNode> getAllRouteNodes(int routeId) {
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_ROUTE_ID + " = " + "'" + routeId + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<RouteNode> result = null;
        if (cursor.moveToFirst()) {
            result = new ArrayList<>();
            do{
                RouteNode routeNode = new RouteNode();
                routeNode.setLatitude(cursor.getDouble(cursor.getColumnIndex(COLUMN_LAT)));
                routeNode.setLongetude(cursor.getDouble(cursor.getColumnIndex(COLUMN_LNG)));
                routeNode.setSpeed(cursor.getFloat(cursor.getColumnIndex(COLUMN_SPEED)));
                routeNode.setTime(cursor.getLong(cursor.getColumnIndex(COLUMN_TIME)));
                routeNode.setOvertaking(cursor.getInt(cursor.getColumnIndex(COLUMN_OVERTAKING)));
                routeNode.setDb(cursor.getInt(cursor.getColumnIndex(COLUMN_DECIBEL)));
                result.add(routeNode);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }
}
