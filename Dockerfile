FROM eclipse-temurin:17-jdk-alpine as challenge-build
RUN mkdir /application
WORKDIR /application
COPY . .
RUN ./mvnw -Dmaven.test.skip clean install

FROM eclipse-temurin:17-jdk-alpine as challenge-jar
COPY --from=challenge-build /application/target/challenge-1.0.jar app.jar
ENV CLIENT_ID=${CLIENT_ID}
ENV CLIENT_SECRET=${CLIENT_SECRET}
EXPOSE 8080
ENTRYPOINT java -DclientId=$CLIENT_ID -DclientSecret=$CLIENT_SECRET -Dfile.encoding=UTF-8 -jar app.jar