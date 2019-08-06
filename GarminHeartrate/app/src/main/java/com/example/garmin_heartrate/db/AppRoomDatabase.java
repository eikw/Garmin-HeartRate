package com.example.garmin_heartrate.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.garmin_heartrate.db.converter.DateConverter;
import com.example.garmin_heartrate.db.dao.FitReadingDao;
import com.example.garmin_heartrate.db.dao.SessionDao;
import com.example.garmin_heartrate.db.dao.UserDao;
import com.example.garmin_heartrate.db.entity.FitReading;
import com.example.garmin_heartrate.db.entity.Session;
import com.example.garmin_heartrate.db.entity.User;

@Database(entities = {User.class, Session.class, FitReading.class}, version = 2)
@TypeConverters(DateConverter.class)
public abstract class AppRoomDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract SessionDao sessionDao();
    public abstract FitReadingDao readingDao();

    public static final String DATABASE_NAME = "garmin-db";

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    private static volatile AppRoomDatabase INSTANCE;

    public static AppRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppRoomDatabase.class, DATABASE_NAME)
                            .build();
                    INSTANCE.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return INSTANCE;
    }

    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated(){
        mIsDatabaseCreated.postValue(true);
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final UserDao mDao;

        PopulateDbAsync(AppRoomDatabase db) {
            mDao = db.userDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Do not prepopulate
            /*
            mDao.deleteAll();
            User word = new Word("Hello");
            mDao.insert(word);
            word = new Word("World");
            mDao.insert(word);
            */
            return null;
        }
    }
}
