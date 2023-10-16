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
}