package me.valacritty.persistence;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

public class Manager<T extends Comparable<T>> implements Serializable {
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
}
