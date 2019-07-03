package com.example.garmin_heartrate.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.garmin_heartrate.R;
import com.example.garmin_heartrate.db.entity.User;
import com.example.garmin_heartrate.viewModel.UserListViewModel;
import com.example.garmin_heartrate.databinding.FragmentUserListBinding;

import java.util.List;

public class UserListFragment extends Fragment {

    public static final String TAG = "UserListFragment";

    private UserAdapter mUserAdapter;

    private FragmentUserListBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_list, container, false);

        mUserAdapter = new UserAdapter(mUserClickCallback);
        mBinding.usersList.setAdapter(mUserAdapter);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final UserListViewModel viewModel = ViewModelProviders.of(this).get(UserListViewModel.class);

        subscribeUi(viewModel.getAllUsers());
    }

    private void subscribeUi(LiveData<List<User>> liveData) {
        liveData.observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                if (users != null) {
                    mBinding.setIsLoading(false);
                    mUserAdapter.setUserList(users);
                } else {
                    mBinding.setIsLoading(true);
                }
                mBinding.executePendingBindings();
            }
        });
    }

    private final UserClickCallback mUserClickCallback = new UserClickCallback() {
        @Override
        public void onClick(User user) {
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                ((MainActivity) getActivity()).show(user);
            }
        }
    };
}
