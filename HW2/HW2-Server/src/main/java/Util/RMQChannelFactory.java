package Util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class RMQChannelFactory extends BasePooledObjectFactory<Channel> {
  private static Connection connection;

  public RMQChannelFactory() throws IOException, TimeoutException {
    ConnectionFactory connectionFactory = new ConnectionFactory();
//    connectionFactory.setHost("34.211.227.166");
    connectionFactory.setHost("172.31.28.167");
    connectionFactory.setPort(5672);
    connectionFactory.setUsername("admin");
    connectionFactory.setPassword("password");
    connection = connectionFactory.newConnection();

  }


  @Override
  public Channel create() throws Exception {
    return connection.createChannel();
  }

  @Override
  public PooledObject<Channel> wrap(Channel channel) {
    return new DefaultPooledObject<>(channel);
  }
}
