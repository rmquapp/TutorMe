package com.accipio.tutorme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SetupActivity extends AppCompatActivity {

    private TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        info = (TextView)findViewById(R.id.info);
        showName();
    }

    private void showName() {
        String firstName = ((TutorMeApplication) SetupActivity.this.getApplication()).getFirstName();
        String greeting = String.format("Hello, %s!", firstName);
        info.setText(greeting);
    }
}
