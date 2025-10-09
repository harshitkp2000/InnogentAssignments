import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

class ProduceAndConsume {
    int n;
    ArrayList<Integer> list = new ArrayList<>(n);
    boolean flag;

    public ProduceAndConsume(int n) {
        this.n = n;
    }

    public synchronized void producer() {
        int value = 1;
        System.out.println();
        while (!flag) {

            if (list.size() == n) {
                System.out.println("Bucket is full");
                try {
                    value = 1;
                    wait();
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
            list.add(value);
            System.out.println("Produced - " + value);
            value++;
            notify();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e);
            }

        }
    }

    public synchronized void consumer() {
        while (!flag) {

            if (list.isEmpty()) {
                System.out.println("Bucket is Empty");
                notify();
                try {
                    wait();
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }

            int val = list.removeFirst();
            System.out.println("Consumed - " + val);
            notify();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e);
            }

        }
        System.out.println("Excecution Completed Successfully!!!");
    }
}

public class ProducerConsumer {
    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        int n;
        while (true) {
            try {
                n = sc.nextInt();
                if (n <= 0) {
                    throw new InputMismatchException();
                }
                break;
            } catch (Exception e) {
                System.out.println(e);
            }

        }
        ProduceAndConsume pc = new ProduceAndConsume(n);

        Thread t1 = new Thread(() -> pc.producer());

        t1.setName("Producer");

        Thread t2 = new Thread(() -> pc.consumer());

        t2.setName("Consumer");

        t1.start();
        t2.start();
    }
}