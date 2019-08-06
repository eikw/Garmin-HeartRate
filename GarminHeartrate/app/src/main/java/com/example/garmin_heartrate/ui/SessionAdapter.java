package com.example.garmin_heartrate.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garmin_heartrate.R;
import com.example.garmin_heartrate.databinding.SessionItemBinding;
import com.example.garmin_heartrate.db.entity.Session;

import java.util.List;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.SessionViewHolder> {

    private List<Session> mSessionList;

    @Nullable
    private final SessionClickCallback mSessionClickCallback;

    public SessionAdapter(@Nullable SessionClickCallback clickCallback) {
        mSessionClickCallback = clickCallback;
    }

    public void setSessionList(final List<Session> sessions) {
        if (mSessionList == null) {
            mSessionList = sessions;
            notifyItemRangeChanged(0, sessions.size());
        } else {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mSessionList.size();
                }

                @Override
                public int getNewListSize() {
                    return sessions.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    Session old = mSessionList.get(oldItemPosition);
                    Session session = sessions.get(newItemPosition);
                    return old.getId() == session.getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Session old = mSessionList.get(oldItemPosition);
                    Session session = sessions.get(newItemPosition);
                    return old.getId() == session.getId()
                            && old.getUserId() == session.getUserId()
                            && old.getStartTime() == session.getStartTime()
                            && old.getEndTime() == session.getEndTime();
                }
            });

            mSessionList = sessions;
            diffResult.dispatchUpdatesTo(this);
        }
    }
    @Nullable
    public int getLatestSessionId() {
        if (mSessionList.isEmpty()) {
            return 0;
        } else {
            int count = 0;
            for(Session session: mSessionList) {
                if(session.getId() > count) {
                    count = session.getId();
                }
            }
            return count;
        }
    }

    @Override
    public SessionViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        SessionItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.session_item, parent, false);
        binding.setCallback(mSessionClickCallback);
        return new SessionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionViewHolder holder, int position) {
        holder.binding.setSession(mSessionList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mSessionList == null ? 0 : mSessionList.size();
    }

    static class SessionViewHolder extends RecyclerView.ViewHolder {
        final SessionItemBinding binding;

        SessionViewHolder(SessionItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
