package com.npdeas.b1k3labapp.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.npdeas.b1k3labapp.Database.Structs.User;

import java.util.List;


public class UserTask {

    private AppDatabase noteDatabase;

    public UserTask(Context context) {
        noteDatabase = AppDatabase.getAppDatabase(context);
    }

    public void insertTask(final User user) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                noteDatabase.userDao().insertAll(user);
                return null;
            }
        }.execute();
    }

    public void updateTask(final User user) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                //noteDatabase.userDao().(note);
                return null;
            }
        }.execute();
    }

    public void deleteTask(final User user) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                noteDatabase.userDao().delete(user);
                return null;
            }
        }.execute();
    }

    public void deleteAllTask() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                noteDatabase.userDao().deleteAll();
                return null;
            }
        }.execute();
    }

    public LiveData<User> getTask(int id) {
        return noteDatabase.userDao().gerUser(id);
    }

    public List<User> getAllTask() {
        return noteDatabase.userDao().getAll();
    }


}
