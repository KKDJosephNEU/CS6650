package Util;

import io.swagger.client.model.LiftRide;
import java.util.concurrent.ThreadLocalRandom;

public class Skier {

  private Integer resortID;
  private Integer skierId;
  private String seasonID;
  private String dayID;
  private LiftRide liftRide;

  public Skier(){
    this.resortID = ThreadLocalRandom.current().nextInt(1, 11);
    this.skierId = ThreadLocalRandom.current().nextInt(1, 100001);
    this.seasonID = "2022";
    this.dayID = String.valueOf(ThreadLocalRandom.current().nextInt(1, 367));
    this.liftRide = new LiftRide();
    this.liftRide.setLiftID(ThreadLocalRandom.current().nextInt(1, 41));
    this.liftRide.setTime(ThreadLocalRandom.current().nextInt(1, 361));
  }

  public Integer getResortID() {
    return resortID;
  }

  public void setResortID(Integer resortID) {
    this.resortID = resortID;
  }

  public Integer getSkierId() {
    return skierId;
  }

  public void setSkierId(Integer skierId) {
    this.skierId = skierId;
  }

  public String getSeasonID() {
    return seasonID;
  }

  public void setSeasonID(String seasonID) {
    this.seasonID = seasonID;
  }

  public String getDayID() {
    return dayID;
  }

  public void setDayID(String dayID) {
    this.dayID = dayID;
  }

  public LiftRide getLiftRide() {
    return liftRide;
  }

  public void setLiftRide(LiftRide liftRide) {
    this.liftRide = liftRide;
  }
}
