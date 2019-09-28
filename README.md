# audit-log application
	This application is a REST API to keep registries of operations performed in Mongo database.

## Technologies(Frameworks and Plugins)
This project I have developed using Intellij IDE and these technologies and frameworks:

	-Java 8
    -Springboot,
    -Gradle,
    -Swagger,
    -Lombok,
    -Actuator,
    -Docker,
	-PMD plugin,
	-Checkstyle,
	-Mongo database,
    -Spring rest.

## About project	
	This project is formed per one SpringBoot Application.
        Notes about application:
            -It is configured to listen 8090 port;
            -In this Swagger url: http://localhost:8090/audit-log/swagger-ui.html you can check all documentation about rest endpoints;
            -It is configured to use Swagger, using it you can check the endpoints and payloads using an internet browser. To access Swagger interface use this url: http://localhost:8090/audit-log/swagger-ui.html, just a detail, if you are using Docker in Windowns machine maybe you need to change the localhost to a -specific ip (I need to that because I have used Docker toolbox)
			-Basic the API has these endpoints:
				Post - http://localhost:8090/audit-log/operation/ - create new operation performed in database;
                Get - http://localhost:8090/audit-log/operation/{id} - retrieve an operation performed in database;
                Get - http://localhost:8090/audit-log/operation/ - retrieve all operations performed in database;
				Delete - http://localhost:8090/audit-log/operation/{id} - delete operation performed in database;

			-There are unit tests for service layer,
			-There are integration tests for API that simulate the complete flows and these tests are configured to use Flapdoodle framework, it simulate a Mongo database in memory, I mean to execute the integration you don't need a Mongo instance running to execute the integration tests, after it the this Mongo memory instance will be destroyed.
			-This project is using PMD (https://maven.apache.org/plugins/maven-pmd-plugin/) and Checkstyle (https://maven.apache.org/plugins/maven-checkstyle-plugin/) plugins to keep a good quality in -the code.
			-During every build process, these process are executed:
				Execute unit tests for service layer
				Execute integration tests
				Execute Checkstyle verification
				Execute PMD verification	
				build jar file

## Run 
To run application without an IDE you need to follow these steps:
```bash
-Execute the Gradle build;
-To running this application you need to use the production profile and you need have a valid Mongo db instance running,
     also you need to configure the Mongo connection in application-prod.properties.
```
Inside Intellij IDE:
```bash
-Import the project;
-Execute Gradle import;
-Check Enable annotation processing field in Intellij options
-Configure Mongo connection in prod profile file;
-Start application using prod profile in Intellij IDE.
```

## Docker
 To use docker you need follow these steps:
 ```bash
	-Build the application,
	-Build docker image with this command: docker build -t audit-log . or docker build -t audit-log . (you need to run this command in root project that you want to *create the docker image);
    -Execute the docker-compose file with this command: docker-compose up (you need to run this command in root project). You can -check if applications are running using the actuator feature, to do do that you need to access this url: http://{docker ip:8090}/audit-log/health;
```

If you have questions, please feel free to contact me.