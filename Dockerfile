FROM adoptopenjdk:11-jre-hotspot
ADD build/libs/bookmanagement*.jar app.jar
EXPOSE 8080
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]