package com.accipio.tutorme;


/**
 * Created by rachel on 2016-11-03.
 */
public class Tutor {
    private String id;
    private String name;
    private String desc;
    private String[] courses;
    private String rating;
    private int status;

    public Tutor(String id, String name, String desc, String[] courses, String rating, int status) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.courses = courses;
        this.rating = rating;
        this.status = status;
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

    public String getRating() {
        return rating;
    }

    public int getStatus() {
        return status;
    }
}
