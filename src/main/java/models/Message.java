package models;

import java.util.Date;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("messages")
public class Message {
  @Id 
  private ObjectId id;
  private String content;
  private ObjectId userId;
  private Date moment;

  public Message(){}

  public Message(String content, String userId) {
    this.content = content;
    this.userId = new ObjectId(userId);
    this.moment = new Date();
  }    
}
