package Server;

import DAO.LiftRideDao;
import Util.StringUtil;
import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ResortServlet")
public class ResortServlet extends HttpServlet {
  private StringUtil stringUtil = new StringUtil();
  private LiftRideDao liftRideDao = new LiftRideDao();

  protected void doPost(HttpServletRequest request,
      HttpServletResponse response)
      throws ServletException, IOException {

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
    int check = stringUtil.isGETUrlValid(urlParts);
    if (check != 1) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      res.getWriter().write(gson.toJson("400 Bad Request"));
    } else {
      res.setStatus(HttpServletResponse.SC_OK);
      String result = "";
      int resortID = Integer.parseInt(urlParts[1]);
      int seasonID = Integer.parseInt(urlParts[3]);
      int dayID = Integer.parseInt(urlParts[5]);
      int numOfSkiers = liftRideDao.getNumberOfSkiers(resortID, seasonID, dayID);
      result += numOfSkiers + " unique skiers ski in resort " + resortID + " season " + seasonID + " day " + dayID;
      res.getWriter().write(gson.toJson(result));
    }

  }
}
