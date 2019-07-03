package com.example.garmin_heartrate.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garmin_heartrate.R;
import com.example.garmin_heartrate.databinding.RecyclerviewItemBinding;
import com.example.garmin_heartrate.db.entity.User;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {

    private List<User> mUsers;

    @Nullable
    private final UserClickCallback mUserClickCallback;


    UserListAdapter(@Nullable UserClickCallback clickCallback) {
        mUserClickCallback = clickCallback;
        setHasStableIds(true);
    }

    public void setUserList(final List<User> userList) {
        if (mUsers == null) {
            mUsers = userList;
            notifyItemRangeInserted(0, userList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() { return mUsers.size(); }

                @Override
                public int getNewListSize() { return mUsers.size(); }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mUsers.get(oldItemPosition).getId() ==
                            mUsers.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    User newUser = mUsers.get(newItemPosition);
                    User oldUser = mUsers.get(oldItemPosition);
                    return newUser.getId() == oldUser.getId();
                }
            });
            mUsers = userList;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_item,
                        parent, false);
        binding.setCallback(mUserClickCallback);
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.binding.setUser(mUsers.get(position));
        holder.binding.executePendingBindings();
    }

    void setUsers(List<User> users) {
        mUsers = users;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() { return mUsers == null ? 0 : mUsers.size(); }

    @Override
    public long getItemId(int position) { return mUsers.get(position).getId(); }

    class UserViewHolder extends RecyclerView.ViewHolder {

        final RecyclerviewItemBinding binding;

        public UserViewHolder(RecyclerviewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
