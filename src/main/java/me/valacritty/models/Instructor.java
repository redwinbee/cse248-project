package me.valacritty.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

public class Instructor {
    private String id;
    private String fullName;
    private Campus homeCampus;
    private String homePhone;
    private String workPhone;
    private String address;
    private LocalDate dateHired;
    private HashSet<String> courses;
    private String rank;
    private boolean canTeachOnline;
    private ArrayList<Character> campuses;
    private boolean canTeachSecondCourse;
    private boolean canTeachThirdCourse;
    private int numEves;
    private ArrayList<String> availableEarlyMornings;
    private ArrayList<String> availableMornings;
    private ArrayList<String> availableEarlyEvenings;
    private ArrayList<String> availableEvenings;
    private ArrayList<String> availableWeekends;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Campus getHomeCampus() {
        return homeCampus;
    }

    public void setHomeCampus(Campus homeCampus) {
        this.homeCampus = homeCampus;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDateHired() {
        return dateHired;
    }

    public void setDateHired(LocalDate dateHired) {
        this.dateHired = dateHired;
    }

    public HashSet getCourses() {
        return courses;
    }

    public void setCourses(HashSet courses) {
        this.courses = courses;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public boolean isCanTeachOnline() {
        return canTeachOnline;
    }

    public void setCanTeachOnline(boolean canTeachOnline) {
        this.canTeachOnline = canTeachOnline;
    }

    public ArrayList<Character> getCampuses() {
        return campuses;
    }

    public void setCampuses(ArrayList<Character> campuses) {
        this.campuses = campuses;
    }

    public boolean isCanTeachSecondCourse() {
        return canTeachSecondCourse;
    }

    public void setCanTeachSecondCourse(boolean canTeachSecondCourse) {
        this.canTeachSecondCourse = canTeachSecondCourse;
    }

    public boolean isCanTeachThirdCourse() {
        return canTeachThirdCourse;
    }

    public void setCanTeachThirdCourse(boolean canTeachThirdCourse) {
        this.canTeachThirdCourse = canTeachThirdCourse;
    }

    public int getNumEves() {
        return numEves;
    }

    public void setNumEves(int numEves) {
        this.numEves = numEves;
    }

    public ArrayList<String> getAvailableEarlyMornings() {
        return availableEarlyMornings;
    }

    public void setAvailableEarlyMornings(ArrayList<String> availableEarlyMornings) {
        this.availableEarlyMornings = availableEarlyMornings;
    }

    public ArrayList<String> getAvailableMornings() {
        return availableMornings;
    }

    public void setAvailableMornings(ArrayList<String> availableMornings) {
        this.availableMornings = availableMornings;
    }

    public ArrayList<String> getAvailableEarlyEvenings() {
        return availableEarlyEvenings;
    }

    public void setAvailableEarlyEvenings(ArrayList<String> availableEarlyEvenings) {
        this.availableEarlyEvenings = availableEarlyEvenings;
    }

    public ArrayList<String> getAvailableEvenings() {
        return availableEvenings;
    }

    public void setAvailableEvenings(ArrayList<String> availableEvenings) {
        this.availableEvenings = availableEvenings;
    }

    public ArrayList<String> getAvailableWeekends() {
        return availableWeekends;
    }

    public void setAvailableWeekends(ArrayList<String> availableWeekends) {
        this.availableWeekends = availableWeekends;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Instructor{");
        sb.append("id='").append(id).append('\'');
        sb.append(", fullName='").append(fullName).append('\'');
        sb.append(", homeCampus='").append(homeCampus).append('\'');
        sb.append(", homePhone='").append(homePhone).append('\'');
        sb.append(", workPhone='").append(workPhone).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", dateHired=").append(dateHired);
        sb.append(", courses=").append(courses);
        sb.append(", rank='").append(rank).append('\'');
        sb.append(", canTeachOnline=").append(canTeachOnline);
        sb.append(", campuses=").append(campuses);
        sb.append(", canTeachSecondCourse=").append(canTeachSecondCourse);
        sb.append(", canTeachThirdCourse=").append(canTeachThirdCourse);
        sb.append(", numEves=").append(numEves);
        sb.append(", availableEarlyMornings=").append(availableEarlyMornings);
        sb.append(", availableMornings=").append(availableMornings);
        sb.append(", availableEarlyEvenings=").append(availableEarlyEvenings);
        sb.append(", availableEvenings=").append(availableEvenings);
        sb.append(", availableWeekends=").append(availableWeekends);
        sb.append('}');
        return sb.toString();
    }
}