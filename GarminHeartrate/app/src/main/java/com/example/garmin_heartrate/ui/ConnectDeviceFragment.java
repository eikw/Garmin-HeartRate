package com.example.garmin_heartrate.ui;

import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.garmin_heartrate.BasicApp;
import com.example.garmin_heartrate.DataRepository;
import com.example.garmin_heartrate.R;
import com.example.garmin_heartrate.connectivity.WatchConnectionInfo;
import com.example.garmin_heartrate.db.entity.FitReading;
import com.example.garmin_heartrate.db.entity.Session;
import com.example.garmin_heartrate.viewModel.ConnectDeviceViewModel;
import com.garmin.android.connectiq.ConnectIQ;
import com.garmin.android.connectiq.IQApp;
import com.garmin.android.connectiq.IQDevice;
import com.garmin.android.connectiq.exception.InvalidStateException;
import com.garmin.android.connectiq.exception.ServiceUnavailableException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ConnectDeviceFragment extends Fragment implements ConnectIQ.ConnectIQListener, ConnectIQ.IQApplicationInfoListener, ConnectIQ.IQDeviceEventListener, ConnectIQ.IQApplicationEventListener, AdapterView.OnItemSelectedListener {

    public static final String TAG = ConnectDeviceFragment.class.getSimpleName();
    public static final int KEY_MESSAGE_PAYLOAD = -2;
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_SESSION_ID = "session_id";

    private ConnectDeviceViewModel mViewModel;

    private Spinner mDeviceList;
    private TextView mStatusText;
    private Button mSaveButton;

    private int mUserId;
    private int mSessionId;

    private boolean mSdkReady;
    private ConnectIQ mConnectIQ;
    private List<IQDevice> mDevices;
    private IQDevice mDevice;
    private IQApp mMyApp;

    public static ConnectDeviceFragment newSession (int userId) {
        ConnectDeviceFragment fragment = new ConnectDeviceFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }


    public static ConnectDeviceFragment newInstance() {
        return new ConnectDeviceFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.connect_device_fragment, container, false);

        mDeviceList = view.findViewById(R.id.device_list);
        mSaveButton = view.findViewById(R.id.device_button_save);
        mStatusText = view.findViewById(R.id.device_status);

        mMyApp = new IQApp(WatchConnectionInfo.APP_ID);
        Log.i(TAG,"Status: " + mMyApp.getStatus().toString());
        mConnectIQ = ConnectIQ.getInstance(this.getContext(), ConnectIQ.IQConnectType.TETHERED);
        mConnectIQ.initialize(this.getContext(), true, this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ConnectDeviceViewModel.Factory factory = new ConnectDeviceViewModel.Factory(
                getActivity().getApplication());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mUserId = bundle.getInt(KEY_USER_ID);
        }

        mViewModel = ViewModelProviders.of(this, factory).get(ConnectDeviceViewModel.class);
    }

    private void populateDeviceList() {
        try {
            mDevices = mConnectIQ.getKnownDevices();

            if (mDevices != null && !mDevices.isEmpty()) {
                mDevice = mDevices.get(0);
                registerWithDevice();

                List<String> deviceNames = new ArrayList<>();
                for (IQDevice device: mDevices) {
                    deviceNames.add(device.getFriendlyName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, deviceNames);
                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                mDeviceList.setAdapter(adapter);
                mDeviceList.setOnItemSelectedListener(this);
            }

        } catch (InvalidStateException e) {
            // should not happen
        } catch (ServiceUnavailableException e) {
            mStatusText.setText("Garmin Service is unavailable");
        }
    }

    public void registerWithDevice() {
        if (mDevice != null && mSdkReady) {
            // Register for device status updates
            try {
                mConnectIQ.registerForDeviceEvents(mDevice, this);
            } catch (InvalidStateException e) {
                Log.wtf(TAG, "InvalidStateException:  We should not be here!");
            }

            // Register for application status updates
            try {
                mConnectIQ.getApplicationInfo(WatchConnectionInfo.APP_ID, mDevice, this);
            } catch (InvalidStateException e1) {
                Log.d(TAG, "e1: " + e1.getMessage());
            } catch (ServiceUnavailableException e1) {
                Log.d(TAG, "e2: " + e1.getMessage());
            }

            // Register to receive messages from the device
            try {
                mConnectIQ.registerForAppEvents(mDevice, mMyApp, this);
            } catch (InvalidStateException e) {
                Toast.makeText(this.getContext(), "ConnectIQ is not in a valid state", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void unregisterWithDevice() {
        if (mDevice != null && mSdkReady) {
            // It is a good idea to unregister everything and shut things down to
            // release resources and prevent unwanted callbacks.
            try {
                mConnectIQ.unregisterForDeviceEvents(mDevice);

                if (mMyApp != null) {
                    mConnectIQ.unregisterForApplicationEvents(mDevice, mMyApp);
                }
            } catch (InvalidStateException e) {
            }
        }
    }

    @Override
    public void onSdkReady() {
        Log.d(TAG, "sdk is ready");
        mSdkReady = true;
        populateDeviceList();
    }

    @Override
    public void onInitializeError(ConnectIQ.IQSdkErrorStatus iqSdkErrorStatus) {
        Log.d(TAG, "sdk initialization error");
        mSdkReady = false;
    }

    @Override
    public void onSdkShutDown() {
        Log.d(TAG, "sdk shut down");
        mSdkReady = false;
    }

    @Override
    public void onMessageReceived(IQDevice iqDevice, IQApp iqApp, List<Object> list, ConnectIQ.IQMessageStatus iqMessageStatus) {
// We know from our Comm sample widget that it will only ever send us strings, but in case
        // we get something else, we are simply going to do a toString() on each object in the
        // message list.
        StringBuilder builder = new StringBuilder();

        if (list.size() > 0) {
            for (Object o : list)
                if (o == null) {
                    builder.append("<null> received");
                } else if (o instanceof HashMap) {
                    try {
                        @SuppressWarnings("rawtypes")
                        ArrayList<Object> readingDto = (ArrayList<Object>) ((HashMap) o).get("CURRENT_SESSION");
                        int startTime = (int) ((HashMap) o).get("START_TIME");
                        int endTime = (int) ((HashMap) o).get("END_TIME");

                        Date end = new Date(new Long(endTime)*1000);
                        Date start = new Date(new Long(startTime)*1000);
                        Session session = new Session(0,mUserId,start, end);

                        List<FitReading> reading = new ArrayList<>();
                        if (readingDto != null) {
                            for (Object element: readingDto) {

                                float altitude = (float) ((HashMap) element).get("altitude");
                                int heartRate = (int) ((HashMap) element).get("heartRate");
                                float speed = (float) ((HashMap) element).get("speed");
                                int cadence = (int) ((HashMap) element).get("cadence");
                                float temperature = (float) ((HashMap) element).get("temperature");
                                float pressure = (float) ((HashMap) element).get("pressure");
                                float heading = (float) ((HashMap) element).get("heading");
                                int timeStamp = (int) ((HashMap) element).get("timestamp");
                                Date time = new Date(new Long(timeStamp)*1000);
                                reading.add(
                                        new FitReading(
                                                0,
                                                0,
                                                time,
                                                speed,
                                                cadence,
                                                heartRate,
                                                temperature,
                                                altitude,
                                                pressure,
                                                heading));
                            }

                            mViewModel.insertSession(session, reading);
                        }

                        builder = null;
                    } catch (Exception ex) {
                        builder.append("MonkeyHash received:\n\n");
                        builder.append(o.toString());
                    }

                } else {
                    builder.append(o.toString());
                    builder.append("\r\n");
                }
        } else {
            builder.append("Received an empty message from the application");
        }

        if (builder != null) {
            Toast.makeText(this.getContext(), builder.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onApplicationInfoReceived(IQApp iqApp) {
        Log.d(TAG, "application info received");
    }

    @Override
    public void onApplicationNotInstalled(String s) {
        // The disc golf app is not installed on the device so we have
        // to let the user know to install it.
        AlertDialog.Builder dialog = new AlertDialog.Builder(this.getContext());
        dialog.setTitle("App not installed");
        dialog.setMessage("Please install the Garmin Connect mobile app to connect your watch");
        dialog.setPositiveButton(android.R.string.ok, null);
        dialog.create().show();
    }

    @Override
    public void onDeviceStatusChanged(IQDevice iqDevice, IQDevice.IQDeviceStatus iqDeviceStatus) {
        mStatusText.setText(iqDeviceStatus.name());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        unregisterWithDevice();
        mDevice = mDevices.get(position);
        registerWithDevice();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
