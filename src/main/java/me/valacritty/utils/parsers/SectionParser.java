package me.valacritty.utils.parsers;

import me.valacritty.models.Course;
import me.valacritty.models.Section;
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

public class SectionParser extends AbstractParser<Section> {
    private static SectionParser instance;

    private SectionParser(String filePath) {
        super(filePath);
    }

    public static SectionParser getInstance(String filePath) {
        if (instance == null) {
            instance = new SectionParser(filePath);
        }
        return instance;
    }

    @Override
    protected Set<Section> createData(List<CSVRecord> records) {
        Set<Section> sections = new TreeSet<>();
        for (CSVRecord record : records) {
            Course course = new Course();
            Section section = new Section();
            course.setCourseNumber(record.get("Subj") + record.get("Crse"));
            course.setTitle(record.get("Crse_Title"));
            section.setCrn(Integer.parseInt(record.get("CRN")));
            section.setPartOfTerm(parsePartOfTerm(record.get("POT")));
            section.setCampus(parseCampus(record.get("Camp")));
            section.setInstructionMethod(parseInstructionMethod(record.get("Inst_Mthd")));
            if (!course.getTitle().startsWith("SUR")) {    // TODO figure out why these courses are formatted differently
                section.setDaysOffered(parseDaysOffered(record.get("Days")));
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
                    section.setTimeRange(new TimeRange(startDateTime, endDateTime));
                }
            }
            section.setCourse(course);
            sections.add(section);
        }

        return sections;
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
