package me.valacritty.models.enums;

import java.util.List;

public enum Rank {
    A1,
    A2,
    A3;

    private static final List<Rank> VALUES = List.of(Rank.values());
    private static final int SIZE = VALUES.size();

    public static Rank randomRank() {
        return VALUES.get((int) (Math.random() * SIZE));
    }
}
