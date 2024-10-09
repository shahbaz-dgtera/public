####
# This Dockerfile is used in order to build a container that runs the Quarkus application in native (no JVM) mode.
#
# Before building the container image run:
#
# ./mvnw package
#
#  OR for native
#
# ./mvnw package -Dquarkus.package.jar.type=legacy-jar
#
# Then, build the image with:
#
# docker build --platform linux/amd64 -f Dockerfile -t quay.apps.ocphubho.neoleap.com.sa/svcocpldap/quarkus .
#
# Then run the container using:
#
# docker run -i --rm -p 8080:8080 quarkus/quarkus-app
#
###
FROM registry.access.redhat.com/ubi8/openjdk-17:1.20

ENV LANGUAGE='en_US:en'


COPY target/lib/* /deployments/lib/
COPY target/*-runner.jar /deployments/quarkus-run.jar

EXPOSE 8080
USER 185
ENV JAVA_OPTS_APPEND="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"

ENTRYPOINT [ "/opt/jboss/container/java/run/run-java.sh" ]

