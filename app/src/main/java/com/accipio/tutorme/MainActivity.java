package com.accipio.tutorme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.LoginResult;

//import com.sinch.android.rtc.ClientRegistration;
//import com.sinch.android.rtc.Sinch;
//import com.sinch.android.rtc.SinchClient;
//import com.sinch.android.rtc.SinchClientListener;
//import com.sinch.android.rtc.SinchError;

import java.io.IOException;
import java.lang.InterruptedException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CallbackManager callbackManager;

    private LoginButton facebookLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSDKinitializeDelay();

        if (AccessToken.getCurrentAccessToken() != null) {
            goToHome();
        }
        else {
            resetUserData();
        }

        hideBars();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);

        EditText password = (EditText)findViewById(R.id.password);
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        findViewById(R.id.facebook).setOnClickListener(this);
        facebookLogin = (LoginButton)findViewById(R.id.fb_login);
        registerLoginCallback(facebookLogin);

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

    private void registerLoginCallback(LoginButton loginButton) {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getFBDetails(loginResult);
                startActivity(SetupActivity.class);
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
            Thread.sleep(150);
        } catch (InterruptedException e) {
            System.out.println(e.getStackTrace());
        }
    }

    private void goToHome() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String fname = prefs.getString("firstName", "");
        String lname = prefs.getString("lastName", "");
        String id = prefs.getString("ID", "");

        setGlobalUserData(fname, lname, id);
        startActivity(BrowseActivity.class);
    }

    private void hideBars() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void startActivity(Class c) {
        Intent intent = new Intent(MainActivity.this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private class getFBProfilePicture extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap bitmap = null;
            try {
                URL imageURL = new URL(urls[0]);
                bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
            } catch (IOException e) {
                System.out.println(e.getStackTrace());
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            ((TutorMeApplication) MainActivity.this.getApplication()).setImage(result);
        }
    }

    private void resetUserData() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Editor editor = sharedPrefs.edit();
        editor.clear();
        editor.commit();
    }

    private void setGlobalUserData(String fname, String lname, String id) {
        ((TutorMeApplication) MainActivity.this.getApplication()).setFirstName(fname);
        ((TutorMeApplication) MainActivity.this.getApplication()).setLastName(lname);
        ((TutorMeApplication) MainActivity.this.getApplication()).setID(id);

        // Retrieve and store profile picture
        getFBProfilePicture task = new getFBProfilePicture();
        task.execute(new String[] { "https://graph.facebook.com/" + id + "/picture?type=large" });
    }

    private void persistUserData(String fname, String lname, String id) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        Editor editor = prefs.edit();
        editor.putString("firstName", fname);
        editor.putString("lastName", lname);
        editor.putString("ID", id);
        editor.apply();
    }

    public void onClick(View view) {
        if (view.getId() == R.id.facebook) {
            facebookLogin.performClick();
        }
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
