package itk.academy.orekhov.repository;

import itk.academy.orekhov.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Disable embedded database
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:postgresql://localhost:5433/ecommerce_db_test",
        "spring.datasource.username=postgres",
        "spring.datasource.password=postgres",
        "spring.datasource.driver-class-name=org.postgresql.Driver"
})
public class UserRepositoryTest {

    private static PostgreSQLContainer<?> postgresContainer;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeAll
    public static void setUpClass() {
        // Запуск контейнера PostgreSQL с TestContainers
        postgresContainer = new PostgreSQLContainer<>("postgres:latest")
                .withDatabaseName("ecommerce_db_test")
                .withUsername("postgres")
                .withPassword("postgres");
        postgresContainer.start();

        // Устанавливаем URL для подключения к тестовой базе данных PostgreSQL
        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgresContainer.getUsername());
        System.setProperty("spring.datasource.password", postgresContainer.getPassword());
    }

    @AfterAll
    public static void tearDownClass() {
        // Остановка контейнера после выполнения тестов
        if (postgresContainer != null) {
            postgresContainer.stop();
        }
    }

    @BeforeEach
    public void setUp() {
        // Создание нового пользователя перед каждым тестом
        user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
    }

    @Test
    public void testSaveUser() {
        // Сохранение пользователя в базе данных
        User savedUser = userRepository.save(user);

        // Проверка, что пользователь был сохранен
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isGreaterThan(0);
        assertThat(savedUser.getName()).isEqualTo("John Doe");
        assertThat(savedUser.getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    public void testFindById() {
        // Сохранение пользователя
        User savedUser = userRepository.save(user);

        // Поиск пользователя по ID
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        // Проверка, что пользователь найден
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("John Doe");
        assertThat(foundUser.get().getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    public void testFindByEmail() {
        // Сохранение пользователя
        userRepository.save(user);

        // Поиск пользователя по email
        Optional<User> foundUser = userRepository.findAll().stream()
                .filter(u -> u.getEmail().equals("john.doe@example.com"))
                .findFirst();

        // Проверка, что пользователь найден по email
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    public void testUpdateUser() {
        // Сохранение пользователя
        User savedUser = userRepository.save(user);

        // Обновление данных пользователя
        savedUser.setName("Jane Doe");
        savedUser.setEmail("jane.doe@example.com");
        userRepository.save(savedUser);

        // Поиск обновленного пользователя
        Optional<User> updatedUser = userRepository.findById(savedUser.getId());

        // Проверка, что данные обновлены
        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getName()).isEqualTo("Jane Doe");
        assertThat(updatedUser.get().getEmail()).isEqualTo("jane.doe@example.com");
    }

    @Test
    public void testDeleteUser() {
        // Сохранение пользователя
        User savedUser = userRepository.save(user);

        // Удаление пользователя
        userRepository.deleteById(savedUser.getId());

        // Проверка, что пользователь удален
        Optional<User> deletedUser = userRepository.findById(savedUser.getId());
        assertThat(deletedUser).isNotPresent();
    }
}
