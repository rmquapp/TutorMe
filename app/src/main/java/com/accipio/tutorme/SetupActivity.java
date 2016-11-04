package com.accipio.tutorme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.CompoundButton;
import android.widget.TextView;

public class SetupActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView info;

    private boolean isTutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        info = (TextView)findViewById(R.id.info);
        findViewById(R.id.go).setOnClickListener(this);

        showName();

        final CheckBox box = (CheckBox)findViewById(R.id.checkBox);
        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                   int visibility = isChecked ? View.VISIBLE : View.INVISIBLE;
                   findViewById(R.id.desc).setVisibility(visibility);
                   findViewById(R.id.teachCourses).setVisibility(visibility);
                   findViewById(R.id.teachCoursesInfo).setVisibility(visibility);
                   findViewById(R.id.rate).setVisibility(visibility);

                    isTutor = isChecked;
               }
            }
        );
    }

    private void showName() {
        String firstName = ((TutorMeApplication) SetupActivity.this.getApplication()).getFirstName();
        String greeting = String.format("Hello, %s!", firstName);
        info.setText(greeting);
    }

    public void onClick(View view) {
        if (isTutor) {
            ((TutorMeApplication) SetupActivity.this.getApplication()).setTutor(true);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SetupActivity.this);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isTutor", isTutor);
            editor.apply();
        }

        if (view.getId() == R.id.go) {
            Intent intent = new Intent(this, BrowseActivity.class);
            startActivity(intent);
        }
    }
}
