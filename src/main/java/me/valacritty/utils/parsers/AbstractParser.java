package me.valacritty.utils.parsers;

import me.valacritty.models.enums.Campus;
import me.valacritty.models.enums.Day;

import java.util.*;
import java.util.stream.Stream;

public abstract class AbstractParser<E extends Comparable<E>> {
    protected abstract Collection<E> parse();

    protected List<String> parseFullName(String fullNameStr) {
        return Arrays.stream(fullNameStr.replaceAll("\\.", "")
                        .split(","))
                .map(String::trim)
                .flatMap(part -> Stream.of(part.split(" ")))
                .toList();
    }

    protected Campus parseCampus(String campusStr) {
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

    protected Set<Day> parseDays(String charStr) {
        EnumSet<Day> out = EnumSet.noneOf(Day.class);
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

    protected String removeAsterisks(String asteriskedString) {
        return asteriskedString.chars()
                .filter(Character::isAlphabetic)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
