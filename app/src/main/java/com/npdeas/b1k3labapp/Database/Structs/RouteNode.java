package com.npdeas.b1k3labapp.Database.Structs;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(
        tableName = "routeNode")
public class RouteNode {

    @PrimaryKey(autoGenerate = true)
    public long id;
    public long routeId;
    @ColumnInfo
    public double longetude;
    @ColumnInfo
    public double latitude;
    @ColumnInfo
    public float speed;
    @ColumnInfo
    public int db;
    @ColumnInfo
    public int overtaking;
    @ColumnInfo
    public long time;

    @Ignore
    public int temperature;
    @Ignore
    public int humidity;

}
