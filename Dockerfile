FROM openjdk:17-jdk

# 정확한 파일 이름을 사용
COPY module-api/build/libs/*SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
