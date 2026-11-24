/**
 * Represents a single student and the scores obtained in different subjects.
 */
public class Student {

    private final String rollNo;
    private String name;
    private final java.util.Map<String, Double> scores = new java.util.LinkedHashMap<>();

    public Student(String rollNo, String name) {
        this.rollNo = rollNo;
        this.name = name;
    }

    public String getRollNo() {
        return rollNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** Adds or updates the score of a subject. */
    public void addScore(String subject, double score) {
        scores.put(subject, score);
    }

    public java.util.Map<String, Double> getScores() {
        return scores;
    }

    /** Average of all subjects of this student (0 when no scores exist). */
    public double getAverage() {
        if (scores.isEmpty()) {
            return 0.0;
        }
        double total = 0.0;
        for (double s : scores.values()) {
            total += s;
        }
        return total / scores.size();
    }

    public double getHighest() {
        return scores.values().stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
    }

    public double getLowest() {
        return scores.values().stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
    }

    /** Converts the average into a letter grade. */
    public String getGrade() {
        double avg = getAverage();
        if (avg >= 90) return "A+";
        if (avg >= 80) return "A";
        if (avg >= 70) return "B";
        if (avg >= 60) return "C";
        if (avg >= 50) return "D";
        if (avg >= 40) return "E";
        return "F";
    }

    public boolean isPassed() {
        return getAverage() >= 40;
    }

    @Override
    public String toString() {
        return String.format("%-8s %-20s %-8.2f %-8s %s",
                rollNo, name, getAverage(), getGrade(), isPassed() ? "PASS" : "FAIL");
    }
}
