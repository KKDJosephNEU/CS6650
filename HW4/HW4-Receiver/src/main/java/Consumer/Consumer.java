package Consumer;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

public class Consumer {
  private static final String RABBIT_QUEUE = "RABBIT_QUEUE";
  private static final String Rabbit_EXCHANGE = "EXCHANGER";

  public static void main(String[] args) throws IOException, TimeoutException {
    ConnectionFactory connectionFactory = new ConnectionFactory();
//    connectionFactory.setHost("34.211.227.166");
//    connectionFactory.setHost("172.31.28.167");
    connectionFactory.setHost("35.93.134.85");
    connectionFactory.setPort(5672);
    connectionFactory.setUsername("admin");
    connectionFactory.setPassword("password");
    Connection connection = connectionFactory.newConnection();
    System.out.println("Start Connection. Waiting for new msg");
    ExecutorService consumerPool = Executors.newFixedThreadPool(128);
    for(int i=0; i<128; i++){
      consumerPool.submit(new ConsumerTask(connection, Rabbit_EXCHANGE, RABBIT_QUEUE));
    }
  }

}
