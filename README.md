# CS-Assignment
CS Interview Process Coding Round Assignment - Spring Boot Project

<B>Build + Run</B>

1. This project uses maven for packaging an executable jar archive. After successfull execution, jar can be found in the /target folder
<B>mvn clean package</B>

2. To Run the Jar Open Git bash OR CMD from the Project path <folder where pom.xml is locates> and ececute the below command
<B>java -jar -Dspring.config.location=environments/sit/ target/CS-Event-Management-1.0-SNAPSHOT.jar</B>

used <i>-Dspring.config.location=environments/sit/  to provide the environment specif log reading capability to project</i>

<B>To Test the Functionality - Follow API Endpoints</B>

1. To check the Health of Application
<B>curl -i -X GET localhost:9090/actuator/health</B>

1. To store the log in DB from Logfile.txt file <br />
<B>curl -i -X POST localhost:9090/event</B>


2. To get the stored event log in DB <br />
<B>curl -i -X GET localhost:9090/event</B>
