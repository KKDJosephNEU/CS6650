package Part1;

import Util.Skier;
import io.swagger.client.api.SkiersApi;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Part1Client {
  private static final String BASE_PATH = "http://54.218.104.140:8080/HW1-Server_war";
  private static final String LOCAL_PATH = "http://localhost:8080/HW1_Server_war_exploded";

  public static void main(String[] args) throws InterruptedException {
    LinkedBlockingQueue<Skier> producerQueue = new LinkedBlockingQueue<>();
    AtomicInteger successCounter = new AtomicInteger(0);
    AtomicInteger failCounter = new AtomicInteger(0);
    ExecutorService producerPool = Executors.newCachedThreadPool();
    CountDownLatch producerRound = new CountDownLatch(200);
    CountDownLatch firstRound = new CountDownLatch(32000);
    CountDownLatch secondRound = new CountDownLatch(168000);
    SkiersApi skiersApi = new SkiersApi();
    skiersApi.getApiClient().setBasePath(BASE_PATH);
    skiersApi.getApiClient().setReadTimeout(50000);
    long startTime = System.currentTimeMillis();
    /* Producer process, produce 200*1k Skiers */
    for(int i=0; i<200; i++){
      producerPool.submit(new Part1ProducerTask(producerQueue, producerRound));
    }
    producerPool.shutdown();
    /* First Round, 32 threads each sends 1k requests */
    for(int i=0; i<32; i++){
      Thread thread = new Thread(new Part1ConsumerTask(producerQueue, successCounter, failCounter, skiersApi, firstRound, 1000));
      thread.start();
    }
    firstRound.await();
    /* Second Round, After first round's done, send the rest 168k requests */
    for(int i=0; i<168; i++){
      Thread thread = new Thread(new Part1ConsumerTask(producerQueue, successCounter, failCounter, skiersApi, secondRound, 1000));
      thread.start();
    }
    secondRound.await();
    producerRound.await();
    long endTime = System.currentTimeMillis();
    System.out.println("Num of successful requests sent: " + successCounter.get());
    System.out.println("Num of unsuccessful requests: " + failCounter.get());
    System.out.println(String.format("Total run time (wall time) %d ms", endTime-startTime));
    System.out.println("Total throughput in requests per second: "
                       + (successCounter.get() / ((double)(endTime-startTime)/1000)));
  }

}
