package com.example.garmin_heartrate;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.garmin_heartrate.db.AppRoomDatabase;
import com.example.garmin_heartrate.db.dao.FitReadingDao;
import com.example.garmin_heartrate.db.dao.SessionDao;
import com.example.garmin_heartrate.db.dao.UserDao;
import com.example.garmin_heartrate.db.entity.FitReading;
import com.example.garmin_heartrate.db.entity.Session;
import com.example.garmin_heartrate.db.entity.User;

import java.util.List;

public class DataRepository {
    private static DataRepository sInstance;

    private final AppRoomDatabase mDatabase;
    private MediatorLiveData<List<User>> mObservableUsers;

    private DataRepository(final AppRoomDatabase database) {
        mDatabase = database;
        mObservableUsers = new MediatorLiveData<>();

        mObservableUsers.addSource(mDatabase.userDao().getAllUsers(),
                userEntities ->  {
                    if (mDatabase.getDatabaseCreated().getValue() != null) {
                        mObservableUsers.postValue(userEntities);
                    }
                });
    }

    public static DataRepository getInstance(final AppRoomDatabase database) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }

    public LiveData<List<User>> getUsers() { return mObservableUsers; }

    public void insertUser(User user) {
        new InsertUserAsyncTask(mDatabase.userDao()).execute(user);
    }

    public LiveData<User> loadUser(final int userId) {
        return mDatabase.userDao().loadUser(userId);
    }

    public void createSession(Session session, List<FitReading> readings) {
        SessionObject dto = new SessionObject(session, readings);
        new InsertSessionAsyncTask(mDatabase.sessionDao(), mDatabase.readingDao()).execute(dto);
    }

    public LiveData<Session> loadSession(final int sessionId) {
        return mDatabase.sessionDao().loadSession(sessionId);
    }


    public LiveData<List<Session>> loadSessions(final int userId) {
        return mDatabase.sessionDao().loadSessions(userId);
    }

    public void saveReading(FitReading reading)  {
        new InsertReadingsAsyncTask(mDatabase.readingDao()).execute(reading);
    }

    public LiveData<List<FitReading>> loadReadings(final int sessionId) {
        return mDatabase.readingDao().loadReadings(sessionId);
    }

    private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        private InsertUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.insert(users[0]);
            return null;
        }
    }

    private class InsertSessionAsyncTask extends AsyncTask<SessionObject, Void, Void> {
        private SessionDao sessionDao;
        private FitReadingDao readingDao;

        private InsertSessionAsyncTask(SessionDao sessionDao, FitReadingDao readingDao) {
            this.sessionDao = sessionDao;
            this.readingDao = readingDao;
        }

        @Override
        protected Void doInBackground(SessionObject... sessions) {
            long result = sessionDao.insert(sessions[0].session);
            for (FitReading reading:sessions[0].readings) {
                reading.setSessionId((int)result);
                readingDao.insert(reading);
            }
            return null;
        }
    }

    private static class InsertReadingsAsyncTask extends AsyncTask<FitReading, Void, Void> {
        private FitReadingDao readingDao;

        private InsertReadingsAsyncTask(FitReadingDao readingDao) {
            this.readingDao = readingDao;
        }

        @Override
        protected Void doInBackground(FitReading... fitReadings) {
            for (FitReading reading: fitReadings) {
                readingDao.insert(reading);
            }
            return null;
        }
    }

    private class SessionObject {
        Session session;
        List<FitReading> readings;

        SessionObject(Session session, List<FitReading> readings) {
            this.session = session;
            this.readings = readings;
        }
    }
}
