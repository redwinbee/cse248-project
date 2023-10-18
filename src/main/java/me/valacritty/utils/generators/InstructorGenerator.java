package me.valacritty.utils.generators;

import me.valacritty.models.Course;
import me.valacritty.models.Instructor;
import me.valacritty.models.enums.Campus;
import me.valacritty.models.enums.Day;
import me.valacritty.models.enums.Rank;
import me.valacritty.models.enums.TimeOfDay;
import me.valacritty.persistence.Configuration;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.FileReader;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InstructorGenerator {
    private static final double RANDOM_FACTOR = 0.5;
    private static final Random RANDOM = new Random();
    private final static List<String> FIRST_NAMES;
    private final static List<String> MIDDLE_NAMES;
    private final static List<String> LAST_NAMES;

    static {
        FIRST_NAMES = parse("first_names.csv");
        MIDDLE_NAMES = parse("middle_names.csv");
        LAST_NAMES = parse("last_names.csv");
    }

    private InstructorGenerator() {

    }

    private static List<String> parse(String fileName) {
        try (FileReader reader = new FileReader("fake_data/" + fileName)) {
            try (CSVParser parser = CSVParser.parse(reader, CSVFormat.Builder.create().setHeader().build())) {
                return parser.getRecords()
                        .stream()
                        .map(record -> record.get(0))
                        .collect(Collectors.toList());
            }
        } catch (IOException ex) {
            System.err.println("An error occurred while parsing the CSV file: " + ex.getMessage());
            return Collections.emptyList();
        }
    }

    public static Set<Instructor> generateFakes(int numInstructors) {
        System.out.printf("[instructor_generator]: generating %d fakes...%n", numInstructors);
        Set<Instructor> out = HashSet.newHashSet(numInstructors);

        for (int i = 0; i < numInstructors; i++) {
            Instructor instructor = new Instructor();
            instructor.setAvailabilities(randomAvailabilities());
            instructor.setFirstName(randomFirstName());
            instructor.setMiddleName(Math.random() > RANDOM_FACTOR ? randomMiddleName() : "");
            instructor.setLastName(randomLastName());
            instructor.setHomeCampus(Campus.randomCampus());
            instructor.setHomePhone(randomHomePhone());
            instructor.setWorkPhone(randomWorkPhone());
            instructor.setAddress(randomAddress());
            instructor.setDateHired(randomDateHired());
            instructor.setCourses(randomCourses(4));
            instructor.setPreviousCourses(randomPreviousCourses(4));
            instructor.setRank(Rank.randomRank());
            instructor.setPreferredCampuses(randomPreferredCampuses());
            instructor.setCanTeachOnline(instructor.getPreferredCampuses().contains(Campus.ONLINE) || instructor.getHomeCampus() == Campus.ONLINE);
            instructor.setCanTeachSecondCourse(Math.random() > RANDOM_FACTOR);
            instructor.setCanTeachThirdCourse(Math.random() > RANDOM_FACTOR);
            out.add(instructor);
        }

        return out;
    }

    private static Map<Day, Set<TimeOfDay>> randomAvailabilities() {
        return new TreeMap<>();
    }

    private static EnumSet<Campus> randomPreferredCampuses() {
        EnumSet<Campus> out = EnumSet.noneOf(Campus.class);
        int max = (int) (Math.random() * Campus.valuesLength());
        for (int i = 0; i < max; i++) {
            out.add(Campus.randomCampus());
        }

        return out;
    }

    private static Set<Course> randomPreviousCourses(int numCourses) {
        return randomCourses(numCourses);   // TODO change this when I implement frequency parsing
    }

    private static Set<Course> randomCourses(int numCourses) {
        Objects.requireNonNull(Configuration.getSectionManager(), "section manager can't be null here");
        Set<Course> out = new HashSet<>(numCourses);
        for (int i = 0; i < numCourses; i++) {
            out.add(Configuration.getSectionManager().random().getCourse());
        }

        return out;
    }

    private static LocalDate randomDateHired() {
        AtomicLong randomEpoch = new AtomicLong(0L);
        long maxEpoch = Instant.now().getEpochSecond();

        RANDOM.longs(Instant.EPOCH.getEpochSecond(), maxEpoch)
                .findAny()
                .ifPresent(epochSeconds -> {
                    try {
                        randomEpoch.set(epochSeconds);
                    } catch (DateTimeException ex) {
                        System.err.printf("[err]: failed to parse date with epoch seconds %d!%n", epochSeconds);
                        System.err.printf("[err]: likely cause: %s%n", ex.getMessage());
                    }
                });

        return Instant.ofEpochSecond(randomEpoch.get()).atZone(ZoneOffset.UTC).toLocalDate();
    }

    private static String randomAddress() {
        return ""; //this doesn't matter
    }

    private static String randomWorkPhone() {
        return ""; // this doesn't matter
    }

    private static String randomHomePhone() {
        return ""; // this doesn't matter
    }

    private static String randomLastName() {
        int idx = (int) (Math.random() * LAST_NAMES.size());
        return LAST_NAMES.get(idx);
    }

    private static String randomMiddleName() {
        int idx = (int) (Math.random() * MIDDLE_NAMES.size());
        return MIDDLE_NAMES.get(idx);
    }

    private static String randomFirstName() {
        int idx = (int) (Math.random() * FIRST_NAMES.size());
        return FIRST_NAMES.get(idx);
    }
}
