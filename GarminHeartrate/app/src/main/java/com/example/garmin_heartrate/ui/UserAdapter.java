package com.example.garmin_heartrate.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garmin_heartrate.R;
import com.example.garmin_heartrate.databinding.RecyclerviewItemBinding;
import com.example.garmin_heartrate.db.entity.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    List<User> mUserList;

    @Nullable
    private final UserClickCallback mUserClickCallback;

    public UserAdapter(@Nullable UserClickCallback clickCallback) {
        mUserClickCallback = clickCallback;
        setHasStableIds(true);
    }

    public void setUserList(final List<User> userList) {
        if (mUserList != null) {
            mUserList = userList;
            notifyItemRangeChanged(0, userList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mUserList == null ? 0 : mUserList.size();
                }

                @Override
                public int getNewListSize() {
                    return userList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mUserList.get(oldItemPosition).getId() ==
                            userList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    User newUser = userList.get(newItemPosition);
                    User oldUser = mUserList.get(oldItemPosition);
                    return newUser.getId() == oldUser.getId();
                }
            });

            mUserList = userList;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerviewItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_item, parent, false);
        binding.setCallback(mUserClickCallback);
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        holder.binding.setUser(mUserList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() { return mUserList == null ? 0 : mUserList.size(); }

    @Override
    public long getItemId(int position) { return mUserList.get(position).getId(); }

    static class UserViewHolder extends RecyclerView.ViewHolder {

        final RecyclerviewItemBinding binding;

        public UserViewHolder(RecyclerviewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
