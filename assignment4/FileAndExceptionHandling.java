import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

class InvalidAgeException extends RuntimeException {
    public InvalidAgeException(String message) {
        super(message);
    }
}

class InvalidStudentException extends RuntimeException {
    public InvalidStudentException(String message) {
        super(message);
    }
}

class InvalidMarksException extends RuntimeException {
    public InvalidMarksException(String message) {
        super(message);
    }
}

class ClassRoom {
    static int counter = 1;
    int id;
    String name;

    ClassRoom(String name) {
        this.id = counter++;
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("ClassRoom{id=%d, name='%s'}", id, name);
    }
}

class Address {
    static int counter = 1;
    int id;
    String pinCode;
    String city;
    int studentId;

    Address(String pinCode, String city, int studentId) {
        this.id = counter++;
        this.pinCode = pinCode;
        this.city = city;
        this.studentId = studentId;
    }

    @Override
    public String toString() {
        return String.format("Address{id=%d, pinCode='%s', city='%s', studentId=%d}",
                id, pinCode, city, studentId);
    }
}

class Student {
    static int counter = 1;
    int id;
    String name;
    int classId;
    int marks;
    String gender;
    int age;
    String result;
    int rank;

    Student(String name, int classId, int marks, String gender, int age) {
        this.id = counter++;
        this.name = name;
        this.classId = classId;
        this.marks = marks;
        this.gender = gender;
        this.age = age;
        this.result = (marks < 50) ? "Fail" : "Pass";
    }

    @Override
    public String toString() {
        return String.format(
                "Student{id=%d, name='%s', classId=%d, marks=%d, gender='%s', age=%d, result='%s', rank=%d}",
                id, name, classId, marks, gender, age, result, rank);
    }
}

public class FileAndExceptionHandling {
    static List<Student> students = new ArrayList<>();
    static List<ClassRoom> classes = new ArrayList<>();
    static List<Address> addresses = new ArrayList<>();
    static List<Student> rank = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void assignRanks() {
        students.sort((a, b) -> b.marks - a.marks);
        int rank = 1;
        for (int i = 0; i < students.size(); i++) {
            if (i > 0 && students.get(i).marks == students.get(i - 1).marks) {
                students.get(i).rank = students.get(i - 1).rank;
            } else {
                students.get(i).rank = rank;
            }
            rank++;
        }
    }

    public static ClassRoom getClassById(int classId) {
        for (ClassRoom c : classes) {
            if (c.id == classId)
                return c;
        }
        return null;
    }

    public static void addStudent() {
        try {

            String name = getStringInput("Enter student name: ");
            int classId = getIntInput("Enter classId: ");
            int marks = getIntInput("Enter marks: ");
            if (marks < 0 || marks > 100) {
                throw new InvalidMarksException("Marks should be between 0 - 100");
            }
            System.out.print("Enter gender (M/F): ");
            String gender = sc.next();
            int age = getIntInput("Enter age: ");
            if (age > 20) {
                throw new InvalidAgeException("Age is Invalid :" + age);
            }

            if (getClassById(classId) == null) {
                System.out.println(" Invalid classId. Please add class first.\n");
                return;
            }

            Student s = new Student(name, classId, marks, gender, age);
            students.add(s);

            assignRanks();
            getRank();
            System.out.println("Student inserted successfully!");
            fileHandling("Students.txt");
        } catch (InputMismatchException e) {
            System.out.println("Add correct input :" + e.getMessage());
        } catch (InvalidMarksException e) {
            System.out.println(e.getMessage());
        } catch (InvalidAgeException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void addAddress() {
        System.out.print("Enter pin code: ");
        String pin = sc.next();
        System.out.print("Enter city: ");
        String city = sc.next();
        int sid = getIntInput("Enter studentId: ");

        if (students.stream().noneMatch(s -> s.id == sid)) {
            System.out.println(" Invalid studentId. Address not added.\n");
            return;
        }

        Address a = new Address(pin, city, sid);
        addresses.add(a);
        System.out.println(" Address inserted successfully!");
        fileHandling("Addresses.txt");
    }

    public static void addClassRoom() {
        System.out.print("Enter class name: ");
        String cname = sc.next();
        ClassRoom c = new ClassRoom(cname);
        classes.add(c);
        System.out.println(" ClassRoom inserted successfully!");
        fileHandling("Classes.txt");
    }

    public static void deleteStudent() {
        try {

            int sid = getIntInput("Enter studentId to delete: ");

            Optional<Student> stuOpt = students.stream().filter(s -> s.id == sid).findFirst();
            if (stuOpt.isEmpty()) {
                throw new InvalidStudentException("Student not found");

            }

            Student s = stuOpt.get();
            students.remove(s);
            fileHandling("Students.txt");
            addresses.removeIf(a -> a.studentId == sid);
            fileHandling("Addresses.txt");
            if (students.stream().noneMatch(st -> st.classId == s.classId)) {
                classes.removeIf(c -> c.id == s.classId);
                System.out.println("Class also removed as it has no students.");
                fileHandling("Classes.txt");
            }

            assignRanks();
            getRank();
            System.out.println("Student and related records deleted.");
        } catch (InvalidStudentException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void getRank() {
        rank.clear();
        rank.addAll(
                students.stream()
                        .filter(s -> s.rank <= 5)
                        .collect(Collectors.toList()));
        fileHandling("Ranks.txt");
    }

    public static void fileHandling(String fileName) {
        try {
            FileWriter writer = new FileWriter(fileName);
            if (fileName.equals("Students.txt")) {
                writer.write(
                        String.format("%-15s %-15s %-15s %-15s %-15s %-15s%n", "ID", "NAME", "ClASS ID", "MARKS",
                                "GENDER",
                                "AGE"));
                writer.write(
                        "--------------------------------------------------------------------------------------------\n");

                // Write student data
                for (Student s : students) {
                    writer.write(String.format("%-15d %-15s %-15d %-15d %-15s %-15d%n",
                            s.id, s.name, s.classId, s.marks, s.gender, s.age));
                }

                System.out.println("Student File updated successfully!");
            } else if (fileName.equals("Classes.txt")) {
                writer.write(
                        String.format("%-15s %-15s%n", "ID", "CLASSNAME"));
                writer.write(
                        "--------------------------------------------------------------------------------------------\n");

                // Write student data
                for (ClassRoom s : classes) {
                    writer.write(String.format("%-15d %-15s%n",
                            s.id, s.name));
                }

                System.out.println("Classes File updated successfully!");
            } else if (fileName.equals("Addresses.txt")) {
                writer.write(
                        String.format("%-15s %-15s %-15s %-15s%n", "ID", "PINCODE", "CITY", "STUDENTID"));
                writer.write(
                        "--------------------------------------------------------------------------------------------\n");

                // Write student data
                for (Address s : addresses) {
                    writer.write(String.format("%-15d %-15s %-15s %-15d%n",
                            s.id, s.pinCode, s.city, s.studentId));
                }

                System.out.println("Address File updated successfully!");
            } else if (fileName.equals("Ranks.txt")) {
                writer.write(
                        String.format("%-15s %-25s %-15s%n", "ID", "NAME", "RANK"));
                writer.write(
                        "--------------------------------------------------------------------------------------------\n");

                // Write student data
                for (Student s : rank) {
                    writer.write(String.format("%-15d %-25s %-15d%n",
                            s.id, s.name, s.rank));
                }

                System.out.println("Ranks File updated successfully!");
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("IOException handled  :" + e.getMessage());
        }
    }

    public static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = sc.nextInt();
                sc.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input!");
                sc.nextLine();
            }
        }
    }

    public static String getStringInput(String prompt) {
        System.out.print(prompt);
        return sc.nextLine();
    }

    public static void readStudents() {
        readFile("Students.txt", "===== Student Records =====");
    }

    public static void readClasses() {
        readFile("Classes.txt", "===== Class Records =====");
    }

    public static void readAddresses() {
        readFile("Addresses.txt", "===== Address Records =====");
    }

    public static void readRank() {
        readFile("Ranks.txt", "===== Rank Records =====");
    }

    public static void readFile(String fileName, String header) {
        System.out.println(header);
        try (Scanner fileScanner = new Scanner(new File(fileName))) {
            while (fileScanner.hasNextLine()) {
                System.out.println(fileScanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName + ". Please generate it first.");
        }
        System.out.println();
    }

    public static void main(String args[]) {
        while (true) {
            System.out.println("====== Student Management System ======");
            System.out.println("1. Add Student");
            System.out.println("2. Add Address");
            System.out.println("3. Add ClassRoom");
            System.out.println("4. Delete Student");
            System.out.println("5. Generate Rank File ");
            System.out.println("6. Read File Students.txt");
            System.out.println("7. Read File Classes.txt ");
            System.out.println("8. Read File Addresses.txt ");
            System.out.println("9. Read File Ranks.txt ");
            System.out.println("10. Exit");
            int choice = getIntInput("Enter choice: ");

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> addAddress();
                case 3 -> addClassRoom();
                case 4 -> deleteStudent();
                case 5 -> getRank();
                case 6 -> readStudents();
                case 7 -> readClasses();
                case 8 -> readAddresses();
                case 9 -> readRank();
                case 10 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice! Try again.");
            }

        }
    }
}