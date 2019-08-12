package com.example.garmin_heartrate.ui;

import android.content.Intent;
import android.os.Bundle;

import com.example.garmin_heartrate.BasicApp;
import com.example.garmin_heartrate.DataRepository;
import com.example.garmin_heartrate.R;
import com.example.garmin_heartrate.db.entity.Session;
import com.example.garmin_heartrate.viewModel.UserListViewModel;
import com.example.garmin_heartrate.db.entity.User;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final int NEW_USER_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            UserListFragment fragment = new UserListFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment, UserListFragment.TAG).commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void show(User user) {
        UserFragment userFragment = UserFragment.forUser(user.getId());

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("user")
                .replace(R.id.fragment_container,
                        userFragment, null).commit();
    }

    public void show(Session session) {
        SessionFragment sessionFragment = SessionFragment.forSession(session.getId());

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("session")
                .replace(R.id.fragment_container,
                        sessionFragment, null).commit();
    }
}
