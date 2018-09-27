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
        String conversationId = (String) db.getDatastore().getDB().eval("getIdConversacionFunction('" + userId + "', '" + guestId + "')");
        if(conversationId == null){
          // create members
          List<ObjectId> members = new ArrayList<>();
          ObjectId userObjectId = new ObjectId(userId);
          ObjectId guestObjectId = new ObjectId(guestId);
          members.add(userObjectId);
          members.add(guestObjectId);
          // create messages
          List<Message> messages = new ArrayList<>();
          Message m = new Message(chatMessage, userId);
          messages.add(m);
          // create conversation
          Conversation c = new Conversation();
          c.setMessages(messages);
          c.setMembers(members);
          c.setCreated();
          // persist document
          db.getDatastore().save(c);
        }else{
          // update conversation in DB
          ObjectId conversationObjectId = new ObjectId(conversationId);
          Conversation c = db.getDatastore().find(Conversation.class).field("_id").equal(conversationObjectId).get();
          Message m = new Message(chatMessage, userId);
          c.getMessages().add(m);
          db.getDatastore().save(c);
          // prepare response message
          System.out.println("1 ++++++++++++++++++++++++++++++++++++++++++++++++++++++");
          JSONObject rptaMessage = new JSONObject();
          rptaMessage.put("sender_id", userId);
          rptaMessage.put("guest_id", guestId);
          rptaMessage.put("conversation_id", conversationId);
          rptaMessage.put("message", chatMessage);
          rptaMessage.put("moment", m.getMoment());
          System.out.println("2 ++++++++++++++++++++++++++++++++++++++++++++++++++++++");
          App.sendMessage(rptaMessage);
        }
      }catch(Exception e){
        e.printStackTrace();
      }
    }
    //String userId = ApplicationHelper.getUserIdBySession(App.userUsernameMap, session);
  }
}