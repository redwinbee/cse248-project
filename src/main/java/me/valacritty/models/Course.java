package me.valacritty.models;

import java.util.StringJoiner;

public class Course implements Comparable<Course>, Cloneable {
    private String courseNumber;
    private String title;
    private byte credits;

    public Course() {
        this.credits = 4;    // TODO remove this when we get real data
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public byte getCredits() {
        return credits;
    }

    public void setCredits(byte credits) {
        this.credits = credits;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Course.class.getSimpleName() + "[", "]")
                .add("courseNumber='" + courseNumber + "'")
                .add("title='" + title + "'")
                .add("credits=" + credits)
                .toString();
    }

    @Override
    public int compareTo(Course o) {
        return this.getCourseNumber().compareTo(o.getCourseNumber());
    }

    @Override
    public Course clone() {
        try {
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return (Course) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
