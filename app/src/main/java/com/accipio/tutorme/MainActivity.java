package com.accipio.tutorme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

//import com.sinch.android.rtc.ClientRegistration;
//import com.sinch.android.rtc.Sinch;
//import com.sinch.android.rtc.SinchClient;
//import com.sinch.android.rtc.SinchClientListener;
//import com.sinch.android.rtc.SinchError;

import java.lang.InterruptedException;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    private LoginButton loginButton;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSDKinitializeDelay();

        boolean loggedIn = AccessToken.getCurrentAccessToken() != null;
        if (loggedIn) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String fname = prefs.getString("firstName", "");
            String lname = prefs.getString("lastName", "");
            String id = prefs.getString("ID", "");

            setGlobalUserData(fname, lname, id);
            startMainActivity();
        }
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);

        loginButton = (LoginButton)findViewById(R.id.login_button);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        registerLoginCallback();

// TODO: Fix
//        // Instantiate a SinchClient using the SinchClientBuilder.
//        android.content.Context context = this.getApplicationContext();
//        SinchClient sinchClient = Sinch.getSinchClientBuilder().context(context)
//                .applicationKey("<application key>")
//                .applicationSecret("<application secret>")
//                .environmentHost("sandbox.sinch.com")
//                .userId("<user id>")
//                .build();
//
//        // Specify the client capabilities.
//        sinchClient.setSupportMessaging(true);
//        sinchClient.setSupportActiveConnectionInBackground(true);
//        sinchClient.startListeningOnActiveConnection();
//
//        sinchClient.addSinchClientListener(new SinchClientListener() {
//            public void onClientStarted(SinchClient client) { }
//            public void onClientStopped(SinchClient client) { }
//            public void onClientFailed(SinchClient client, SinchError error) { }
//            public void onRegistrationCredentialsRequired(SinchClient client, ClientRegistration registrationCallback) { }
//            public void onLogMessage(int level, String area, String message) { }
//        });
//        //Start this after login
//        sinchClient.start();
    }

    private void registerLoginCallback() {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getFBDetails(loginResult);
                startMainActivity();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
                // TODO: Error dialog
            }
        });
    }

    private void getFBDetails(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
            loginResult.getAccessToken(),
            new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String ID = response.getJSONObject().getString("id");
                            String name = response.getJSONObject().getString("name");
                            String fname = name.split(" ")[0];
                            String lname = name.split(" ")[1];

                            setGlobalUserData(fname, lname, ID);
                            persistUserData(fname, lname, ID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
            });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,id");
        request.setParameters(parameters);
        try {
            request.executeAsync().get();
        } catch (ExecutionException e) {
            e.printStackTrace();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void FacebookSDKinitializeDelay() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println(e.getStackTrace());
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(MainActivity.this, SetupActivity.class);
        startActivity(intent);
    }

    private void setGlobalUserData(String fname, String lname, String id) {
        ((TutorMeApplication) MainActivity.this.getApplication()).setFirstName(fname);
        ((TutorMeApplication) MainActivity.this.getApplication()).setLastName(lname);
        ((TutorMeApplication) MainActivity.this.getApplication()).setID(id);
    }

    private void persistUserData(String fname, String lname, String id) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        Editor editor = prefs.edit();
        editor.putString("firstName", fname);
        editor.putString("lastName", lname);
        editor.putString("ID", id);
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
