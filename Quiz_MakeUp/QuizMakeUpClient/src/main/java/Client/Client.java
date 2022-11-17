package Client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Client {

  private static final String BASE_PATH = "http://34.214.248.63:8080/QuizMakeUp_war/prime/";
  private static final String LOCAL_PATH = "http://localhost:8080/QuizMakeUp_war_exploded/prime/";

  public static void main(String[] args) throws InterruptedException {
    LinkedBlockingQueue<Integer> producerQueue = new LinkedBlockingQueue<>();
    LinkedBlockingQueue<Recorder> recorders = new LinkedBlockingQueue<>();
    ExecutorService producerPool = Executors.newCachedThreadPool();
    AtomicInteger primeCounter = new AtomicInteger();
    CountDownLatch producerRound = new CountDownLatch(100);
    CountDownLatch firstRound = new CountDownLatch(16000);
    CountDownLatch secondRound = new CountDownLatch(84000);
    long startTime = System.currentTimeMillis();
    /* Producer process, produce 100*1k prime numbers */
    for(int i=0; i<100; i++){
      producerPool.submit(new ProducerTask(producerQueue, producerRound));
    }
    producerPool.shutdown();
    /* First Round, 32 threads each sends 500 requests */
    for(int i=0; i<32; i++){
      Thread thread = new Thread(new ConsumerTask(producerQueue, BASE_PATH, firstRound, 500, recorders, primeCounter));
      thread.start();
    }
    firstRound.await();
    /* Second Round, After first round's done, send the rest 84k requests */
    for(int i=0; i<168; i++){
      Thread thread = new Thread(new ConsumerTask(producerQueue, BASE_PATH, secondRound, 500, recorders, primeCounter));
      thread.start();
    }
    secondRound.await();
    producerRound.await();
    long endTime = System.currentTimeMillis();

    /* Calculate the performance and write csv file*/
    List<Recorder> recorderList = new ArrayList<>();
    long sum = 0;
    for(Recorder recorder : recorders) {
      recorderList.add(recorder);
      sum += recorder.getLatency();
    }

    int index = (int) Math.ceil(99 / 100.0 * recorderList.size()) - 1;
    long p99Latency = recorderList.get(index).getLatency();

    Collections.sort(recorderList, (r1, r2) -> (int) (r1.getLatency() - r2.getLatency()));

    long minLatency = recorderList.get(0).getLatency();
    long maxLatency = recorderList.get(recorderList.size()-1).getLatency();
    long medianLatency = recorderList.get(recorderList.size()/2).getLatency();
    double meanLatency = (double) sum / recorderList.size();

    System.out.println("total prime number " + primeCounter.get());
    System.out.println("mean response time (milliseconds): " + meanLatency);
    System.out.println("median response time (milliseconds): " + medianLatency);
    System.out.println("throughput (requests/second): " + (100000 / ((double)(endTime-startTime)/1000)));
    System.out.println("p99 (99th percentile) response time: " + p99Latency);
    System.out.println("min response time (milliseconds): " + minLatency);
    System.out.println("max response time (milliseconds): " + maxLatency);
  }

}
