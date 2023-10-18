package me.valacritty.persistence;

import me.valacritty.models.Instructor;
import me.valacritty.models.Section;
import me.valacritty.utils.generators.InstructorGenerator;
import me.valacritty.utils.parsers.InstructorParser;
import me.valacritty.utils.parsers.InstructorRecentCoursesParser;
import me.valacritty.utils.parsers.SectionParser;

import java.io.File;

public class Configuration {
    private static final File INSTRUCTOR_FILE = new File("bin/instructors.dat");
    private static final File SECTION_FILE = new File("bin/sections.dat");
    private static Manager<Instructor> instructorManager;
    private static Manager<Section> sectionManager;
    private static boolean instructorsExisted = false;

    private Configuration() {
    }

    public static void initializeManagers() {
        System.out.printf("[configuration]: initializing managers...%n");
        Storage<Instructor> instructorStorage = new Storage<>();
        Storage<Section> sectionStorage = new Storage<>();

        if (!INSTRUCTOR_FILE.exists()) {
            instructorManager = new Manager<>(InstructorParser.getInstance("Instructors.csv").parse());
            instructorManager.addAll(InstructorRecentCoursesParser.getInstance("Instructor_Recent_Courses.csv").parse());
            instructorStorage.backup(instructorManager, INSTRUCTOR_FILE);
        } else {
            instructorManager = instructorStorage.restore(INSTRUCTOR_FILE);
            instructorsExisted = true;
        }

        if (!SECTION_FILE.exists()) {
            sectionManager = new Manager<>(SectionParser.getInstance("CourseInformation.csv").parse());
            sectionStorage.backup(sectionManager, SECTION_FILE);
        } else {
            sectionManager = sectionStorage.restore(SECTION_FILE);
            instructorsExisted = true;
        }
    }

    public static void initializeGenerators(int numElements) {
        if (!instructorsExisted) {
            System.out.printf("[configuration]: initializing generators...%n");
            instructorManager.addAll(InstructorGenerator.generateFakes(numElements));
            saveAll();
        }
    }

    public static void saveAll() {
        System.out.printf("[configuration]: running a backup...%n");
        Storage<Instructor> instructorStorage = new Storage<>();
        Storage<Section> sectionStorage = new Storage<>();
        instructorStorage.backup(instructorManager, INSTRUCTOR_FILE);
        sectionStorage.backup(sectionManager, SECTION_FILE);
    }

    public static Manager<Instructor> getInstructorManager() {
        if (instructorManager == null) {
            instructorManager = new Manager<>(InstructorParser.getInstance("Instructors.csv").parse());
        }
        return instructorManager;
    }

    public static Manager<Section> getSectionManager() {
        if (sectionManager == null) {
            sectionManager = new Manager<>(SectionParser.getInstance("CourseInformation.csv").parse());
        }
        return sectionManager;
    }
}
