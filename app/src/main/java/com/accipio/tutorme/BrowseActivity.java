package com.accipio.tutorme;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;

import java.util.ArrayList;


public class BrowseActivity extends AppCompatActivity {

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

        addDrawerItems();
    }

    private void addDrawerItems() {
        ListView mDrawerList;
        ArrayAdapter<String> mAdapter;
        mDrawerList = (ListView)findViewById(R.id.menu);

        String[] osArray = { "Name", "Item1", "Item2", "Item3", "Item4" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
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
}
