package me.valacritty.utils.parsers;

import me.valacritty.models.Instructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class InstructorRecentCoursesParser extends AbstractParser<Instructor> {
    private static final String PATH = "Instructor_Recent_Courses.csv";
    private static final List<String> NON_COURSES_COLS = new LinkedList<>(List.of("First Name", "Last Name", "Frequencies"));
    private static InstructorRecentCoursesParser instance;

    private InstructorRecentCoursesParser() {
    }

    public static InstructorRecentCoursesParser getInstance() {
        if (instance == null) {
            instance = new InstructorRecentCoursesParser();
        }
        return instance;
    }

    @Override
    public Collection<Instructor> parse() {
        return createData(getCsvData());
    }

    private List<Instructor> createData(List<CSVRecord> records) {
        List<Instructor> instructors = new LinkedList<>();
        for (CSVRecord record : records) {
            Instructor instructor = new Instructor(record.get("First Name"), record.get("Last Name"));
            instructor.setPreviousCourses(parsePreviouslyTaughtCourses(record));
            instructors.add(instructor);
        }

        return instructors;
    }

    private Set<String> parsePreviouslyTaughtCourses(CSVRecord record) {
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

        return selectedCols;
    }

    private List<CSVRecord> getCsvData() {
        try (FileReader reader = new FileReader(PATH)) {
            try (CSVParser parser = CSVParser.parse(reader, CSVFormat.Builder.create().setHeader().build())) {
                return parser.getRecords();
            }
        } catch (IOException ex) {
            System.err.println("An error occurred while parsing the CSV file: " + ex.getMessage());
            return Collections.emptyList();
        }
    }
}
