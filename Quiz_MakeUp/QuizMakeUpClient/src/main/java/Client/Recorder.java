package Client;

public class Recorder {
  private long startTime;
  private long endTime;
  private int code;
  private long latency;

  public Recorder(long startTime, long endTime, int code){
    this.startTime = startTime;
    this.endTime = endTime;
    this.code = code;
    this.latency = endTime-startTime;
  }

  public long getStartTime() {
    return startTime;
  }

  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }

  public long getEndTime() {
    return endTime;
  }

  public void setEndTime(long endTime) {
    this.endTime = endTime;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public long getLatency() {
    return latency;
  }

  public void setLatency(long latency) {
    this.latency = latency;
  }
}
