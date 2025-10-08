
import java.util.ArrayList;
import java.util.*;
import java.util.stream.Collectors;

class Employee {
    String name;
    int salary;
    String department;
    Employee( String name, int salary, String department) {

        this.name = name;
        this.salary = salary;
        this.department = department;
    }

    @Override
    public String toString() {
        return "{name: "  + name + ", salary: " + salary + ", department: " + department+" }";
    }
}
public class Streams {
    public static void main() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of employees: ");
        int n = sc.nextInt();
        List<Employee> employees = new ArrayList<Employee>();
        for(int i = 0; i < n; i++) {

            System.out.println("Enter employee name: ");
            String name = sc.next();
            System.out.println("Enter employee salary: ");
            int salary = sc.nextInt();
            System.out.println("Enter employee department: ");
            String department = sc.next();
            employees.add(new Employee(name, salary, department));
        }

//          1.filter employee by the department name
            List<Employee> filteredEmployee = employees.stream().filter(a -> a.department.toLowerCase().equals("IT".toLowerCase())).collect(Collectors.toList());
            System.out.println(filteredEmployee);

//          2.Compute total salary with reduce
            int sumOfSalary = employees.stream().map(a -> a.salary).reduce(0,(a,b) -> a + b ) ;
            System.out.println(sumOfSalary);

//            3. convert employee name to uppercase using map
            List<String> listofNames = employees.stream().map((a) -> a.name.toUpperCase()).collect(Collectors.toList());
            System.out.println(listofNames);
    }
}
