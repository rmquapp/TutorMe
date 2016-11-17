package com.accipio.tutorme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;


public class SetupActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView info;

    private boolean isTutor;

    private int[] ids = {R.id.email, R.id.toLearn, R.id.toTeach, R.id.description, R.id.rate};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        info = (TextView)findViewById(R.id.info);
        findViewById(R.id.go).setOnClickListener(this);

        showName();

        setupCheckBox();

        restoreInfo();

        setupBubbleTextViews();
    }

    private void showName() {
        String firstName = ((TutorMeApplication) SetupActivity.this.getApplication()).getFirstName();
        String greeting = String.format("Hello, %s!", firstName);
        info.setText(greeting);
    }

    private void setupCheckBox() {
        final CheckBox box = (CheckBox)findViewById(R.id.checkBox);
        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                   @Override
                   public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                       int visibility = isChecked ? View.VISIBLE : View.INVISIBLE;
                       findViewById(R.id.description).setVisibility(visibility);
                       findViewById(R.id.toTeach).setVisibility(visibility);
                       findViewById(R.id.teachCoursesInfo).setVisibility(visibility);
                       findViewById(R.id.rate).setVisibility(visibility);

                       isTutor = isChecked;
                   }
               }
        );
    }

    private void setupBubbleTextViews() {
        BubbleTextView toLearn = (BubbleTextView) findViewById(R.id.toLearn);
        BubbleTextView toTeach = (BubbleTextView) findViewById(R.id.toTeach);
        BubbleTextView[] bubbles = {toLearn, toTeach};

        for (BubbleTextView bubble : bubbles) {
            /* Ensures that predictive text is off (also disables auto-capitalization for certain keyboards) */
            bubble.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

            String[] item = getResources().getStringArray(R.array.course);
            //TODO: also populate from items in database?

            bubble.setAdapter(new ArrayAdapter(this,
                    android.R.layout.simple_dropdown_item_1line, item));
            bubble.setTokenizer(new SpaceTokenizer());
        }
        toLearn.setBubbles();
        toTeach.setBubbles();
    }

    public void onClick(View view) {
        if (view.getId() == R.id.go) {
            saveInfo();

            Intent intent = new Intent(this, BrowseActivity.class);
            startActivity(intent);
        }
    }

    private void restoreInfo() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        for (int id : ids) {
            String text = prefs.getString(String.valueOf(id), "");
            EditText editText = (EditText) findViewById(id);
            if (text != null && !text.isEmpty()) {
                editText.setText(text);
            }
        }

        if (((TutorMeApplication) SetupActivity.this.getApplication()).isTutor()) {
            CheckBox box = (CheckBox)findViewById(R.id.checkBox);
            box.setChecked(true);
        }
    }

    private void saveInfo() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SetupActivity.this);
        SharedPreferences.Editor editor = prefs.edit();

        for (int id : ids) {
            EditText editText = (EditText) findViewById(id);
            String text = editText.getText().toString();
            editor.putString(String.valueOf(id), text);
        }

        ((TutorMeApplication) SetupActivity.this.getApplication()).setTutor(isTutor);
        editor.putBoolean("isTutor", isTutor);

        editor.apply();
    }
}
