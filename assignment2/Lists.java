import java.util.*;

public class Lists {
    public static void main(String[] args) {

        int n = 10000;
        int m = 50000;
        int k = 100000;
        List<Integer> arrayList = new ArrayList<>();
        long arrayInsertTime = testInsertion(arrayList, n);
        long arrayDeleteTime = testDeletion(arrayList, n);
        long arrayInsertTime1 = testInsertion(arrayList, m);
        long arrayDeleteTime1 = testDeletion(arrayList, m);
        long arrayInsertTime2 = testInsertion(arrayList, k);
        long arrayDeleteTime2 = testDeletion(arrayList, k);

        List<Integer> linkedList = new LinkedList<>();
        long linkedInsertTime = testInsertion(linkedList, n);
        long linkedDeleteTime = testDeletion(linkedList, n);
        long linkedInsertTime1 = testInsertion(linkedList, m);
        long linkedDeleteTime1 = testDeletion(linkedList, m);
        long linkedInsertTime2 = testInsertion(linkedList, k);
        long linkedDeleteTime2 = testDeletion(linkedList, k);

        System.out.println(
                "ArrayList for 10k - Insertion: " + arrayInsertTime + " ms, Deletion: " + arrayDeleteTime + " ms");
        System.out.println(
                "ArrayList for 50k - Insertion: " + arrayInsertTime1 + " ms, Deletion: " + arrayDeleteTime1 + " ms");
        System.out.println(
                "ArrayList for 100k - Insertion: " + arrayInsertTime2 + " ms, Deletion: " + arrayDeleteTime2 + " ms");
        System.out.println();
        System.out
                .println("LinkedList for 10k - Insertion: " + linkedInsertTime + " ms, Deletion: " + linkedDeleteTime
                        + " ms");
        System.out.println(
                "LinkedList for 50k - Insertion: " + linkedInsertTime1 + " ms, Deletion: " + linkedDeleteTime1 + " ms");
        System.out.println(
                "LinkedList for 100k - Insertion: " + linkedInsertTime2 + " ms, Deletion: " + linkedDeleteTime2
                        + " ms");

    }

    public static long testInsertion(List<Integer> list, int n) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            list.add(i);
        }
        long end = System.currentTimeMillis();
        return end - start;
    }

    public static long testDeletion(List<Integer> list, int n) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < n / 2; i++) {
            list.remove(list.size() / 2);
        }
        long end = System.currentTimeMillis();
        return end - start;
    }
}
