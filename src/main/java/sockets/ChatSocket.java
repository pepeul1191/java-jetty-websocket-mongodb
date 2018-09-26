package sockets;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import com.mongodb.BasicDBList;
import models.Conversation;
import models.Message;
import org.bson.types.ObjectId;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import configs.App;
import configs.Database;
import helpers.ApplicationHelper;

@WebSocket
public class ChatSocket {
  private String sender, msg;

  @OnWebSocketConnect
  public void onConnect(Session session) throws Exception {
    Map<String, String> queryParams = ApplicationHelper.MakePairs(session.getUpgradeRequest().getQueryString());
    String userId = queryParams.get("user_id");
    App.userUsernameMap.put(userId, session);
    App.broadcastMessage(sender = "Server", msg = (session + " joined the chat"));
  }

  @OnWebSocketClose
  public void onClose(Session session, int statusCode, String reason) {
    String userId = ApplicationHelper.getUserIdBySession(App.userUsernameMap, session);
    App.userUsernameMap.remove(userId);
    App.broadcastMessage(sender = "Server", msg = (userId + " left the chat"));
  }

  @OnWebSocketMessage
  public void onMessage(Session session, String message) {
    Map<String, String> queryParams = ApplicationHelper.MakePairs(session.getUpgradeRequest().getQueryString());
    JSONObject data = new JSONObject(message);
    String userId = data.getString("user_id");
    String guestId = data.getString("guest_id");
    String action = data.getString("action");
    if(action.equalsIgnoreCase("chatMessage")){
      Database db = new Database();
      try{
        String chatMessage = data.getString("message");
        ObjectId conversationObjectId = (ObjectId) db.getDatastore().getDB().eval("getIdConversacionFunction('" + userId + "', '" + guestId + "')");
        if(conversationObjectId == null){
          // create members
          List<ObjectId> members = new ArrayList<>();
          ObjectId mb1 = new ObjectId(userId);
          ObjectId mb2 = new ObjectId(guestId);
          members.add(mb1);
          members.add(mb2);
          // create messages
          List<Message> messages = new ArrayList<>();
          Message m1 = new Message(chatMessage, userId);
          messages.add(m1);
          // create conversation
          Conversation c1 = new Conversation();
          c1.setMessages(messages);
          c1.setMembers(members);
          c1.setCreated();
          // persist document
          db.getDatastore().save(c1);
        }else{
          //myCursor.get(0);
        }
      }catch(Exception e){
        e.printStackTrace();
      }
    }
    //String userId = ApplicationHelper.getUserIdBySession(App.userUsernameMap, session);
    App.broadcastMessage(sender = userId, msg = message);
  }
}