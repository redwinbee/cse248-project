package me.valacritty;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import me.valacritty.models.Campus;
import me.valacritty.models.Day;
import me.valacritty.models.Instructor;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class InstructorDeserializer extends StdDeserializer<Instructor> {
    protected InstructorDeserializer() {
        super(Instructor.class);
    }

    @Override
    public Instructor deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        Instructor instructor = new Instructor();

        // straightforward plug and play data
        String id = node.get("ID No").asText();
        String name = node.get("Name").asText();
        String workPhone = node.get("Bus Phone").asText();
        String homePhone = node.get("Home Phone").asText();
        String address = node.get("Address").asText();
        String rank = node.get("Rank").asText();

        instructor.setId(id);
        instructor.setFullName(name);
        instructor.setWorkPhone(workPhone);
        instructor.setHomePhone(homePhone);
        instructor.setAddress(address);
        instructor.setRank(rank);

        // data that needs extra parsing
        Campus homeCampus = parseHomeCampus(node);
        LocalDate hiredDate = parseHiredDate(node);
        boolean canTeachOnline = parseCanTeachOnline(node);
        HashSet<String> courses = parseCourses(node);
        HashSet<Campus> campuses = parseCampuses(node);
        boolean[] secondThirdCourse = parseSecondThirdCourse(node);

        ArrayList<Day> earlyMorningAvail = new ArrayList<>();
        ArrayList<Day> morningAvail = new ArrayList<>();
        parseMorningAvailability(node, earlyMorningAvail, morningAvail);

        ArrayList<Day> earlyEveAvail = new ArrayList<>();
        ArrayList<Day> eveAvail = new ArrayList<>();
        parseEveningAvailability(node, earlyEveAvail, eveAvail);

        ArrayList<Day> weekendAvail = new ArrayList<>(2);
        parseWeekendAvail(node, weekendAvail);

        instructor.setHomeCampus(homeCampus);
        instructor.setDateHired(hiredDate);
        instructor.setCanTeachOnline(canTeachOnline);
        instructor.setCourses(courses);
        instructor.setCampuses(campuses);
        instructor.setCanTeachSecondCourse(secondThirdCourse[0]);
        instructor.setCanTeachThirdCourse(secondThirdCourse[1]);
        instructor.setAvailableMornings(morningAvail);
        instructor.setAvailableEarlyMornings(earlyMorningAvail);
        instructor.setAvailableEvenings(eveAvail);
        instructor.setAvailableEarlyEvenings(earlyEveAvail);
        instructor.setAvailableWeekends(weekendAvail);

        return instructor;
    }

    private void organizeAvailability(ArrayList<Day> earlyAvail, ArrayList<Day> avail, String[] days) {
        boolean seenTuesday = false;
        for (String day : days) {
            switch (day) {
                case "M" -> avail.add(Day.MONDAY);
                case "*M" -> earlyAvail.add(Day.MONDAY);
                case "T", "*T" -> {
                    if (!seenTuesday) {
                        if (day.equalsIgnoreCase("T")) {
                            avail.add(Day.TUESDAY);
                        } else {
                            earlyAvail.add(Day.TUESDAY);
                        }
                        seenTuesday = true;
                    } else {
                        if (day.equalsIgnoreCase("T")) {
                            avail.add(Day.THURSDAY);
                        } else {
                            earlyAvail.add(Day.THURSDAY);
                        }
                    }
                }
                case "W" -> avail.add(Day.WEDNESDAY);
                case "*W" -> earlyAvail.add(Day.WEDNESDAY);
            }
        }
    }

    private void parseWeekendAvail(JsonNode node, ArrayList<Day> weekendAvail) {
        String[] days = node.get("Sat Sun").asText().split("\\s+");
        for (String day : days) {
            switch (day) {
                case "Sat" -> weekendAvail.add(Day.SATURDAY);
                case "Sun" -> weekendAvail.add(Day.SUNDAY);
            }
        }
    }

    private void parseEveningAvailability(JsonNode node, ArrayList<Day> earlyEveAvail, ArrayList<Day> eveAvail) {
        String[] days = getRawDaysFromString(node.get("*=3-4PM Days PM MTWTF").asText());
        organizeAvailability(earlyEveAvail, eveAvail, days);
    }

    private void parseMorningAvailability(JsonNode node, ArrayList<Day> earlyMorningAvail, ArrayList<Day> morningAvail) {
        String[] days = getRawDaysFromString(node.get("*=7-8AM Days AM MTWTF").asText());
        organizeAvailability(earlyMorningAvail, morningAvail, days);
    }

    private boolean[] parseSecondThirdCourse(JsonNode node) {
        // not a big fan of my solution but it works
        boolean[] ret = new boolean[2];
        String[] rawBools = node.get("2nd 3rd Crse").asText().split("\\s+");
        for (int i = 0; i < ret.length; i++) {
            ret[i] = getBoolFromString(rawBools[i]);
        }

        return ret;
    }

    private HashSet<Campus> parseCampuses(JsonNode node) {
        String[] rawCampuses = node.get("Campus 1 2 3 4").asText().split("\\s+");
        HashSet<Campus> ret = new HashSet<>(rawCampuses.length);
        for (String camp : rawCampuses) {
            ret.add(getCampusFromString(camp));
        }

        return ret;
    }

    private Campus parseHomeCampus(JsonNode node) {
        String rawCampus = node.get("Home Camp").asText();
        return getCampusFromString(rawCampus);
    }

    private HashSet<String> parseCourses(JsonNode node) {
        String rawCourses = node.get("Courses").asText();
        return Arrays.stream(rawCourses.split("\\s+")).collect(Collectors.toCollection(HashSet::new));
    }


    private boolean parseCanTeachOnline(JsonNode node) {
        String rawCanOnline = node.get("ONL").asText();
        return getBoolFromString(rawCanOnline);
    }

    private LocalDate parseHiredDate(JsonNode node) {
        String rawTime = node.get("College Date").asText();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return LocalDate.parse(rawTime, formatter);
    }

    private String[] getRawDaysFromString(String value) {
        ArrayList<String> matches = new ArrayList<>();
        Pattern pattern = Pattern.compile("(\\*?[A-Z])");       // credit: ChatGPT for the regex
        Matcher matcher = pattern.matcher(value);
        while (matcher.find()) {
            matches.add(matcher.group(1));
        }

        return matches.toArray(new String[0]);
    }

    private Campus getCampusFromString(String value) {
        switch (value.charAt(0)) {
            case 'A' -> {
                return Campus.AMERMAN;
            }
            case 'E' -> {
                return Campus.EAST;
            }
            case 'W' -> {
                return Campus.GRANT;
            }
            case 'O' -> {
                return Campus.ONLINE;
            }
            default -> {
                return null;
            }
        }
    }

    private boolean getBoolFromString(String boolStr) {
        return boolStr.startsWith("Y");
    }
}
