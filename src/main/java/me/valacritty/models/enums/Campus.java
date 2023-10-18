package me.valacritty.models.enums;

import java.util.List;

public enum Campus {
    AMERMAN,
    GRANT,
    EAST,
    ONLINE;

    private static final List<Campus> VALUES = List.of(Campus.values());
    private static final int SIZE = VALUES.size();

    public static Campus randomCampus() {
        return VALUES.get((int) (Math.random() * SIZE));
    }

    public static double valuesLength() {
        return SIZE;
    }
}
