package com.accipio.tutorme;

import android.app.Application;
import android.graphics.Bitmap;


/**
 * Created by rachel on 2016-11-02.
 */
public class TutorMeApplication extends Application {

    private String userID;
    private String firstName;
    private String lastName;
    private boolean tutor = false;
    private Bitmap image;

    public String getID() {
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

    public boolean isTutor() {
        return tutor;
    }

    public void setTutor(boolean isTutor) {
        tutor = isTutor;
    }

    public void setImage(Bitmap picture) {
        image = picture;
    }

    public Bitmap getImage() {
        return image;
    }
}
