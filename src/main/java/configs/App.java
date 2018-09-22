package configs;

import static spark.Spark.exception;
import static spark.Spark.staticFiles;
import java.util.Map;
import org.eclipse.jetty.websocket.api.Session;
import java.util.concurrent.ConcurrentHashMap;
import static spark.Spark.port;
import static spark.Spark.options;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.webSocket;
import configs.FilterHandler;
import sockets.ChatSocket;
import static j2html.TagCreator.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONObject;

public class App {
	public static Map<Session, String> userUsernameMap = new ConcurrentHashMap<>();
	public static int nextUserNumber = 1;

  public static void main(String args[]){
    exception(Exception.class, (e, req, res) -> e.printStackTrace());
		staticFiles.location("/public");
		staticFiles.header("Access-Control-Allow-Origin", "*");
		staticFiles.header("Access-Control-Request-Method",  "*");
		staticFiles.header("Access-Control-Allow-Headers",  "*");
		//staticFiles.expireTime(600);
		webSocket("/chat", ChatSocket.class);
		//puerto
		port(4000);
		//CORS
		options("/*", (request, response) -> {
			String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
			if (accessControlRequestHeaders != null) {
				response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
			}
			String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
			if (accessControlRequestMethod != null) {
				response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
			}
			return "OK";
		});
		//filters
		before("*", FilterHandler.setHeaders);
		before("*", FilterHandler.ambinteLogs);
		//ruta de test/conexion
		get("/test/conexion", (request, response) -> {
			return "Conxión OK";
		});
	}
	//
	public static void broadcastMessage(String sender, String message) {
		userUsernameMap.keySet().stream().filter(Session::isOpen).forEach(session -> {
			try {
				String mensaje = String.valueOf(
					new JSONObject()
					  .put("userMessage", createHtmlMessageFromSender(sender, message))
					  .put("userlist", userUsernameMap.values())
				);
				session.getRemote().sendString(mensaje);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	//Builds a HTML element with a sender-name, a message, and a timestamp,
	private static String createHtmlMessageFromSender(String sender, String message) {
		return article(
			b(sender + " says:"),
			span(attrs(".timestamp"), new SimpleDateFormat("HH:mm:ss").format(new Date())),
			p(message)
		).render();
	}
}