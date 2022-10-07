package Part1;

import Util.Skier;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

public class Part1ProducerTask implements Runnable{
  LinkedBlockingQueue<Skier> queue;
  CountDownLatch lock;
  public Part1ProducerTask(LinkedBlockingQueue<Skier> queue, CountDownLatch lock){
    this.queue = queue;
    this.lock = lock;
  }

  @Override
  public void run() {
    for(int i=0; i<1000; i++)
      queue.add(new Skier());
    lock.countDown();
  }
}
