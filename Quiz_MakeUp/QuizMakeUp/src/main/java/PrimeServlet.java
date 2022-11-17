import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "PrimeServlet", value = "/prime/*")
public class PrimeServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    res.setContentType("text/plain");
    String urlPath = req.getPathInfo();

    // check we have a URL!
    if (urlPath == null || urlPath.isEmpty()) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      res.getWriter().write("missing paramterers");
      return;
    }

    String[] urlParts = urlPath.split("/");

    if (!isUrlValid(urlParts)) {
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      res.getWriter().write("It's not a prime number between 1 and 10000");
    } else {
      res.setStatus(HttpServletResponse.SC_OK);
      res.getWriter().write("It works!");
    }
  }

  private boolean isUrlValid(String[] urlPath) {
    if (urlPath[1] == null || !urlPath[1].chars().allMatch(Character :: isDigit)) return false;

    int num = Integer.parseInt(urlPath[1]);
    if (num < 1 || num > 10000 || !isPrime(num)) {
      return false;
    }
    return true;
  }

  private boolean isPrime(int num){
    if(num == 1 || num % 2 == 0 && num != 2) return false;
    for(int i=3; i<Math.sqrt(num); i+=2){
      if(num % i == 0) return false;
    }
    return true;
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

  }
}