package com.accipio.tutorme;


/**
 * Created by rachel on 2016-11-03.
 */
public class Tutor {
    private String id;
    private String name;
    private String desc;
    private String[] courses;
    private float rating;
    private int status;
    private String rate;

    public Tutor(String id, String name, String desc, String[] courses, float rating, int status, String rate) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.courses = courses;
        this.rating = rating;
        this.status = status;
        this.rate = rate;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String[] getCourses() {
        return courses;
    }

    public float getRating() {
        return rating;
    }

    public int getStatus() {
        return status;
    }

    public String getRate() { return rate; }
}
