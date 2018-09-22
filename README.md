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

--- 

Fuentes

+ https://www.mkyong.com/maven/how-to-create-a-web-application-project-with-maven/
+ https://stackoverflow.com/questions/9846046/run-main-class-of-maven-project
+ http://sparkjava.com/
+ https://sparktutorials.github.io/2015/11/08/spark-websocket-chat.html
+ https://github.com/tipsy/spark-websocket