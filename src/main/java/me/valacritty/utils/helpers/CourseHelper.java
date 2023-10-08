package me.valacritty.utils.helpers;

import me.valacritty.Main;
import me.valacritty.models.Course;
import me.valacritty.models.Section;

import java.util.Optional;

public class CourseHelper {
    private CourseHelper() {

    }

    public static Optional<Course> findCourse(String courseNumber) {
        try {
            return Main.getSections().stream()
                    .map(Section::getCourse)
                    .filter(course -> course.getCourseNumber().equalsIgnoreCase(courseNumber))
                    .findFirst();
        } catch (NullPointerException ignored) {}

        return Optional.empty();
    }
}
