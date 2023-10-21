package me.valacritty.models;

import me.valacritty.models.enums.Campus;
import me.valacritty.models.enums.Day;
import me.valacritty.models.enums.Rank;
import me.valacritty.models.enums.TimeOfDay;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

public class Instructor implements Comparable<Instructor>, Serializable {
    private Map<Day, Set<TimeOfDay>> availabilities;
    private Map<Course, Integer> courseFrequencies;
    private String id;
    private String firstName;
    private String middleName;
    private String lastName;
    private Campus homeCampus;
    private String homePhone;
    private String workPhone;
    private String address;
    private LocalDate dateHired;
    private Set<Course> courses;
    private Set<Course> previousCourses;
    private Rank rank;
    private boolean canTeachOnline;
    private Set<Campus> preferredCampuses;
    private boolean canTeachSecondCourse;
    private boolean canTeachThirdCourse;

    public Instructor(String firstName, String middleName, String lastName) {
        this.id = generateRandomId();
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.courses = new TreeSet<>();
        this.preferredCampuses = EnumSet.noneOf(Campus.class);
        this.availabilities = new TreeMap<>();
        this.courseFrequencies = new TreeMap<>();
    }

    public Instructor(String firstName, String lastName) {
        this(firstName, "", lastName);
    }

    public Instructor() {
        this.id = generateRandomId();
        this.availabilities = new TreeMap<>();
        this.courseFrequencies = new TreeMap<>();
    }

    /**
     * Adds onto the availability of this instructor the given set of days
     * and their corresponding time of days. This function will assume that
     * all days provided are part of the same time of day.
     *
     * @param days      The days to be added.
     * @param timeOfDay The time of day of the set of provided days.
     */
    public void addAvailability(Set<Day> days, TimeOfDay timeOfDay) {
        for (Day day : days) {
            availabilities.computeIfAbsent(day, k -> new HashSet<>()).add(timeOfDay);
        }
    }

    public void removeAvailability(Day day, TimeOfDay timeOfDay) {
        availabilities.computeIfAbsent(day, k -> new HashSet<>()).add(timeOfDay);
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

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
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

    public Map<Day, Set<TimeOfDay>> getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(Map<Day, Set<TimeOfDay>> availabilities) {
        this.availabilities = availabilities;
    }

    public Map<Course, Integer> getCourseFrequencies() {
        return courseFrequencies;
    }

    public void setCourseFrequencies(Map<Course, Integer> courseFrequencies) {
        this.courseFrequencies = courseFrequencies;
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
                .toString();
    }

    @Override
    public int compareTo(Instructor o) {
        return this.getId().compareTo(o.getId());
    }

    public String getFullName() {
        return getFirstName() +
                " " +
                getLastName();
    }
}