package com.npdeas.b1k3labapp.Database.Utils;

import android.arch.persistence.room.TypeConverter;

import com.npdeas.b1k3labapp.Route.Npdeas.ModalType;

public class StatusConverter {

    @TypeConverter
    public static ModalType toStatus(int status) {
        return ModalType.getValue(status);
    }

    @TypeConverter
    public static Integer toInteger(ModalType status) {
        return status.value;
    }
}
