FROM openjdk:21-jdk-bullseye

RUN apt-get update && \
    apt-get install -y libx11-6 && \
    rm -rf /var/lib/apt/lists/*

RUN mkdir /app

WORKDIR /app

COPY src/application/ /app/
COPY /lib/javafx-sdk-21.0.2 /app/javafx-sdk-21.0.2
COPY InventorySystem.jar /app/

CMD ["java", "--module-path", "/app/javafx-sdk-21.0.2/lib", "--add-modules", "javafx.controls,javafx.fxml", "-Djava.library.path=/app/javafx-sdk-21.0.2/lib", "-jar", "InventorySystem.jar"]