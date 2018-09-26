# Static File Server Boilerplate Java Spark

Crear proyecto con Maven

    $ mvn archetype:generate -DgroupId=pe.softweb -DartifactId=lite -DarchetypeArtifactId=maven-archetype-webapp -DinteractiveMode=false

Ubicación de los archivos estáticos:

    src/main/resources/public

Crear war usando Maven:

    $ mvn package

Cargar dependencias bower y npm:

    $ bower install && npm install

Ejecutar Main Class usando Maven:

    $ mvn clean && mvn install && mvn exec:java -Dexec.mainClass="configs.App"

#### Consultas MongoDB:

Query si elementos existen en arreglo:

```
db.getCollection('conversations').find({
  "$and":[
    {
      members: {  
        "$in": [
          ObjectId("5ba84a28686d3e4988a57b64"),
        ]
      }
    },
    {
      members: {
        "$in": [
          ObjectId("5ba84a28686d3e4988a57b65"),
        ]
      }
    },   
  ]
})

```

Función para devolver conversación en función a dos miembros:

```
db.system.js.save({
    _id: "getIdConversacionFunction",
    value: function (usuario_id_1, usuario_id_2) {
        var convsersation_id = null;
        var doc = db.getCollection('conversations').find({
          "$and":[
            {
              members: {  
                "$in": [
                  ObjectId(usuario_id_1),
                ]
              }
            },
            {
              members: {
                "$in": [
                  ObjectId(usuario_id_2),
                ]
              }
            },   
          ]
        }).toArray();
        if(doc.length == 1){
           convsersation_id = doc[0]['_id'].str;
        }
        return convsersation_id;
    }
})
```

Llamar a función:

```
db.eval("getConversacionFunction('5ba84a28686d3e4988a57b64', '5ba84a28686d3e4988a57b65')")
```

---

--- 

Fuentes

+ https://www.mkyong.com/maven/how-to-create-a-web-application-project-with-maven/
+ https://stackoverflow.com/questions/9846046/run-main-class-of-maven-project
+ http://sparkjava.com/
+ https://sparktutorials.github.io/2015/11/08/spark-websocket-chat.html
+ https://github.com/tipsy/spark-websocket
+ https://github.com/perwendel/spark/issues/921
+ http://chuwiki.chuidiang.org/index.php?title=Serializaci%C3%B3n_de_objetos_en_java