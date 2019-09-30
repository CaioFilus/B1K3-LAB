package com.npdeas.b1k3labapp.Database.Daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.npdeas.b1k3labapp.Database.Structs.RouteNode;

import java.util.List;

@Dao
public interface RouteNodeDao {

    @Query("SELECT * FROM routeNode")
    List<RouteNode> getAll();

    @Query("SELECT * FROM routeNode where id LIKE :id")
    List<RouteNode> gerUser(int id);

    @Query("SELECT COUNT(*) from routeNode")
    int countUsers();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(RouteNode... users);

    @Delete
    void delete(RouteNode user);

    @Query("DELETE FROM routeNode")
    void deleteAll();

    @Query("SELECT * FROM routeNode WHERE routeId=:routeId")
    List<RouteNode> findRouteNodesForRoute(long routeId);

}
