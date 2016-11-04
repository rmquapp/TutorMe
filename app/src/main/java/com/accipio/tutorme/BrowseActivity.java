package com.accipio.tutorme;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;

import java.util.ArrayList;


public class BrowseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_browse);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.browse_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);

        ArrayList<Tutor> tutors = new ArrayList<>();
        // Test data
        String[] courses = {"CPSC411"};
        Tutor tutor1 = new Tutor("123456", "Person One", "Grad student studying Physics", courses, "4.1", 0);
        Tutor tutor2 = new Tutor("111111", "Test Person", "Professional tutor", courses, "3.0", 1);
        Tutor tutor3 = new Tutor("222222", "Other Person", "4th year Science student", courses, "5.0", 1);
        tutors.add(tutor3);
        tutors.add(tutor2);
        tutors.add(tutor1);
        TutorsAdapter adapter = new TutorsAdapter(tutors);
        recyclerView.setAdapter(adapter);
    }
}
