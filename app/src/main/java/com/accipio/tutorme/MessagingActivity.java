package com.accipio.tutorme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.login.LoginManager;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        drawer = (DrawerLayout) findViewById(R.id.drawer_messaging_layout);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        View mActionBarView = getLayoutInflater().inflate(R.layout.action_bar, null);
        actionBar.setCustomView(mActionBarView);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

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
                intent = new Intent(this, BrowseActivity.class);
                break;
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

    public void toggleMenu(View view) {
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

        String fname = ((TutorMeApplication) MessagingActivity.this.getApplication()).getFirstName();
        String lname = ((TutorMeApplication) MessagingActivity.this.getApplication()).getLastName();
        TextView name = (TextView)hView.findViewById(R.id.header_name);
        name.setText(String.format("%s %s", fname, lname));

        Bitmap picture = ((TutorMeApplication) MessagingActivity.this.getApplication()).getImage();
        CircleImageView image = (CircleImageView)hView.findViewById(R.id.header_image);
        image.setImageBitmap(picture);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String email = prefs.getString("email", "");
        TextView emailView = (TextView)hView.findViewById(R.id.header_email);
        emailView.setText(email);
    }
}
