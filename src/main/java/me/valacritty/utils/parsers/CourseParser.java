package me.valacritty.utils.parsers;

import me.valacritty.models.Course;
import me.valacritty.models.TimeRange;
import me.valacritty.models.enums.Day;
import me.valacritty.models.enums.InstructionMethod;
import me.valacritty.models.enums.PartOfTerm;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CourseParser extends AbstractParser<Course> {
    private static final String PATH = "CourseInformation.csv";
    private static CourseParser instance;

    private CourseParser() {
    }

    public static CourseParser getInstance() {
        if (instance == null) {
            instance = new CourseParser();
        }
        return instance;
    }

    private static List<String> splitData(List<String> data) {
        List<String> out = new LinkedList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                out.add(line);
            }
        } catch (IOException ex) {
            System.err.println("An error occurred while reading the CSV data: " + ex.getMessage());
        }
        return out;
    }

    private List<String> getCsvData() {
        try (FileReader reader = new FileReader(PATH)) {
            try (CSVParser csvParser = CSVParser.parse(reader, CSVFormat.DEFAULT)) {
                List<String> csvData = new LinkedList<>();
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

    public TreeSet<Course> parse() {
        List<String> data = getCsvData();
        TreeSet<Course> courses = new TreeSet<>();
        for (String course : data.stream().skip(1).toList()) {  // skip the header
            courses.add(createCourseFromData(course));
        }

        return courses;
    }

    private Course createCourseFromData(String course) {
        String[] entries = course.split(",");
        Course out = new Course();
        out.setCourseNumber(entries[1] + entries[2]);
        out.setTitle(entries[3].replaceAll("\"", ""));
        out.setCrn(Integer.parseInt(entries[4]));
        out.setPartOfTerm(parsePartOfTerm(entries[6]));
        out.setCampus(parseCampus(entries[7]));
        out.setInstructionMethod(parseInstructionMethod(entries[9]));
        if (!out.getTitle().startsWith("SUR")) { // TODO figure out why these courses are formatted differently
            out.setDaysOffered(parseDaysOffered(entries[18]));
            if (!entries[16].isBlank() && !entries[17].isBlank() && !entries[19].isBlank() && !entries[20].isBlank()) {
                String startDate = entries[16];
                String endDate = entries[17];
                String startTime = entries[19];
                String endTime = entries[20];
                LocalDateTime startDateTime = parseDateTime(startDate, startTime);
                LocalDateTime endDateTime = parseDateTime(endDate, endTime);
                out.setTimeRange(new TimeRange(startDateTime, endDateTime));
            }
        }
        return out;
    }

    private LocalDateTime parseDateTime(String date, String time) {
        String[] patterns = new String[] { "MM/dd/yyyy", "M/d/yyyy" };
        LocalTime parsedTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("h:mma"));
        for (String pattern : patterns) {
            try {
                LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
                return LocalDateTime.of(parsedDate, parsedTime);
            } catch (DateTimeException ex) {
                // try next formatter
            }
        }
        long unixEpochTime = System.currentTimeMillis() / 1000; // Convert to seconds
        return LocalDateTime.ofEpochSecond(unixEpochTime, 0, java.time.ZoneOffset.UTC);
    }

    private EnumSet<Day> parseDaysOffered(String entry) {
        EnumSet<Day> out = EnumSet.noneOf(Day.class);
        char[] values = entry.toCharArray();
        for (char value : values) {
            switch (value) {
                case 'U' -> out.add(Day.SUNDAY);
                case 'M' -> out.add(Day.MONDAY);
                case 'T' -> out.add(Day.TUESDAY);
                case 'W' -> out.add(Day.WEDNESDAY);
                case 'R' -> out.add(Day.THURSDAY);
                case 'S' -> out.add(Day.SATURDAY);
            }
        }

        return out;
    }

    private InstructionMethod parseInstructionMethod(String entry) {
        if (entry.equals("TR")) {
            return InstructionMethod.IN_PERSON;
        }
        return InstructionMethod.BLENDED;
    }

    private PartOfTerm parsePartOfTerm(String entry) {
        switch (entry) {
            case "FE" -> {
                return PartOfTerm.EVENING;
            }
            case "FD" -> {
                return PartOfTerm.DAY;
            }
            case "SS" -> {
                return PartOfTerm.SAT_OR_SUN;
            }
            case "LSD", "LS" -> {
                return PartOfTerm.LATE_START_DAY;
            }
            case "LSE" -> {
                return PartOfTerm.LATE_START_EVENING;
            }
            default -> {
                return PartOfTerm.ONLINE;
            }
        }
    }
}
