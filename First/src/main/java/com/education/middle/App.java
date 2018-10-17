package com.education.middle;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;

import com.education.middle.data.LinkDatabase;
import com.education.middle.data.Row;
import com.education.middle.data.RowStatus;

import java.util.Calendar;
import java.util.List;

import static com.education.middle.data.LinkDatabase.DB_NAME;

public class App extends Application {
    private static final String TAG = App.class.getSimpleName();
    private static App app;
    private LinkDatabase database;
    @Override
    public void onCreate(){
        super.onCreate();
        app = this;
        database = Room.databaseBuilder(App.this, LinkDatabase.class, DB_NAME)
                .fallbackToDestructiveMigration()
                .build();

        database.checkEmpty();
        Log.i(TAG, "Application created");
    }
    public static App getInstance(){
        return app;
    }
    public String getMainPackage(){
        return getApplicationContext().getPackageName();
    }
    public Context getContext(){
        return getApplicationContext();
    }
    public LinkDatabase getDatabase(){
        return database;
    }
}