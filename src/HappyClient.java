import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.function.BiConsumer;


/**
 * Created by ilya on 07.10.17.
 */
public class HappyClient {

    public static void main(String[] args) {
        HappyConsumer consumer = new HappyConsumer();
        Collection<Thread> producers = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);

        BiConsumer<Integer, String> biFunction = (i, s) -> {
            try {
                latch.await();
                Thread.sleep(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            consumer.putMassage(s);
            return;
        };

        producers.add(new Thread(() -> {
            biFunction.accept(50, new String(new char[]{
                    '\u0021',
                    '\u0029',
                    '\u0029',
                    ' '
            }));
        }));
        producers.add(new Thread(() -> {
            biFunction.accept(40, new String(new char[]{
                    '\u0079',
                    '\u006f',
                    '\u0075',
                    ' '
            }));
        }));
        producers.add(new Thread(() -> {
            biFunction.accept(30, new String(new char[]{
                    '\u0074',
                    '\u006f',
                    ' ',
            }));
        }));
        producers.add(new Thread(() -> {
            biFunction.accept(20, new String(new char[]{
                    '\u0064',
                    '\u0061',
                    '\u0079',
                    ' '
            }));
        }));
        producers.add(new Thread(() -> {
            biFunction.accept(10, new String(new char[]{
                    '\u0062',
                    '\u0069',
                    '\u0072',
                    '\u0074',
                    '\u0068',
            }));
        }));
        producers.add(new Thread(() -> {
            biFunction.accept(0, new String(new char[]{
                    '\u0048',
                    '\u0061',
                    '\u0070',
                    '\u0070',
                    '\u0079',
                    ' '
            }));
        }));
        producers.stream().forEach(p -> p.start());
        latch.countDown();
        producers.stream().forEach(p -> {
            try {
                p.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        consumer.writeToSonsole();
    }

}

class HappyConsumer {

    private volatile Queue<String> massages = new LinkedList<>();

    public synchronized void putMassage(String massage) {
        massages.add(massage);
    }

    public void writeToSonsole() {
        massages.stream().forEach(System.out::print);
    }
}