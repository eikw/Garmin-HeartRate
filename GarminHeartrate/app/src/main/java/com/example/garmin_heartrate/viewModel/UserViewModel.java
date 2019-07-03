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
import com.example.garmin_heartrate.db.entity.Session;
import com.example.garmin_heartrate.db.entity.User;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private final LiveData<User> mObservableUser;

    public ObservableField<User> user = new ObservableField<>();

    private final int mUserId;

    private final LiveData<List<Session>> mObservableSessions;

    private DataRepository mRepository;

    public UserViewModel(@NonNull Application application, DataRepository repository, final int userId) {
        super(application);
        mRepository = repository;
        mUserId = userId;

        mObservableSessions = repository.loadSessions(mUserId);
        mObservableUser = repository.loadUser(mUserId);
    }

    public LiveData<List<Session>> getSessions() { return mObservableSessions; }

    public LiveData<User> getObservableUser() { return mObservableUser; }

    public void setUser(User user) { this.user.set(user); }

    public void insertUser(User user) { mRepository.insertUser(user); }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final int mUserId;

        private final DataRepository mRepository;

        public Factory(@NonNull Application application, int userId) {
            mApplication = application;
            mUserId = userId;
            mRepository = ((BasicApp) application).getRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new UserViewModel(mApplication, mRepository, mUserId);
        }

    }

}
