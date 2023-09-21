package me.valacritty.models;

import me.valacritty.models.enums.Campus;
import me.valacritty.models.enums.Day;
import me.valacritty.models.enums.InstructionMethod;
import me.valacritty.models.enums.PartOfTerm;

import java.time.LocalTime;
import java.util.EnumSet;

public class Course implements Comparable<Course> {
    private String courseNumber;
    private String title;
    private int crn;
    private byte credits;
    private PartOfTerm partOfTerm;
    private Campus campus;
    private InstructionMethod instructionMethod;
    private EnumSet<Day> daysOffered;
    private LocalTime beginTime;
    private LocalTime endTime;

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

    public int getCrn() {
        return crn;
    }

    public void setCrn(int crn) {
        this.crn = crn;
    }

    public byte getCredits() {
        return credits;
    }

    public void setCredits(byte credits) {
        this.credits = credits;
    }

    public PartOfTerm getPartOfTerm() {
        return partOfTerm;
    }

    public void setPartOfTerm(PartOfTerm partOfTerm) {
        this.partOfTerm = partOfTerm;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    public InstructionMethod getInstructionMethod() {
        return instructionMethod;
    }

    public void setInstructionMethod(InstructionMethod instructionMethod) {
        this.instructionMethod = instructionMethod;
    }

    public EnumSet<Day> getDaysOffered() {
        return daysOffered;
    }

    public void setDaysOffered(EnumSet<Day> daysOffered) {
        this.daysOffered = daysOffered;
    }

    public LocalTime getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(LocalTime beginTime) {
        this.beginTime = beginTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Course{" + "courseNumber='" + courseNumber + '\'' +
                ", title='" + title + '\'' +
                ", crn=" + crn +
                ", credits=" + credits +
                ", partOfTerm=" + partOfTerm +
                ", campus=" + campus +
                ", instructionMethod=" + instructionMethod +
                ", daysOffered=" + daysOffered +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                '}';
    }

    @Override
    public int compareTo(Course o) {
        return Integer.compare(this.getCrn(), o.getCrn());
    }
}
