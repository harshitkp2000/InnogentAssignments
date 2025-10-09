
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class PerformanceComparison {

    private static final int range = 100_000_000;

    public static long parallelStreamSum() {
        long start = System.currentTimeMillis();

        int sum = IntStream.rangeClosed(1, range)
                .parallel()
                .reduce(0, Integer::sum);

        long end = System.currentTimeMillis();
        System.out.println("Parallel Stream sum = " + sum);
        return end - start;
    }

    public static long executorServiceSum() throws InterruptedException, ExecutionException {
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        long start = System.currentTimeMillis();
        List<Future<Integer>> futures = new ArrayList<>();

        int chunkSize = range / numThreads;

        for (int i = 0; i < numThreads; i++) {
            int startRange = i * chunkSize + 1;
            int endRange = (i == numThreads - 1) ? range : (i + 1) * chunkSize;

            futures.add(executor.submit(() -> {
                int localSum = 0;
                for (int j = startRange; j <= endRange; j++) {
                    localSum += j;
                }
                return localSum;
            }));
        }

        int totalSum = 0;
        for (Future<Integer> f : futures) {
            totalSum += f.get();
        }

        long end = System.currentTimeMillis();
        System.out.println("ExecutorService sum = " + totalSum);

        executor.shutdown();
        return end - start;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Available CPU cores: " + Runtime.getRuntime().availableProcessors());
        System.out.println("Starting performance comparison\n");

        long parallelTime = parallelStreamSum();
        long executorTime = executorServiceSum();

        System.out.println("Parallel Stream Time: " + parallelTime + " ms");
        System.out.println("ExecutorService Time: " + executorTime + " ms");
        System.out.println("Faster Approach: " + (parallelTime < executorTime ? "Parallel Stream" : "ExecutorService"));
    }
}
