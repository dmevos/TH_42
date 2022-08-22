import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    final static Integer SIZE_RND_NUM = 10000000;
    final static Integer COUNT_OF_WRITE_THREADS = 4;
    final static Integer COUNT_OF_READ_THREADS = 5;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello world!");

        Map<Integer, Integer> concurrentMap = new ConcurrentHashMap<>();
        Map<Integer, Integer> synchroMap = Collections.synchronizedMap(new HashMap<>());

        List<Integer> massiv = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < SIZE_RND_NUM; i++) {
            massiv.add(random.nextInt(10));
        }

        long startTs1 = System.currentTimeMillis();
        funcMap(concurrentMap, massiv);
        long endTs1 = System.currentTimeMillis();
        System.out.println("Время работы с ConcurrentHashMap: " + (endTs1 - startTs1) + "мс");


        long startTs2 = System.currentTimeMillis();
        funcMap(synchroMap, massiv);
        long endTs2 = System.currentTimeMillis();
        System.out.println("Время работы с synchronizedMap: " + (endTs2 - startTs2) + "мс");
    }

    private static void funcMap(Map<Integer, Integer> map, List<Integer> massiv) throws InterruptedException {
        List<Thread> writeThreads = new ArrayList<>();
        List<Thread> readThread = new ArrayList<>();
        for (int i = 0; i < COUNT_OF_WRITE_THREADS; i++) {
            int finalI = i;
            writeThreads.add(new Thread(() -> {
                for (int j = 0; j < SIZE_RND_NUM / COUNT_OF_WRITE_THREADS; j++) {
                    map.put((finalI * SIZE_RND_NUM / COUNT_OF_WRITE_THREADS) + j, massiv.get(j));
                }
            }));
            writeThreads.get(i).start();
        }
        for (int i = 0; i < COUNT_OF_READ_THREADS; i++) {
            readThread.add(new Thread(() -> {
                for (int j = 0; j < map.size(); j++) {
//                    System.out.println(j + " " + concurrentMap.get(j));
                    Integer a = map.get(j);
                }
            }));
            readThread.get(i).start();
        }
        for (int i = 0; i < COUNT_OF_WRITE_THREADS; i++) {
            writeThreads.get(i).join();
        }
        for (int i = 0; i < COUNT_OF_READ_THREADS; i++) {
            readThread.get(i).join();
        }
    }
}