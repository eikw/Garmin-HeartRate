package com.example.garmin_heartrate.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.garmin_heartrate.R;
import com.example.garmin_heartrate.databinding.FragmentUserBinding;
import com.example.garmin_heartrate.db.entity.Session;
import com.example.garmin_heartrate.db.entity.User;
import com.example.garmin_heartrate.viewModel.UserViewModel;

import java.util.List;


public class UserFragment extends Fragment {

    private static final String KEY_USER_ID = "user_id";
    public static final int  CONNCET_DEVICE_REQUEST_CODE = 1;

    private FragmentUserBinding mBinding;

    private SessionAdapter mSessionAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false);

        mBinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ConnectDevice.class);
                startActivityForResult(intent, CONNCET_DEVICE_REQUEST_CODE);
            }
        });

        mSessionAdapter = new SessionAdapter(mSessionClickCallback);
        mBinding.userList.setAdapter(mSessionAdapter);
        return mBinding.getRoot();
    }

    private final SessionClickCallback mSessionClickCallback = new SessionClickCallback() {
        @Override
        public void onClick(Session session) {
            // TODO
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        UserViewModel.Factory factory = new UserViewModel.Factory(
                getActivity().getApplication(), getArguments().getInt(KEY_USER_ID));

        final UserViewModel model = ViewModelProviders.of(this, factory).get(UserViewModel.class);

        mBinding.setUserViewModel(model);

        subscribeToModel(model);
    }

    private void subscribeToModel(final UserViewModel model) {
        model.getObservableUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                model.setUser(user);
            }
        });

        model.getSessions().observe(this, new Observer<List<Session>>() {
            @Override
            public void onChanged(List<Session> sessions) {
                if (sessions != null) {
                    mBinding.setIsLoading(false);
                    mSessionAdapter.setSessionList(sessions);
                } else {
                    mBinding.setIsLoading(true);
                }
            }
        });
    }

    public static UserFragment forUser(int userId) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }
}
