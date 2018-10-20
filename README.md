# About
This project is a hack-a-thon style experimental project to create simple executables for the Alfresco Content Services and Share applications. Initially this project aims to create Jetty-based executable "fat JARs" for these applications, which may form the basis of compiling these applications into native binaries at some point in the future using [GraalVM](https://www.graalvm.org/) / [SubstrateVM](https://github.com/oracle/graal/tree/master/substratevm).

## Build & Configuration

  1. Build ```mvn clean package```
  2. ```config/content-services``` extra configuration classpath for Alfresco Content Service
  3. ```config/share`` extra configuration classpath for Alfresco Share
  4. create alfreco-global.properties for acs and share
     - databse connection
     - alfresco settings
  5. Run applications
    - Content Services ```java -jar content-services-jetty/target/alfresco-content-services-6.0.7-ga.jar```
    - Share ```java -jar share-jetty/target/alfresco-share-6.0.c.jar```

## TODO:
Improvements for production usage:

1. [DONE] setup different default port for share = 8081
2. [DONE] shorter jar names
   - include alfresco acs and share version for easier recognition
3. [DONE] make jetty runners configurable (main args)
  - port
  - configuration extra classpath directory
4. setup docker and docker-compose file
