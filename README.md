# CS-Assignment
CS Interview Process Coding Round Assignment - Spring Boot Project

<B>Build + Run - USE JAVA 1.8 VERSION TO INSTALL / CREATE THE JAR FILE</B>

1. This project uses maven for packaging an executable jar archive. After successfull execution, jar can be found in the /target folder <br />
<B>mvn clean package OR mvn clean install</B>

2. To Run the Jar Open Git bash OR CMD from the Project path i.e. the folder path where pom.xml is located and execute the below command <br />
<B>java -jar -Dspring.config.location=environments/sit/ target/CS-Event-Management-1.0-SNAPSHOT.jar</B>

  I have used <b><i>-Dspring.config.location=environments/sit/</b> to provide the environment specific file reading capability to project</i>

<B>To Test the Functionality - Follow API Endpoints</B> <br />

1. To check the Health of Application<br />
<B>curl -i -X GET localhost:9090/actuator/health</B>

1. To store the log in DB from Logfile.txt file <br />
<B>curl -i -X POST localhost:9090/event</B>


2. To get the stored event log in DB <br />
<B>curl -i -X GET localhost:9090/event</B>
