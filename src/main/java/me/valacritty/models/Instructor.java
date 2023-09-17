package me.valacritty.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

public class Instructor implements Comparable<Instructor> {
    private String id;
    private String name;
    private Campus homeCampus;
    private String homePhone;
    private String workPhone;
    private String address;
    private LocalDate dateHired;
    private HashSet<String> courses;
    private String rank;
    private boolean canTeachOnline;
    private HashSet<Campus> preferredCampuses;
    private boolean canTeachSecondCourse;
    private boolean canTeachThirdCourse;
    private ArrayList<Day> availableEarlyMornings;
    private ArrayList<Day> availableMornings;
    private ArrayList<Day> availableEarlyAfternoons;
    private ArrayList<Day> availableAfternoons;
    private ArrayList<Day> availableEvenings;
    private ArrayList<Day> availableWeekends;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public HashSet<String> getCourses() {
        return courses;
    }

    public void setCourses(HashSet<String> courses) {
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

    public HashSet<Campus> getPreferredCampuses() {
        return preferredCampuses;
    }

    public void setPreferredCampuses(HashSet<Campus> preferredCampuses) {
        this.preferredCampuses = preferredCampuses;
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

    public ArrayList<Day> getAvailableEarlyMornings() {
        return availableEarlyMornings;
    }

    public void setAvailableEarlyMornings(ArrayList<Day> availableEarlyMornings) {
        this.availableEarlyMornings = availableEarlyMornings;
    }

    public ArrayList<Day> getAvailableMornings() {
        return availableMornings;
    }

    public void setAvailableMornings(ArrayList<Day> availableMornings) {
        this.availableMornings = availableMornings;
    }

    public ArrayList<Day> getAvailableEarlyAfternoons() {
        return availableEarlyAfternoons;
    }

    public void setAvailableEarlyAfternoons(ArrayList<Day> availableEarlyAfternoons) {
        this.availableEarlyAfternoons = availableEarlyAfternoons;
    }

    public ArrayList<Day> getAvailableAfternoons() {
        return availableAfternoons;
    }

    public void setAvailableAfternoons(ArrayList<Day> availableAfternoons) {
        this.availableAfternoons = availableAfternoons;
    }

    public ArrayList<Day> getAvailableEvenings() {
        return availableEvenings;
    }

    public void setAvailableEvenings(ArrayList<Day> availableEvenings) {
        this.availableEvenings = availableEvenings;
    }

    public ArrayList<Day> getAvailableWeekends() {
        return availableWeekends;
    }

    public void setAvailableWeekends(ArrayList<Day> availableWeekends) {
        this.availableWeekends = availableWeekends;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Instructor{");
        sb.append("id='").append(id).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", homeCampus=").append(homeCampus);
        sb.append(", homePhone='").append(homePhone).append('\'');
        sb.append(", workPhone='").append(workPhone).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", dateHired=").append(dateHired);
        sb.append(", courses=").append(courses);
        sb.append(", rank='").append(rank).append('\'');
        sb.append(", canTeachOnline=").append(canTeachOnline);
        sb.append(", preferredCampuses=").append(preferredCampuses);
        sb.append(", canTeachSecondCourse=").append(canTeachSecondCourse);
        sb.append(", canTeachThirdCourse=").append(canTeachThirdCourse);
        sb.append(", availableEarlyMornings=").append(availableEarlyMornings);
        sb.append(", availableMornings=").append(availableMornings);
        sb.append(", availableEarlyAfternoons=").append(availableEarlyAfternoons);
        sb.append(", availableAfternoons=").append(availableAfternoons);
        sb.append(", availableEvenings=").append(availableEvenings);
        sb.append(", availableWeekends=").append(availableWeekends);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int compareTo(Instructor o) {
        return this.getId().compareTo(o.getId());
    }
}