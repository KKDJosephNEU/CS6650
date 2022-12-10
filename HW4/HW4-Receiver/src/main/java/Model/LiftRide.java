package Model;

public class LiftRide {
  int resortID;
  int seasonID;
  int dayID;
  int skierID;
  int liftID;
  int time;
  public LiftRide(String[] urlParts){
    resortID = Integer.parseInt(urlParts[1]);
    seasonID = Integer.parseInt(urlParts[3]);
    dayID = Integer.parseInt(urlParts[5]);
    skierID = Integer.parseInt(urlParts[7]);
    liftID = Integer.parseInt(urlParts[9]);
    time = Integer.parseInt(urlParts[10]);
  }

  public int getLiftID() {
    return liftID;
  }

  public void setLiftID(int liftID) {
    this.liftID = liftID;
  }

  public int getTime() {
    return time;
  }

  public void setTime(int time) {
    this.time = time;
  }

  public int getResortID() {
    return resortID;
  }

  public void setResortID(int resortID) {
    this.resortID = resortID;
  }

  public int getSeasonID() {
    return seasonID;
  }

  public void setSeasonID(int seasonID) {
    this.seasonID = seasonID;
  }

  public int getDayID() {
    return dayID;
  }

  public void setDayID(int dayID) {
    this.dayID = dayID;
  }

  public int getSkierID() {
    return skierID;
  }

  public void setSkierID(int skierID) {
    this.skierID = skierID;
  }
}
