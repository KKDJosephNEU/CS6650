package Client;

import Util.Recorder;
import Util.Skier;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.SkiersApi;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ConsumerTask implements Runnable{
  LinkedBlockingQueue<Skier> queue;
  LinkedBlockingQueue<Recorder> recorders;
  AtomicInteger successCounter;
  AtomicInteger failCounter;
  SkiersApi skiersApi;
  CountDownLatch lock;
  int numOfPosts;

  public ConsumerTask(LinkedBlockingQueue<Skier> queue, AtomicInteger successCounter,
      AtomicInteger failCounter, SkiersApi skiersApi, CountDownLatch lock, int num,
      LinkedBlockingQueue<Recorder> recorders){
    this.queue = queue;
    this.successCounter = successCounter;
    this.failCounter = failCounter;
    this.skiersApi = skiersApi;
    this.lock = lock;
    this.numOfPosts = num;
    this.recorders =recorders;
  }

  @Override
  public void run() {
    for(int i=0; i<numOfPosts; i++){
      try{
        Skier skier = queue.poll(2, TimeUnit.SECONDS);
        long startTime = System.currentTimeMillis();
        ApiResponse<Void> res = skiersApi.writeNewLiftRideWithHttpInfo(skier.getLiftRide(), skier.getResortID(), skier.getSeasonID(), skier.getDayID(), skier.getSkierId());
        long endTime = System.currentTimeMillis();
        int failNum = 0;
        while(failNum < 5 && res.getStatusCode() != 201){
          res = skiersApi.writeNewLiftRideWithHttpInfo(skier.getLiftRide(), skier.getResortID(), skier.getSeasonID(), skier.getDayID(), skier.getSkierId());
          failNum++;
        }
        if(res.getStatusCode() != 201) failCounter.incrementAndGet();

        else {
          successCounter.incrementAndGet();
          recorders.add(new Recorder(startTime, endTime, res.getStatusCode()));
        }
      } catch (ApiException | InterruptedException e){
        e.printStackTrace();
        throw new RuntimeException(e);
      } finally {
        lock.countDown();
      }
    }
    try {
      TimeUnit.SECONDS.sleep(5);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
