package Client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ConsumerTask implements Runnable{
  LinkedBlockingQueue<Integer> queue;
  LinkedBlockingQueue<Recorder> recorders;
  AtomicInteger primeCounter;
  CountDownLatch lock;
  int numOfPosts;
  String path;
  private static final HttpClient httpClient = HttpClient.newBuilder()
      .version(HttpClient.Version.HTTP_1_1)
      .connectTimeout(Duration.ofSeconds(10000))
      .build();

  public ConsumerTask(LinkedBlockingQueue<Integer> queue, String path, CountDownLatch lock, int num,
      LinkedBlockingQueue<Recorder> recorders, AtomicInteger primeCounter){
    this.queue = queue;
    this.lock = lock;
    this.numOfPosts = num;
    this.recorders =recorders;
    this.path = path;
    this.primeCounter = primeCounter;
  }

  @Override
  public void run() {
    for(int i=0; i<numOfPosts; i++){
      try{
        int num = queue.poll(2, TimeUnit.SECONDS);
        long startTime = System.currentTimeMillis();
        HttpResponse<String> res = sendRequest(path, num);
        long endTime = System.currentTimeMillis();
        if(res.statusCode() == 200) primeCounter.incrementAndGet();
        recorders.add(new Recorder(startTime, endTime, res.statusCode()));
      } catch (InterruptedException e){
        e.printStackTrace();
        throw new RuntimeException(e);
      } finally {
        lock.countDown();
      }
    }
  }

  private HttpResponse<String> sendRequest(String path, int num){
    HttpRequest request = HttpRequest.newBuilder()
        .GET()
        .uri(URI.create(path + num))
        .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
        .build();

    HttpResponse<String> response = null;
    try {
      response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
    return response;
  }
}
