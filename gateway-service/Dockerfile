FROM adoptopenjdk:8-jdk-hotspot-focal
MAINTAINER Rahul Krishna <imralk+oss@gmail.com>
##
##  Declare Docker ARGs whose values are set when building the image; but they are not available to the container
##
##  1.  App/WAR Name & Version
ARG APP_ARTIFACTID
ARG WAR_ARTIFACTID
ARG APP_VERSION
##  2.  Exposed Port
ARG EXPOSE_PORT
##  3.  Database's URL
ARG DATABASE_DRIVER
ARG DATABASE_URL
ARG DATABASE_USERNAME
ARG DATABASE_PASSWORD
## 4.  Service discovery
ARG DAYTRADER_ACCOUNTS_SERVICE
ARG DAYTRADER_GATEWAY_SERVICE
ARG DAYTRADER_PORTFOLIOS_SERVICE
ARG DAYTRADER_QUOTES_SERVICE

##  Set Docker ENV Values that are not only available when building the image, but also to the running container
##
##  1. SSL Variables
ENV DAYTRADER_KEYSTORE_FILENAME=/var/ssl/daytrader/keystore.jks
ENV DAYTRADER_KEYSTORE_PASSWORD=password
ENV DAYTRADER_TRUSTSTORE_LOCATION=/var/ssl/daytrader/truststore.jks
ENV DAYTRADER_TRUSTSTORE_PASSWORD=password
##  2.  Database Variables
ENV DAYTRADER_DATABASE_DRIVER=${DATABASE_DRIVER}
ENV DAYTRADER_DATABASE_URL=${DATABASE_URL}
ENV DAYTRADER_DATABASE_USERNAME=${DATABASE_USERNAME}
ENV DAYTRADER_DATABASE_PASSWORD=${DATABASE_PASSWORD}
##  3.  Tomcat Variables
ENV SERVER_PORT=${EXPOSE_PORT}
ENV SERVER_PORT_HTTPS=${EXPOSE_PORT}
##  4.  Routing Variables
ENV DAYTRADER_ACCOUNTS_SERVICE=http://daytrader-accounts-service:8080
ENV DAYTRADER_GATEWAY_SERVICE=http://daytrader-gateway-service:8080
ENV DAYTRADER_PORTFOLIOS_SERVICE=http://daytrader-portfolios-service:8080
ENV DAYTRADER_QUOTES_SERVICE=http://daytrader-quotes-service:8080
##  5.  Logging Variables
ENV DAYTRADER_LOG_FILENAME=/tmp/${APP_ARTIFACTID}-${APP_VERSION}.log
ENV DAYTRADER_LOG_LEVEL=TRACE
ENV DAYTRADER_LOG_APPENDER=ConsoleAppender
##
##  Setup the environment for the running containerff
##
##  1.  Create Database Folder
RUN mkdir -p -m 0777 /var/dat/daytrader
##  2.  Create Logging Folder, Logging File, & Set Permissions
RUN mkdir -p -m 0777 /var/log/daytrader
RUN touch $DAYTRADER_LOG_FILENAME
RUN chmod 666 $DAYTRADER_LOG_FILENAME
##  3.  Create SSL Folder, Add Truststore, & Set Permissions
RUN mkdir -p -m 0777 /var/ssl/daytrader
ARG JKS_FILE=target/${WAR_ARTIFACTID}-${APP_VERSION}/WEB-INF/classes/truststore.jks
ADD ${JKS_FILE} $DAYTRADER_TRUSTSTORE_LOCATION
RUN chmod 666 $DAYTRADER_TRUSTSTORE_LOCATION
##  4.  Inform Docker that the container listens on the specified port
EXPOSE ${EXPOSE_PORT}
##
##  Run the application in the container
##
##  1.  Setup the JAVA_OPTS for the application
ENV JAVA_OPTS="-Djavax.net.ssl.trustStore=/var/ssl/daytrader/truststore.jks -Djavax.net.ssl.trustStorePassword=password"
##  2.  Add the WAR to the container
ARG WAR_FILE=target/${WAR_ARTIFACTID}-${APP_VERSION}.war
ADD ${WAR_FILE} app.war
##  3.  Start the executable WAR
ENTRYPOINT exec java $JAVA_OPTS -jar app.war
