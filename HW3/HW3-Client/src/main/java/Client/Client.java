package Client;

import Util.Recorder;
import Util.Skier;
import com.opencsv.CSVWriter;
import io.swagger.client.api.SkiersApi;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Client {

  private static final String BASE_PATH = "http://54.218.104.140:8080/HW3-Server_war";
  private static final String LOCAL_PATH = "http://localhost:8080/HW3_Server_war_exploded";
  private static final String LB_PATH = "http://cs6650-lb2-1427586006.us-west-2.elb.amazonaws.com/HW3-Server_war";
  private static final String CSV_PATH = "D:/Desktop_D/CS6650/HW/HW3/HW3-Client/src/main/java/Result/result.csv";

  public static void writeCSV(List<Recorder> recorders){
    try {
      CSVWriter writer = new CSVWriter(new FileWriter(CSV_PATH));
      String[] header = {"Start Time", "End Time", "Request Type", "Latency", "Response Code"};
      writer.writeNext(header);
      SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
      for(Recorder recorder : recorders){
        String[] temp = new String[5];
        temp[0] = ft.format(new Date(recorder.getStartTime())) + "\t";
        temp[1] = ft.format(new Date(recorder.getEndTime())) + "\t";
        temp[0] = recorder.getStartTime() + "\t";
        temp[1] = recorder.getEndTime() + "\t";
        temp[2] = "POST";
        temp[3] = recorder.getLatency() + "\t";
        temp[4] = String.valueOf(recorder.getCode());
        writer.writeNext(temp);
      }
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public static void main(String[] args) throws InterruptedException {
    LinkedBlockingQueue<Skier> producerQueue = new LinkedBlockingQueue<>();
    LinkedBlockingQueue<Recorder> recorders = new LinkedBlockingQueue<>();
    AtomicInteger successCounter = new AtomicInteger(0);
    AtomicInteger failCounter = new AtomicInteger(0);
    ExecutorService producerPool = Executors.newCachedThreadPool();
    CountDownLatch producerRound = new CountDownLatch(200);
    CountDownLatch firstRound = new CountDownLatch(8000);
    CountDownLatch secondRound = new CountDownLatch(4000);
    CountDownLatch thirdRound = new CountDownLatch(16000);
    SkiersApi skiersApi = new SkiersApi();
    skiersApi.getApiClient().setBasePath(LB_PATH);
    skiersApi.getApiClient().setReadTimeout(38400000);
    long startTime = System.currentTimeMillis();
    /* Producer process, produce 200*1k Skiers */
    for(int i=0; i<200; i++){
      producerPool.submit(new ProducerTask(producerQueue, producerRound));
    }
    producerPool.shutdown();

    /* First 5 Rounds, 8 threads each sends 1k requests */
    for(int round=0; round<5; round++){
      for(int i=0; i<8; i++){
        Thread thread = new Thread(new ConsumerTask(producerQueue, successCounter, failCounter, skiersApi, firstRound, 1000, recorders));
        thread.start();
      }
      firstRound.await();
      TimeUnit.SECONDS.sleep(8);
    }

    /* Second 10 Rounds, 4 threads each sends 1k requests */
    for(int round=0; round<10; round++){
      for(int i=0; i<4; i++){
        Thread thread = new Thread(new ConsumerTask(producerQueue, successCounter, failCounter, skiersApi, secondRound, 1000, recorders));
        thread.start();
      }
      secondRound.await();
      TimeUnit.SECONDS.sleep(8);
    }

    TimeUnit.SECONDS.sleep(5);

    /* Third 5 Rounds, 8 threads each sends 1k requests */
    for(int round=0; round<5; round++){
      for(int i=0; i<8; i++){
        Thread thread = new Thread(new ConsumerTask(producerQueue, successCounter, failCounter, skiersApi, firstRound, 1000, recorders));
        thread.start();
      }
      firstRound.await();
      TimeUnit.SECONDS.sleep(8);
    }

    TimeUnit.SECONDS.sleep(5);

    /* Fourth 5 Rounds, 16 threads each sends 1k requests */
    for(int round=0; round<5; round++){
      for(int i=0; i<16; i++){
        Thread thread = new Thread(new ConsumerTask(producerQueue, successCounter, failCounter, skiersApi, thirdRound, 1000, recorders));
        thread.start();
      }
      thirdRound.await();
      TimeUnit.SECONDS.sleep(8);
    }

    producerRound.await();
    long endTime = System.currentTimeMillis();

    /* Calculate the performance and write csv file*/
    List<Recorder> recorderList = new ArrayList<>();
    long sum = 0;
    for(Recorder recorder : recorders) {
      recorderList.add(recorder);
      sum += recorder.getLatency();
    }

    writeCSV(recorderList);

    int index = (int) Math.ceil(99 / 100.0 * recorderList.size()) - 1;
    long p99Latency = recorderList.get(index).getLatency();

    Collections.sort(recorderList, (r1, r2) -> (int) (r1.getLatency() - r2.getLatency()));

    long minLatency = recorderList.get(0).getLatency();
    long maxLatency = recorderList.get(recorderList.size()-1).getLatency();
    long medianLatency = recorderList.get(recorderList.size()/2).getLatency();
    double meanLatency = (double) sum / recorderList.size();

    System.out.println("mean response time (milliseconds): " + meanLatency);
    System.out.println("median response time (milliseconds): " + medianLatency);
    System.out.println("throughput (requests/second): " + (successCounter.get() / ((double)(endTime-startTime)/1000)));
    System.out.println("p99 (99th percentile) response time: " + p99Latency);
    System.out.println("min response time (milliseconds): " + minLatency);
    System.out.println("max response time (milliseconds): " + maxLatency);
  }

}
