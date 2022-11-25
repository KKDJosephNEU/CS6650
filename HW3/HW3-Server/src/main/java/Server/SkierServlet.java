package Server;

import Model.LiftRide;
import Util.RMQChannelFactory;
import Util.StringUtil;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

@WebServlet(name = "Server.SkierServlet")
public class SkierServlet extends HttpServlet {
  private static final String RABBIT_QUEUE = "RABBIT_QUEUE";
  private static final String Rabbit_EXCHANGE = "EXCHANGER";
  private ObjectPool<Channel> channelObjectPool;
  private StringUtil stringUtil = new StringUtil();

  @Override
  public void init(){

    try {
      channelObjectPool = new GenericObjectPool<>(new RMQChannelFactory());
    } catch (IOException | TimeoutException e) {
      e.printStackTrace();
    }
  }

  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    res.setContentType("application/json");
    String urlPath = req.getPathInfo();
    Gson gson = new Gson();

    // check we have a URL!
    if (urlPath == null || urlPath.isEmpty()) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      res.getWriter().write(gson.toJson("404 Not Found"));
      return;
    }

    String[] urlParts = urlPath.split("/");
    // and now validate url path and return the response status code
    // (and maybe also some value if input is valid)
    if (!stringUtil.isUrlValid(urlParts)) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      res.getWriter().write(gson.toJson("400 Bad Request"));
    } else {
      LiftRide liftRide = new LiftRide(urlParts);
      try {
        Channel channel = channelObjectPool.borrowObject();
        channel.exchangeDeclare(Rabbit_EXCHANGE, "fanout");
        channel.basicPublish(Rabbit_EXCHANGE, "", null, gson.toJson(liftRide).getBytes(
            StandardCharsets.UTF_8));
        channelObjectPool.returnObject(channel);
      } catch (Exception e) {
        e.printStackTrace();
      }
      res.setStatus(HttpServletResponse.SC_CREATED);
      res.getWriter().write(gson.toJson("201 Write Successful"));
    }
  }

  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    res.setContentType("application/json");
    String urlPath = req.getPathInfo();
    Gson gson = new Gson();

    // check we have a URL!
    if (urlPath == null || urlPath.isEmpty()) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      res.getWriter().write(gson.toJson("404 Not Found"));
      return;
    }
    String[] urlParts = urlPath.split("/");
    if (!stringUtil.isUrlValid(urlParts)) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      res.getWriter().write(gson.toJson("400 Bad Request"));
    } else {
      res.setStatus(HttpServletResponse.SC_OK);
      // do any sophisticated processing with urlParts which contains all the url params
      // TODO: process url params in `urlParts`
      res.getWriter().write(gson.toJson("200 It Works"));
    }
  }


}
