# CodeAlpha_StudentGradeTracker

**Java Programming Internship — Task 1: Student Grade Tracker**
CodeAlpha | Student: Piyush Vishnupant Divase | ID: CA/DF1/433397

---

## Task Requirements Covered

| Requirement | Implementation |
|---|---|
| Java program to input and manage student grades | Menu driven console application (`StudentGradeTracker.java`) |
| Calculate average, highest and lowest scores | `Student.getAverage()`, `getHighest()`, `getLowest()` |
| Use arrays or ArrayLists to store and manage data | `ArrayList<Student>` in `GradeTracker.java` |
| Display a summary report of all students | `GradeTracker.buildSummaryReport()` |
| Console-based or GUI-based interface | Console-based interface with a 7-option menu |

Additional persistence: all records are stored in `data/grades.csv` using file I/O,
so data survives between runs.

## Project Structure

```
CodeAlpha_StudentGradeTracker/
├── src/
│   ├── Student.java            # Student model (scores, average, grade)
│   ├── GradeTracker.java       # Collection manager, statistics, report, CSV I/O
│   └── StudentGradeTracker.java# Main class + console menu (entry point)
├── data/
│   └── grades.csv              # Saved student records (created/updated by app)
├── run.sh                      # Compile & run script
└── README.md
```

## How to Compile and Run

```bash
javac -d out src/*.java
java -cp out StudentGradeTracker
```

or simply:

```bash
chmod +x run.sh
./run.sh
```

## Menu Options

```
1. Add Student                -> Roll no, name and subject scores
2. Enter / Update Scores      -> Add or change a subject score
3. Display Summary Report     -> Ranked table + class statistics
4. Search Student Report      -> Subject-wise report card of one student
5. Remove Student             -> Delete a record
6. Save Data                  -> Persist to data/grades.csv
7. Save & Exit
```

## Grading Scale

| Average | Grade |
|---|---|
| >= 90 | A+ |
| >= 80 | A |
| >= 70 | B |
| >= 60 | C |
| >= 50 | D |
| >= 40 | E |
| < 40 | F |

A student with an average below 40 is reported as **FAIL**.

## Sample Output

```
---------------------------------------------------------------------------
                     STUDENT GRADE SUMMARY REPORT
---------------------------------------------------------------------------
ROLL NO  NAME                 AVERAGE  GRADE    RESULT
---------------------------------------------------------------------------
 CA04     Sneha Joshi          93.00    A+       PASS
 CA01     Aarav Patil          86.60    A        PASS
 CA02     Isha Deshmukh        68.00    C        PASS
 CA03     Rohan Kale           44.60    E        PASS
---------------------------------------------------------------------------
 Total Students       : 4
 Class Average        : 73.05
 Highest Average      : 93.00  (Sneha Joshi - CA04)
 Lowest Average       : 44.60  (Rohan Kale - CA03)
 Passed / Failed      : 4 / 0
---------------------------------------------------------------------------
```

## Concepts Used

- Object Oriented Programming: encapsulation, classes, objects
- `ArrayList` and `LinkedHashMap` collections
- Java Streams for average / max / min calculations
- `String.format` for formatted tabular output
- File I/O with `java.nio.file.Files` and `BufferedReader` / `BufferedWriter`
- Exception handling
