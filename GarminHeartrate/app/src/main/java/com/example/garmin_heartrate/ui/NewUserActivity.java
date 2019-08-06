package com.example.garmin_heartrate.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.garmin_heartrate.R;
import com.example.garmin_heartrate.db.entity.User;

public class NewUserActivity extends AppCompatActivity {

    public static final String EXTRA_CREATE = "com.example.android.garmin_heartrate.userCreate";

    private EditText mEditFirstNameView;
    private EditText mEditLastNameView;
    private EditText mEditHeightView;
    private EditText mEditWeightView;
    private EditText mEditAgeView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_user);
        mEditFirstNameView = findViewById(R.id.edit_firstName);
        mEditLastNameView = findViewById(R.id.edit_lastName);
        mEditHeightView = findViewById(R.id.edit_height);
        mEditWeightView = findViewById(R.id.edit_weight);
        mEditAgeView = findViewById(R.id.edit_age);

        final Button button = findViewById(R.id.button_save);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createIntent = new Intent();

                String firstName = mEditFirstNameView.getText().toString();
                String lastName = mEditLastNameView.getText().toString();
                double weight = Double.parseDouble(mEditWeightView.getText().toString());
                double height = Double.parseDouble(mEditHeightView.getText().toString());
                int age = Integer.parseInt(mEditAgeView.getText().toString());

                User user = new User(0, firstName, lastName, height, weight, age);

                createIntent.putExtra(EXTRA_CREATE, user);
                setResult(RESULT_OK, createIntent);
                finish();
            }
        });
    }
}
