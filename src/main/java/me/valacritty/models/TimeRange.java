package me.valacritty.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class TimeRange implements Serializable {
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    /**
     * This constructor will use the LocalTime for the time portion of the internal date/time
     * which would normally fail to be created, but we instead use the UNIX Epoch as the date part
     * to allow a LocalDateTime to be created. This method should only be used when the time
     * part is the only relevant part.
     *
     * @param startTime The start time
     * @param endTime   The end time
     */
    public TimeRange(LocalTime startTime, LocalTime endTime) {
        // we don't care about the date when we use this constructor, just use the unix epoch
        this.startTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC).with(startTime);
        this.endTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC).with(endTime);
    }

    public TimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public boolean isInRange(LocalTime otherTime) {
        return otherTime.isAfter(startTime.toLocalTime()) && otherTime.isBefore(endTime.toLocalTime());
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    private String formatTime(LocalTime time) {
        int minute = time.getMinute();

        if (minute != 0) {
            return time.format(DateTimeFormatter.ofPattern("h:mma"));
        } else {
            return time.format(DateTimeFormatter.ofPattern("ha"));
        }
    }

    @Override
    public String toString() {
        String startTimeStr = formatTime(LocalTime.from(startTime));
        String endTimeStr = formatTime(LocalTime.from(endTime));

        return startTimeStr + "-" + endTimeStr;
    }

    public boolean isInRange(TimeRange otherRange) {
        LocalTime otherStartTime = otherRange.getStartTime().toLocalTime();
        LocalTime otherEndTime = otherRange.getEndTime().toLocalTime();
        return isInRange(otherEndTime) && isInRange(otherEndTime);
    }
}
