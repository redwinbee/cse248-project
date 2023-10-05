package me.valacritty.models;

import me.valacritty.models.enums.Campus;
import me.valacritty.models.enums.Day;
import me.valacritty.models.enums.InstructionMethod;
import me.valacritty.models.enums.PartOfTerm;

import java.util.EnumSet;
import java.util.StringJoiner;

public class Section implements Comparable<Section>, Cloneable {
    private Course course;
    private int crn;
    private PartOfTerm partOfTerm;
    private Campus campus;
    private InstructionMethod instructionMethod;
    private EnumSet<Day> daysOffered;
    private TimeRange timeRange;

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getCrn() {
        return crn;
    }

    public void setCrn(int crn) {
        this.crn = crn;
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

    public TimeRange getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(TimeRange timeRange) {
        this.timeRange = timeRange;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Section.class.getSimpleName() + "[", "]")
                .add("course=" + course)
                .add("crn=" + crn)
                .add("partOfTerm=" + partOfTerm)
                .add("campus=" + campus)
                .add("instructionMethod=" + instructionMethod)
                .add("daysOffered=" + daysOffered)
                .add("timeRange=" + timeRange)
                .toString();
    }

    @Override
    public int compareTo(Section o) {
        return Integer.compare(this.getCrn(), o.getCrn());
    }

    @Override
    public Section clone() {
        try {
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return (Section) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
