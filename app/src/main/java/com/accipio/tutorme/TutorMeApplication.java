package com.accipio.tutorme;

import android.app.Application;

/**
 * Created by rachel on 2016-11-02.
 */
public class TutorMeApplication extends Application {

    private String userID;
    private String firstName;
    private String lastName;

    public String setID() {
        return userID;
    }

    public void setID(String ID) {
        userID = ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String fname) {
        firstName = fname;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lname) {
        lastName = lname;
    }
}
