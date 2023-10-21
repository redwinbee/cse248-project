package me.valacritty.utils.parsers;

import me.valacritty.models.Course;
import me.valacritty.models.Instructor;
import me.valacritty.utils.helpers.CourseHelper;
import org.apache.commons.csv.CSVRecord;

import java.util.*;
import java.util.stream.Collectors;

public class InstructorRecentCoursesParser extends AbstractParser<Instructor> {
    private static final List<String> NON_COURSES_COLS = new LinkedList<>(List.of("First Name", "Last Name", "Frequencies"));
    private static InstructorRecentCoursesParser instance;

    private InstructorRecentCoursesParser(String filePath) {
        super(filePath);
    }

    public static InstructorRecentCoursesParser getInstance(String filePath) {
        if (instance == null) {
            instance = new InstructorRecentCoursesParser(filePath);
        }
        return instance;
    }

    protected Set<Instructor> createData(List<CSVRecord> records) {
        Set<Instructor> instructors = new TreeSet<>();
        for (CSVRecord record : records) {
            Instructor instructor = new Instructor(record.get("First Name"), record.get("Last Name"));
            instructor.setId(record.get("ID"));
            instructor.setPreviousCourses(parsePreviouslyTaughtCourses(record));
            instructor.setCourseFrequencies(parseCourseFrequencies(record.get("Frequencies")));
            instructors.add(instructor);
        }

        return instructors;
    }

    private Map<Course, Integer> parseCourseFrequencies(String frequencies) {
        Map<Course, Integer> freqs = new TreeMap<>();
        Arrays.stream(frequencies.split(","))
                .forEach(course -> {
                    // split it again to get the parts such as: MAT001: 4 -> [MAT001, 4]
                    String[] freqSplit = course.split(":");
                    freqs.put(CourseHelper.getCourseFromNumber(freqSplit[0]), Integer.parseInt(freqSplit[1].trim()));
                });
        return freqs;
    }

    private Set<Course> parsePreviouslyTaughtCourses(CSVRecord record) {
        // pack all the courses this instructor has taught before into a master list
        Set<String> selectedCols = new TreeSet<>();
        for (String header : record.toMap().keySet()) {
            if (!NON_COURSES_COLS.contains(header)) {
                Arrays.stream(record.get(header).split(","))
                        .map(String::trim)
                        .filter(string -> !string.isBlank())
                        .forEach(selectedCols::add);
            }
        }

        return selectedCols.stream()
                .map(CourseHelper::allCoursesMatching)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }
}
