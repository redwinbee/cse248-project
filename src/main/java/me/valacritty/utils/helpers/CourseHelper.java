package me.valacritty.utils.helpers;

import me.valacritty.models.Course;
import me.valacritty.models.Section;
import me.valacritty.persistence.Configuration;

import java.util.Optional;

public class CourseHelper {
    private CourseHelper() {

    }

    public static Optional<Course> findCourse(String courseNumber) {
        try {
            return Configuration.getSectionManager().all().stream()
                    .map(Section::getCourse)
                    .filter(course -> course.getCourseNumber().equalsIgnoreCase(courseNumber))
                    .findFirst();
        } catch (NullPointerException ignored) {
        }

        return Optional.empty();
    }
}