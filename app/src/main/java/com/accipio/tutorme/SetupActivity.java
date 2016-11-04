package com.accipio.tutorme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SetupActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        info = (TextView)findViewById(R.id.info);
        findViewById(R.id.go).setOnClickListener(this);

        showName();
    }

    private void showName() {
        String firstName = ((TutorMeApplication) SetupActivity.this.getApplication()).getFirstName();
        String greeting = String.format("Hello, %s!", firstName);
        info.setText(greeting);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.go) {
            Intent intent = new Intent(this, BrowseActivity.class);
            startActivity(intent);
        }
    }
}
