package ConnectionManager;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import org.apache.commons.dbcp2.BasicDataSource;

public class DBCPDataSource {
  private static BasicDataSource dataSource;

  static {
    // https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-jdbc-url-format.html
    dataSource = new BasicDataSource();
    Properties props = new Properties();
    try {
      props.load(Objects.requireNonNull(
          DBCPDataSource.class.getClassLoader().getResourceAsStream("setting.properties")));
    } catch (IOException e) {
      e.printStackTrace();
    }
    // NEVER store sensitive information below in plain text!
    String HOST_NAME = props.getProperty("MySQL_IP_ADDRESS");
    String PORT = props.getProperty("MySQL_PORT");
    String USERNAME = props.getProperty("DB_USERNAME");
    String PASSWORD = props.getProperty("DB_PASSWORD");
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    String DATABASE = "cs6650HYD";
    String url = String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC", HOST_NAME, PORT,
        DATABASE);
    dataSource.setUrl(url);
    dataSource.setUsername(USERNAME);
    dataSource.setPassword(PASSWORD);
    dataSource.setInitialSize(10);
    dataSource.setMaxTotal(60);
  }

  public static BasicDataSource getDataSource() {
    return dataSource;
  }
}
