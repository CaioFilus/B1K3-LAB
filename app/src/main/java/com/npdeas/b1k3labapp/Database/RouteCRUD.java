package com.npdeas.b1k3labapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.npdeas.b1k3labapp.Route.Npdeas.ModalType;
import com.npdeas.b1k3labapp.Route.Npdeas.RouteNode;
import com.npdeas.b1k3labapp.Route.Route;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class RouteCRUD extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "smartMobility.db";

    public static final String TABLE_NAME = "routes";
    public static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_MODAL = "modal";
    private static final String COLUMN_DATETIME = "datetime";
    private static final String COLUMN_WEB_ID = "web_id";
    private static final String COLUMN_SPLIT_ID = "split_id";
    private static final String COLUMN_IS_SENDED = "is_sended";
    private static final String COLUMN_IMG = "map_image";


    private static final int DATABASE_VERSION = 1;

    private Context context;

    public RouteCRUD(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        onCreate(getWritableDatabase());
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DATETIME + " DATETIME DEFAULT CURRENT_TIMESTAMP," +
                COLUMN_MODAL + " INTEGER, " +
                COLUMN_IMG + " BLOB DEFAULT(x''), " +

                COLUMN_WEB_ID + " INTEGER DEFAULT 0, " +
                COLUMN_IS_SENDED + " INTEGER DEFAULT 0, " +
                COLUMN_SPLIT_ID + " INTEGER DEFAULT 0 " +
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

    public Route getRoute(int id) {
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + "'" + id + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Route route = null;/*
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

    public void delete(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        RouteNodeCRUD routeNodeDao = new RouteNodeCRUD(context);
        routeNodeDao.delete(id);
        db.delete(TABLE_NAME, "id=?", new String[]{String.valueOf(id)});
    }


    public long addRoute(Route route) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, route.getRouteName());
        contentValues.put(COLUMN_MODAL, route.getModal().value);
        contentValues.put(COLUMN_DATETIME, route.getDataTime());
        //contentValues.put(COLUMN_IMG, new byte[0]);

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result;
    }

    public void update(Route route) {
        SQLiteDatabase db = this.getWritableDatabase();


        Bitmap bmp = route.getImg();
        byte[] byteArray = null;
        if (bmp != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();

            //bmp.recycle();
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, route.getRouteName());
        contentValues.put(COLUMN_MODAL, route.getModal().value);
        contentValues.put(COLUMN_DATETIME, route.getDataTime());
        contentValues.put(COLUMN_IMG, byteArray);

        contentValues.put(COLUMN_WEB_ID, route.getWebId());
        contentValues.put(COLUMN_SPLIT_ID, route.getSplitId());
        contentValues.put(COLUMN_IS_SENDED, (route.isSended() ? 1 : 0));
        //contentValues.put(COLUMN_IMG, new byte[0]);

        String[] args = new String[]{String.valueOf(route.getId())};
        db.update(TABLE_NAME, contentValues, "id = ?", args);
    }

    public ArrayList<Route> getAllRoutes() {
        String query = "Select * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Route> result = null;
        if (cursor.moveToFirst()) {
            result = new ArrayList<>();
            RouteNodeCRUD routeNodeDao = new RouteNodeCRUD(context);
            do {
                Route route = new Route(context, false);
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                byte[] byteArr = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMG));
                route.setId(id);
                route.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                route.setModal(ModalType.getValue(cursor.getInt(cursor.getColumnIndex(COLUMN_MODAL))));
                route.setDateTime(cursor.getString(cursor.getColumnIndex(COLUMN_DATETIME)));
                route.setImg(byteArr!=null?BitmapFactory.decodeByteArray(byteArr, 0, byteArr.length):null);

                route.setWebId(cursor.getLong(cursor.getColumnIndex(COLUMN_WEB_ID)));
                route.setSended(cursor.getInt(cursor.getColumnIndex(COLUMN_IS_SENDED)) != 0);
                route.setSplitId(cursor.getInt(cursor.getColumnIndex(COLUMN_SPLIT_ID)));

                ArrayList<RouteNode> nodes = routeNodeDao.getAllRouteNodes(id);
                if (nodes != null) {
                    for (RouteNode node : nodes) {
                        route.addNodeInRoute(node);
                    }
                }
                route.loadFinish();
                result.add(route);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return result;
    }

    public ArrayList<Route> getUnsendedRoutes() {
        String query = "Select * FROM " + TABLE_NAME + " WHERE is_sended='0' ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Route> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            RouteNodeCRUD routeNodeDao = new RouteNodeCRUD(context);
            while (cursor.moveToNext()) {
                Route route = new Route(context, false);
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                byte[] byteArr = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMG));
                route.setId(id);
                route.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                route.setModal(ModalType.getValue(cursor.getInt(cursor.getColumnIndex(COLUMN_MODAL))));
                route.setDateTime(cursor.getString(cursor.getColumnIndex(COLUMN_DATETIME)));
                route.setImg(byteArr!=null?BitmapFactory.decodeByteArray(byteArr, 0, byteArr.length):null);

                route.setWebId(cursor.getLong(cursor.getColumnIndex(COLUMN_WEB_ID)));
                route.setSended(cursor.getInt(cursor.getColumnIndex(COLUMN_IS_SENDED)) != 0);
                route.setSplitId(cursor.getInt(cursor.getColumnIndex(COLUMN_SPLIT_ID)));

                route.setRouteNodes(routeNodeDao.getAllRouteNodes(id));
                result.add(route);
            }
        }
        db.close();
        cursor.close();
        return result;
    }
}
