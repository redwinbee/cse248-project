package me.valacritty.models.enums;

import me.valacritty.models.TimeRange;

import java.time.LocalTime;

public enum TimeOfDay {
    EARLY_MORNING(new TimeRange(LocalTime.of(7, 0), LocalTime.of(8, 0))),
    MORNING(new TimeRange(LocalTime.of(8, 0), LocalTime.of(12, 0))),
    EARLY_AFTERNOON(new TimeRange(LocalTime.of(12, 0), LocalTime.of(15, 0))),
    AFTERNOON(new TimeRange(LocalTime.of(15, 0), LocalTime.of(16, 0))),
    EVENING(new TimeRange(LocalTime.of(16, 0), LocalTime.of(21, 0))),
    IRRELEVANT(null); // TODO I don't like this enum but it works for now I guess

    private final TimeRange range;

    TimeOfDay(TimeRange range) {
        this.range = range;
    }
}
