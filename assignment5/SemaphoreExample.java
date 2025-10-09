import java.util.concurrent.Semaphore;

class Parking {
    private Semaphore sp;

    public Parking(int place) {
        sp = new Semaphore(place);
    }

    public void park(String name) {
        try {
            System.out.println(name + " is trying to park");
            sp.acquire();
            System.out.println(name + " has parked.");
            Thread.sleep(3000);
            System.out.println(name + " is leaving the parking.");
            sp.release();

        } catch (InterruptedException e) {

            System.out.println(e.getMessage());
        }
    }
}

class Car implements Runnable {
    private String name;
    private Parking parking;

    public Car(String name, Parking parking) {
        this.name = name;
        this.parking = parking;
    }

    @Override
    public void run() {
        parking.park(name);
    }
}

public class SemaphoreExample {
    public static void main(String[] args) {

        Parking parking = new Parking(3); // There are 3 place for parking a car

        // Create 5 threads t1 to t5
        Thread t1 = new Thread(new Car("Car-1", parking));
        Thread t2 = new Thread(new Car("Car-2", parking));
        Thread t3 = new Thread(new Car("Car-3", parking));
        Thread t4 = new Thread(new Car("Car-4", parking));
        Thread t5 = new Thread(new Car("Car-5", parking));

        // Start all threads
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
    }
}
