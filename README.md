# About
This project is a hack-a-thon style experimental project to create simple executables for the Alfresco Content Services and Share applications. Initially this project aims to create Jetty-based executable "fat JARs" for these applications, which may form the basis of compiling these applications into native binaries at some point in the future using [GraalVM](https://www.graalvm.org/) / [SubstrateVM](https://github.com/oracle/graal/tree/master/substratevm).

## Build

  1. Build by ```mvn clean package``` to create jar executables:
     - Content Services (ACS) - ```content-services-jetty/target/alfresco-content-services-6.0.7-ga.jar```
     - Share ```share-jetty/target/alfresco-share-6.0.c.jar```
  2. Configure
     - Copy [config/alfresco-global.properties.sample]() to ```config/alfresco-global.properties```
     - Default configuration expects a MySQL database *alfresco* running on localhost. User and password: *alfresco*
     - **For Production usage**: Customize configuration and do not use *alfresco* as password!
  3. Run applications
     - Content Services   
       ```java -jar content-services-jetty/target/alfresco-content-services-6.0.7-ga.jar```
     - Share   
       ```java -jar share-jetty/target/alfresco-share-6.0.c.jar```


## Configuration
Both jars can be configured to custom needs by command line arguments. Use ```-h``` to show all available options and defaults.

### Alfresco Content Services
```java -jar content-services-jetty/target/alfresco-content-services-6.0.7-ga.jar -h```

```
usage: java -jar <jar> [-c <arg>] [-contextpath <arg>] [-favicon <arg>]
       [-h] [-p <arg>]

Alfresco Content Services

 -c,--config <arg>    Configuration path containing
                      alfresco-global.properties [string]. Default
                      './config/content-services'
 -contextpath <arg>   Alfresco webapp context path [string]. Default
                      '/alfresco'
 -favicon <arg>       Alfresco favicon [string]. Default
                      'webapps/alfresco/favicon.ico'
 -h,--help            This information
 -p,--port <arg>      Port [integer]. Default 8080
 ```

### Alfresco Share

```java -jar share-jetty/target/alfresco-share-6.0.c.jar -h```

```
usage: java -jar <jar> [-c <arg>] [-contextpath <arg>] [-favicon <arg>]
       [-h] [-p <arg>]

Alfresco Share

 -c,--config <arg>    Configuration path containing
                      alfresco-global.properties [string]. Default
                      './config/share'
 -contextpath <arg>   Share webapp context path [string]. Default '/share'
 -favicon <arg>       Share favicon [string]. Default
                      'webapps/share/favicon.ico'
 -h,--help            This information
 -p,--port <arg>      Port [integer]. Default 8081
 ```

## TODO:
Improvements for production usage:

1. [DONE] setup different default port for share = 8081
2. [DONE] shorter jar names
   - include alfresco acs and share version for easier recognition
3. [DONE] make jetty runners configurable (main args)
  - port
  - configuration extra classpath directory
4. setup docker and docker-compose file
5. [DONE] default alfresco-global properties
