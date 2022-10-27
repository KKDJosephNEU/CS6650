package Util;

public class StringUtil {

  public boolean isUrlValid(String[] urlPath) {
    return urlPath.length == 8 && allIntegerCheck(urlPath[1]) && urlPath[2].equals("seasons")
           && allIntegerCheck(urlPath[3]) && urlPath[4].equals("days")
           && allIntegerCheck(urlPath[5]) && dayCheck(urlPath[5]) && urlPath[6].equals("skiers")
           && allIntegerCheck(urlPath[7]);
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

}
