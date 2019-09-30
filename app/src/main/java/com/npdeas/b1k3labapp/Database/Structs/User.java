package com.npdeas.b1k3labapp.Database.Structs;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import com.npdeas.b1k3labapp.Database.Utils.StatusConverter;
import com.npdeas.b1k3labapp.Database.security.Encrypt;
import com.npdeas.b1k3labapp.Route.Npdeas.ModalType;

/**
 * Created by NPDEAS on 8/24/2018.
 */

@Entity(tableName = "user")
public class User {


    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo
    public String name;
    @ColumnInfo
    public String password;
    @ColumnInfo
    public String email;
    @TypeConverters(StatusConverter.class)
    public ModalType modalType = ModalType.BIKE;
    @ColumnInfo
    public String bluetooth;
    @ColumnInfo
    public long webId;


    public String getUsername() {
        return name;
    }

    public void setName(String username) {
        this.name = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password, boolean encrypt) {
        if (encrypt) {
            try {
                password = Encrypt.encrypt(password);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.password = password;
    }

    public void setPassword(String password) {
        setPassword(password, false);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getWebId() {
        return webId;
    }

    public void setWebId(long userId) {
        this.webId = userId;
    }

    public ModalType getModalType() {
        return modalType;
    }

    public void setModalType(ModalType modalType) {
        this.modalType = modalType;
    }

    private static User currentUser;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

}
