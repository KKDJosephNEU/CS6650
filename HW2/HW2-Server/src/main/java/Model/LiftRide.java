package Model;

public class LiftRide {
  int resortID;
  int seasonID;
  int dayID;
  int skierID;
  public LiftRide(String[] urlParts){
    resortID = Integer.parseInt(urlParts[1]);
    seasonID = Integer.parseInt(urlParts[3]);
    dayID = Integer.parseInt(urlParts[5]);
    skierID = Integer.parseInt(urlParts[7]);
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
