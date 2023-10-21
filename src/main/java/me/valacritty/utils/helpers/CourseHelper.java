package me.valacritty.utils.helpers;

import me.valacritty.models.Course;
import me.valacritty.models.Section;
import me.valacritty.persistence.Configuration;

import java.util.Set;
import java.util.stream.Collectors;

public class CourseHelper {
    private CourseHelper() {

    }

    public static Set<Course> allCoursesMatching(String courseNumber) {
        return Configuration.getSectionManager().stream()
                .map(Section::getCourse)
                .filter(course -> course.getCourseNumber().equalsIgnoreCase(courseNumber))
                .collect(Collectors.toSet());
    }

    public static Course getCourseFromNumber(String courseNumber) {
        String cleansed = cleanString(courseNumber.trim());
        return Configuration.getSectionManager().stream()
                .map(Section::getCourse)
                .filter(course -> {
                    String num = course.getCourseNumber().trim();
                    boolean test = num.equalsIgnoreCase(cleansed);
                    return test;
                })
                .findAny()
                .orElse(new Course("MAT000"));
    }

    private static String cleanString(String dirtyString) {
        return dirtyString.endsWith("L") ? dirtyString.substring(0, dirtyString.length() - 1) : dirtyString;
    }
}