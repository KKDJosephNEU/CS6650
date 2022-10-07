import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SkierServlet")
public class SkierServlet extends HttpServlet {

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

    if (!isUrlValid(urlParts)) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      res.getWriter().write(gson.toJson("400 Bad Request"));
    } else {
      res.setStatus(HttpServletResponse.SC_CREATED);
      // do any sophisticated processing with urlParts which contains all the url params
      // TODO: process url params in `urlParts`
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

    if (!isUrlValid(urlParts)) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      res.getWriter().write(gson.toJson("400 Bad Request"));
    } else {
      res.setStatus(HttpServletResponse.SC_OK);
      // do any sophisticated processing with urlParts which contains all the url params
      // TODO: process url params in `urlParts`
      res.getWriter().write(gson.toJson("200 It Works"));
    }
  }

  private boolean isUrlValid(String[] urlPath) {
    return urlPath.length == 8 && allIntegerCheck(urlPath[1]) && urlPath[2].equals("seasons")
           && allIntegerCheck(urlPath[3]) && urlPath[4].equals("days")
           && allIntegerCheck(urlPath[5]) && dayCheck(urlPath[5]) && urlPath[6].equals("skiers")
           && allIntegerCheck(urlPath[7]);
  }

  private boolean allIntegerCheck(String str){
    for(char ch : str.toCharArray()){
      if(!Character.isDigit(ch)) return false;
    }
    return true;
  }

  private boolean dayCheck(String str){
    if(str == null || str.length() == 0 || str.length() > 3) return false;
    int day = Integer.parseInt(str);
    return day >= 1 && day <= 366;
  }
}
