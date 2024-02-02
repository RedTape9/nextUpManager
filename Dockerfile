FROM --platform=linux/amd64 openjdk:21
LABEL maintainer="ser.jaudszims@gmail.com"
EXPOSE 8080
ADD backend/target/next-up-manager.jar next-up-manager.jar
CMD [ "sh", "-c", "java -jar /next-up-manager.jar" ]