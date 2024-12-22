package itk.academy.orekhov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // Аннотация указывает, что это главный класс приложения Spring Boot
public class Application {

    public static void main(String[] args) {
        // Метод запускает Spring Boot приложение
        SpringApplication.run(Application.class, args);
    }
}

