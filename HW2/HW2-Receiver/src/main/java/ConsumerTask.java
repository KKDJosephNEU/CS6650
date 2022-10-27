import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ConsumerTask implements Runnable{
  private static Connection connection;
  private static String EXCHANGE;
  private static String QUEUE;
  private static ConcurrentHashMap<Integer, List<String>> map;
  public ConsumerTask(Connection connection, String exchangeName, String queueName, ConcurrentHashMap<Integer, List<String>> map){
    ConsumerTask.connection = connection;
    EXCHANGE = exchangeName;
    QUEUE = queueName;
    ConsumerTask.map = map;
  }

  @Override
  public void run() {
    for(int i=0; i<10; i++){
      try {
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE, "fanout");
        channel.queueDeclare(QUEUE, false, false, false, null);
        channel.queueBind(QUEUE, EXCHANGE, "");
        channel.basicQos(1);
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
          String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
          Gson gson = new Gson();
          LiftRide liftRide = gson.fromJson(message, LiftRide.class);
          System.out.println(message);
          List<String> msgList = map.getOrDefault(liftRide.getSkierID(), new ArrayList<String>());
          msgList.add(message);
          map.put(liftRide.getSkierID(), msgList);
          channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        channel.basicConsume(QUEUE, false, deliverCallback, consumerTag -> {});
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
