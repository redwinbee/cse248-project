package me.valacritty.utils.parsers;

import me.valacritty.models.Instructor;
import me.valacritty.models.enums.Campus;
import me.valacritty.models.enums.Day;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class InstructorParser extends AbstractParser<Instructor> {
    private static final String DELIMITER = "===";
    private static final String PATH = "Instructors.csv";
    private static InstructorParser instance;

    private InstructorParser() {
    }

    public static InstructorParser getInstance() {
        if (instance == null) {
            instance = new InstructorParser();
        }
        return instance;
    }

    private static List<List<String>> splitData(List<String> data) {
        List<List<String>> instructorsData = new LinkedList<>();
        List<String> currentInstructorData = new LinkedList<>();
        instructorsData.add(currentInstructorData);

        for (String str : data) {
            if (str.equals(DELIMITER)) {
                currentInstructorData = new LinkedList<>();
                instructorsData.add(currentInstructorData);
            } else {
                currentInstructorData.add(str);
            }
        }
        return instructorsData;
    }

    public TreeSet<Instructor> parse() {
        List<List<String>> data = getCsvData();
        TreeSet<Instructor> instructors = new TreeSet<>();
        for (List<String> ins : data) {
            if (ins.isEmpty())
                continue;
            instructors.add(createInstructorFromData(ins));
        }

        return instructors;
    }

    private List<List<String>> getCsvData() {
        try (FileReader reader = new FileReader(PATH)) {
            try (CSVParser csvParser = CSVParser.parse(reader, CSVFormat.DEFAULT)) {
                List<String> csvData = new ArrayList<>();
                for (CSVRecord record : csvParser) {
                    csvData.addAll(record.toList());
                }
                return splitData(csvData);
            }
        } catch (IOException ex) {
            System.err.println("An error occurred while reading the CSV data: " + ex.getMessage());
            return Collections.emptyList();
        }
    }

    private Instructor createInstructorFromData(List<String> ins) {
        Instructor out = new Instructor();
        out.setId(ins.get(0));
        out.setName(ins.get(1));
        out.setHomeCampus(parseCampus(ins.get(15)));
        out.setHomePhone(ins.get(2));
        out.setWorkPhone(ins.get(30));
        out.setAddress(ins.get(16) + " ," + ins.get(31));
        out.setDateHired(parseDateHired(ins.get(17)));
        out.setCourses(parseCourses(ins.get(32)));
        out.setRank(ins.get(3));
        out.setCanTeachOnline(parseCanTeachOnline(ins.get(4)));
        out.setPreferredCampuses(parsePreferredCampuses(ins.get(5)));
        out.setCanTeachSecondCourse(getBooleanFromString(ins.get(6)));
        out.setCanTeachThirdCourse(getBooleanFromString(ins.get(21)));
        out.setAvailableEarlyMornings(parseAvailableWeekdays(ins.get(8)));
        out.setAvailableMornings(parseAvailableWeekdays(ins.get(23)));
        out.setAvailableEarlyAfternoons(parseAvailableWeekdays(ins.get(9)));
        out.setAvailableAfternoons(parseAvailableWeekdays(ins.get(24)));
        out.setAvailableEvenings(parseAvailableWeekdays(ins.get(11)));
        out.setAvailableWeekends(parseAvailableWeekends(ins.get(10) + ins.get(25)));

        return out;
    }

    private EnumSet<Day> parseAvailableWeekends(String weekendsStr) {
        EnumSet<Day> out = EnumSet.noneOf(Day.class);
        String[] split = weekendsStr.split("(?=(Sun))");
        for (String day : split) {
            switch (day) {
                case "Sat" -> out.add(Day.SATURDAY);
                case "Sun" -> out.add(Day.SUNDAY);
            }
        }

        return out;
    }

    private Set<Day> parseAvailableWeekdays(String availabilityStr) {
        String cleaned = removeAsterisks(availabilityStr);
        return parseDays(cleaned);
    }

    private EnumSet<Campus> parsePreferredCampuses(String parsePreferredCampusesStr) {
        return Arrays.stream(parsePreferredCampusesStr.split("\\s+"))
                .map(this::parseCampus)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(Campus.class)));
    }

    private boolean parseCanTeachOnline(String canTeachOnlineStr) {
        return getBooleanFromString(canTeachOnlineStr);
    }

    private List<String> parseCourses(String coursesStr) {
        return Arrays.stream(coursesStr.split("\\s+")).toList();
    }

    private LocalDate parseDateHired(String dateHiredStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return LocalDate.parse(dateHiredStr, formatter);
    }

    private boolean getBooleanFromString(String boolString) {
        return boolString.startsWith("Y");
    }
}