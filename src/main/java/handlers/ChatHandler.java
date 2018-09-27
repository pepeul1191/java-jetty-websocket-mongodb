package handlers;

import spark.Request;
import spark.Response;
import spark.Route;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import configs.Database;
import org.bson.types.ObjectId;
import models.Conversation;
import models.Message;

public class ChatHandler{
  public static Route listar = (Request request, Response response) -> {
    String rpta = "";
    ObjectId convesationObjectId = new ObjectId(request.params(":conversation_id"));
    Database db = new Database();
    try {
      List<JSONObject> rptaTemp = new ArrayList<JSONObject>();
      Conversation c = db.getDatastore().find(Conversation.class).field("_id").equal(convesationObjectId).get();
      List<Message> rptaList = c.getMessages();
      for (Message message : rptaList) {
        JSONObject obj = new JSONObject();
        obj.put("content", message.getContent());
        obj.put("moment", message.getMoment());
        obj.put("user_id", message.getUserId());
        rptaTemp.add(obj);
      }
      rpta = rptaTemp.toString();
    }catch (Exception e) {
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