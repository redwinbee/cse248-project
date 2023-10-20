package me.valacritty.utils.parsers;

import me.valacritty.models.Course;
import me.valacritty.models.Instructor;
import me.valacritty.models.enums.Campus;
import me.valacritty.models.enums.Rank;
import me.valacritty.models.enums.TimeOfDay;
import me.valacritty.utils.helpers.CourseHelper;
import org.apache.commons.csv.CSVRecord;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InstructorParser extends AbstractParser<Instructor> {
    private static final String DELIMITER = "===";
    private static InstructorParser instance;

    private InstructorParser(String filePath) {
        super(filePath);
    }

    public static InstructorParser getInstance(String filePath) {
        if (instance == null) {
            instance = new InstructorParser(filePath);
        }
        return instance;
    }

    private static List<List<CSVRecord>> splitData(List<CSVRecord> records) {
        List<List<CSVRecord>> allData = new LinkedList<>();
        List<CSVRecord> current = new LinkedList<>();

        for (CSVRecord record : records) {
            if (record.get(0).equals(DELIMITER)) {
                if (!current.isEmpty()) {
                    allData.add(new LinkedList<>(current));
                    current.clear();
                }
            } else {
                current.add(record);
            }
        }

        return allData;
    }

    @Override
    protected Set<Instructor> createData(List<CSVRecord> records) {
        List<List<String>> flattened = flattenRecords(records);
        Set<Instructor> instructors = new TreeSet<>();
        for (List<String> ins : flattened) {
            Instructor out = createInstructorFromData(ins);

            instructors.add(out);
        }

        return instructors;
    }

    private List<List<String>> flattenRecords(List<CSVRecord> records) {
        List<List<CSVRecord>> splitRecords = splitData(records);
        List<List<String>> combined = new LinkedList<>();
        for (List<CSVRecord> nestedRecords : splitRecords) {
            List<String> flattened = nestedRecords.stream()
                    .map(CSVRecord::values)
                    .flatMap(Stream::of)
                    .toList();

            combined.add(flattened);
        }

        return combined;
    }

    private Instructor createInstructorFromData(List<String> ins) {
        Instructor out = new Instructor();
        List<String> fullName = parseFullName(ins.get(1));

        out.setId(ins.get(0));
        out.setFirstName(fullName.get(1));
        try {
            out.setMiddleName(fullName.get(2));
        } catch (IndexOutOfBoundsException ex) {
            // no middle name
        }
        out.setLastName(fullName.get(0));
        out.setHomeCampus(parseCampus(ins.get(15)));
        out.setHomePhone(ins.get(2));
        out.setWorkPhone(ins.get(30));
        out.setAddress(ins.get(16) + " ," + ins.get(31));
        out.setDateHired(parseDateHired(ins.get(17)));
        out.setCourses(parseCourses(String.join(" ", ins.subList(32, 53))));
        out.setRank(Rank.valueOf(ins.get(3)));
        out.setCanTeachOnline(parseCanTeachOnline(ins.get(4)));
        out.setPreferredCampuses(parsePreferredCampuses(ins.get(5)));
        out.setCanTeachSecondCourse(getBooleanFromString(ins.get(6)));
        out.setCanTeachThirdCourse(getBooleanFromString(ins.get(21)));
        out.addAvailability(parseDays(ins.get(8)), TimeOfDay.EARLY_MORNING);
        out.addAvailability(parseDays(ins.get(23)), TimeOfDay.MORNING);
        out.addAvailability(parseDays(ins.get(9)), TimeOfDay.EARLY_AFTERNOON);
        out.addAvailability(parseDays(ins.get(24)), TimeOfDay.AFTERNOON);
        out.addAvailability(parseDays(ins.get(11)), TimeOfDay.EVENING);
        if (!ins.get(25).isBlank()) {
            out.addAvailability(parseDays(ins.get(25)), TimeOfDay.MORNING);
        }

        return out;
    }

    private EnumSet<Campus> parsePreferredCampuses(String parsePreferredCampusesStr) {
        return Arrays.stream(parsePreferredCampusesStr.split("\\s+"))
                .map(this::parseCampus)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(Campus.class)));
    }

    private boolean parseCanTeachOnline(String canTeachOnlineStr) {
        return getBooleanFromString(canTeachOnlineStr);
    }

    private Set<Course> parseCourses(String coursesStr) {
        return Arrays.stream(coursesStr.split("\\s+"))
                .map(CourseHelper::allCoursesMatching)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private LocalDate parseDateHired(String dateHiredStr) {
        String[] patterns = new String[]{"MM/dd/yyyy", "M/d/yyyy"};

        for (String pattern : patterns) {
            try {
                return LocalDate.parse(dateHiredStr, DateTimeFormatter.ofPattern(pattern));
            } catch (DateTimeException ignored) {
            }
        }

        return null;
    }

    private boolean getBooleanFromString(String boolString) {
        return boolString.startsWith("Y");
    }
}