FROM openjdk:8-jdk-alpine as TEMP_BUILD_IMAGE
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY build.gradle settings.gradle gradlew $APP_HOME
COPY gradle $APP_HOME/gradle
RUN ./gradlew build || return 0
COPY . .
RUN ./gradlew build

FROM openjdk:8-jdk-alpine
ENV ARTIFACT_NAME=panel-service.jar
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY --from=TEMP_BUILD_IMAGE $APP_HOME/build/libs/$ARTIFACT_NAME .
EXPOSE 8080 5555
ENTRYPOINT ["java","-Duser.timezone=Europe/Kiev","-jar","panel-service.jar"]