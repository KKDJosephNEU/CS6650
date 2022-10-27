package Util;

import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;

public class test {
  final private static int NUM_REQUESTS = 10_000;

  public static void main(String[] args) throws ApiException {

    // Method 1
    long start = System.currentTimeMillis();
    SkiersApi skiersApi = new SkiersApi();
    //skiersApi.getApiClient().setBasePath("http://localhost:8080/HW1_Server_war_exploded");
    skiersApi.getApiClient().setBasePath("http://54.218.104.140:8080/HW1-Server_war");
    for (int i = 0; i < NUM_REQUESTS; i++) {
      LiftRide liftRide = new LiftRide();
      Integer resortID = 1;
      String seasonID = "2022";
      String dayID = "1";
      Integer skierID = 1;
      ApiResponse res = skiersApi.writeNewLiftRideWithHttpInfo(liftRide, resortID, seasonID, dayID, skierID);
      System.out.println(i + " " + res.getStatusCode());
    }
    long end = System.currentTimeMillis();
    System.out.println("Time elapsed for " + NUM_REQUESTS + " post requests is: " + (end - start) + "ms");
    System.out.println("Time per request is: " + (double) ((end - start)) / NUM_REQUESTS + " ms");


  }
}