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
        String[] courses = {"CPSC411"};
        Tutor tutor1 = new Tutor("123456", "Person One", "Grad student studying Memes", courses, "4.1", 0, "35");
        Tutor tutor2 = new Tutor("111111", "Test Person", "Professional tutor", courses, "3.0", 1, "23");
        Tutor tutor3 = new Tutor("222222", "Other Person", "4th year Science student", courses, "5.0", 1, "10");
        tutors.add(tutor3);
        tutors.add(tutor2);
        tutors.add(tutor1);
        TutorsAdapter adapter = new TutorsAdapter(tutors);
        return adapter;
    }

    private void handleCheckBox() {
        boolean isTutor = ((TutorMeApplication) BrowseActivity.this.getApplication()).isTutor();

        if (isTutor) {
            final SwitchCompat status = (SwitchCompat) findViewById(R.id.status);
            status.setVisibility(View.VISIBLE);
            status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String s = isChecked ? "Online" : "Offline";
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

        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        //Dialog yourDialog = new Dialog(this);
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_layout, (ViewGroup)findViewById(R.id.your_dialog_root_element));
        builder1.setView(layout);

        final TextView seekText = (TextView) layout.findViewById(R.id.seekbar);
        final SeekBar seek = (SeekBar)layout.findViewById(R.id.your_dialog_seekbar);
        builder1.setTitle("Filter Options");
        builder1.setCancelable(true);

        final TextView editText = (TextView) layout.findViewById(R.id.edit);
        final EditText input = (EditText) layout.findViewById(R.id.phone);

        final CheckBox checkBox = (CheckBox) layout.findViewById(R.id.checkbox_id);

        final TextView tv = (TextView) layout.findViewById(R.id.tv);
        final NumberPicker np = (NumberPicker) layout.findViewById(R.id.np);
        np.setMinValue(1);
        //Specify the maximum value/number of NumberPicker
        np.setMaxValue(5);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected number from picker
                tv.setText("Minimum Rating: " + newVal);
            }
        });

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                //Do something here with new value
                seekText.setText("Maximum Price: " + progress);
            }

            public void onStartTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub

            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }
        });


        builder1.setPositiveButton(
                "Okay",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id ) {
                        String checked1;
                        if (checkBox.isChecked()) {
                           checked1 = "1";
                        }
                        else {
                            checked1 = "0";
                        }
                        int value = seek.getProgress();
                        if (value <= 0){
                            value = 50;
                        }
                        int ratingNumber = np.getValue();
                        String m_Text = input.getText().toString();
                        if (m_Text.equals("")) {
                            m_Text = "NONE";
                        }
                        String filterStringFinal = "price_" + value + "-";

                        filterStringFinal = filterStringFinal + "rating_" + ratingNumber + "-";
                        filterStringFinal = filterStringFinal + m_Text + "-"+ checked1;
                        // EG "price.30-rating.5-CPSC441"
                        //Log.d("tag",filterStringFinal);


                        adapter.getFilter().filter(filterStringFinal);

                        //recycler.setAdapter(adapter);
                        //dialog.dismiss();
                    }
                });

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        AlertDialog yourDialog = builder1.create();
        yourDialog.show();
    }
}
