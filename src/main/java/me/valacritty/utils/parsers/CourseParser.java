package me.valacritty.utils.parsers;

import me.valacritty.models.Course;
import me.valacritty.models.TimeRange;
import me.valacritty.models.enums.Day;
import me.valacritty.models.enums.InstructionMethod;
import me.valacritty.models.enums.PartOfTerm;
import org.apache.commons.csv.CSVRecord;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class CourseParser extends AbstractParser<Course> {
    private static CourseParser instance;

    private CourseParser(String filePath) {
        super(filePath);
    }

    public static CourseParser getInstance(String filePath) {
        if (instance == null) {
            instance = new CourseParser(filePath);
        }
        return instance;
    }

    @Override
    protected Set<Course> createData(List<CSVRecord> records) {
        Set<Course> courses = new TreeSet<>();
        for (CSVRecord record : records) {
            Course out = new Course();
            out.setCourseNumber(record.get("Subj") + record.get("Crse"));
            out.setTitle(record.get("Crse_Title"));
            out.setCrn(Integer.parseInt(record.get("CRN")));
            out.setPartOfTerm(parsePartOfTerm(record.get("POT")));
            out.setCampus(parseCampus(record.get("Camp")));
            out.setInstructionMethod(parseInstructionMethod(record.get("Inst_Mthd")));
            if (!out.getTitle().startsWith("SUR")) {    // TODO figure out why these courses are formatted differently
                out.setDaysOffered(parseDaysOffered(record.get("Days")));
                if (!record.get("Start_Dt").isBlank()
                        && !record.get("End_Dt").isBlank()
                        && !record.get("Beg_Time").isBlank()
                        && !record.get("End_Time").isBlank()) {
                    String startDate = record.get("Start_Dt");
                    String endDate = record.get("End_Dt");
                    String startTime = record.get("Beg_Time");
                    String endTime = record.get("End_Time");
                    LocalDateTime startDateTime = parseDateTime(startDate, startTime);
                    LocalDateTime endDateTime = parseDateTime(endDate, endTime);
                    out.setTimeRange(new TimeRange(startDateTime, endDateTime));
                }
            }
            courses.add(out);
        }

        return courses;
    }

    private LocalDateTime parseDateTime(String date, String time) {
        String[] patterns = new String[]{"MM/dd/yyyy", "M/d/yyyy"};
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
