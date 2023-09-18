package me.valacritty.utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import me.valacritty.models.Campus;
import me.valacritty.models.Day;
import me.valacritty.models.Instructor;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Parser {
    private static final String DELIMITER = "===";
    private static final String PATH = "Instructors.csv";
    private static Parser instance;

    private Parser() {
    }

    public static Parser getInstance() {
        if (instance == null) {
            instance = new Parser();
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
        try (CSVReader reader = new CSVReader(new FileReader(PATH))) {
            List<String> csvData = reader.readAll().stream().flatMap(Arrays::stream).toList();
            return splitData(csvData);
        } catch (IOException | CsvException ex) {
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

    private ArrayList<Day> parseDays(String charStr) {
        ArrayList<Day> out = new ArrayList<>(7);
        boolean sawTuesday = false;
        for (String day : charStr.chars().mapToObj(Character::toString).toList()) {
            switch (day) {
                case "M" -> out.add(Day.MONDAY);
                case "T" -> {
                    if (!sawTuesday) {
                        out.add(Day.TUESDAY);
                    } else {
                        out.add(Day.THURSDAY);
                    }
                    sawTuesday = true;
                }
                case "W" -> out.add(Day.WEDNESDAY);
                case "F" -> out.add(Day.FRIDAY);
                default -> {
                    // NOOP
                }
            }
        }

        return out;
    }

    private Campus parseCampus(String campusStr) {
        switch (campusStr.charAt(0)) {
            case 'A' -> {
                return Campus.AMERMAN;
            }
            case 'W', 'G' -> {
                return Campus.GRANT;
            }
            case 'E' -> {
                return Campus.EAST;
            }
            case 'O' -> {
                return Campus.ONLINE;
            }
            default -> {
                return null;
            }
        }
    }

    private ArrayList<Day> parseAvailableWeekends(String weekendsStr) {
        ArrayList<Day> out = new ArrayList<>(2);
        String[] split = weekendsStr.split("(?=(Sun))");
        for (String day : split) {
            switch (day) {
                case "Sat" -> out.add(Day.SATURDAY);
                case "Sun" -> out.add(Day.SUNDAY);
            }
        }

        return out;
    }

    private ArrayList<Day> parseAvailableWeekdays(String availabilityStr) {
        String cleaned = removeAsterisks(availabilityStr);
        return parseDays(cleaned);
    }

    private HashSet<Campus> parsePreferredCampuses(String parsePreferredCampusesStr) {
        return Arrays.stream(parsePreferredCampusesStr.split("\\s+"))
                .map(this::parseCampus)
                .collect(Collectors.toCollection(HashSet::new));
    }

    private boolean parseCanTeachOnline(String canTeachOnlineStr) {
        return getBooleanFromString(canTeachOnlineStr);
    }

    private HashSet<String> parseCourses(String coursesStr) {
        return Arrays.stream(coursesStr.split("\\s+")).collect(Collectors.toCollection(HashSet::new));
    }

    private LocalDate parseDateHired(String dateHiredStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return LocalDate.parse(dateHiredStr, formatter);
    }

    private String removeAsterisks(String asteriskedString) {
        return asteriskedString.chars()
                .filter(Character::isAlphabetic)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private boolean getBooleanFromString(String boolString) {
        return boolString.startsWith("Y");
    }
}