import java.util.concurrent.CountDownLatch;

class Worker implements Runnable {
    private String task;
    private CountDownLatch latch;

    Worker(String task, CountDownLatch latch) {
        this.task = task;
        this.latch = latch;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " started task: " + task);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(Thread.currentThread().getName() + " finished task: " + task);
        latch.countDown();
    }
}

public class CountDownLatchExample {
    public static void main(String[] args) {

        CountDownLatch latch = new CountDownLatch(3);

        Thread t1 = new Thread(new Worker("Load Configuration", latch), "t1");
        Thread t2 = new Thread(new Worker("Connect to Database", latch), "t2");
        Thread t3 = new Thread(new Worker("Initialize Cache", latch), "t3");

        t1.start();
        t2.start();
        t3.start();

        System.out.println("Main thread waiting for initialization tasks to complete...");
        try {
            latch.await();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("task completed");
    }
}
