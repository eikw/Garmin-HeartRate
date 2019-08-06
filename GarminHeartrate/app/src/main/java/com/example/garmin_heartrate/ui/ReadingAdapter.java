package com.example.garmin_heartrate.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garmin_heartrate.R;
import com.example.garmin_heartrate.databinding.ReadingItemBinding;
import com.example.garmin_heartrate.db.entity.FitReading;

import java.util.List;

public class ReadingAdapter extends RecyclerView.Adapter<ReadingAdapter.ReadingViewHolder> {

    private List<FitReading> mReadingList;

    @Nullable
    private final ReadingClickCallback mReadingClickCallback;

    public ReadingAdapter (@Nullable ReadingClickCallback clickCallback) {
        mReadingClickCallback = clickCallback;
    }

    public void setReadingList(final List<FitReading> readings) {
        if (mReadingList == null) {
            mReadingList = readings;
            notifyItemChanged(0, readings.size());
        } else {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mReadingList.size();
                }

                @Override
                public int getNewListSize() {
                    return readings.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    FitReading old = mReadingList.get(oldItemPosition);
                    FitReading reading = readings.get(newItemPosition);
                    return old.getId() == reading.getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    FitReading old = mReadingList.get(oldItemPosition);
                    FitReading reading = readings.get(newItemPosition);
                    return old.getId() == reading.getId()
                            && old.getSessionId() == reading.getSessionId()
                            && old.getTimestamp() == reading.getTimestamp();
                }
            });

            mReadingList = readings;
            diffResult.dispatchUpdatesTo(this);
        }
    }

    @Override
    public ReadingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ReadingItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.reading_item, parent, false);
        binding.setCallback(mReadingClickCallback);
        return new ReadingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReadingViewHolder holder, int position) {
        holder.binding.setReading(mReadingList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mReadingList == null ? 0 : mReadingList.size();
    }

    static class ReadingViewHolder extends RecyclerView.ViewHolder {
        final ReadingItemBinding binding;

        ReadingViewHolder(ReadingItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
