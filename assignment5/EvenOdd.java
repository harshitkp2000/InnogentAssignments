import java.util.Scanner;

class MyThread {
    boolean flag = false;

    public synchronized void printEven(int i) {

        if (!flag) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
        System.out.println("Thread Even - " + i);
        flag = false;
        notify();

    }

    public synchronized void printOdd(int i) {
        if (flag) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
        System.out.println("Thread Odd - " + i);
        flag = true;
        notify();
    }

}

public class EvenOdd {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        MyThread printEvenOdd = new MyThread();
        Thread even = new Thread(() -> {
            for (int i = 2; i <= n; i += 2) {
                if (i % 2 == 0)
                    printEvenOdd.printEven(i);
            }
        });
        Thread odd = new Thread(() -> {
            for (int i = 1; i <= n; i += 2) {
                if (i % 2 != 0)
                    printEvenOdd.printOdd(i);
            }
        });
        even.start();
        odd.start();
    }
}