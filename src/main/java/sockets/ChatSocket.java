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
  public void onConnect(Session user) throws Exception {
    System.out.println("1 ++++++++++++++++++++++++++++++++++++++++++++++++++");
    Map<String, String> queryParams = ApplicationHelper.MakePairs(user.getUpgradeRequest().getQueryString());
    System.out.println(queryParams);
    System.out.println("2 ++++++++++++++++++++++++++++++++++++++++++++++++++");
    String username = "User" + App.nextUserNumber++;
    App.userUsernameMap.put(user, username);
    App.broadcastMessage(sender = "Server", msg = (username + " joined the chat"));
  }

  @OnWebSocketClose
  public void onClose(Session user, int statusCode, String reason) {
    String username = App.userUsernameMap.get(user);
    App.userUsernameMap.remove(user);
    App.broadcastMessage(sender = "Server", msg = (username + " left the chat"));
  }

  @OnWebSocketMessage
  public void onMessage(Session user, String message) {
    App.broadcastMessage(sender = App.userUsernameMap.get(user), msg = message);
  }
}