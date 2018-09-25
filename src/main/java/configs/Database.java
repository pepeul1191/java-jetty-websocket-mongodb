package configs;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

public class Database {
    private Morphia morphia;
    private Datastore datastore;
    
    public Database(){
        this.morphia = new Morphia();
        this.morphia.mapPackage("models");
        this.datastore = morphia.createDatastore(new MongoClient(), "chats");
        this.datastore.ensureIndexes();
    }

    public Morphia getMorphia() {
        return this.morphia;
    }

    public Datastore getDatastore() {
        return this.datastore;
    }
}
