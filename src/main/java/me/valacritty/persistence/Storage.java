package me.valacritty.persistence;

import java.io.*;

public class Storage {
    private Storage() {

    }

    public static <T extends Comparable<T>> void backup(Manager<T> manager, File outFile) {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outFile));
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            System.out.printf("[storage]: writing %d objects to disk...%n", manager.size());
            oos.writeObject(manager);
        } catch (IOException ex) {
            System.err.printf("[storage]: failed to backup data, possible cause: %s%n", ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> Manager<T> restore(File inFile) {
        Manager<T> out = new Manager<>();
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inFile))) {
            ObjectInputStream ois = new ObjectInputStream(bis);
            out = (Manager<T>) ois.readObject();
            System.out.printf("[storage]: read %d objects from disk...%n", out.size());
        } catch (IOException | ClassNotFoundException ex) {
            System.err.printf("[storage]: failed to restore data! possible cause: %s%n", ex.getMessage());
        }

        return out;
    }
}
