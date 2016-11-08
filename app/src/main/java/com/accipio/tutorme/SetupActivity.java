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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

        setupCheckBox();

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
                       findViewById(R.id.desc).setVisibility(visibility);
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
    }

    public void onClick(View view) {
        if (view.getId() == R.id.go) {
            saveInfo();

            Intent intent = new Intent(this, BrowseActivity.class);
            startActivity(intent);
        }
    }

    private void saveInfo() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SetupActivity.this);
        SharedPreferences.Editor editor = prefs.edit();

        Map<String, Integer> map = new HashMap<String, Integer>()
        {{
            put("email", R.id.email);
            put("toLearn", R.id.toLearn);
            put("desc", R.id.desc);
            put("toTeach", R.id.toTeach);
            put("rate", R.id.rate);
        }};

        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            EditText editText = (EditText) findViewById((Integer)pair.getValue());
            String text = editText.getText().toString();
            if (text != null && !text.isEmpty()) {
                editor.putString(pair.getKey().toString(), text);
            }
        }
        it.remove();

        if (isTutor) {
            ((TutorMeApplication) SetupActivity.this.getApplication()).setTutor(true);

            editor.putBoolean("isTutor", isTutor);
        }
        editor.apply();
    }
}
