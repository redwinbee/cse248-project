package me.valacritty.utils.helpers;

import me.valacritty.Main;
import me.valacritty.models.Course;

import java.util.Optional;

public class CourseHelper {
    private CourseHelper() {

    }

    public static Optional<Course> findCourse(String courseNumber) {
        return Main.getCourses().stream()
                .filter(course -> course.getCourseNumber().equalsIgnoreCase(courseNumber))
                .findFirst();
    }
}
