FROM eclipse-temurin:21-jdk AS builder

WORKDIR /workspace

COPY mvnw pom.xml ./
COPY .mvn .mvn

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

COPY src src

RUN ./mvnw clean package -DskipTests


FROM eclipse-temurin:21-jre

WORKDIR /application

RUN addgroup --system chronicles \
    && adduser --system --ingroup chronicles chronicles

COPY --from=builder /workspace/target/*.jar application.jar

USER chronicles

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "application.jar"]