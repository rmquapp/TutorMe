package com.accipio.tutorme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import android.view.MenuItem;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class BrowseActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_browse);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        View mActionBarView = getLayoutInflater().inflate(R.layout.action_bar, null);
        actionBar.setCustomView(mActionBarView);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        RecyclerView recycler = (RecyclerView)findViewById(R.id.browse_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);
        recycler.setHasFixedSize(false);

        populateList(recycler);

        handleCheckBox();

        setupNavigationDrawer();
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
                // TODO: replace with messaging activity if we get to that point
                return;
            case R.id.drawer_about:
                // TODO: replace with about page
                return;
            case R.id.drawer_policy:
                // TODO: replace with privacy policy page
                return;
            default:
                break;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void populateList(RecyclerView recycler) {
        ArrayList<Tutor> tutors = new ArrayList<>();
        // Test data
        String[] courses = {"CPSC411"};
        Tutor tutor1 = new Tutor("123456", "Person One", "Grad student studying Memes", courses, "4.1", 0);
        Tutor tutor2 = new Tutor("111111", "Test Person", "Professional tutor", courses, "3.0", 1);
        Tutor tutor3 = new Tutor("222222", "Other Person", "4th year Science student", courses, "5.0", 1);
        tutors.add(tutor3);
        tutors.add(tutor2);
        tutors.add(tutor1);
        TutorsAdapter adapter = new TutorsAdapter(tutors);
        recycler.setAdapter(adapter);
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(Gravity.LEFT)) {
            drawer.closeDrawer(Gravity.LEFT);
        }
        else {
            drawer.openDrawer(Gravity.LEFT);
        }
    }

    private void setupNavigationDrawer() {
        NavigationView navView = (NavigationView) findViewById(R.id.menu);
        navView.setNavigationItemSelectedListener(this);

        View hView =  navView.getHeaderView(0);

        String fname = ((TutorMeApplication) BrowseActivity.this.getApplication()).getFirstName();
        String lname = ((TutorMeApplication) BrowseActivity.this.getApplication()).getLastName();
        TextView name = (TextView)hView.findViewById(R.id.header_name);
        name.setText(String.format("%s %s", fname, lname));

        Bitmap picture = ((TutorMeApplication) BrowseActivity.this.getApplication()).getImage();
        CircleImageView image = (CircleImageView)hView.findViewById(R.id.header_image);
        image.setImageBitmap(picture);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String email = prefs.getString("email", "");
        TextView emailView = (TextView)hView.findViewById(R.id.header_email);
        emailView.setText(email);
    }
}
