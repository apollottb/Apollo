# maven

- groupId: com.apollottb.appengine
- artifactId: apollo


# Google App Engine

- Project ID: apollo-13
- Project Number: 544505541815


# Edit:

1. apollo/apollo-war/src/main/webapp/[jsp, css, js files] (mapped to /filename.jsp)
2. apollo/apollo-war/src/main/webapp/WEB-INF/web.xml (URL - servlet match)
3. apollo/apollo-war/src/main/java/com/apollottb/appengine/apollo/[servlets]


# Build + Test:

1. apollo/

    mvn clean install

2. apollo/apollo-ear

    mvn appengine:devserver

3. Access localhost:8080


# Upload to GAE Server:

1. apollo/apollo-ear

    mvn appengine:update

2. Copy key.

3. https://apollo-13.appspot.com
