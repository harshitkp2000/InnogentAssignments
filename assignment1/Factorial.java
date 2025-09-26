
import java.util.Scanner;

public class Factorial {

    // value - stack (because that is a local variable)
    // i - stack (because that is a loop variable)
    public static void factorialUsingIterator(int n) {
        int value = 1;
        for (int i = 1; i <= n; i++) {
            value = value * i;
        }
        System.out.println(value);
    }

    // n - stack(because n is passed a parameter and is a local variable)
    // value - stack
    // recursive call - stack (recursion does a repeatative method calls)
    public static int factorial(int n, int value) {
        if (n < 1)
            return value;
        value = value * n;
        return factorial(n - 1, value);
    }

    // method call - stack
    public static int factorialUsingRecursion(int n) {
        return factorial(n, 1);
    }

    // sc - heap (object is kept in heap memory)
    // n- stack (beacuse it is a local variable)\
    // factorialUsingIterator - stack (beause it is a method call)
    // factorialUsingRecursion - stack (again a method call)
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        factorialUsingIterator(n);
        System.out.println(factorialUsingRecursion(n));
    }
}

// method calls and the local variables are kept in STACK and the objects are
// kept in the HEAP