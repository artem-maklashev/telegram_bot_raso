FROM openjdk:20

ENV SPRING_DATASOURCE_URL="jdbc:mysql://130.193.39.169:33007/goldentroup"
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=402986
ENV eureka.client.service-url.defaultZone=http://host.docker.internal:8761/eureka/

# Копируем JAR файл из двух уровней вверх относительно Dockerfile
COPY target/telegram_bot_raso-0.0.1-SNAPSHOT.jar /tmp/telegram_bot_raso-0.0.1-SNAPSHOT.jar
WORKDIR /tmp

EXPOSE 8079

CMD ["java", "-jar", "/tmp/telegram_bot_raso-0.0.1-SNAPSHOT.jar"]
