package sockets;

import java.util.Map;

import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import configs.App;
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
    String userId = ApplicationHelper.getUserIdBySession(App.userUsernameMap, session);
    App.broadcastMessage(sender = userId, msg = message);
  }
}