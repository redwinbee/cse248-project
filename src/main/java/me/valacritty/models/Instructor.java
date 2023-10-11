package me.valacritty.models;

import me.valacritty.models.enums.Campus;
import me.valacritty.models.enums.Day;
import me.valacritty.models.enums.Rank;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

public class Instructor implements Comparable<Instructor>, Serializable {
    private String id;
    private String firstName;
    private String middleName;
    private String lastName;
    private Campus homeCampus;
    private String homePhone;
    private String workPhone;
    private String address;
    private LocalDate dateHired;
    private List<Course> courses;
    private Set<Course> previousCourses;
    private Rank rank;
    private boolean canTeachOnline;
    private Set<Campus> preferredCampuses;
    private boolean canTeachSecondCourse;
    private boolean canTeachThirdCourse;
    private Set<Day> availableEarlyMornings;
    private Set<Day> availableMornings;
    private Set<Day> availableEarlyAfternoons;
    private Set<Day> availableAfternoons;
    private Set<Day> availableEvenings;
    private Set<Day> availableWeekends;

    public Instructor(String firstName, String middleName, String lastName) {
        this.id = generateRandomId();
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.courses = new LinkedList<>();
        this.preferredCampuses = EnumSet.noneOf(Campus.class);
        this.availableEarlyMornings = EnumSet.noneOf(Day.class);
        this.availableMornings = EnumSet.noneOf(Day.class);
        this.availableEarlyAfternoons = EnumSet.noneOf(Day.class);
        this.availableAfternoons = EnumSet.noneOf(Day.class);
        this.availableEvenings = EnumSet.noneOf(Day.class);
        this.availableWeekends = EnumSet.noneOf(Day.class);
    }

    public Instructor(String firstName, String lastName) {
        this(firstName, "", lastName);
    }

    public Instructor() {

    }

    private String generateRandomId() {
        Random random = new Random();
        int max = (int) Math.pow(10, 8) - 1;
        return String.valueOf(random.nextInt(max + 1));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public Set<Course> getPreviousCourses() {
        return previousCourses;
    }

    public void setPreviousCourses(Set<Course> previousCourses) {
        this.previousCourses = previousCourses;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public boolean isCanTeachOnline() {
        return canTeachOnline;
    }

    public void setCanTeachOnline(boolean canTeachOnline) {
        this.canTeachOnline = canTeachOnline;
    }

    public Set<Campus> getPreferredCampuses() {
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

    public Set<Day> getAvailableEarlyMornings() {
        return availableEarlyMornings;
    }

    public void setAvailableEarlyMornings(Set<Day> availableEarlyMornings) {
        this.availableEarlyMornings = availableEarlyMornings;
    }

    public Set<Day> getAvailableMornings() {
        return availableMornings;
    }

    public void setAvailableMornings(Set<Day> availableMornings) {
        this.availableMornings = availableMornings;
    }

    public Set<Day> getAvailableEarlyAfternoons() {
        return availableEarlyAfternoons;
    }

    public void setAvailableEarlyAfternoons(Set<Day> availableEarlyAfternoons) {
        this.availableEarlyAfternoons = availableEarlyAfternoons;
    }

    public Set<Day> getAvailableAfternoons() {
        return availableAfternoons;
    }

    public void setAvailableAfternoons(Set<Day> availableAfternoons) {
        this.availableAfternoons = availableAfternoons;
    }

    public Set<Day> getAvailableEvenings() {
        return availableEvenings;
    }

    public void setAvailableEvenings(Set<Day> availableEvenings) {
        this.availableEvenings = availableEvenings;
    }

    public Set<Day> getAvailableWeekends() {
        return availableWeekends;
    }

    public void setAvailableWeekends(EnumSet<Day> availableWeekends) {
        this.availableWeekends = availableWeekends;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Instructor.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("firstName='" + firstName + "'")
                .add("middleName='" + middleName + "'")
                .add("lastName='" + lastName + "'")
                .add("homeCampus=" + homeCampus)
                .add("homePhone='" + homePhone + "'")
                .add("workPhone='" + workPhone + "'")
                .add("address='" + address + "'")
                .add("dateHired=" + dateHired)
                .add("courses=" + courses)
                .add("previousCourses=" + previousCourses)
                .add("rank=" + rank)
                .add("canTeachOnline=" + canTeachOnline)
                .add("preferredCampuses=" + preferredCampuses)
                .add("canTeachSecondCourse=" + canTeachSecondCourse)
                .add("canTeachThirdCourse=" + canTeachThirdCourse)
                .add("availableEarlyMornings=" + availableEarlyMornings)
                .add("availableMornings=" + availableMornings)
                .add("availableEarlyAfternoons=" + availableEarlyAfternoons)
                .add("availableAfternoons=" + availableAfternoons)
                .add("availableEvenings=" + availableEvenings)
                .add("availableWeekends=" + availableWeekends)
                .toString();
    }

    @Override
    public int compareTo(Instructor o) {
        return this.getId().compareTo(o.getId());
    }
}