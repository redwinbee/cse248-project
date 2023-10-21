package me.valacritty.persistence;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public Set<T> matches(Predicate<T> predicate) {
        return managed.stream()
                .filter(predicate)
                .collect(Collectors.toSet());
    }

    @SuppressWarnings("unchecked")  // T isn't going to change
    public T random() {
        int skip = (int) (Math.random() * managed.size());
        return (T) managed.toArray()[skip];
    }

    public Stream<T> stream() {
        return managed.stream();
    }

    public Optional<T> findBy(Predicate<? super T> predicate) {
        return managed.stream()
                .filter(predicate)
                .findFirst();
    }
}
