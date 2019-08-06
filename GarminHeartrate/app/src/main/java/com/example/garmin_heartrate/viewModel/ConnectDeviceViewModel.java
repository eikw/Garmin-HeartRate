package com.example.garmin_heartrate.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.garmin_heartrate.BasicApp;
import com.example.garmin_heartrate.DataRepository;
import com.example.garmin_heartrate.db.entity.FitReading;
import com.example.garmin_heartrate.db.entity.Session;

import java.util.List;

public class ConnectDeviceViewModel extends AndroidViewModel {

    private DataRepository mRepository;

    public ConnectDeviceViewModel(@NonNull Application application, DataRepository repository) {
        super(application);
        mRepository = repository;
    }

    public void insertSession(Session session, List<FitReading> readings) {
        mRepository.createSession(session, readings);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;
        private final DataRepository mRepository;

        public Factory(@NonNull Application application) {
            mApplication = application;
            mRepository = ((BasicApp) application).getRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new ConnectDeviceViewModel(mApplication, mRepository);
        }

    }

}
