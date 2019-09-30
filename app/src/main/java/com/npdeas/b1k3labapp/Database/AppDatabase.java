package com.npdeas.b1k3labapp.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.npdeas.b1k3labapp.Database.Daos.RouteDao;
import com.npdeas.b1k3labapp.Database.Daos.RouteNodeDao;
import com.npdeas.b1k3labapp.Database.Daos.UserDao;
import com.npdeas.b1k3labapp.Database.Structs.Route;
import com.npdeas.b1k3labapp.Database.Structs.RouteNode;
import com.npdeas.b1k3labapp.Database.Structs.User;

@Database(entities = {User.class,
        Route.class,
        RouteNode.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public abstract UserDao userDao();

    public abstract RouteDao routeDao();
    public abstract RouteNodeDao routeNodeDao();


    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "user-database")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }


    public void closeDb() throws Exception {
        mDatabase.close();
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
