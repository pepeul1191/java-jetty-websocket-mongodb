package handlers;

import spark.Request;
import spark.Response;
import spark.Route;
import org.json.JSONObject;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
import configs.Database;

public class ChatHandler{
  public static Route listar = (Request request, Response response) -> {
    String rpta = "";
    String userId = request.queryParams("user_id");
    String guestId = request.queryParams("guest_id");
    Database db = new Database();
    try {
      List<JSONObject> rptaTemp = new ArrayList<JSONObject>();
      BasicDBList messagesCursor = (BasicDBList) db.getDatastore().getDB().eval("getMessagesFunction('" + userId + "', '" + guestId + "')");
      System.out.println("1 ++++++++++++++++++++++++++++++++++++++++");
      System.out.println(messagesCursor);
      System.out.println("2 ++++++++++++++++++++++++++++++++++++++++");
      if(messagesCursor != null){
        for(Object messageObject : messagesCursor){
          DBObject message = (DBObject) messageObject;
          JSONObject obj = new JSONObject();
          //System.out.println("1 ++++++++++++++++++++++++++++++++");
          //System.out.println(message.get("moment"));
          //System.out.println("2 ++++++++++++++++++++++++++++++++");
          obj.put("content", message.get("content"));
          obj.put("moment", message.get("moment"));
          obj.put("user_id", message.get("userId"));
          rptaTemp.add(obj);
        }
        rpta = rptaTemp.toString();
      }else{
        rpta = "[]";
      }
    }catch (Exception e) {
      e.printStackTrace();
      String[] error = {"Se ha producido un error en listar los mensajes de la conversión", e.toString()};
      JSONObject rptaTry = new JSONObject();
      rptaTry.put("tipo_mensaje", "error");
      rptaTry.put("mensaje", error);
      rpta = rptaTry.toString();
      response.status(500);
    }
    return rpta;
  };
}