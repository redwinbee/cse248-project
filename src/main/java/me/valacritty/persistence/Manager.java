package me.valacritty.persistence;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Manager<T extends Comparable<T>> implements Serializable {
    private T type;
    private final Set<T> managed;

    public Manager() {
        this.managed = new TreeSet<>();
    }

    public Manager(Set<T> managedColl) {
        this.managed = managedColl;
    }

    public Set<T> all() {
        return managed;
    }

    public int size() {
        return managed.size();
    }

    public void addAll(Set<T> set) {
        managed.addAll(set);
    }

    public Set<T> matches(Predicate<T> predicate) {
        return managed.stream()
                .filter(predicate)
                .collect(Collectors.toSet());
    }
}
