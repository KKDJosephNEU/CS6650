package Util;

public class StringUtil {
/*
* POST
*   /skiers/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}/lift/{liftID}/{time}
* GET
*   /resorts/{resortID}/seasons/{seasonID}/day/{dayID}/skiers             ==> 1
*   /skiers/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}   ==> 2
*   /skiers/{skierID}/vertical                                            ==> 3
*/

  public boolean isPOSTUrlValid(String[] urlPath) {
    return urlPath.length == 11 && allIntegerCheck(urlPath[1]) && urlPath[2].equals("seasons")
        && allIntegerCheck(urlPath[3]) && urlPath[4].equals("days") && allIntegerCheck(urlPath[5])
        && dayCheck(urlPath[5]) && urlPath[6].equals("skiers") && allIntegerCheck(urlPath[7])
        && urlPath[8].equals("lift") && allIntegerCheck(urlPath[9]) && allIntegerCheck(urlPath[10])
        && timeCheck(urlPath[10]);
  }

  public int isGETUrlValid(String[] urlPath) {
    if(urlPath.length < 3 || urlPath.length > 8) return 0;
    if(urlPath.length == 3){
      if(allIntegerCheck(urlPath[1]) && urlPath[2].equals("vertical")) return 3;
      else return 0;
    }
    else if(urlPath.length == 7){
      if(allIntegerCheck(urlPath[1]) && urlPath[2].equals("seasons")
         && allIntegerCheck(urlPath[3]) && urlPath[4].equals("day") && allIntegerCheck(urlPath[5])
         && urlPath[6].equals("skiers")) return 1;
      else return 0;
    }
    else if(urlPath.length == 8){
      if(allIntegerCheck(urlPath[1]) && urlPath[2].equals("seasons")
         && allIntegerCheck(urlPath[3]) && urlPath[4].equals("days") && allIntegerCheck(urlPath[5])
         && dayCheck(urlPath[5]) && urlPath[6].equals("skiers") && allIntegerCheck(urlPath[7])) return 2;
      else return 0;
    }
    else return 0;
  }

  public boolean allIntegerCheck(String str){
    for(char ch : str.toCharArray()){
      if(!Character.isDigit(ch)) return false;
    }
    return true;
  }

  public boolean dayCheck(String str){
    if(str == null || str.length() == 0 || str.length() > 3) return false;
    int day = Integer.parseInt(str);
    return day >= 1 && day <= 366;
  }

  public boolean timeCheck(String str){
    if(str == null || str.length() == 0 || str.length() > 3) return false;
    int time = Integer.parseInt(str);
    return time >= 1 && time <= 360;
  }

}
