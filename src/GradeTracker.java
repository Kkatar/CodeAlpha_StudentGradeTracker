import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Manages the collection of students: add, search, delete, report and
 * file persistence (CSV based storage).
 */
public class GradeTracker {

    private final List<Student> students = new ArrayList<>();
    private final Path dataFile;

    public GradeTracker(String dataFilePath) {
        this.dataFile = Paths.get(dataFilePath);
    }

    public List<Student> getStudents() {
        return students;
    }

    public boolean isEmpty() {
        return students.isEmpty();
    }

    public Student findByRollNo(String rollNo) {
        for (Student s : students) {
            if (s.getRollNo().equalsIgnoreCase(rollNo)) {
                return s;
            }
        }
        return null;
    }

    public boolean addStudent(Student student) {
        if (findByRollNo(student.getRollNo()) != null) {
            return false;
        }
        students.add(student);
        return true;
    }

    public boolean removeStudent(String rollNo) {
        Student s = findByRollNo(rollNo);
        if (s == null) {
            return false;
        }
        students.remove(s);
        return true;
    }

    // ------------------------------------------------------------------
    //  Statistics
    // ------------------------------------------------------------------
    public double getClassAverage() {
        return students.stream().mapToDouble(Student::getAverage).average().orElse(0.0);
    }

    public Student getTopScorer() {
        return students.stream().max(Comparator.comparingDouble(Student::getAverage)).orElse(null);
    }

    public Student getLowestScorer() {
        return students.stream().min(Comparator.comparingDouble(Student::getAverage)).orElse(null);
    }

    public long getPassCount() {
        return students.stream().filter(Student::isPassed).count();
    }

    // ------------------------------------------------------------------
    //  Summary report
    // ------------------------------------------------------------------
    public String buildSummaryReport() {
        StringBuilder sb = new StringBuilder();
        String line = "---------------------------------------------------------------------------";

        sb.append("\n").append(line).append("\n");
        sb.append("                     STUDENT GRADE SUMMARY REPORT\n");
        sb.append(line).append("\n");

        if (students.isEmpty()) {
            sb.append(" No student records available.\n");
            sb.append(line).append("\n");
            return sb.toString();
        }

        sb.append(String.format("%-8s %-20s %-8s %-8s %s%n",
                "ROLL NO", "NAME", "AVERAGE", "GRADE", "RESULT"));
        sb.append(line).append("\n");

        List<Student> sorted = new ArrayList<>(students);
        sorted.sort(Comparator.comparingDouble(Student::getAverage).reversed());
        for (Student s : sorted) {
            sb.append(" ").append(s).append("\n");
        }

        sb.append(line).append("\n");
        sb.append(String.format(" Total Students       : %d%n", students.size()));
        sb.append(String.format(" Class Average        : %.2f%n", getClassAverage()));
        sb.append(String.format(" Highest Average      : %.2f  (%s - %s)%n",
                getTopScorer().getAverage(), getTopScorer().getName(), getTopScorer().getRollNo()));
        sb.append(String.format(" Lowest Average       : %.2f  (%s - %s)%n",
                getLowestScorer().getAverage(), getLowestScorer().getName(), getLowestScorer().getRollNo()));
        sb.append(String.format(" Passed / Failed      : %d / %d%n",
                getPassCount(), students.size() - getPassCount()));
        sb.append(line).append("\n");
        return sb.toString();
    }

    /** Detailed report of one student. */
    public String buildStudentReport(Student s) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n--- Report Card : ").append(s.getName())
          .append(" (").append(s.getRollNo()).append(") ---\n");
        if (s.getScores().isEmpty()) {
            sb.append(" No scores recorded.\n");
            return sb.toString();
        }
        for (var e : s.getScores().entrySet()) {
            sb.append(String.format("   %-15s : %6.2f%n", e.getKey(), e.getValue()));
        }
        sb.append(String.format("   %-15s : %6.2f%n", "Average", s.getAverage()));
        sb.append(String.format("   %-15s : %6.2f%n", "Highest", s.getHighest()));
        sb.append(String.format("   %-15s : %6.2f%n", "Lowest", s.getLowest()));
        sb.append(String.format("   %-15s : %s (%s)%n", "Grade", s.getGrade(),
                s.isPassed() ? "PASS" : "FAIL"));
        return sb.toString();
    }

    // ------------------------------------------------------------------
    //  File I/O  (simple CSV: rollNo,name,subject,score)
    // ------------------------------------------------------------------
    public void save() throws IOException {
        if (dataFile.getParent() != null) {
            Files.createDirectories(dataFile.getParent());
        }
        try (BufferedWriter w = Files.newBufferedWriter(dataFile)) {
            w.write("rollNo,name,subject,score");
            w.newLine();
            for (Student s : students) {
                if (s.getScores().isEmpty()) {
                    w.write(s.getRollNo() + "," + escape(s.getName()) + ",,");
                    w.newLine();
                    continue;
                }
                for (var e : s.getScores().entrySet()) {
                    w.write(s.getRollNo() + "," + escape(s.getName()) + ","
                            + escape(e.getKey()) + "," + e.getValue());
                    w.newLine();
                }
            }
        }
    }

    public void load() throws IOException {
        students.clear();
        if (!Files.exists(dataFile)) {
            return;
        }
        try (BufferedReader r = Files.newBufferedReader(dataFile)) {
            String line = r.readLine(); // skip header
            while ((line = r.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] p = line.split(",", -1);
                if (p.length < 4) continue;
                Student s = findByRollNo(p[0]);
                if (s == null) {
                    s = new Student(p[0], p[1]);
                    students.add(s);
                }
                if (!p[2].isBlank() && !p[3].isBlank()) {
                    s.addScore(p[2], Double.parseDouble(p[3]));
                }
            }
        }
    }

    private static String escape(String v) {
        return v.replace(",", " ");
    }
}
