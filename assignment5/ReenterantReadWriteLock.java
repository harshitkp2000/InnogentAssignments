import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Counter {
    private int count = 0;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    Lock readLock = lock.readLock();
    Lock writeLock = lock.writeLock();

    public int readOperationOnCount() {
        readLock.lock();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        try {
            return count;
        } finally {
            readLock.unlock();
        }
    }

    public void writeOperationOnCount() {
        writeLock.lock();
        try {
            Thread.sleep(3000);
            System.out.println(Thread.currentThread().getName() + " - increasing count");

        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        try {
            count++;
        } finally {
            writeLock.unlock();
        }
    }
}

public class ReenterantReadWriteLock {
    public static void main(String[] args) {
        Counter counting = new Counter();
        Thread t1 = new Thread(() -> System.out.println(counting.readOperationOnCount()), "t1");
        Thread t2 = new Thread(() -> System.out.println(counting.readOperationOnCount()), "t2");

        Thread t4 = new Thread(() -> counting.writeOperationOnCount(), "t4");
        Thread t3 = new Thread(() -> counting.writeOperationOnCount(), "t3");

        t1.start();
        t2.start();
        t3.start();
        t4.start();

    }
}
