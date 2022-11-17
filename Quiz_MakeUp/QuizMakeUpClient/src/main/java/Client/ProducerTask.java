package Client;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerTask implements Runnable{
  LinkedBlockingQueue<Integer> queue;
  CountDownLatch lock;
  public ProducerTask(LinkedBlockingQueue<Integer> queue, CountDownLatch lock){
    this.queue = queue;
    this.lock = lock;
  }

  @Override
  public void run() {
    for(int i=0; i<1000; i++)
      queue.add(oddNum(1, 10000));
    lock.countDown();
  }

  private int oddNum(int min, int max){
    if (max % 2 == 0) --max;
    if (min % 2 == 0) ++min;
    return min + 2 * (int)(Math.random() * ((max - min) / 2 + 1));
  }
}
