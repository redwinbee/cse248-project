package me.valacritty.persistence;

import me.valacritty.models.Instructor;
import me.valacritty.models.Section;
import me.valacritty.utils.generators.InstructorGenerator;
import me.valacritty.utils.parsers.InstructorParser;
import me.valacritty.utils.parsers.InstructorRecentCoursesParser;
import me.valacritty.utils.parsers.SectionParser;

import java.io.File;
import java.util.Set;

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

        if (!INSTRUCTOR_FILE.exists()) {
            instructorManager = new Manager<>(InstructorParser.getInstance("Instructors.csv").parse(false));
            Set<Instructor> recents = InstructorRecentCoursesParser.getInstance("Instructor_Recent_Courses.csv").parse(true);
            for (Instructor recent : recents) {
                instructorManager
                        .findBy(instructor -> instructor.getId().equalsIgnoreCase(recent.getId()))
                        .ifPresent(instructor -> {
                            instructor.setCourses(recent.getCourses());
                            instructor.setCourseFrequencies(recent.getCourseFrequencies());
                            System.out.printf("[configuration::debug]: re-assigning courses for %s%n", instructor.getFullName());
                        });
            }
            Storage.backup(instructorManager, INSTRUCTOR_FILE);
        } else {
            instructorManager = Storage.restore(INSTRUCTOR_FILE);
            instructorsExisted = true;
        }

        if (!SECTION_FILE.exists()) {
            sectionManager = new Manager<>(SectionParser.getInstance("CourseInformation.csv").parse(true));
            Storage.backup(sectionManager, SECTION_FILE);
        } else {
            sectionManager = Storage.restore(SECTION_FILE);
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
        Storage.backup(instructorManager, INSTRUCTOR_FILE);
        Storage.backup(sectionManager, SECTION_FILE);
    }

    public static Manager<Instructor> getInstructorManager() {
        if (instructorManager == null) {
            instructorManager = new Manager<>(InstructorParser.getInstance("Instructors.csv").parse(false));
        }
        return instructorManager;
    }

    public static Manager<Section> getSectionManager() {
        if (sectionManager == null) {
            sectionManager = new Manager<>(SectionParser.getInstance("CourseInformation.csv").parse(true));
        }
        return sectionManager;
    }
}
