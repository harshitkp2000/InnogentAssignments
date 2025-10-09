import java.io.*;
import java.util.concurrent.*;

class LineCountTask implements Callable<Integer> {
    private File file;

    public LineCountTask(File file) {
        this.file = file;
    }

    @Override
    public Integer call() {
        int lineCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.readLine() != null) {
                lineCount++;
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        System.out.println(
                file.getName() + ": " + lineCount + " lines");
        return lineCount;
    }
}

public class ExecutorAndCallable {
    public static void main(String[] args) {

        File file1 = new File("file1.txt");
        File file2 = new File("file2.txt");
        File file3 = new File("file3.txt");

        ExecutorService executor = Executors.newFixedThreadPool(3);

        Callable<Integer> task1 = new LineCountTask(file1);
        Callable<Integer> task2 = new LineCountTask(file2);
        Callable<Integer> task3 = new LineCountTask(file3);

        Future<Integer> future1 = executor.submit(task1);
        Future<Integer> future2 = executor.submit(task2);
        Future<Integer> future3 = executor.submit(task3);

        int totalLines = 0;
        try {
            totalLines = future1.get() + future2.get() + future3.get();
        } catch (InterruptedException e) {

            System.err.println(e.getMessage());
        } catch (ExecutionException e) {
            System.err.println(e.getMessage());
        }

        System.out.println("Total number of lines " + totalLines);

        executor.shutdown();
    }
}
