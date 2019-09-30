package com.npdeas.b1k3labapp.Database.Structs;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Relation;
import android.arch.persistence.room.TypeConverters;
import android.graphics.Bitmap;

import com.npdeas.b1k3labapp.Database.Utils.DatetimeConverter;
import com.npdeas.b1k3labapp.Database.Utils.StatusConverter;
import com.npdeas.b1k3labapp.Route.Npdeas.ModalType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(tableName = "route")
public class Route {

    @PrimaryKey(autoGenerate = true)
    public long id;
    @ColumnInfo
    public long webId;
    @ColumnInfo
    public String name;
    @ColumnInfo
    public String img;
    @TypeConverters(StatusConverter.class)
    public ModalType modal;
    @TypeConverters(DatetimeConverter.class)
    public Date startDate;
    @TypeConverters(DatetimeConverter.class)
    public Date finishDate;

    @Ignore
    public List<RouteNode> routeNodes = new ArrayList<>();


}
