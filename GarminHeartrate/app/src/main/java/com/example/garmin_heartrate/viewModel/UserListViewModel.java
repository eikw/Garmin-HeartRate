package com.example.garmin_heartrate.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.garmin_heartrate.BasicApp;
import com.example.garmin_heartrate.DataRepository;
import com.example.garmin_heartrate.db.entity.User;

import java.util.List;

public class UserListViewModel extends AndroidViewModel {

    private DataRepository mRepository;
    private MediatorLiveData<List<User>> mObservableUsers;

    public UserListViewModel(Application application) {
        super(application);

        mObservableUsers = new MediatorLiveData<>();
        mObservableUsers.setValue(null);
        mRepository = ((BasicApp) application).getRepository();
        LiveData<List<User>> users = mRepository.getUsers();
        mObservableUsers.addSource(users, mObservableUsers::setValue);
    }

    public LiveData<List<User>> getAllUsers() { return mObservableUsers; }

    public void insert(User user) { mRepository.insertUser(user); }
}
