package helpers;

import java.util.HashMap;
import java.util.Map;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public abstract class ApplicationHelper {
  private Config constants = ConfigFactory.defaultApplication();
  
  public String getConfValue(String key){
    return constants.getString(key);
  }
  
  public static Map<String, String> MakePairs(String input){
    Map<String, String> retVal = new HashMap<>();
    int fromIndex = 0;
    int toIndex = 0;
    while (toIndex != -1){
      String key = "";
      String value = "";
      toIndex = input.indexOf('=', fromIndex);
      if (toIndex - fromIndex > 1){
        key = input.substring(fromIndex, toIndex);
        fromIndex = toIndex + 1;
        toIndex = input.indexOf('&', fromIndex);
        if (toIndex == -1){
            value = input.substring(fromIndex, input.length());
        } else{
            value = input.substring(fromIndex, toIndex);
        }
        retVal.put(key, value);
        fromIndex = toIndex + 1;
      } else{
        fromIndex = input.indexOf('&', toIndex) + 1;
      }
    }
    return retVal;
  }
}
