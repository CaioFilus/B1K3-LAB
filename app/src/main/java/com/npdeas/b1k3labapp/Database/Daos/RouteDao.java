package com.npdeas.b1k3labapp.Database.Daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.npdeas.b1k3labapp.Database.Structs.Route;

import java.util.List;

@Dao
public interface RouteDao {

    @Query("SELECT * FROM route")
    List<Route> getAll();

    @Query("SELECT * FROM Route where id LIKE :id")
    List<Route> getRoute(int id);

    @Query("SELECT COUNT(*) from Route")
    int countUsers();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertAll(Route users);

    @Update
    void update(Route route);


    @Delete
    void delete(Route user);

    @Query("DELETE FROM Route")
    void deleteAll();
}
