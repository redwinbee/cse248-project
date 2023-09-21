package me.valacritty.models;

import me.valacritty.models.enums.Campus;
import me.valacritty.models.enums.Day;

import java.time.LocalDate;
import java.util.EnumSet;
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
    private EnumSet<Campus> preferredCampuses;
    private boolean canTeachSecondCourse;
    private boolean canTeachThirdCourse;
    private EnumSet<Day> availableEarlyMornings;
    private EnumSet<Day> availableMornings;
    private EnumSet<Day> availableEarlyAfternoons;
    private EnumSet<Day> availableAfternoons;
    private EnumSet<Day> availableEvenings;
    private EnumSet<Day> availableWeekends;

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

    public EnumSet<Campus> getPreferredCampuses() {
        return preferredCampuses;
    }

    public void setPreferredCampuses(EnumSet<Campus> preferredCampuses) {
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

    public EnumSet<Day> getAvailableEarlyMornings() {
        return availableEarlyMornings;
    }

    public void setAvailableEarlyMornings(EnumSet<Day> availableEarlyMornings) {
        this.availableEarlyMornings = availableEarlyMornings;
    }

    public EnumSet<Day> getAvailableMornings() {
        return availableMornings;
    }

    public void setAvailableMornings(EnumSet<Day> availableMornings) {
        this.availableMornings = availableMornings;
    }

    public EnumSet<Day> getAvailableEarlyAfternoons() {
        return availableEarlyAfternoons;
    }

    public void setAvailableEarlyAfternoons(EnumSet<Day> availableEarlyAfternoons) {
        this.availableEarlyAfternoons = availableEarlyAfternoons;
    }

    public EnumSet<Day> getAvailableAfternoons() {
        return availableAfternoons;
    }

    public void setAvailableAfternoons(EnumSet<Day> availableAfternoons) {
        this.availableAfternoons = availableAfternoons;
    }

    public EnumSet<Day> getAvailableEvenings() {
        return availableEvenings;
    }

    public void setAvailableEvenings(EnumSet<Day> availableEvenings) {
        this.availableEvenings = availableEvenings;
    }

    public EnumSet<Day> getAvailableWeekends() {
        return availableWeekends;
    }

    public void setAvailableWeekends(EnumSet<Day> availableWeekends) {
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