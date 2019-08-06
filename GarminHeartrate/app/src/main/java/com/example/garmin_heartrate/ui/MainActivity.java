package com.example.garmin_heartrate.ui;

import android.content.Intent;
import android.os.Bundle;

import com.example.garmin_heartrate.BasicApp;
import com.example.garmin_heartrate.DataRepository;
import com.example.garmin_heartrate.R;
import com.example.garmin_heartrate.db.entity.Session;
import com.example.garmin_heartrate.viewModel.UserListViewModel;
import com.example.garmin_heartrate.db.entity.User;
import com.example.garmin_heartrate.viewModel.UserViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

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

        if (requestCode == NEW_USER_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            User user = (User)data.getSerializableExtra(NewUserActivity.EXTRA_CREATE);
            DataRepository repo = ((BasicApp) getApplication()).getRepository();
            repo.insertUser(user);

            Toast.makeText(
                    getApplicationContext(),
                    user.getFullName(),
                    Toast.LENGTH_LONG
            ).show();
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG
            ).show();
        }
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

    public void create() {

    }

    /*
    private UserListViewModel mUserListViewModel;
    public static final int NEW_USER_ACTIVITY_REQUEST_CODE = 1;
    public static final String USER_DETAIL_KEY = "com.example.garmin_heartrate.user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewUserActivity.class);
                startActivityForResult(intent, NEW_USER_ACTIVITY_REQUEST_CODE);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final UserListAdapter adapter = new UserListAdapter(mUserClickCallback);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUserListViewModel = ViewModelProviders.of(this).get(UserListViewModel.class);

        mUserListViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                adapter.setUsers(users);
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private final UserClickCallback mUserClickCallback = new UserClickCallback() {
        @Override
        public void onClick(User user) {
            Intent intent = new Intent(MainActivity.this, UserDetailActivity.class);
            intent.putExtra(USER_DETAIL_KEY, user);
            startActivity(intent);
        }
    };
    */
}
