import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Task 1 - Student Grade Tracker
 * A console based application to input and manage student grades,
 * calculate average / highest / lowest scores and display a summary report.
 */
public class StudentGradeTracker {

    private static final Scanner sc = new Scanner(System.in);
    private static final GradeTracker tracker = new GradeTracker("data/grades.csv");
    private static final List<String> DEFAULT_SUBJECTS =
            Arrays.asList("Maths", "Physics", "Chemistry", "English", "Computer");

    public static void main(String[] args) {
        loadData();
        System.out.println("=================================================");
        System.out.println("        STUDENT GRADE TRACKER  (CodeAlpha)       ");
        System.out.println("=================================================");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt(" Enter your choice: ");
            switch (choice) {
                case 1: addStudent();      break;
                case 2: addOrUpdateScore();break;
                case 3: viewSummary();     break;
                case 4: searchStudent();   break;
                case 5: removeStudent();   break;
                case 6: saveData();        break;
                case 7:
                    saveData();
                    running = false;
                    System.out.println("\n Thank you for using Student Grade Tracker. Goodbye!");
                    break;
                default:
                    System.out.println(" Invalid choice. Please select 1-7.");
            }
        }
        sc.close();
    }

    private static void printMenu() {
        System.out.println("\n--------------------- MENU ----------------------");
        System.out.println(" 1. Add Student");
        System.out.println(" 2. Enter / Update Scores");
        System.out.println(" 3. Display Summary Report");
        System.out.println(" 4. Search Student Report");
        System.out.println(" 5. Remove Student");
        System.out.println(" 6. Save Data");
        System.out.println(" 7. Save & Exit");
        System.out.println("------------------------------------------------");
    }

    // ---------------------------------------------------------------
    //  Menu actions
    // ---------------------------------------------------------------
    private static void addStudent() {
        System.out.println("\n--- Add Student ---");
        String roll = readLine(" Enter Roll No : ");
        if (roll.isEmpty()) {
            System.out.println(" Roll No cannot be empty.");
            return;
        }
        if (tracker.findByRollNo(roll) != null) {
            System.out.println(" A student with this Roll No already exists.");
            return;
        }
        String name = readLine(" Enter Name    : ");
        if (name.isEmpty()) {
            System.out.println(" Name cannot be empty.");
            return;
        }
        Student s = new Student(roll, name);

        System.out.print(" Do you want to enter subject scores now? (y/n): ");
        if (readLine("").equalsIgnoreCase("y")) {
            for (String subject : DEFAULT_SUBJECTS) {
                double mark = readScore("  " + subject + " (0-100): ");
                if (mark >= 0) s.addScore(subject, mark);
            }
        }
        tracker.addStudent(s);
        System.out.println(" Student added successfully.");
    }

    private static void addOrUpdateScore() {
        System.out.println("\n--- Enter / Update Scores ---");
        if (ensureData()) return;
        String roll = readLine(" Enter Roll No : ");
        Student s = tracker.findByRollNo(roll);
        if (s == null) {
            System.out.println(" Student not found.");
            return;
        }
        String subject = readLine(" Enter Subject : ");
        if (subject.isEmpty()) {
            System.out.println(" Subject cannot be empty.");
            return;
        }
        double mark = readScore(" Enter Score (0-100): ");
        if (mark < 0) return;
        s.addScore(subject, mark);
        System.out.println(" Score saved for " + s.getName() + " in " + subject + ".");
    }

    private static void viewSummary() {
        System.out.println(tracker.buildSummaryReport());
    }

    private static void searchStudent() {
        System.out.println("\n--- Search Student ---");
        if (ensureData()) return;
        String roll = readLine(" Enter Roll No : ");
        Student s = tracker.findByRollNo(roll);
        if (s == null) {
            System.out.println(" Student not found.");
        } else {
            System.out.println(tracker.buildStudentReport(s));
        }
    }

    private static void removeStudent() {
        System.out.println("\n--- Remove Student ---");
        if (ensureData()) return;
        String roll = readLine(" Enter Roll No : ");
        if (tracker.removeStudent(roll)) {
            System.out.println(" Student removed.");
        } else {
            System.out.println(" Student not found.");
        }
    }

    // ---------------------------------------------------------------
    //  Helpers
    // ---------------------------------------------------------------
    private static boolean ensureData() {
        if (tracker.isEmpty()) {
            System.out.println(" No student records available. Please add students first.");
            return true;
        }
        return false;
    }

    private static String readLine(String prompt) {
        if (!prompt.isEmpty()) System.out.print(prompt);
        return sc.hasNextLine() ? sc.nextLine().trim() : "";
    }

    private static int readInt(String prompt) {
        while (true) {
            String v = readLine(prompt);
            try {
                return Integer.parseInt(v);
            } catch (NumberFormatException e) {
                System.out.print(" Please enter a valid number: ");
            }
        }
    }

    /** Reads a score in range 0-100, returns -1 on invalid input. */
    private static double readScore(String prompt) {
        String v = readLine(prompt);
        try {
            double d = Double.parseDouble(v);
            if (d < 0 || d > 100) {
                System.out.println(" Score must be between 0 and 100.");
                return -1;
            }
            return d;
        } catch (NumberFormatException e) {
            System.out.println(" Invalid score, skipped.");
            return -1;
        }
    }

    private static void loadData() {
        try {
            tracker.load();
            if (!tracker.isEmpty()) {
                System.out.println(" Loaded " + tracker.getStudents().size()
                        + " student record(s) from data/grades.csv");
            }
        } catch (IOException e) {
            System.out.println(" Could not load saved data: " + e.getMessage());
        }
    }

    private static void saveData() {
        try {
            tracker.save();
            System.out.println(" Data saved to data/grades.csv");
        } catch (IOException e) {
            System.out.println(" Could not save data: " + e.getMessage());
        }
    }
}
