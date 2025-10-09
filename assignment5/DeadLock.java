
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

class Pen {
    Lock lock = new ReentrantLock();

    public void writeWithPenAndPaper(Paper paper) {
        try {
            if (lock.tryLock(1000, TimeUnit.MILLISECONDS)) {
                System.out.println(Thread.currentThread().getName() + " acquired pen " + this);
                // Try to acquire paper inside
                paper.finishWritingWithLock();
            } else {
                System.out.println(Thread.currentThread().getName() + " could not acquire pen " + this);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {

            lock.unlock();
        }
    }

    public void finishWritingWithLock() {
        System.out.println(Thread.currentThread().getName() + " finished writing " + this);
    }
}

class Paper {
    Lock lock = new ReentrantLock();

    public void writeWithPaperAndPen(Pen pen) {
        try {
            if (lock.tryLock(1000, TimeUnit.MILLISECONDS)) {
                System.out.println(Thread.currentThread().getName() + " acquired paper " + this);
                // Try to acquire pen inside
                pen.finishWritingWithLock();
            } else {
                System.out.println(Thread.currentThread().getName() + " could not acquire paper " + this);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {

            lock.unlock();
        }
    }

    public void finishWritingWithLock() {
        System.out.println(Thread.currentThread().getName() + " finished writing " + this);
    }
}

class Task1 implements Runnable {
    private Pen pen;
    private Paper paper;

    Task1(Pen pen, Paper paper) {
        this.pen = pen;
        this.paper = paper;
    }

    @Override
    public void run() {
        pen.writeWithPenAndPaper(paper);
    }
}

class Task2 implements Runnable {
    private Pen pen;
    private Paper paper;

    Task2(Pen pen, Paper paper) {
        this.pen = pen;
        this.paper = paper;
    }

    @Override
    public void run() {
        paper.writeWithPaperAndPen(pen);
    }
}

public class DeadLock {
    public static void main(String[] args) {
        Pen pen = new Pen();
        Paper paper = new Paper();

        Thread thread1 = new Thread(new Task1(pen, paper), "thread1");
        Thread thread2 = new Thread(new Task2(pen, paper), "thread2");

        thread1.start();
        thread2.start();
    }
}
