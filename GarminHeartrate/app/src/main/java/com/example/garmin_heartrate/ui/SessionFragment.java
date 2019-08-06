package com.example.garmin_heartrate.ui;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.garmin_heartrate.R;
import com.example.garmin_heartrate.databinding.SessionFragmentBinding;
import com.example.garmin_heartrate.db.entity.FitReading;
import com.example.garmin_heartrate.db.entity.Session;
import com.example.garmin_heartrate.viewModel.SessionViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SessionFragment extends Fragment {

    private static final String KEY_SESSION_ID = "session_id";

    private SessionViewModel mViewModel;

    private SessionFragmentBinding mBinding;

    private ReadingAdapter mReadingAdapter;

    private LineChart mChart;

    public static SessionFragment newInstance() {
        return new SessionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,R.layout.session_fragment, container, false);
        mReadingAdapter = new ReadingAdapter(mReadingCallback);
        mBinding.readingList.setAdapter(mReadingAdapter);

        mChart = mBinding.chart;
        return mBinding.getRoot();
    }

    private final ReadingClickCallback mReadingCallback = new ReadingClickCallback() {
        @Override
        public void onClick(FitReading reading) {

        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // mViewModel = ViewModelProviders.of(this).get(SessionViewModel.class);
        SessionViewModel.Factory factory = new SessionViewModel.Factory(
                getActivity().getApplication(), getArguments().getInt(KEY_SESSION_ID));

        mViewModel = ViewModelProviders.of(this, factory).get(SessionViewModel.class);

        mBinding.setSessionViewModel(mViewModel);

        subscribeToModel(mViewModel);
    }

    private void subscribeToModel(final SessionViewModel model) {
        model.getObservableSession().observe(this, new Observer<Session>() {
            @Override
            public void onChanged(Session session) {
                model.setSession(session);
            }
        });

        model.getReadings().observe(this, new Observer<List<FitReading>>() {
            @Override
            public void onChanged(List<FitReading> fitReadings) {
                if (fitReadings != null) {
                    mBinding.setIsLoading(false);
                    mReadingAdapter.setReadingList(fitReadings);

                    List<Entry> entriesHeartRate = new ArrayList<>();
                    List<Entry> entriesSpeed = new ArrayList<>();
                    List<Entry> entriesAltitude = new ArrayList<>();
                    List<Entry> entriesCadence = new ArrayList<>();
                    List<Entry> entriesTemp = new ArrayList<>();
                    List<Entry> entriesHeading = new ArrayList<>();
                    List<Entry> entriesPressure = new ArrayList<>();
                    List<String> XLabels = new ArrayList<>();

                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

                    for(int i = 0; i < fitReadings.size(); i++) {
                        entriesHeartRate.add(new Entry(i, (float) fitReadings.get(i).getHeartRate()));
                        entriesSpeed.add(new Entry(i, (float) fitReadings.get(i).getSpeed()));
                        entriesAltitude.add(new Entry(i, (float) fitReadings.get(i).getAltitude()));
                        entriesCadence.add(new Entry(i, (float) fitReadings.get(i).getCadence()));
                        entriesTemp.add(new Entry(i, (float) fitReadings.get(i).getTemperature()));
                        entriesHeading.add(new Entry(i, (float) fitReadings.get(i).getHeading()));
                        entriesPressure.add(new Entry(i, (float) fitReadings.get(i).getPressure()));
                        XLabels.add(format.format(fitReadings.get(i).getTimestamp()));
                    }

                    List<ILineDataSet> dataSets = new ArrayList<>();
                    LineDataSet dataSet = new LineDataSet(entriesHeartRate, "HeartRate");
                    dataSet.setColor(ColorTemplate.rgb("FF0000"));
                    dataSet.setDrawCircles(false);
                    LineDataSet dataSetSpeed = new LineDataSet(entriesSpeed, "Speed");
                    dataSetSpeed.setColor(ColorTemplate.rgb("0000FF"));
                    dataSetSpeed.setDrawCircles(false);

                    //LineDataSet dataAltitude = new LineDataSet(entriesAltitude, "Altitude");
                    //dataAltitude.setColor(ColorTemplate.rgb("FFFF00"));
                    LineDataSet dataCadence = new LineDataSet(entriesCadence, "Cadence");
                    dataCadence.setColor(ColorTemplate.rgb("00FF00"));
                    dataCadence.setDrawCircles(false);
                    LineDataSet dataTemp = new LineDataSet(entriesTemp, "Temp");
                    dataTemp.setColor(ColorTemplate.rgb("0F0F00"));
                    dataTemp.setDrawCircles(false);
                    LineDataSet dataHeading = new LineDataSet(entriesHeading, "Heading");
                    dataHeading.setColor(ColorTemplate.rgb("F0F000"));
                    dataHeading.setDrawCircles(false);
                    //LineDataSet dataPressure = new LineDataSet(entriesPressure, "Pressure");
                    //dataPressure.setColor(ColorTemplate.rgb("0000FF"));


                    dataSets.add(dataSet);
                    dataSets.add(dataSetSpeed);

                    //dataSets.add(dataAltitude);
                    dataSets.add(dataCadence);
                    dataSets.add(dataTemp);
                    dataSets.add(dataHeading);
                    //dataSets.add(dataPressure);


                    LineData data = new LineData(dataSets);

                    ValueFormatter formatter = new ValueFormatter() {
                        @Override
                        public String getAxisLabel(float value, AxisBase axis) {
                            return XLabels.get((int) value);
                        }
                    };
                    XAxis xAxis = mChart.getXAxis();

                    xAxis.setGranularity(XLabels.size() / 4); // minimum axis-step (interval) is 1
                    xAxis.setValueFormatter(formatter);

                    mChart.setData(data);
                    mChart.invalidate();
                } else {
                    mBinding.setIsLoading(true);
                }
            }
        });
    }

    public static SessionFragment forSession(int sessionId) {
        SessionFragment fragment = new SessionFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_SESSION_ID, sessionId);
        fragment.setArguments(args);
        return fragment;
    }

}
