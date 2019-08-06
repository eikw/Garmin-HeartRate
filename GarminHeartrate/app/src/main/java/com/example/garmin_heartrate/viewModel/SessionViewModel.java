package com.example.garmin_heartrate.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.garmin_heartrate.BasicApp;
import com.example.garmin_heartrate.DataRepository;
import com.example.garmin_heartrate.db.entity.FitReading;
import com.example.garmin_heartrate.db.entity.Session;

import java.util.List;

public class SessionViewModel extends AndroidViewModel {

    private final LiveData<Session> mObservableSession;

    public ObservableField<Session> session = new ObservableField<>();

    private final int mSessionId;

    private final LiveData<List<FitReading>> mObservableReadings;

    private DataRepository mRepository;

    public SessionViewModel(@NonNull Application application, DataRepository repository, final int sessionId) {
        super(application);
        mRepository = repository;
        mSessionId = sessionId;

        mObservableReadings = repository.loadReadings(mSessionId) ;
        mObservableSession = repository.loadSession(mSessionId);
    }

    public LiveData<List<FitReading>> getReadings() { return mObservableReadings; }

    public LiveData<Session> getObservableSession() { return mObservableSession; }

    public void setSession(Session session) { this.session.set(session); }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        @NonNull
        private final Application mApplication;

        private final int mSessionId;

        private final DataRepository mRepository;

        public Factory(@NonNull Application application, int sessionId) {
            mApplication = application;
            mSessionId = sessionId;
            mRepository = ((BasicApp) application).getRepository();
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new SessionViewModel(mApplication, mRepository, mSessionId);
        }
    }
}
