package me.valacritty;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import me.valacritty.models.Campus;
import me.valacritty.models.Instructor;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
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

        instructor.setHomeCampus(homeCampus);
        instructor.setDateHired(hiredDate);
        instructor.setCanTeachOnline(canTeachOnline);
        instructor.setCourses(courses);

        return instructor;
    }

    private Campus parseHomeCampus(JsonNode node) {
        String rawCampus = node.get("Home Camp").asText();
        switch (rawCampus.charAt(0)) {
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

    private HashSet<String> parseCourses(JsonNode node) {
        String rawCourses = node.get("Courses").asText();
        return Arrays.stream(rawCourses.split("\\s+")).collect(Collectors.toCollection(HashSet::new));
    }


    private boolean parseCanTeachOnline(JsonNode node) {
        String rawCanOnline = node.get("ONL").asText();
        return rawCanOnline.equalsIgnoreCase("Y");
    }

    private LocalDate parseHiredDate(JsonNode node) {
        String rawTime = node.get("College Date").asText();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return LocalDate.parse(rawTime, formatter);
    }
}
