package com.accipio.tutorme;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainMenu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Begin the animation on the menu circle
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.spin_2_win);
        // The circle we are animating
        ImageView menuCircle = (ImageView)findViewById(R.id.menuCircle);
        menuCircle.startAnimation(animation);
    }
}
