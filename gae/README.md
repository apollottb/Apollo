## maven

- groupId: com.apollottb.appengine
- artifactId: apollo


## Google App Engine

- Project ID: apollo-13
- Project Number: 544505541815


## Files

- HTML / CSS / JS

    apollo/apollo-war/src/main/webapp/example.jsp (mapped to /example.jsp)

- URL-Servlet matching

    apollo/apollo-war/src/main/webapp/WEB-INF/web.xml (URL - servlet match)

- Servlets

    apollo/apollo-war/src/main/java/com/apollottb/appengine/apollo/MyServlet.java


## Commands

    Command                  Description                           Where to execute
    ---------------------------------------------------------------------------------
    mvn clean install        Build                                 apollo/
    mvn appengine:devserver  Test locally (localhost:8080)         apollo/apollo-ear
    mvn appengine:update     Deploy to GAE (apollo-13.appspot.com) apollo/apollo-ear
