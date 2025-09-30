import java.util.*;
import java.util.stream.Collectors;

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

public class StudentManagementSystem {
    static ArrayList<Student> students = new ArrayList<>();
    static ArrayList<ClassRoom> classes = new ArrayList<>();
    static ArrayList<Address> addresses = new ArrayList<>();
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

    public static List<Address> getAddressesByStudent(int studentId) {
        return addresses.stream().filter(a -> a.studentId == studentId).collect(Collectors.toList());
    }

    public static void addStudent() {
        System.out.print("Enter student name: ");
        String name = sc.next();
        System.out.print("Enter classId: ");
        int classId = sc.nextInt();
        System.out.print("Enter marks: ");
        int marks = sc.nextInt();
        System.out.print("Enter gender (M/F): ");
        String gender = sc.next();
        System.out.print("Enter age: ");
        int age = sc.nextInt();

        if (age > 20) {
            System.out.println(" Student not inserted (Age > 20)\n");
            return;
        }
        if (getClassById(classId) == null) {
            System.out.println(" Invalid classId. Please add class first.\n");
            return;
        }

        Student s = new Student(name, classId, marks, gender, age);
        students.add(s);
        assignRanks();
        System.out.println("Student inserted successfully!");
        showStudents();
    }

    public static void addAddress() {
        System.out.print("Enter pin code: ");
        String pin = sc.next();
        System.out.print("Enter city: ");
        String city = sc.next();
        System.out.print("Enter studentId: ");
        int sid = sc.nextInt();

        if (students.stream().noneMatch(s -> s.id == sid)) {
            System.out.println(" Invalid studentId. Address not added.\n");
            return;
        }

        Address a = new Address(pin, city, sid);
        addresses.add(a);
        System.out.println(" Address inserted successfully!");
        showAddresses();
    }

    public static void addClassRoom() {
        System.out.print("Enter class name: ");
        String cname = sc.next();
        ClassRoom c = new ClassRoom(cname);
        classes.add(c);
        System.out.println(" ClassRoom inserted successfully!");
        showClasses();
    }

    public static void deleteStudent() {
        System.out.print("Enter studentId to delete: ");
        int sid = sc.nextInt();

        Optional<Student> stuOpt = students.stream().filter(s -> s.id == sid).findFirst();
        if (stuOpt.isEmpty()) {
            System.out.println(" Student not found.");
            return;
        }

        Student s = stuOpt.get();
        students.remove(s);
        addresses.removeIf(a -> a.studentId == sid);

        if (students.stream().noneMatch(st -> st.classId == s.classId)) {
            classes.removeIf(c -> c.id == s.classId);
            System.out.println("ℹ️ Class also removed as it has no students.");
        }
        assignRanks();
        System.out.println("Student and related records deleted.");
    }

    public static void showStudents() {
        System.out.println("\n--- Student Table ---");
        for (Student s : students)
            System.out.println(s);
        System.out.println();
    }

    public static void showAddresses() {
        System.out.println("\n--- Address Table ---");
        for (Address a : addresses)
            System.out.println(a);
        System.out.println();
    }

    public static void showClasses() {
        System.out.println("\n--- Class Table ---");
        for (ClassRoom c : classes)
            System.out.println(c);
        System.out.println();
    }

    public static boolean applyFilters(Student s, String gender, Integer age, String city, String pin,
            Integer classId) {
        if (gender != null && !s.gender.equalsIgnoreCase(gender))
            return false;
        if (age != null && s.age != age)
            return false;
        if (classId != null && s.classId != classId)
            return false;
        List<Address> stuAddrs = getAddressesByStudent(s.id);
        if (city != null && stuAddrs.stream().noneMatch(a -> a.city.equalsIgnoreCase(city)))
            return false;
        if (pin != null && stuAddrs.stream().noneMatch(a -> a.pinCode.equals(pin)))
            return false;
        return true;
    }

    public static Map<String, Object> getOptionalFilters() {
        Map<String, Object> filters = new HashMap<>();

        System.out.print("Do you want to apply extra filters (yes/no)? ");
        String choice = sc.next();
        if (!choice.equalsIgnoreCase("yes")) {
            return filters;
        }

        System.out.print("Filter by gender (M/F or skip): ");
        String g = sc.next();
        if (!g.equalsIgnoreCase("skip"))
            filters.put("gender", g);

        System.out.print("Filter by age (-1 to skip): ");
        int a = sc.nextInt();
        if (a != -1)
            filters.put("age", a);

        // ClassId
        System.out.print("Filter by classId (-1 to skip): ");
        int cid = sc.nextInt();
        if (cid != -1)
            filters.put("classId", cid);

        // City
        System.out.print("Filter by city (or skip): ");
        String city = sc.next();
        if (!city.equalsIgnoreCase("skip"))
            filters.put("city", city);

        // Pincode
        System.out.print("Filter by pincode (or skip): ");
        String pin = sc.next();
        if (!pin.equalsIgnoreCase("skip"))
            filters.put("pin", pin);

        return filters;
    }

    public static void findStudentsByPincode() {
        System.out.print("Enter pincode: ");
        String pin = sc.next();

        Map<String, Object> filters = getOptionalFilters();
        applyQueryFilters(
                (String) filters.get("gender"),
                (Integer) filters.get("age"),
                (String) filters.get("city"),
                pin,
                (Integer) filters.get("classId"));
    }

    public static void findStudentsByCity() {
        System.out.print("Enter city: ");
        String city = sc.next();

        Map<String, Object> filters = getOptionalFilters();
        applyQueryFilters(
                (String) filters.get("gender"),
                (Integer) filters.get("age"),
                city,
                (String) filters.get("pin"),
                (Integer) filters.get("classId"));
    }

    public static void findStudentsByClass() {
        System.out.print("Enter class name: ");
        String cname = sc.next();
        Optional<ClassRoom> cOpt = classes.stream().filter(c -> c.name.equalsIgnoreCase(cname)).findFirst();
        if (cOpt.isEmpty()) {
            System.out.println(" Class not found.");
            return;
        }

        Map<String, Object> filters = getOptionalFilters();
        applyQueryFilters(
                (String) filters.get("gender"),
                (Integer) filters.get("age"),
                (String) filters.get("city"),
                (String) filters.get("pin"),
                cOpt.get().id);
    }

    public static void getPassedStudents() {
        Map<String, Object> filters = getOptionalFilters();
        List<Student> res = students.stream()
                .filter(s -> s.result.equals("Pass"))
                .filter(s -> applyFilters(s,
                        (String) filters.get("gender"),
                        (Integer) filters.get("age"),
                        (String) filters.get("city"),
                        (String) filters.get("pin"),
                        (Integer) filters.get("classId")))
                .collect(Collectors.toList());

        if (res.isEmpty()) {
            System.out.println(" No passed students found.");
        } else {
            System.out.println("Passed Students:");
            res.forEach(System.out::println);
        }
    }

    public static void getFailedStudents() {
        Map<String, Object> filters = getOptionalFilters();
        List<Student> res = students.stream()
                .filter(s -> s.result.equals("Fail"))
                .filter(s -> applyFilters(s,
                        (String) filters.get("gender"),
                        (Integer) filters.get("age"),
                        (String) filters.get("city"),
                        (String) filters.get("pin"),
                        (Integer) filters.get("classId")))
                .collect(Collectors.toList());

        if (res.isEmpty()) {
            System.out.println(" No failed students found.");
        } else {
            System.out.println("Failed Students:");
            res.forEach(System.out::println);
        }
    }

    public static void applyQueryFilters(String gender, Integer age, String city, String pin, Integer classId) {
        List<Student> res = students.stream()
                .filter(s -> applyFilters(s, gender, age, city, pin, classId))
                .collect(Collectors.toList());

        if (res.isEmpty()) {
            System.out.println(" No students found.");
        } else {
            System.out.println(" Result:");
            res.forEach(System.out::println);
        }
    }

    public static void paginateStudents() {
        System.out.print("Enter gender filter (or ALL): ");
        String genderInput = sc.next();
        String finalGender = genderInput.equalsIgnoreCase("ALL") ? null : genderInput;

        System.out.print("Enter start index: ");
        int start = sc.nextInt();
        System.out.print("Enter end index: ");
        int end = sc.nextInt();

        System.out.print("Order by (name/marks/none): ");
        String order = sc.next();

        List<Student> res = students.stream()
                .filter(s -> finalGender == null || s.gender.equalsIgnoreCase(finalGender))
                .collect(Collectors.toList());

        if (order.equalsIgnoreCase("name")) {
            res.sort(Comparator.comparing(s -> s.name));
        } else if (order.equalsIgnoreCase("marks")) {
            res.sort((a, b) -> b.marks - a.marks);
        }

        int from = Math.max(0, start - 1);
        int to = Math.min(res.size(), end);

        if (from >= to) {
            System.out.println(" Invalid pagination range.");
            return;
        }

        List<Student> page = res.subList(from, to);
        System.out.println("Paginated Result:");
        page.forEach(System.out::println);
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("====== Student Management System ======");
            System.out.println("1. Add Student");
            System.out.println("2. Add Address");
            System.out.println("3. Add ClassRoom");
            System.out.println("4. Show Students");
            System.out.println("5. Show Addresses");
            System.out.println("6. Show Classes");
            System.out.println("7. Delete Student");
            System.out.println("8. Find Students by Pincode");
            System.out.println("9. Find Students by City");
            System.out.println("10. Find Students by Class");
            System.out.println("11. Get Passed Students");
            System.out.println("12. Get Failed Students");
            System.out.println("13. Paginate Students");
            System.out.println("14. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> addAddress();
                case 3 -> addClassRoom();
                case 4 -> showStudents();
                case 5 -> showAddresses();
                case 6 -> showClasses();
                case 7 -> deleteStudent();
                case 8 -> findStudentsByPincode();
                case 9 -> findStudentsByCity();
                case 10 -> findStudentsByClass();
                case 11 -> getPassedStudents();
                case 12 -> getFailedStudents();
                case 13 -> paginateStudents();
                case 14 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice! Try again.");
            }
        }
    }
}
