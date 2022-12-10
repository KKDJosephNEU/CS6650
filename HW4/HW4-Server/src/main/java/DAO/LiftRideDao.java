package DAO;

import ConnectionManager.DBCPDataSource;
import Model.LiftRide;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;

public class LiftRideDao {
  private static BasicDataSource dataSource;

  public LiftRideDao() {
    dataSource = DBCPDataSource.getDataSource();
  }

  public void createLiftRide(LiftRide newLiftRide) {
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    String insertQueryStatement = "INSERT INTO LiftRides_HYD (skierId, resortId, seasonId, dayId, time, liftId) " +
                                  "VALUES (?,?,?,?,?,?)";
    try {
      conn = dataSource.getConnection();
      preparedStatement = conn.prepareStatement(insertQueryStatement);
      preparedStatement.setInt(1, newLiftRide.getSkierID());
      preparedStatement.setInt(2, newLiftRide.getResortID());
      preparedStatement.setInt(3, newLiftRide.getSeasonID());
      preparedStatement.setInt(4, newLiftRide.getDayID());
      preparedStatement.setInt(5, newLiftRide.getTime());
      preparedStatement.setInt(6, newLiftRide.getLiftID());

      // execute insert SQL statement
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (conn != null) {
          conn.close();
        }
        if (preparedStatement != null) {
          preparedStatement.close();
        }
      } catch (SQLException se) {
        se.printStackTrace();
      }
    }
  }

  // /resorts/{resortID}/seasons/{seasonID}/day/{dayID}/skiers
  public int getNumberOfSkiers(int resortID, int seasonID, int dayID) {
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    int number = 0;
    String selectQueryStatement = "SELECT count(distinct skierID) as skierNum from LiftRides_HYD\n"
                                  + "where resortID = "+ resortID + "\n"
                                  + "and seasonID = "+ seasonID + "\n"
                                  + "and dayID = "+ dayID + ";";
    try {
      conn = dataSource.getConnection();
      preparedStatement = conn.prepareStatement(selectQueryStatement);

      // execute select SQL statement
      ResultSet resultSet = preparedStatement.executeQuery();
      if(resultSet.next()){
        number = resultSet.getInt("skierNum");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (conn != null) {
          conn.close();
        }
        if (preparedStatement != null) {
          preparedStatement.close();
        }
      } catch (SQLException se) {
        se.printStackTrace();
      }
    }
    return number;
  }

  // /skiers/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}
  public int getSkiersVertical(int resortID, int seasonID, int dayID, int skierID) {
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    int number = 0;
    String selectQueryStatement = "SELECT sum(liftID) * 10 as vertical from LiftRides_HYD\n"
                                  + "where resortID = "+ resortID + "\n"
                                  + "and seasonID = "+ seasonID + "\n"
                                  + "and dayID = "+ dayID + "\n"
                                  + "and skierID = "+ skierID + ";";
    try {
      conn = dataSource.getConnection();
      preparedStatement = conn.prepareStatement(selectQueryStatement);

      // execute select SQL statement
      ResultSet resultSet = preparedStatement.executeQuery();
      if(resultSet.next()){
        number = resultSet.getInt("vertical");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (conn != null) {
          conn.close();
        }
        if (preparedStatement != null) {
          preparedStatement.close();
        }
      } catch (SQLException se) {
        se.printStackTrace();
      }
    }
    return number;
  }

  // /skiers/{skierID}/vertical
  public int getSkiersTotalVertical(int skierID) {
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    int number = 0;
    String selectQueryStatement = "SELECT sum(liftID) * 10 as vertical from LiftRides_HYD\n"
                                  + "where skierID = "+ skierID + ";";
    try {
      conn = dataSource.getConnection();
      preparedStatement = conn.prepareStatement(selectQueryStatement);

      // execute select SQL statement
      ResultSet resultSet = preparedStatement.executeQuery();
      if(resultSet.next()){
        number = resultSet.getInt("vertical");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (conn != null) {
          conn.close();
        }
        if (preparedStatement != null) {
          preparedStatement.close();
        }
      } catch (SQLException se) {
        se.printStackTrace();
      }
    }
    return number;
  }

}
