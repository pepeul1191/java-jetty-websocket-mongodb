package models;

import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("conversations")
public class Conversation {
  @Id 
  private ObjectId id;
  @Embedded
  private List<Message> messages;
  private Date created;
  private Date updated;
  private List<ObjectId> members;

  public Conversation() {
  }

  public ObjectId getId() {
    return id;
  }

  public void setId(ObjectId id) {
    this.id = id;
  }

  public List<Message> getMessages() {
    return messages;
  }

  public void setMessages(List<Message> messages) {
    this.messages = messages;
  }
  
  public List<ObjectId> getMembers() {
    return members;
  }

  public void setMembers(List<ObjectId> members) {
    this.members = members;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated() {
    this.created = new Date();
  }
}
