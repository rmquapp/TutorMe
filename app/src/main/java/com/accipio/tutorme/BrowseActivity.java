package com.accipio.tutorme;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v7.widget.SwitchCompat;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Adapter;
import android.widget.SeekBar;
import android.widget.NumberPicker;
import android.widget.EditText;
import android.view.LayoutInflater;

import com.facebook.login.LoginManager;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class BrowseActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {

    DrawerLayout drawer;

    public RecyclerView recycler;
    public TutorsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_browse);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        View mActionBarView = getLayoutInflater().inflate(R.layout.action_bar, null);
        actionBar.setCustomView(mActionBarView);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        recycler = (RecyclerView) findViewById(R.id.browse_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);
        recycler.setHasFixedSize(false);

        adapter = populateList(recycler);
        recycler.setAdapter(adapter);

        handleCheckBox();

        setupNavigationDrawer();
    }

    public void popDialog(View view){
        showDialog(adapter,recycler);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        goToSelection(id);
        return true;
    }

    private void goToSelection(int id) {
        Intent intent = null;
        switch (id) {
            case R.id.drawer_home:
                // Browse activity, no page change
                toggleMenu(getCurrentFocus());
                return;
            case R.id.drawer_settings:
                intent = new Intent(this, SetupActivity.class);
                break;
            case R.id.drawer_messages:
                intent = new Intent(this, MessagesActivity.class);
                break;
            case R.id.drawer_about:
                // TODO: replace with about page
                return;
            case R.id.drawer_policy:
                // TODO: replace with privacy policy page
                return;
            case R.id.drawer_logout:
                // TODO: remove after demo
                LoginManager.getInstance().logOut();
                intent = new Intent(this, MainActivity.class);
                break;
            default:
                break;
        }
        drawer.closeDrawer(Gravity.LEFT, false);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private TutorsAdapter populateList(RecyclerView recycler) {
        ArrayList<Tutor> tutors = new ArrayList<>();
        // Test data
        tutors.add(new Tutor("000000", "Fred Riley", "Grad student of Chemistry", new String[] {"CHEM210", "CHEM", "CHEMISTRY"}, 4.1f, 0, "20"));
        tutors.add(new Tutor("111111", "Clarence McCoy", "Professional math tutor", new String[] {"MATH", "MATH211", "MATH271", "MATH"}, 3.0f, 1, "35"));
        tutors.add(new Tutor("222222", "Karl Wells", "4th year Science, A- student", new String[] {"SCIENCE", "CHEM210"}, 5.0f, 1, "15"));
        tutors.add(new Tutor("333333", "Arlene Ruiz", "3rd year studying Business", new String[] {"FNCE317", "ACCT217", "ACCT301"}, -1, 0, "20"));
        tutors.add(new Tutor("444444", "Jay Owens", "Professional tutor, specializing in Electrical Engineering", new String[] {"ENGG", "ENEL300", "ENEL"}, 3.9f, 0, "50"));
        tutors.add(new Tutor("555555", "Allen Chambers", "Teaching math, stats, econ, and more", new String[] {"MATH", "STATS", "ECON201", "ECON203"}, -1, 0, "10"));
        tutors.add(new Tutor("666666", "Kendra Rodriguez", "Masters student in Computer Science", new String[] {"CPSC", "CPSC231", "CPSC233", "CPSC331", "CPSC441"}, 4.5f, 1, "30"));
        tutors.add(new Tutor("777777", "Robert Chan", "Professional tutor, TA'd many CPSC courses", new String[] {"CPSC", "CPSC", "CPSC217", "CPSC219", "CPSC235"}, -1, 1, "40"));
        tutors.add(new Tutor("888888", "Shawn Ingram", "Studying Physics", new String[] {"PHYSICS", "PHYS211", "PHYS221"}, 5.0f, 1, "10"));
        tutors.add(new Tutor("999999", "Melody Fletcher", "English and languages", new String[] {"ENGL201", "ENGL301", "FRENCH", "GERMAN"}, 3.5f, 0, "25"));
        tutors.add(new Tutor("123345", "Matt Smith", "TA for stats, cpsc, and math", new String[] {"MATH211", "MATH249", "MATH271", "STAT213", "STAT205", "CPSC", "CPSC231"}, 3.3f, 0, "20"));
        tutors.add(new Tutor("234567", "Roland Byrd", "Economics & business tutor, 4.0 GPA", new String[] {"ECON201", "MKTG317", "FINANCE"}, -1, 1, "15"));
        tutors.add(new Tutor("345678", "Caleb Bennet", "Need help with any math course? Contact me!", new String[] {"MATH", "MATH211", "MATH249", "MATH267", "MATH311"}, 2.0f, 1, "10"));
        tutors.add(new Tutor("456789", "Linh Cybulski", "Grad student, excellent tutor", new String[] {"MATH", "STATS205", "STATS", "ECON", "ENGG"}, 3.0f, 0, "10"));
        tutors.add(new Tutor("567890", "Eduardo Kahle", "Math and chem - help with assignments, studying", new String[] {"MATH211", "MATH271", "MATH249", "CHEM213", "CHEM"}, -1, 1, "10"));
        TutorsAdapter adapter = new TutorsAdapter(tutors);
        return adapter;
    }

    private void handleCheckBox() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isTutor = prefs.getBoolean(String.valueOf("isTutor"), false);

        if (isTutor) {
            final SwitchCompat status = (SwitchCompat) findViewById(R.id.status);
            status.setVisibility(View.VISIBLE);
            status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String s = isChecked ? "Available" : "Unavailable";
                    status.setText(s);
                }
            });
        }
    }

    public void toggleMenu(View view) {
        if (drawer.isDrawerOpen(Gravity.LEFT)) {
            drawer.closeDrawer(Gravity.LEFT);
        } else {
            drawer.openDrawer(Gravity.LEFT);
        }
    }

    private void setupNavigationDrawer() {
        NavigationView navView = (NavigationView) findViewById(R.id.menu);
        navView.setNavigationItemSelectedListener(this);

        View hView = navView.getHeaderView(0);

        String fname = ((TutorMeApplication) BrowseActivity.this.getApplication()).getFirstName();
        String lname = ((TutorMeApplication) BrowseActivity.this.getApplication()).getLastName();
        TextView name = (TextView) hView.findViewById(R.id.header_name);
        name.setText(String.format("%s %s", fname, lname));

        Bitmap picture = ((TutorMeApplication) BrowseActivity.this.getApplication()).getImage();
        CircleImageView image = (CircleImageView) hView.findViewById(R.id.header_image);
        image.setImageBitmap(picture);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String email = prefs.getString("email", "");
        TextView emailView = (TextView) hView.findViewById(R.id.header_email);
        emailView.setText(email);
    }

    public void showDialog(final TutorsAdapter adapter, final RecyclerView recycler) {
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_layout, (ViewGroup)findViewById(R.id.dialog_layout));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);
        builder.setTitle(" Search Options");
        builder.setCancelable(true);

        final TextView seekText = (TextView) layout.findViewById(R.id.maxrate);
        final SeekBar seek = (SeekBar)layout.findViewById(R.id.seekbar);
        final EditText input = (EditText) layout.findViewById(R.id.searchCourse);
        final CheckBox checkBox = (CheckBox) layout.findViewById(R.id.checkbox_id);

        final TextView tv = (TextView) layout.findViewById(R.id.minrating);
        final NumberPicker np = (NumberPicker) layout.findViewById(R.id.np);
        np.setMinValue(1);
        np.setMaxValue(5);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                tv.setText("Minimum Rating: " + newVal);
            }
        });

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                seekText.setText("Maximum Rate: " + progress);
            }

            public void onStartTrackingTouch(SeekBar arg0) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        builder.setPositiveButton(
                "Okay",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id ) {
                        String checked = (checkBox.isChecked()) ? "1" : "0";
                        int rating = np.getValue();
                        int rate = seek.getProgress();
                        rate = (rate <=0) ? 80 : rate;
                        String mText = input.getText().toString().toUpperCase();
                        mText = (mText.equals("")) ? "NONE" : mText;

                        String filterStringFinal = "rate_" + rate + "-";
                        filterStringFinal = filterStringFinal + "rating_" + rating + "-";
                        filterStringFinal = filterStringFinal + mText + "-" + checked;

                        adapter.getFilter().filter(filterStringFinal);
                    }
                });

        builder.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog yourDialog = builder.create();
        yourDialog.show();
    }
}
